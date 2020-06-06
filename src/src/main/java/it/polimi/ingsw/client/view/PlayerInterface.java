package it.polimi.ingsw.client.view;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.client.NetworkHandler;
import it.polimi.ingsw.network.client.SocketConnection;
import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import it.polimi.ingsw.client.view.divinities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  Class used to manage the user experience during the game.
 */

public class PlayerInterface {

    private Map map;
    private Game game;
    private Player player;
    private String iD;
    private Divinity divinity;
    private int firstboxX , firstboxY;

    private NetworkHandler networkHandler;
    private Scanner input;
    private static final int port = 65535;

    /**
     * Create Client's connection to the Server and starts the game
     */
    public PlayerInterface(){

        input = new Scanner(System.in);
        boolean connected = false;
        while(!connected){

            String username = "";
            while(username.isEmpty() || username.endsWith(" ")) {
                System.out.println("Insert your Username");
                username = input.nextLine();
            }
            System.out.println("Please wait, we are trying to connect you to the server");
            SocketConnection connection = new SocketConnection(username, port);
            networkHandler = new NetworkHandler();
            networkHandler.setConnection(connection);
            try {
                connection.startConnection();
            } catch (IOException e) {
                System.out.println("You have been disconnected from the server");
            }
            try{
                connection.run();
                String response = connection.getMessage().getContent();
                System.out.println(response);
                if(response.equalsIgnoreCase("You logged in successfully")){
                    connected = true;
                    iD = username;
                }
            }catch (NullPointerException e){
                System.out.println("You need to connect");
            }
        }

        catchMVEvent();
    }

    /**
     * Method used to update the screen
     */
    private void printScreen(){
        map.printMap();
        if (!(divinity == null)) {
            System.out.println("Your Divinity: " + divinity.getName() + "\nDescription:" + divinity.getDescription());
        }
    }

