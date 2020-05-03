package it.polimi.ingsw.view;
import com.sun.tools.javac.main.Option;
import it.polimi.ingsw.network.client.NetworkHandler;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.ConnectionRequest;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import it.polimi.ingsw.view.divinityCLI.*;
import org.graalvm.compiler.phases.graph.ScopedPostOrderNodeIterator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Gabriele Gatti
 *  This class implements the useful strument for the Player:
 *  his action and his support's operation
 */

public class PlayerInterfaceCLI {
    //avviso per il server
    private ArrayList<PlayerColor> vaildColors;
    private MapCLI mapCLI;
    private Game game;
    private Player player;
    private String iD;
    private DivinityCLI divinityCLI;
    private NetworkHandler networkHandler;
    private BoxCLI oldBoxCLI;
    private Worker workerMoved;
    private boolean blocked;
    /**
     * Create Client's link to Server and begin player construction
     */
    public PlayerInterfaceCLI() {

        System.out.println("Welcome To Santorini CLI");
        Scanner scanner=new Scanner(System.in);
        do {
            System.out.print("Please, Insert Your ID (Might Be Change In The Future): ");
            iD=scanner.nextLine();
        }while (iD.contains(" "));
        networkHandler = new NetworkHandler();
        networkHandler.send(new ConnectionRequest(iD));
        catchMVEvent();
    }

    /**
     * True Player Constructor
     * @deprecated fixare la parte dei colori
     */
    synchronized public void setPlayer() {
        String avialableColor;
        try {
            wait();
        } catch (InterruptedException ignored){}
        int age;
        System.out.println("Please, Insert Your Information");
        Scanner in=new Scanner(System.in);
        System.out.print("\tInsert Your Age: ");
        age = in.nextInt();
        while (age<0){
            System.out.print("\t\tPlease. Insert A Valid Age: ");
            age = in.nextInt();
        }
        System.out.println("\tIn The End Select Your Color: Available Colours");
        /*metodo che stampa i colori*/
        // inserire il metodo che mi restituisce la classe Game dal Server

    }

    /**
     * print the screen
     */
    public void printScreen(){
        String nameDivinity;
        mapCLI.printMap();
        if (game.isWithGods()){
            if(divinityCLI==null) {
                nameDivinity = ((PlayerDivinity) player).getGodName();
                if (nameDivinity.equals("Apollo")) {
                    divinityCLI = new ApolloCLI();
                }
                if (nameDivinity.equals("Artemis")) {
                    divinityCLI = new ArtemisCLI();
                }
                if (nameDivinity.equals("Athena")) {
                    divinityCLI = new AthenaCLI();
                }
                if (nameDivinity.equals("Atlas")) {
                    divinityCLI = new AtlasCLI();
                }
                if (nameDivinity.equals("Demeter")) {
                    divinityCLI = new DemeterCLI();
                }
                if (nameDivinity.equals("Hephaestus")) {
                    divinityCLI = new HephaestusCLI();
                }
                if (nameDivinity.equals("Pan")) {
                    divinityCLI = new PanCLI();
                }
                if (nameDivinity.equals("Prometheus")) {
                    divinityCLI = new PrometheusCLI();
                }
                if (nameDivinity.equals("Minotaur")||nameDivinity.equals("Asterios")) {
                    divinityCLI = new ApolloCLI();
                }
            }
            System.out.println("Your Divinity: " + divinityCLI.getName()+"\nDescription:" + divinityCLI.getDescription());
        }

    }

