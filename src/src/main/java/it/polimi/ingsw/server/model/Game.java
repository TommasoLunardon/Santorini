package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.mvevents.GameUpdatingEvent;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.model.exceptions.*;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Game with all the methods needed to create and play a match
 * @author Tommaso Lunardon
 */
public class Game implements Serializable{
    private ArrayList<Player> players;
    private Map map;
    private boolean withGods;
    private int numPlayers;
    private int minAge;
    private boolean withAthena;
    private ArrayList<String> gameGods;
    public final ArrayList<String> availableGods = new ArrayList<>();
    private transient VirtualView virtualView;
    private ArrayList<Pair> pairs;
    private ArrayList<String> IDs;
    private ArrayList<String> availableColors;


    public Game(int numPlayers, boolean withGods){

        this.withGods = withGods;
        this.map = new Map();
        players = new ArrayList<>();
        this.numPlayers = numPlayers;
        minAge = 120;
        gameGods = new ArrayList<>();
        String[] g = {"Apollo", "Arthemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Minotaur", "Pan", "Prometheus"};
        availableGods.addAll(Arrays.asList(g));
        pairs = new ArrayList<>();
        IDs = new ArrayList<>();
        availableColors = new ArrayList<>();
        String[] c = {"Yellow", "Blue", "Red"};
        availableColors.addAll(Arrays.asList(c));

    }

    public ArrayList<String> getAvailableColors() {
         return availableColors;
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> p = players;
        return p;
    }

    public Map getMap() {
        Map m = map;
        return m;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isWithGods() {
        return withGods;
    }

    public int getMinAge(){
        return minAge;
    }

    private boolean isWithAthena() {
        return withAthena;
    }

    public boolean isGameFull(){
        return this.getPlayers().size() >= this.getNumPlayers();
    }

    public ArrayList<String> getGods() {
        return gameGods;
    }

    /**
     * Method used to insert a player in the game
     * @param ID is the player's ID
     * @param age is the player's age
     * @param color is the player's color
     * @throws InvalidInputException if the parameters are wrong
     */
    public void addPlayer(String ID, int age, PlayerColor color) throws InvalidInputException{

        boolean incorrect = false;
        for (Player value : players) {
            if (value.getColor().equals(color)) {
                incorrect = true;
            }
            if (value.getPlayerID().equals(ID)) {
                incorrect = true;
            }
        }

         if(age<1 || age> 120 || incorrect){throw new InvalidInputException();}

        Player player = new Player(ID,age,color,map);

        players.add(player);
        IDs.add(player.getPlayerID());
        pairs.add(new Pair(player, player.getPlayerID()));
        availableColors.remove(color.toString());
        if(age<minAge){ minAge = age;}

    }

    /**
     * Method used to assign a divinity to one player
     * @param player is the selected player
     * @param card is the selected card
     * @throws InvalidInputException if parameters are wrong
     */
    void becomeDivinity(Player player, String card) throws InvalidInputException {
        removePlayer(player);

        if(card.equalsIgnoreCase("Apollo")){
            PlayerApollo p = new PlayerApollo(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Arthemis")){
            PlayerArthemis p = new PlayerArthemis(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Athena")){
            PlayerAthena p = new PlayerAthena(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
            withAthena = true;
        }

        if(card.equalsIgnoreCase("Atlas")){
            PlayerAtlas p = new PlayerAtlas(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Demeter")){
            PlayerDemeter p = new PlayerDemeter(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Ephaestus")){
            PlayerEphaestus p = new PlayerEphaestus(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Minotaur")){
            PlayerMinotaur p = new PlayerMinotaur(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Pan")){
            PlayerPan p = new PlayerPan(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

        if(card.equalsIgnoreCase("Prometheus")){
            PlayerPrometheus p = new PlayerPrometheus(player.getPlayerID(),player.getPlayerAge(),player.getColor(),map);
            players.add(p);
            pairs.add(new Pair(p,p.getPlayerID()));
        }

    }


    /**
     * Method used to remove a player from the game
     * @param player is the selected player
     * @throws InvalidInputException if the selected player doesn't belong to the game
     */
    public void removePlayer(Player player) throws InvalidInputException {
        if(!players.contains(player)){ throw new InvalidInputException();}
            try{player.removeWorkers();
            }catch (WorkerNotExistException e){
                System.out.println("This player doesn't have any worker");
            }
            players.remove(player);
            for(int i = 0; i < pairs.size(); i++){
                if(pairs.get(i).getID().equals(player.getPlayerID())){
                    pairs.remove(i);
                }
            }

    }

    /**
     * Method used to insert the cards selected for this game
     * @param cards is the list of selected cards
     * @throws InvalidInputException if the parameter is wrong
     */
     public void selectGodCards(ArrayList<String> cards) throws InvalidInputException{
        if(cards.size() != numPlayers){throw new InvalidInputException();}
        if(!availableGods.containsAll(cards)){throw new InvalidInputException();}

         gameGods.addAll(cards);

    }


    /**
     * Method used to assign the selected card to each player
     * @param player is the selected player
     * @param card is the selected card
     * @throws InvalidInputException if the parameters are wrong
     */
    public void chooseCard(Player player, String card)throws InvalidInputException{
        if(!getPlayers().contains(player) || !gameGods.contains(card) ){throw new InvalidInputException();}

                becomeDivinity(player,card);
                gameGods.remove(card);

    }

    /**
     * Method used to select the starter for this game
     * @param player is the starter selected
     * @throws InvalidInputException if the player selected doesn't belong to the game
     */
    public void chooseStarter(Player player)throws InvalidInputException{
        if(!getPlayers().contains(player)){throw new InvalidInputException();}

        if(isWithAthena()){
            int k = 0;
            ArrayList<PlayerNotAthena> p = new ArrayList<>();

            for (int i = 0; i < getNumPlayers(); i++){
                if(getPlayers().get(i) instanceof PlayerAthena){ k = i;}
                else{p.add( (PlayerNotAthena) getPlayers().get(i) );}
            }
            PlayerAthena a = (PlayerAthena) getPlayers().get(k);
            a.attach(p);
        }

        int pos = getPlayers().indexOf(player);
        Player previous = getPlayers().get(0);
        getPlayers().set(pos,previous);
        getPlayers().set(0,player);

    }

    /**
     * Method used to place the players' workers on the map
     * @param player is the selected player
     * @param box1 is the first selected box
     * @param box2 is the second selected box
     * @throws InvalidBoxException if the selected boxes aren't valid
     * @throws InvalidInputException if the player doesn't belong to the game
     */
    public void placeWorkers(Player player, Box box1, Box box2) throws InvalidBoxException, InvalidInputException {
        if(!getPlayers().contains(player)){throw new InvalidInputException();}

        player.setWorker1(box1);
        player.setWorker2(box2);
    }


    public ArrayList<Pair> getPairs() {
        ArrayList<Pair> p = pairs;
        return p;
    }

    public ArrayList<String> getIDs() {
        ArrayList<String> i = IDs;
        return i;
    }

    public void setVirtualView(VirtualView view){
        this.virtualView = view;
    }

    /**
     * Method used to notify an MVEvent
     * @param event is the event notified
     */
     public void notify(MVEvent event) throws SocketTimeoutException {
        event.manage(virtualView);
   }

    /**
     * Method used to update all the connected clients, sending the most updated game version
     */
    public void gameUpdate() throws SocketTimeoutException {
        for(int i = 0; i < getIDs().size(); i++){
            GameUpdatingEvent event = new GameUpdatingEvent(getIDs().get(i),this);
            this.notify(event);
        }

    }

}





