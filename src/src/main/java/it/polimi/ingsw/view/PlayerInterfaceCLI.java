package it.polimi.ingsw.view;
import it.polimi.ingsw.network.client.NetworkHandler;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;

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
    private Map map;
    private Game game;
    private Player player;
    private String iD;
    private DivinityCLI divinityCLI;
    private boolean waitForServer;


    /**
     * Create Client's link to Server and begin player construction
     * @deprecated
     */
    public PlayerInterfaceCLI() {
        setPlayer();
    }


    /**
     * ask to server the available colors
     * @return list of available colors
     * @deprecated going to add a MVEvent CommunicationEvent
     */
    private String receivePlayerColors(){
        ArrayList<PlayerColor> list;
        waitForServer=true;
        //eventi di attesa colori: COMUNICATION EVENT
        waitForServerCLI();
        waitForServer=false;
        notifyAll();
        return list;
    }

    /**
     * print something on screen while
     */
    private void waitForServerCLI() {
        while (waitForServer) {
            try {
               wait(100,0);
                System.out.println("█");
            }
            catch (InterruptedException ignored){}
        }
        System.out.print("\n");
        notify();
    }
    /**
     * True Player Constructor
     * @deprecated fixare la parte dei colori
     */
    synchronized public void setPlayer() {
        String avialableColor;
        waitForServer=false;
        try {
            wait();
        } catch (InterruptedException ignored){}
        String iD;
        int age;
        System.out.println("Please, Insert Your Information");
        Scanner in=new Scanner(System.in);
        System.out.print("\tInsert ID (can't use space character) : ");
        iD = in.nextLine();
        while (iD.contains(" ")){
            System.out.print("\t\tPlease Insert Valid ID: ");
            iD = in.nextLine();
        }
        this.iD=iD;
        System.out.print("\tInsert Your Age: ");
        age = in.nextInt();
        while (age<0){
            System.out.print("\t\tPlease. Insert A Valid Age: ");
            age = in.nextInt();
        }
        receivePlayerColors();
        System.out.println("\tIn The End Select Your Color: Available Colours");
        /*metodo che stampa i colori*/
        // inserire il metodo che mi restituisce la classe Game dal Server
        if (a)

        waitForServer=true;
    }


    /**
     * print the screen
     * @param youPlay if the player is now play
     * @param endTurn if the print is been made in the end of turn
     */
    public void printScreen(boolean youPlay, boolean endTurn){
        waitForServer=false;
        try {
            wait();
        } catch (InterruptedException ignored){}
        String nameDivinity;
        if (youPlay){
            System.out.println("I'ts An Other Player Turn, Wait For Your");
        }else {
            System.out.println("I'ts Your Turn!!!");
        }
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
        if (youPlay&&!endTurn){
            System.out.println("Make Your Play:");
        }

    }

    public void win(){
        System.out.println("You Win :D");
    }

    public void lose(){
        System.out.println("You Lose :(");
    }

    public void build(){
        Scanner in=new Scanner(System.in);

    }


    private boolean usableBox(int x, int y, Box box){

    }


    /**
     * Communicate to server player's worker, make an input check and print new position on the map
     */
    public void move() throws WorkerNotExistException {
        Scanner in = new Scanner(System.in);
        int boxX, boxY;
        int index;
        Box box;
        Worker worker;
        if (player.canMove()){
            System.out.println("These are Your Worker: ");
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
            box=worker.getBox();
            System.out.println("Now You Can Move In A Green Box");
            for (int x=3; 0<x; x--){
                for (int y=3; y>0; y--){
                    if (box.getNeighboursMatrix()[x][y]==null){
                        System.out.print("\t\t"+ box.getNeighboursMatrix()[x][y].getPosition()[1] + "|");
                        System.out.print("\u001b[38;5;0m"+"█");
                    }
                    else{
                        if (worker.canMove(box.getNeighboursMatrix()[x][y])) {
                            System.out.print("\u001b[38;5;1m" + "█");
                        }
                        else {
                            System.out.print("\u001b[48;5;34m" + "█");
                        }
                    }
                }
            }
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
            new WorkerSelectedEvent(iD,worker);
            new BoxSelectedEvent(iD,box.getNeighboursMatrix()[boxX][boxY] );
        }
        else{
            System.out.println("Sorry You Haven't Movable Worker So");
            lose();
            //segnala la sconfitta al server
        }

    }

    /**
     * if this player is the first logged he must select number of player and type of the game
     */
    public void setTypeGame(){
        waitForServer=false;
        try {
            wait();
        } catch (InterruptedException ignored){}
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
        waitForServer=true;
    }

}