    /**
     * the real view's body catch all server communication and work consecutively
     */
    private void catchMVEvent() {
        Scanner input=new Scanner(System.in);
        String communicationString;
        String supportString;
        boolean supportBoolean;
        ArrayList<String> divinitysAviable;
        Object[] info;
        while (true) {
            try {
                communicationString = networkHandler.receiveCommunicationEvent();
                /* every communication event case*/
                if (blocked || !communicationString.equals("Your are active now")) {
                    System.out.print(" . . ");

                } else {
                    if (communicationString.substring(0,communicationString.length()-1).equals("Please insert the number of Players")) {
                            int numPla;
                            do {
                                try {
                                    do {
                                        System.out.print(communicationString + " <2/3> : ");
                                        numPla = input.nextInt();
                                    } while (!(numPla == 2 || numPla == 3));
                                    networkHandler.send(new NumPlayersSelectedEvent(iD, numPla));
                                } catch (IOException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.equals("Please insert your data, colors available are: ")) {
                            int age;
                            String colorS;
                            do {
                                try {
                                    do {
                                        System.out.println("\tPlease, insert your age: ");
                                        age = input.nextInt();
                                    } while (!(age <= 0 || age > 120));
                                    do {
                                        System.out.println("\tSelect Your Color: Available Colours <" + communicationString.subSequence(46, 52) + ", " + communicationString.subSequence(53, 57) + ", " + communicationString.subSequence(58, 61) + "> : ");
                                        colorS = input.nextLine();
                                    } while (!(colorS.equals("Yellow") || colorS.equals("Blue") || colorS.equals("Red")));
                                    PlayerColor color;
                                    switch (colorS) {
                                        case ("Yellow"):
                                            color = PlayerColor.YELLOW;
                                        case ("Blue"):
                                            color = PlayerColor.BLUE;
                                        default:
                                            color = PlayerColor.RED;
                                    }
                                    networkHandler.send(new PlayerDataEnteredEvent(iD, age, color));
                                } catch (IOException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 45).equals("Please select the cards, gods available are: ")) {
                            System.out.println(communicationString.subSequence(0, 44));
                            String selectedDivinity;
                            do {
                                if (communicationString.contains("Apollo")) {
                                    divinityCLI = new ApolloCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }

                                if (communicationString.contains("Artemis")) {
                                    divinityCLI = new ArtemisCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Athena")) {
                                    divinityCLI = new AthenaCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Atlas")) {
                                    divinityCLI = new AtlasCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Demeter")) {
                                    divinityCLI = new DemeterCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Hephaestus")) {
                                    divinityCLI = new HephaestusCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Minotaur")) {
                                    divinityCLI = new MinotaurCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Pan")) {
                                    divinityCLI = new PanCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Prometheus")) {
                                    divinityCLI = new PrometheusCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                try {
                                    ArrayList<String> divinitySelection = new ArrayList<String>();
                                    for (int i = 0; i < game.getNumPlayers(); i++) {
                                        do {
                                            System.out.print("Select divinity #" + i + " : ");
                                            selectedDivinity = input.nextLine();
                                        } while (!((selectedDivinity.equals("Apollo") || selectedDivinity.equals("Artemis") || selectedDivinity.equals("Athena") || selectedDivinity.equals("Atlas") || selectedDivinity.equals("Demeter") || selectedDivinity.equals("Hephaestus") || selectedDivinity.equals("Minotaur") || selectedDivinity.equals("Pan") || selectedDivinity.equals("Prometeus")) && !(divinitySelection.contains(selectedDivinity))));
                                        divinitySelection.add(selectedDivinity);
                                    }
                                    networkHandler.send(new GodsSelectedEvent(iD, divinitySelection));
                                } catch (IOException ignored) {
                                }

                                try {
                                    networkHandler.receivePlayerJoinedEvent();
                                    break;
                                } catch (InvalidSenderException ignored) {
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 30).equals("Do you want to play with gods?")) {
                            try {
                                do {
                                    System.out.print(communicationString + "<yes/no>: ");
                                    supportString = input.nextLine();
                                } while (!(supportString.equals("yes") || supportString.equals("no") || supportString.equals("No") || supportString.equals("Yes")));
                                supportBoolean = supportString.equals("yes") || supportString.equals("Yes");
                                networkHandler.send(new WithGodsSelectedEvent(iD, supportBoolean));
                            } catch (IOException ignored) {
                            }
                        }
                    if (communicationString.substring(0, 39).equals("Select your Card, cards available are: ")) {
                            String selectedDivinity;
                            ArrayList<String> godSelected = new ArrayList<>();
                            supportString = communicationString.substring(40, communicationString.length());
                            do {
                                if (communicationString.contains("Apollo")) {
                                    divinityCLI = new ApolloCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }

                                if (communicationString.contains("Artemis")) {
                                    divinityCLI = new ArtemisCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Athena")) {
                                    divinityCLI = new AthenaCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Atlas")) {
                                    divinityCLI = new AtlasCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Demeter")) {
                                    divinityCLI = new DemeterCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Hephaestus")) {
                                    divinityCLI = new HephaestusCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Minotaur")) {
                                    divinityCLI = new MinotaurCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Pan")) {
                                    divinityCLI = new PanCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                if (communicationString.contains("Prometheus")) {
                                    divinityCLI = new PrometheusCLI();
                                    System.out.println(divinityCLI.getName());
                                    System.out.println(divinityCLI.getDescription());
                                }
                                try {
                                    do {
                                        System.out.print("Select you divinity: ");
                                        selectedDivinity = input.nextLine();
                                    } while (!(communicationString.contains(selectedDivinity) && (selectedDivinity.equals("Apollo") || selectedDivinity.equals("Artemis") || selectedDivinity.equals("Athena") || selectedDivinity.equals("Atlas") || selectedDivinity.equals("Demeter") || selectedDivinity.equals("Hephaestus") || selectedDivinity.equals("Minotaur") || selectedDivinity.equals("Pan") || selectedDivinity.equals("Prometeus"))));
                                    godSelected.add(selectedDivinity);
                                    switch (selectedDivinity) {
                                        case "Apollo":
                                            divinityCLI = new ApolloCLI();
                                        case "Artemis":
                                            divinityCLI = new ArtemisCLI();
                                        case "Athena":
                                            divinityCLI = new AthenaCLI();
                                        case "Atlas":
                                            divinityCLI = new AtlasCLI();
                                        case "Demeter":
                                            divinityCLI = new DemeterCLI();
                                        case "Hephaestus":
                                            divinityCLI = new HephaestusCLI();
                                        case "Minotaur":
                                            divinityCLI = new MinotaurCLI();
                                        case "Pan":
                                            divinityCLI = new PanCLI();
                                        default:
                                            divinityCLI = new PrometheusCLI();
                                    }
                                    networkHandler.send(new GodsSelectedEvent(iD, godSelected));
                                } catch (IOException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 45).equals("Select the Starter, players in the game are: ")) {
                            String playerSelect;
                            try {
                                do {
                                    System.out.println("\tThere's other's player:");
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
                    if (communicationString.substring(0, 27).equals("Please Select the first box")) {
                            int coordinateX, coordinateY;
                            mapCLI.printMap();
                            do {
                                printScreen();
                                try {
                                    do {
                                        System.out.print("Select your worker's first box (only whitout player's)\n\tcoordinates X: ");
                                        coordinateX = input.nextInt();
                                        System.out.println("\tcoordinates Y:");
                                        coordinateY = input.nextInt();
                                    } while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker())));
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                } catch (IOException | InvalidIndicesException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 28).equals("Please Select the second box")) {
                            int coordinateX, coordinateY;
                            mapCLI.printMap();
                            do {
                                printScreen();
                                try {
                                    do {
                                        System.out.print("Select your worker's second box (only whitout player's)\n\tcoordinates X: ");
                                        coordinateX = input.nextInt();
                                        System.out.println("\tcoordinates Y:");
                                        coordinateY = input.nextInt();
                                    } while (!((coordinateX >= 0 && coordinateX < 5) && (coordinateY >= 0 && coordinateY < 5) && !(game.getMap().getBox(coordinateX, coordinateY).hasWorker())));
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                } catch (IOException | InvalidIndicesException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 30).equals("Please Select one box to build")) {
                            printScreen();
                            int coordinateX, coordinateY;
                            //Box box;

                            do {
                                while (true) {
                                    try {
                                        do {
                                            System.out.print("Select a box where build:\n\t coordinate X:");
                                            coordinateX = input.nextInt();
                                            System.out.print("\t coordinate Y:");
                                            coordinateY = input.nextInt();
                                        } while (!(coordinateX >= 0 && coordinateX < 4 && coordinateY >= 0 && coordinateY < 5 && !game.getMap().getBox(coordinateX, coordinateY).hasWorker() && !game.getMap().getBox(coordinateX, coordinateY).hasDome() && game.getMap().getBox(coordinateX, coordinateY).getLevel() < 3));

                                        networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                        break;
                                    } catch (IOException | InvalidIndicesException ignored) {
                                    }
                                }
                                try {
                                    networkHandler.receiveInvalidConstructionEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 29).equals("Please Select one box to move")) {
                            printScreen();
                            int coordinateX, coordinateY/*, workerNum*/;

                            do {
                                //Box oldBox = workerMoved.getBox();
                                do {
                                    System.out.print("Select a box where move that worker\n\tcoordinate X: ");
                                    coordinateX = input.nextInt();
                                    System.out.print("\tcoordinate Y: ");
                                    coordinateY = input.nextInt();
                                } while (!(coordinateX >= 0 && coordinateX < 5 && coordinateY >= 0 && coordinateY < 5));
                                try {
                                    networkHandler.send(new BoxSelectedEvent(iD, game.getMap().getBox(coordinateX, coordinateY)));
                                } catch (InvalidIndicesException | IOException ignored) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 24).equals("Please Select one worker")) {
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
                                } while (workerNum < 0 || workerNum >= player.getWorkers().size());
                                workerMoved = player.getWorkers().get(workerNum);
                                try {
                                    networkHandler.send(new WorkerSelectedEvent(iD, workerMoved));
                                } catch (IOException ignore) {
                                }
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 19).equals("Your are active now")) {
                            blocked = false;
                        }
                    if (communicationString.substring(0, 49).equals("Please wait for the opponent to finish its action")) {
                            blocked = true;
                        }
                    if (communicationString.substring(0, 40).equals("Game over, thanks for playing Santorini!")) {
                            System.out.println("\t\t\t\t" + communicationString);
                            break;
                        }
                    if (communicationString.substring(0, 23).equals("Do you want to restart?")) {
                            do {
                                System.out.print(communicationString + " <yes/no> : ");
                                supportString = input.next();
                            } while (!(supportString.equals("yes") || supportString.equals("Yes") || supportString.equals("no") || supportString.equals("No")));
                            supportBoolean = supportString.equals("yes") || supportString.equals("Yes");
                            try {
                                networkHandler.send(new RestartConfirmationEvent(iD, supportBoolean));
                            } catch (IOException ignored) {
                            }
                        }
                    if (communicationString.substring(0, 42).equals("Please wait, the host is creating the game")) {
                            System.out.println(communicationString);
                            do {
                                try {
                                    supportString = networkHandler.receiveCommunicationEvent();
// controllare che cio mi permetta di avere un ciclo ottimale
                                    if (supportString.equals("Your are active now") || supportString.equals("Please insert your data, colors available are: ")) {
                                        break;
                                    }
                                } catch (InvalidSenderException ignored) {
                                }
                            } while (true);
                        }
                    if (communicationString.substring(0, 34).equals("Sorry but the game is already full") || communicationString.substring(0, 40).equals("The host decided to conclude the matches")) {
                            System.out.println(communicationString);
                            break;
                        }
                    if (communicationString.substring(0, 42).equals("Do you want to use your God special power?")) {
                            System.out.println("Your divinity:" + divinityCLI.getName() + divinityCLI.getDescription());
                            do {
                                System.out.print(communicationString + " <yes/no> : ");
                                supportString = input.next();
                            } while (!(supportString.equals("yes") || supportString.equals("Yes") || supportString.equals("no") || supportString.equals("No")));
                            supportBoolean = supportString.equals("yes") || supportString.equals("Yes");
                            try {
                                networkHandler.send(new RestartConfirmationEvent(iD, supportBoolean));
                            } catch (IOException ignored) {
                            }
                        }
                    if (communicationString.substring(0, 32).equals("You need to change your username")) {
                            do {
                                System.out.println("You have to change your USER ID : ");
                                //supportString = input.nextLine();
                                try {
                                    networkHandler.receiveInvalidInputEvent();
                                } catch (InvalidSenderException e) {
                                    break;
                                }
                            } while (true);
                        }
                }
            }
            catch (InvalidSenderException a) {
                try {
                    networkHandler.receiveCardSelectionEvent();
                    }
                catch (InvalidSenderException b) {
                    try {
                        game = networkHandler.receiveGameUpdatingEvent();
                        updateMapCLIToMap();
                        printScreen();
                    }
                    catch (InvalidSenderException c) {
                        try {
                            divinitysAviable = networkHandler.receiveGodCardsSelectedEvent();
                        }
                        catch (InvalidSenderException e) {
                            try {
                                networkHandler.receiveLoserPlayerEvent();
                                System.out.println("\t\t\t\tGAME OVER");
                                 /*try {
                                     do{
                                         System.out.print("Want You Play Another Time? <yes/no>: ");
                                         supportString=input.nextLine();
                                     }
                                     while(! (supportString.equals("yes")||supportString.equals("no")||supportString.equals("No")||supportString.equals("Yes")));

                                     if (supportString.equals("yes") || supportString.equals("Yes")) {
                                         networkHandler.send(new RestartConfirmationEvent(iD, true));
                                     }
                                     else{
                                         networkHandler.send(new RestartConfirmationEvent(iD, false));
                                         break;
                                     }
                                 }catch (IOException ignored){}*/
                                }
                            catch (InvalidSenderException l) {
                                    /*try {
                                        communicationString = networkHandler.receiveStarterSelectionEvent();
                                        do {
                                            System.out.println("Select The Starter Player");
                                            for (int i = 0; i<game.getPlayers().size();i++) {

                                            }
                                                supportString = input.nextLine();
                                            }
                                        } catch (InvalidSenderException g) {*/
                                            try {
                                                networkHandler.receiveWinnerPlayerEvent();
                                                System.out.println("\\t\\t\\t\\tYOU WIN :D");
                                                try {
                                                    do {
                                                        System.out.print("Want You Play Another Time? <yes/no>: ");
                                                        supportString = input.nextLine();
                                                    }
                                                    while (!(supportString.equals("yes") || supportString.equals("no") || supportString.equals("No") || supportString.equals("Yes")));

                                                    if (supportString.equals("yes") || supportString.equals("Yes")) {
                                                        networkHandler.send(new RestartConfirmationEvent(iD, true));
                                                    } else {
                                                        networkHandler.send(new RestartConfirmationEvent(iD, false));
                                                        break;
                                                    }
                                                } catch (IOException ignored) {
                                                }
                                            } catch (InvalidSenderException ignored) { }
                            }
                        }
                    }
                }
            }
        }
    }


    public void updateMapCLIToMap() {
        for (int x=0; x<5; x++){
            for (int y=0; y<5; y++){
                Box boxGame;
                BoxCLI boxCLI;
                try{
                    if ((boxGame=game.getMap().getBox(x,y)).getLevel()!=(boxCLI=mapCLI.getBoxCLI(x,y)).getLevel()){
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



/*    public void build(Scanner in) {
        System.out.println("You've Moved, Now You'Have To Build.");
        int boxX, boxY;
        try {
            Box box = game.getMap().getBox(workerMoved.getBox().getPosition()[0], workerMoved.getBox().getPosition()[1]);
            Box box1;
            for (int y=3; y>0; y--){
                if (y==3) System.out.print((box.getPosition()[1]+1)+" | ");
                if (y==2) System.out.print((box.getPosition()[1])+" | ");
                if (y==1) System.out.print((box.getPosition()[1]-1)+" | ");
                for (int x=0; x<3; x++){
                    box1=box.getNeighboursMatrix()[x][y];
                    if (box.getNeighboursMatrix()[x][y]==null){
                        System.out.print("\u001b[38;5;0m"+"█");
                    }
                    else{
                        if (box1.getLevel()==3||box1.hasDome()) {
                            System.out.print("\u001b[38;5;1m" + "█");
                        }
                        else {
                            System.out.print("\u001b[48;5;34m" + "█");
                        }
                    }
                }
                System.out.print("\n");
            }
            System.out.println(" ______\n ");
            if (box.getPosition()[0]-1>=0)
                System.out.print((box.getPosition()[0]-1));
            else System.out.print("-");
            if (box.getPosition()[0]>=0)
                System.out.print((box.getPosition()[0]));
            else System.out.print("-");
            if (box.getPosition()[0]+1>=0)
                System.out.print((box.getPosition()[0]+1));
            else System.out.print("-");
            } catch (InvalidIndicesException ignore){}
            while (true) {
                System.out.print("Box Coordinates X:");
                boxX = in.nextInt();
                System.out.print("Box Coordinates Y:");
                boxY = in.nextInt();
                try {
                    mapCLI.build(boxX, boxY);
                    workerMoved = null;
                    break;
                } catch (InputFailedException e) {
                    System.out.println("σφάλμα INSERT VALID INFOS");
                }
            }

    }

    /**
     * Communicate to server player's worker, make an input check and print new position on the map
     */
  /*  public void move(Scanner in) {
        int boxX, boxY;
        int index;
        Box box;
        Worker worker;
        try {
            if (this.player.canMove()){
                System.out.println("\nThese are your worker: ");
                for (int i=0; i<player.getWorkers().size();i++){
                    if (player.getWorkers().get(i).canMove()){
                        System.out.println("\t (" + (player.getWorkers().get(i).getBox().getPosition()[0]) + (player.getWorkers().get(i).getBox().getPosition()[1]) + ") worker #" +i);
                    }
                }
                do {
                    System.out.print("Select Worker #: ");
                    index = in.nextInt();
                }while (index<0||index>3);
                worker=player.getWorkers().get(index);
                workerMoved=worker;
                box=worker.getBox();
                Box box1;
                System.out.println("Now You Can Move In A Green Box");
                for (int y=3; y>0; y--){
                    if (y==3) System.out.print((box.getPosition()[1]+1)+" | ");
                    if (y==2) System.out.print((box.getPosition()[1])+" | ");
                    if (y==1) System.out.print((box.getPosition()[1]-1)+" | ");
                    for (int x=0; x<3; x++){
                        box1=box.getNeighboursMatrix()[x][y];
                        if (box.getNeighboursMatrix()[x][y]==null){
                            System.out.print("\u001b[38;5;0m"+"█");
                        }
                        else{
                            if (!worker.canMove(box.getNeighboursMatrix()[x][y])) {
                                System.out.print("\u001b[38;5;1m" + "█");
                            }
                            else {
                                System.out.print("\u001b[48;5;34m" + "█");
                            }
                        }
                    }
                    System.out.print("\n");
                }
                System.out.println(" ______\n ");
                if (box.getPosition()[0]-1>=0)
                    System.out.print((box.getPosition()[0]-1));
                else System.out.print("-");
                if (box.getPosition()[0]>=0)
                    System.out.print((box.getPosition()[0]));
                else System.out.print("-");
                if (box.getPosition()[0]+1>=0)
                    System.out.print((box.getPosition()[0]+1));
                else System.out.print("-");

                while (true){
                    System.out.print("Box Coordinates X:");
                    boxX=in.nextInt();
                    System.out.print("Box Coordinates Y:");
                    boxY=in.nextInt();
                    try {
                        mapCLI.moveWorker(boxX,boxY,worker);
                        break;
                    }
                    catch (InputFailedException e){
                        System.out.println("σφάλμα INSERT VALID INFOS");
                    }
                }

            }
            else{
                System.out.println("Sorry You Haven't Movable Worker So");
                lose();
                //segnala la sconfitta al server
            }
        } catch (WorkerNotExistException e) {
            e.printStackTrace();
        }

    }*/

    /*/**
     * if this player is the first logged he must select number of player and type of the game
     *//*
    public void setTypeGame(){

        int num;
        int type;
        boolean whitGods;
        Scanner in=new Scanner(System.in);
        do{
            System.out.print("You Are The First Player, Select Number Of Players (Just 2 Or 3): ");
            num=in.nextInt();
            if (num<=3 && num>=2){
                if (num==3){
                    type=2;
                }
                else{
                    System.out.println("Select Type Of Game:\n\t1-Not Whit Divinity\n\t2-Whit Divinity\n: ");
                    type=in.nextInt();
                    if (type==2||type==1){
                        whitGods= type == 2;
                        break;
                    }
                }
            }
        }while (true);
        new NumPlayersSelectedEvent(iD,num);
        new WithGodsSelectedEvent(iD,whitGods);
    }
*/
}
