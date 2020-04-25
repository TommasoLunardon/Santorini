package it.polimi.ingsw.view;
import it.polimi.ingsw.network.client.NetworkHandler;
import it.polimi.ingsw.network.events.vcevents.PlayerDataEnteredEvent;
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
    private DivinityCLI divinityCLI;
   // private boolean waitForServer;


    /**
     * Create Client's link to Server and begin player construction
     * @deprecated
     */
    public PlayerInterfaceCLI(NetworkHandler networkHandler, String origin) {
        setPlayer(origin);
    }

    /**
     * ask to server the available colors
     * @return list of available colors
     */
    /*private String receivePlayerColors(){
        ArrayList<PlayerColor> list;
        waitForServer=true;
        //eventi di attesa colori
        waitForServerCLI();
        waitForServer=false;
        notifyAll();
        return list;
    }*/

    /**
     * print something on screen while
     */
    /*private void waitForServerCLI() {
        while (waitForServer) {
            try {
               wait(100,0);
                System.out.println("█");

            }
            catch (InterruptedException e){}
        }
        System.out.print("\n");
    }*/
    /**
     * True Player Constructor
     */
    synchronized public void setPlayer(String origin) {
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
        System.out.print("\tInsert Your Age: ");
        age = in.nextInt();
        while (age<0){
            System.out.print("\t\tPlease. Insert A Valid Age: ");
            age = in.nextInt();
        }
        System.out.println("\tIn The End Select Your Color: Available Colours");
        /*metodo che stampa i colori*/
        // inserire il metodo che mi restituisce la classe Game dal Server
        // inserire il metodo che mi restituisce la classe Player dal Server
    }

    public void getValidColors(){

    }
    /**
     * print the screen
     * @param youPlay if the player is now play
     * @param endTurn if the print is been made in the end of turn
     */
    public void printScreen(boolean youPlay, boolean endTurn){
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

    /**
     * Communicate to server player's worker, make an input check and print new position on the map
     * @deprecated
     */
    public void move() throws WorkerNotExistException {
        Scanner in = new Scanner(System.in);
        int boxX, boxY;
        int index=-1;
        Box box;
        Worker worker;
        if (player.canMove()){
            System.out.println("These are Your Worker: ");
            for (int i=0; i<player.getWorkers().size();i++){
                if (player.getWorkers().get(i).canMove()){
                    System.out.println("\t (" + (player.getWorkers().get(i).getBox().getPosition()[0]) + (player.getWorkers().get(i).getBox().getPosition()[1]) + ") worker #" +i);
                }
            }
            System.out.print("Select Worker #: ");
            index=in.nextInt();
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


        }
        else{
            System.out.println("Sorry You Haven't Movable Worker So");
            lose();
            //segnala la sconfitta al server
        }

    }

}