    /**
     * Method used to provide to the client the interaction with the server
     */
    private void catchMVEvent() {
        String communicationString;
        boolean supportBoolean;

        networkHandler.sendPing();


        while (true) {
                Message receivedMessage = networkHandler.receiveMessage();

                try{
                    CommunicationEvent event = (CommunicationEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null) {
                        communicationString = event.getMessage();

                        if (event.getTarget().equalsIgnoreCase(iD)) {

                            if (communicationString.equals("Please wait for the opponent to finish its action")) {
                                System.out.println("Please wait for your turn");

                            }
                            if (communicationString.equals("Please insert the number of Players")) {
                                try{
                                    int numPla = 0;
                                    while (!(numPla == 2 || numPla == 3)){
                                        System.out.print(communicationString + " <2/3> : ");
                                        try {
                                            numPla = Integer.parseInt(input.next());
                                        }catch (NumberFormatException e){
                                            System.out.println("Please insert a valid number");
                                            numPla = 0;
                                        }
                                    }
                                    networkHandler.send(new NumPlayersSelectedEvent(iD, numPla));
                                } catch (IOException ignored) {
                                }
                            }
                            if (communicationString.contains("Please insert your data, colors available are:")) {
                                int age = 0;
                                String color = "";
                                    try {
                                        while ((age <= 0 || age > 120)) {
                                            System.out.println("Please, insert your age: ");
                                            try {
                                                age = Integer.parseInt(input.next());
                                            }catch(NumberFormatException e){
                                                System.out.println("Please insert a valid input");
                                            }
                                        }
                                        while (!(color.equalsIgnoreCase("Yellow") || color.equalsIgnoreCase("Blue") || color.equalsIgnoreCase("Red"))) {
                                            System.out.println("Select a color, " + communicationString.substring(25));
                                            color = input.next();
                                        }
                                        PlayerColor colorPlayer = null;
                                        if (color.equalsIgnoreCase("Yellow")) {
                                            colorPlayer = PlayerColor.YELLOW;
                                        }
                                        if (color.equalsIgnoreCase("Blue")) {
                                            colorPlayer = PlayerColor.BLUE;
                                        }
                                        if (color.equalsIgnoreCase("Red")) {
                                            colorPlayer = PlayerColor.RED;
                                        }
                                        networkHandler.send(new PlayerDataEnteredEvent(iD, age, colorPlayer));
                                    } catch (IOException ignored) {
                                    }
                            }
                            if (communicationString.contains("Please select the cards, gods available are:")) {
                                System.out.println(communicationString.subSequence(0, 43));
                                String selectedCard;
                                    findDivinity(communicationString);
                                    try {
                                        ArrayList<String> divinitySelection = new ArrayList<>();
                                        for (int i = 0; i < game.getNumPlayers(); i++) {
                                            do {
                                                System.out.println("Select divinity #" + i + " : ");
                                                selectedCard = input.next();
                                            } while (!((selectedCard.equals("Apollo") || selectedCard.equals("Arthemis") || selectedCard.equals("Athena") || selectedCard.equals("Atlas") || selectedCard.equals("Demeter") || selectedCard.equals("Hephaestus") || selectedCard.equals("Minotaur") || selectedCard.equals("Pan") || selectedCard.equals("Prometheus")) && !(divinitySelection.contains(selectedCard))));
                                            divinitySelection.add(selectedCard);
                                        }
                                        networkHandler.send(new GodsSelectedEvent(iD, divinitySelection));
                                    } catch (IOException ignored) {
                                    }
                            }
                            if (communicationString.equals("Do you want to play with gods?")) {
                                try {
                                    String response = "";
                                    while (!(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no"))) {

                                        System.out.print("Do you want to play with gods? (Please answer with yes or no): ");
                                        response = input.next();
                                    }
                                    supportBoolean = response.equalsIgnoreCase("yes");
                                    networkHandler.send(new WithGodsSelectedEvent(iD, supportBoolean));
                                } catch (IOException ignored) {
                                }
                            }
                            if (communicationString.contains("Select your Card, cards available are:")) {
                                String selectedDivinity = "Not Selected";

                                    findDivinity(communicationString);
                                    try {
                                        while (!(communicationString.contains(selectedDivinity))) {
                                            System.out.print("Select you divinity: ");
                                            selectedDivinity = input.next();
                                        }

                                        if(selectedDivinity.equalsIgnoreCase("Apollo")) {
                                            divinity = new Apollo();
                                            }
                                        if(selectedDivinity.equalsIgnoreCase("Arthemis")) {
                                            divinity = new Arthemis();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Athena")) {
                                            divinity = new Athena();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Atlas")) {
                                            divinity = new Atlas();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Demeter")) {
                                            divinity = new Demeter();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Hephaestus")) {
                                            divinity = new Hephaestus();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Minotaur")) {
                                            divinity = new Minotaur();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Pan")) {
                                            divinity = new Pan();
                                        }
                                        if(selectedDivinity.equalsIgnoreCase("Prometheus")) {
                                            divinity = new Prometheus();
                                        }

                                        networkHandler.send(new CardSelectedEvent(iD, selectedDivinity));
                                    } catch (IOException ignored) {
                                    }
                            }
                            if (communicationString.contains("Select the Starter, players in the game are:")) {
                                String playerSelect = "Not selected";
                                    try {
                                        while (!(game.getIDs().contains(playerSelect))) {
                                            System.out.println("\tSelect the starter, IDs available are:");
                                            for (int i = 0; i < game.getNumPlayers(); i++) {
                                                System.out.println("\t\t>" + game.getIDs().get(i));
                                            }
                                            System.out.print("\tSelect player using his ID: ");
                                            playerSelect = input.next();
                                        }
                                        networkHandler.send(new StarterSelectedEvent(iD, playerSelect));
                                    } catch (IOException ignored) {
                                    }
                            }
                            if (communicationString.equals("Please Select the first box")) {

                                    printScreen();
                                    try {
                                        int coordinateX = -1;
                                        int coordinateY = -1;

                                        while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker()))){
                                           try {
                                               System.out.print("Select your first worker's starting box (only empty boxes can be chosen)\n\tcoordinates X: ");
                                               coordinateX = Integer.parseInt(input.next());
                                               System.out.println("\tcoordinate Y:");
                                               coordinateY = Integer.parseInt(input.next());
                                           }catch (NumberFormatException e){
                                               System.out.println("Please insert valid indices");
                                           }
                                        }
                                        firstboxX = coordinateX;
                                        firstboxY = coordinateY;
                                        networkHandler.send(new BoxSelectedEvent(iD,coordinateX,coordinateY));
                                    } catch (IOException | InvalidIndicesException ignored) {
                                    }
                            }
                            if (communicationString.equals("Please Select the second box")) {

                                    try {
                                        int coordinateX = -1;
                                        int coordinateY = -1;
                                        do {
                                            System.out.println("Select your second worker's starting box (only empty boxes can be chosen)\n\tcoordinates X: ");
                                            try {
                                                coordinateX = Integer.parseInt(input.next());
                                                System.out.println("\tcoordinates Y:");
                                                coordinateY = Integer.parseInt(input.next());
                                            }catch (NumberFormatException e){
                                                System.out.println("Please insert valid indices");
                                            }
                                        } while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && (coordinateX != firstboxX || coordinateY != firstboxY) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker())));

                                        networkHandler.send(new BoxSelectedEvent(iD, coordinateX,coordinateY));
                                    } catch (IOException | InvalidIndicesException ignored) {}
                            }
                            if (communicationString.equals("Please Select one box to build")) {
                                printScreen();

                                    try {
                                        int coordinateX = -1;
                                        int coordinateY = -1;
                                        do {
                                            try {
                                                System.out.print("Select a box to build in :\n\t coordinate X:");
                                                coordinateX = Integer.parseInt(input.next());
                                                System.out.println("\t coordinate Y:");
                                                coordinateY = Integer.parseInt(input.next());
                                            }catch (NumberFormatException e){
                                                System.out.println("Please insert valid indices");
                                            }

                                        } while (!(coordinateX >= 0 && coordinateX < 5 && coordinateY >= 0 && coordinateY < 5));

                                        networkHandler.send(new BoxSelectedEvent(iD, coordinateX, coordinateY));
                                    } catch (IOException ignored) {
                                    }
                            }
                            if (communicationString.equals("Please Select one box to move")) {
                                printScreen();

                                try {
                                    int coordinateX = -1;
                                    int coordinateY = -1;
                                    do {
                                        try {
                                            System.out.print("Select a box to move in :\n\t coordinate X:");
                                            coordinateX = Integer.parseInt(input.next());
                                            System.out.println("\t coordinate Y:");
                                            coordinateY = Integer.parseInt(input.next());
                                        }catch (NumberFormatException e){
                                            System.out.println("Please insert valid indices");
                                        }

                                    } while (!(coordinateX >= 0 && coordinateX < 5 && coordinateY >= 0 && coordinateY < 5));

                                    networkHandler.send(new BoxSelectedEvent(iD, coordinateX, coordinateY));
                                } catch (IOException ignored) {
                                }
                            }
                            if (communicationString.equals("Please Select one worker")) {

                                int workerNum = -1;
                                Worker workerMoved;
                                System.out.println("It's your turn, please select your worker ");

                                        System.out.println("These are your workers:");
                                        for (int i = 0; i < player.getWorkers().size(); i++) {
                                            System.out.println("\t> #" + i + " position (x: " + player.getWorkers().get(i).getBox().getPosition()[0] + ", y: " + player.getWorkers().get(i).getBox().getPosition()[1] + ")");
                                        }
                                            while (!((workerNum == 0 || workerNum == 1) && player.getWorkers().get(workerNum).canMove())) {
                                                try {
                                                    System.out.print("Select your worker: ");
                                                    workerNum = Integer.parseInt(input.next());
                                                }catch(NumberFormatException e){
                                                System.out.println("Please insert a valid value");
                                                }
                                            }

                                    try {
                                        networkHandler.send(new WorkerSelectedEvent(iD, workerNum));
                                    } catch (IOException ignore) {
                                    }
                            }
                            if (communicationString.equals("You are active now")) {
                                System.out.println(communicationString);
                            }
                            if (communicationString.equals("Game over, thanks for playing Santorini!")) {
                                System.out.println("\t\t\t\t" + communicationString);
                            }
                            if (communicationString.equals("Do you want to restart?")) {
                                try {
                                    boolean gotResponse = false;
                                    while (!gotResponse) {

                                        System.out.println("Do you want to restart? (Please answer with yes or no): ");
                                        String s = input.nextLine();
                                        System.out.println(s);
                                        if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no")) {
                                            gotResponse = true;
                                            supportBoolean = s.equalsIgnoreCase("yes");
                                            networkHandler.send(new RestartConfirmationEvent(iD, supportBoolean));
                                        }
                                    }

                                } catch (IOException ignored) {
                                }
                            }
                            if (communicationString.equals("Please wait, the host is creating the game")) {
                                System.out.println(communicationString);
                            }
                            if (communicationString.equals("Sorry but the game is already full") || communicationString.equals("The host decided to conclude the matches")) {
                                System.out.println(communicationString);
                                break;
                            }
                            if (communicationString.equals("Do you want to use your God special power?")) {
                                try {
                                    boolean gotResponse = false;
                                    while (!gotResponse) {

                                        System.out.println("Do you want to user your god special power? (Please answer with yes or no): ");
                                        String s = input.nextLine();
                                        System.out.println(s);
                                        if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no")) {
                                            gotResponse = true;
                                            supportBoolean = s.equalsIgnoreCase("yes");
                                            networkHandler.send(new UseofSpecialPowerEvent(iD, supportBoolean));
                                        }
                                    }

                                } catch (IOException ignored) {
                                }
                            }
                            if (communicationString.equals("You need to change your username")) {
                                System.out.println(communicationString);
                            }
                        }
                    }
                }catch (ClassCastException | JsonSyntaxException ignored) {}

                try {
                CardSelectionEvent event = (CardSelectionEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println(event.getSelectedGod());
                    }
                }catch (ClassCastException | JsonSyntaxException ignored) {}

                try {
                GameUpdatingEvent event = (GameUpdatingEvent) networkHandler.deserialization(receivedMessage.getContent());
                if(event != null && event.getTarget().equalsIgnoreCase(iD)){
                    game = event.getGame();
                    updateMap();
                    printScreen();
                    for(int i = 0; i< game.getPlayers().size(); i++){
                        if(game.getPlayers().get(i).getPlayerID().equals(iD)){
                            player = game.getPlayers().get(i);
                            break;
                        }
                    }
                }
            }
            catch (ClassCastException | JsonSyntaxException ignored) {}

                try {
                GodCardsSelectedEvent event = (GodCardsSelectedEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println(event.getSelectedCards());
                    }
                }
            catch (ClassCastException | JsonSyntaxException ignored) {}

                try {
                LoserPlayerEvent event = (LoserPlayerEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println("\t\t\t\tGAME OVER");
                    }
                }
            catch (ClassCastException | JsonSyntaxException ignored) {}

                try {
                WinnerPlayerEvent event = (WinnerPlayerEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println("YOU WIN");
                    }
                }
            catch (ClassCastException | JsonSyntaxException ignored) {}

                try{
                    InvalidInputEvent event = (InvalidInputEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println("Your input wasn't correct, please try again");
                    }

                }catch (ClassCastException | JsonSyntaxException ignored) {}

            try{
                InvalidMovementEvent event = (InvalidMovementEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                    System.out.println("Your move wasn't valid, please try again");
                }

            }catch (ClassCastException | JsonSyntaxException ignored) {}

            try{
                InvalidConstructionEvent event = (InvalidConstructionEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                    System.out.println("Your construction wasn't valid, please try again");
                }


            }catch (ClassCastException | JsonSyntaxException ignored) {}

            try{
                StarterSelectionEvent event = (StarterSelectionEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                    System.out.println(event.getStarter());
                }

            }catch (ClassCastException | JsonSyntaxException ignored) {}

            try{
                PlayerJoinedEvent event = (PlayerJoinedEvent) networkHandler.deserialization(receivedMessage.getContent());
                if (event != null && event.getTarget().equalsIgnoreCase(iD)) {
                    System.out.println(event.getP().getPlayerID());
                    System.out.println(event.getP().getPlayerAge());
                    System.out.println(event.getP().getColor());
                }

            }catch (ClassCastException | JsonSyntaxException ignored) {}

        }
    }

    private void findDivinity(String communicationString) {
        if (communicationString.contains("Apollo")) {
            Divinity div = new Apollo();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }

        if (communicationString.contains("Arthemis")) {
            Divinity div = new Arthemis();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Athena")) {
            Divinity div = new Athena();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Atlas")) {
            Divinity div = new Atlas();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Demeter")) {
            Divinity div = new Demeter();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Hephaestus")) {
            Divinity div = new Hephaestus();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Minotaur")) {
            Divinity div = new Minotaur();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Pan")) {
            Divinity div = new Pan();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
        if (communicationString.contains("Prometheus")) {
            Divinity div = new Prometheus();
            System.out.println(div.getName());
            System.out.println(div.getDescription());
        }
    }

    /**
     * Method used to update the client's map.
     */
    private void updateMap(){
        map = new MapCLI();
        for (int x=0; x < 5; x++){
            for (int y=0; y < 5; y++){
                Box boxGame;
                BoxCLI boxCLI;
                try{
                    if ((boxGame = game.getMap().getBox(x,y)).getLevel() != (boxCLI = map.getBoxCLI(x,y)).getLevel()){
                        boxCLI.setLevel(boxGame.getLevel());
                    }
                    if (boxGame.hasWorker()){
                        boxCLI.setWorker(boxGame.getWorker().getPlayer().getColor());
                    }
                    if (boxGame.hasDome()){
                        boxCLI.buildDome();
                    }
                }catch (InvalidIndicesException | WorkerNotExistException ignore) {}
            }
        }
    }

    public static void main(String[] args){
        new PlayerInterface();
    }

}
