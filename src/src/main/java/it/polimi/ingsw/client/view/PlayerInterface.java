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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Gabriele Gatti
 *  Class used to manage the user experience during the game.
 */

public class PlayerInterface {

    private Map map;
    private Game game;
    private Player player;
    private String iD;
    private Divinity divinity;
    private Worker workerMoved;

    private NetworkHandler networkHandler;
    private static Scanner input;
    private static SocketConnection connection;
    private static final int port = 65535;

    /**
     * Create Client's connection to the Server and starts game
     */
    public PlayerInterface() throws FileNotFoundException {

        input = new Scanner(System.in);
        boolean connected = false;
        while(!connected){

            System.out.println("Insert your Username");

            String username = input.nextLine();
            connection= new SocketConnection(username, port);
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

       /* networkHandler.sendPing();
        networkHandler.receivePing();

        */
        catchMVEvent();
    }

    /**
     * Method used to update the screen
     */
    public void printScreen(){
        String nameDivinity;
        map.printMap();
        if (game.isWithGods()){
            if(divinity ==null) {
                nameDivinity = ((PlayerDivinity) player).getGodName();
                if (nameDivinity.equals("Apollo")) {
                    divinity = new Apollo();
                }
                if (nameDivinity.equals("Artemis")) {
                    divinity = new Artemis();
                }
                if (nameDivinity.equals("Athena")) {
                    divinity = new Athena();
                }
                if (nameDivinity.equals("Atlas")) {
                    divinity = new Atlas();
                }
                if (nameDivinity.equals("Demeter")) {
                    divinity = new Demeter();
                }
                if (nameDivinity.equals("Hephaestus")) {
                    divinity = new Hephaestus();
                }
                if (nameDivinity.equals("Pan")) {
                    divinity = new Pan();
                }
                if (nameDivinity.equals("Prometheus")) {
                    divinity = new Prometheus();
                }
                if (nameDivinity.equals("Minotaur")) {
                    divinity = new Minotaur();
                }
            }
            System.out.println("Your Divinity: " + divinity.getName()+"\nDescription:" + divinity.getDescription());
        }
    }

    /**
     * Method used to communicate with the server and progress in the game.
     */
    private void catchMVEvent() {
        String communicationString;
        boolean supportBoolean;
        ArrayList<String> godsAvailable;

        while (true) {
                Message receivedMessage = networkHandler.receiveMessage();

                try{
                    CommunicationEvent event = (CommunicationEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    communicationString = event.getMessage();

                    if(!event.getTarget().equalsIgnoreCase(iD)){

                    }else {

                        if (communicationString.equals("Please wait for the opponent to finish its action")) {
                            System.out.print("Please wait for your turn");
                            //wait(10000);

                        }
                        if (communicationString.equals("Please insert the number of Players")) {
                            int numPla;
                            try {
                                do {
                                    System.out.print(communicationString + " <2/3> : ");
                                    numPla = input.nextInt();
                                } while (!(numPla == 2 || numPla == 3));
                                networkHandler.send(new NumPlayersSelectedEvent(iD, numPla));
                            } catch (IOException ignored) {}
                        }
                        if (communicationString.contains("Please insert your data, colors available are:")) {
                            int age;
                            String color;
                            do {
                                try {
                                    do {
                                        System.out.println("\tPlease, insert your age: ");
                                        age = 12; //For test purposes

                                    } while ((age <= 0 || age > 120));
                                    System.out.println("Select a color, " + communicationString.substring(25));
                                    do {
                                        color = input.nextLine();
                                        System.out.println(color);
                                    } while (!(color.equalsIgnoreCase("Yellow") || color.equalsIgnoreCase("Blue") || color.equalsIgnoreCase("Red")));
                                    PlayerColor colorPlayer = null;
                                    if(color.equalsIgnoreCase("Yellow")) {
                                        colorPlayer = PlayerColor.YELLOW;
                                    }
                                    if(color.equalsIgnoreCase("Blue")) {
                                        colorPlayer = PlayerColor.BLUE;
                                    }
                                    if(color.equalsIgnoreCase("Red")){
                                        colorPlayer = PlayerColor.RED;
                                    }
                                    networkHandler.send(new PlayerDataEnteredEvent(iD, age, colorPlayer));
                                    System.out.println("Data sent");
                                    break;
                                } catch (IOException ignored) {}

                            } while (true);
                        }
                        if (communicationString.contains("Please select the cards, gods available are:")) {
                            System.out.println(communicationString.subSequence(0, 43));
                            String selectedDivinity;
                            do {
                                if (communicationString.contains("Apollo")) {
                                    divinity = new Apollo();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Artemis")) {
                                    divinity = new Artemis();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Athena")) {
                                    divinity = new Athena();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Atlas")) {
                                    divinity = new Atlas();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Demeter")) {
                                    divinity = new Demeter();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Hephaestus")) {
                                    divinity = new Hephaestus();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Minotaur")) {
                                    divinity = new Minotaur();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Pan")) {
                                    divinity = new Pan();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Prometheus")) {
                                    divinity = new Prometheus();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                try {
                                    ArrayList<String> divinitySelection = new ArrayList<>();
                                    for (int i = 0; i < game.getNumPlayers(); i++) {
                                        do {
                                            System.out.println("Select divinity #" + i + " : ");
                                            selectedDivinity = input.nextLine();
                                        } while (!((selectedDivinity.equals("Apollo") || selectedDivinity.equals("Artemis") || selectedDivinity.equals("Athena") || selectedDivinity.equals("Atlas") || selectedDivinity.equals("Demeter") || selectedDivinity.equals("Hephaestus") || selectedDivinity.equals("Minotaur") || selectedDivinity.equals("Pan") || selectedDivinity.equals("Prometeus")) && !(divinitySelection.contains(selectedDivinity))));
                                        divinitySelection.add(selectedDivinity);
                                    }
                                    networkHandler.send(new GodsSelectedEvent(iD, divinitySelection));
                                    break;
                                } catch (IOException ignored) {}
                            } while (true);
                        }
                        if (communicationString.equals("Do you want to play with gods?")) {
                            try { boolean gotResponse = false;
                                while(!gotResponse) {

                                        System.out.println("Do you want to play with gods? (Please answer with yes or no): ");
                                        String s = input.nextLine();

                                     if(s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
                                    {
                                        supportBoolean = s.equalsIgnoreCase("yes");
                                        networkHandler.send(new WithGodsSelectedEvent(iD, supportBoolean));
                                        gotResponse = true;
                                    }
                                }

                            } catch (IOException ignored) {}
                        }
                        if (communicationString.contains("Select your Card, cards available are:")) {
                            String selectedDivinity;
                            ArrayList<String> godSelected = new ArrayList<>();

                            do {
                                if (communicationString.contains("Apollo")) {
                                    divinity = new Apollo();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }

                                if (communicationString.contains("Artemis")) {
                                    divinity = new Artemis();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Athena")) {
                                    divinity = new Athena();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Atlas")) {
                                    divinity = new Atlas();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Demeter")) {
                                    divinity = new Demeter();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Hephaestus")) {
                                    divinity = new Hephaestus();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Minotaur")) {
                                    divinity = new Minotaur();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Pan")) {
                                    divinity = new Pan();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                if (communicationString.contains("Prometheus")) {
                                    divinity = new Prometheus();
                                    System.out.println(divinity.getName());
                                    System.out.println(divinity.getDescription());
                                }
                                try {
                                    do {
                                        System.out.print("Select you divinity: ");
                                        selectedDivinity = input.nextLine();
                                    } while (!(communicationString.contains(selectedDivinity)));
                                    godSelected.add(selectedDivinity);
                                    switch (selectedDivinity) {
                                        case "Apollo":
                                            divinity = new Apollo();
                                        case "Artemis":
                                            divinity = new Artemis();
                                        case "Athena":
                                            divinity = new Athena();
                                        case "Atlas":
                                            divinity = new Atlas();
                                        case "Demeter":
                                            divinity = new Demeter();
                                        case "Hephaestus":
                                            divinity = new Hephaestus();
                                        case "Minotaur":
                                            divinity = new Minotaur();
                                        case "Pan":
                                            divinity = new Pan();
                                        default:
                                            divinity = new Prometheus();
                                    }
                                    networkHandler.send(new GodsSelectedEvent(iD, godSelected));
                                    break;
                                } catch (IOException ignored) {}

                            } while (true);
                        }
                        if (communicationString.contains("Select the Starter, players in the game are:")) {
                            String playerSelect;
                            try {
                                do {
                                    System.out.println("\tSelect the starter, IDs available are:");
                                    for (int i = 0; i < game.getNumPlayers(); i++) {
                                        System.out.println("\t\t>" + game.getIDs().get(i));
                                    }
                                    System.out.print("\tSelect player using his ID: ");
                                    playerSelect = input.nextLine();
                                } while (!(game.getIDs().contains(playerSelect)));
                                networkHandler.send(new StarterSelectedEvent(iD, playerSelect));
                            } catch (IOException ignored) {
                            }
                        }
                        if (communicationString.equals("Please Select the first box")) {
                            int coordinateX, coordinateY;
                            map.printMap();
                            do {
                                printScreen();
                                try {
                                    do {
                                        System.out.print("Select your first worker's starting box (only empty boxes can be chosen)\n\tcoordinates X: ");
                                        coordinateX = input.nextInt();
                                        System.out.println("\tcoordinate Y:");
                                        coordinateY = input.nextInt();
                                    } while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker())));
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                    break;
                                } catch (IOException | InvalidIndicesException ignored) {}

                            } while (true);
                        }
                        if (communicationString.equals("Please Select the second box")) {
                            int coordinateX, coordinateY;
                            map.printMap();
                            do {
                                printScreen();
                                try {
                                    do {
                                        System.out.print("Select your second worker's starting box (only empty boxes can be chosen)\n\tcoordinates X: ");
                                        coordinateX = input.nextInt();
                                        System.out.println("\tcoordinates Y:");
                                        coordinateY = input.nextInt();
                                    } while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker())));
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                    break;
                                } catch (IOException | InvalidIndicesException ignored) {}

                            } while (true);
                        }
                        if (communicationString.equals("Please Select one box to build")) {
                            printScreen();
                            int coordinateX, coordinateY;

                            while (true) {
                                try {
                                    do {
                                        System.out.print("Select a box to build in :\n\t coordinate X:");
                                        coordinateX = input.nextInt();
                                        System.out.println("\t coordinate Y:");
                                        coordinateY = input.nextInt();

                                    } while (!(coordinateX >= 0 && coordinateX < 5 && coordinateY >= 0 && coordinateY < 5 && !game.getMap().getBox(coordinateX, coordinateY).hasWorker() && !game.getMap().getBox(coordinateX, coordinateY).hasDome() && game.getMap().getBox(coordinateX, coordinateY).getLevel() < 3));

                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                    break;
                                } catch (IOException | InvalidIndicesException ignored) {}
                            }
                        }
                        if (communicationString.equals("Please Select one box to move")) {
                            printScreen();

                            int coordinateX, coordinateY;

                            do {
                                do {
                                    System.out.print("Select a box to move the worker  \n\tcoordinate X: ");
                                    coordinateX = input.nextInt();
                                    System.out.print("\tcoordinate Y: ");
                                    coordinateY = input.nextInt();
                                } while (!(coordinateX >= 0 && coordinateX < 5 && coordinateY >= 0 && coordinateY < 5));
                                try {
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                    break;
                                } catch (InvalidIndicesException | IOException ignored) {}

                            } while (true);
                        }
                        if (communicationString.equals("Please Select one worker")) {
                            int workerNum;
                            System.out.println("It's your turn, please select your worker ");
                            do {
                                do {
                                    System.out.println("These are your workers:");
                                    for (int i = 0; i < player.getWorkers().size(); i++) {
                                        System.out.println("\t> #" + i + " position (x: " + player.getWorkers().get(i).getBox().getPosition()[0] + ", y: " + player.getWorkers().get(i).getBox().getPosition()[1] + ")");
                                    }
                                    System.out.print("Select your worker: ");
                                    workerNum = input.nextInt();
                                    workerMoved = player.getWorkers().get(workerNum);
                                } while (workerNum < 0 || workerNum >= player.getWorkers().size() || !workerMoved.canMove());

                                try {
                                    networkHandler.send(new WorkerSelectedEvent(iD, workerMoved));
                                } catch (IOException ignore) {}

                            } while (true);
                        }
                        if (communicationString.equals("Your are active now")) {
                            System.out.println(communicationString);
                        }
                        if (communicationString.equals("Game over, thanks for playing Santorini!")) {
                            System.out.println("\t\t\t\t" + communicationString);
                        }
                        if (communicationString.equals("Do you want to restart?")) {
                            try { boolean gotResponse = false;
                                while(!gotResponse) {

                                    System.out.println("Do you want to restart? (Please answer with yes or no): ");
                                    String s = input.nextLine();
                                    System.out.println(s);
                                    if(s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
                                    {
                                        supportBoolean = s.equalsIgnoreCase("yes");
                                        networkHandler.send(new RestartConfirmationEvent(iD, supportBoolean));
                                        gotResponse = true;
                                    }
                                }

                            } catch (IOException ignored) {}
                        }
                        if (communicationString.equals("Please wait, the host is creating the game")) {
                            System.out.println(communicationString);
                        }
                        if (communicationString.equals("Sorry but the game is already full") || communicationString.equals("The host decided to conclude the matches")) {
                            System.out.println(communicationString);
                            break;
                        }
                        if (communicationString.equals("Do you want to use your God special power?")) {
                            System.out.println("Your divinity:" + divinity.getName() + divinity.getDescription());
                            try { boolean gotResponse = false;
                                while(!gotResponse) {

                                    System.out.println("Do you want to user your god special power? (Please answer with yes or no): ");
                                    String s = input.nextLine();
                                    System.out.println(s);
                                    if(s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
                                    {
                                        supportBoolean = s.equalsIgnoreCase("yes");
                                        networkHandler.send(new UseofSpecialPowerEvent(iD, supportBoolean));
                                        gotResponse = true;
                                    }
                                }

                            } catch (IOException ignored) {}
                        }
                        if (communicationString.equals("You need to change your username")) {
                            System.out.println(communicationString);
                        }
                        if (communicationString.equals("The host decided to conclude the matches")) {
                            System.out.println(communicationString);
                            break;
                        }
                    }


            }catch (ClassCastException | JsonSyntaxException e) {}

                try {
                CardSelectionEvent event = (CardSelectionEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if(event.getTarget().equalsIgnoreCase(iD)){
                    System.out.println(event.getSelectedGod());
                }
            }catch (ClassCastException | JsonSyntaxException e) {}

                try {
                GameUpdatingEvent event = (GameUpdatingEvent) networkHandler.deserialization(receivedMessage.getContent());
                if(event != null && event.getTarget().equalsIgnoreCase(iD)){
                    game = event.getGame();
                    updateMap();
                    printScreen();
                    for(int i = 0; i< game.getPlayers().size(); i++){
                        if(game.getPlayers().get(i).getPlayerID().equalsIgnoreCase(iD)){
                            player = game.getPlayers().get(i);
                            break;
                        }
                    }
                }

            }
            catch (ClassCastException | JsonSyntaxException e) {}

                try {
                GodCardsSelectedEvent event = (GodCardsSelectedEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if(event.getTarget().equalsIgnoreCase(iD)){
                    godsAvailable = event.getSelectedCards();
                }
            }
            catch (ClassCastException | JsonSyntaxException e) {}

                try {
                LoserPlayerEvent event = (LoserPlayerEvent) JsonHelper.deserialization(receivedMessage.getContent());
                if(event.getTarget().equalsIgnoreCase(iD)) {
                    System.out.println("\t\t\t\tGAME OVER");
                }
            }
            catch (ClassCastException | JsonSyntaxException e) {}

                try {
                WinnerPlayerEvent event = (WinnerPlayerEvent) JsonHelper.deserialization(receivedMessage.getContent());
                    if(event.getTarget().equalsIgnoreCase(iD)) {
                        System.out.println("\\t\\t\\t\\tYOU WIN");
                    }
            }
            catch (ClassCastException | JsonSyntaxException e) {}

        }
    }

    /**
     * Method used to update the client's map.
     */
    public void updateMap() {
        for (int x=0; x<5; x++){
            for (int y=0; y<5; y++){
                Box boxGame;
                BoxCLI boxCLI;
                try{
                    if ((boxGame=game.getMap().getBox(x,y)).getLevel()!=(boxCLI = map.getBoxCLI(x,y)).getLevel()){
                        boxCLI.setLevel(boxGame.getLevel());
                        if (boxGame.hasWorker()){
                            boxCLI.setWorker(boxGame.getWorker().getPlayer().getColor());
                        }
                        if (boxGame.hasDome()){
                            boxCLI.buildDome();
                        }
                    }
                }catch (InvalidIndicesException | WorkerNotExistException | InputFailedException ignore) {}
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        new PlayerInterface();
    }

}
