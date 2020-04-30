package it.polimi.ingsw.server.model;


import it.polimi.ingsw.network.events.MVEvent;
import it.polimi.ingsw.network.events.mvevents.GameUpdatingEvent;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.model.exceptions.*;


import java.io.Serializable;
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
    VirtualView virtualView;
    private ArrayList<Pair> pairs;
    private ArrayList<String> IDs;
    private ArrayList<String> availableColors;


    //Game creation, deciding the game's settings
    public Game(int numPlayers, boolean withGods){

        //NB 3 PLAYERS ---> WITHGODS = TRUE

        this.withGods = withGods;
        this.map = new Map();
        players = new ArrayList<>();
        this.numPlayers = numPlayers;
        minAge = 120;
        gameGods = new ArrayList<>();
        String[] g = {"Apollo", "Arthemis", "Athena", "Atlas", "Demeter", "Ephaestus", "Minotaur", "Pan", "prometheus"};
        availableGods.addAll(Arrays.asList(g));
        pairs = new ArrayList<>();
        IDs = new ArrayList<>();
        availableColors = new ArrayList<>();
        String[] c = {"Yellow", "Blue", "Red"};
        availableColors.addAll(Arrays.asList(c));

    }

    public ArrayList<String> getAvailableColors() {
        ArrayList<String> a = availableColors;
        return a;
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

    public boolean isWithAthena() {
        return withAthena;
    }

    public boolean isGameFull(){
        if (this.getPlayers().size()<this.getNumPlayers()){
            return false;
        }
        return true;
    }

    public ArrayList<String> getGods() {
        return gameGods;
    }

    //Players enter the game
    public void addPlayer(String ID, int age, PlayerColor color) throws InvalidInputException{

        boolean incorrect = false;
        for(int i = 0; i<players.size();i++){
            if(players.get(i).getColor().equals(color)) {incorrect = true;}
            if(players.get(i).getPlayerID().equals(ID)) {incorrect = true;}
        }

         if(age<1 || age> 120 || incorrect){throw new InvalidInputException();}

        Player player = new Player(ID,age,color,map);

        players.add(player);
        IDs.add(player.getPlayerID());
        pairs.add(new Pair(player, player.getPlayerID()));
        availableColors.remove(player.getColor().toString());
        if(age<minAge){ minAge = age;}

    }

    //Method used to assign gods to players (CHECK FOR THE CORRECT CARD ON CONTROLLER)
    public void becomeDivinity(Player player, String card) throws InvalidInputException {
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


    //Players removed from the game
    public void removePlayer(Player player) throws InvalidInputException {
        if(!players.contains(player)){ throw new InvalidInputException();
        }
            try{player.removeWorkers();}catch (WorkerNotExistException e){
                System.out.println("This player doesn't have any worker");
            }
            players.remove(player);
            for(int i = 0; i < pairs.size(); i++){
                if(pairs.get(i).getID().equals(player.getPlayerID())){
                    pairs.remove(i);
                }
            }

    }

    //Selectio of god cards for a game with god cards (Controller checks that all cards are different!)
     public void selectGodCards(ArrayList<String> cards) throws InvalidInputException{
        if(cards.size() != numPlayers){throw new InvalidInputException();}
        if(!availableGods.containsAll(cards)){throw new InvalidInputException();}

         gameGods.addAll(cards);

    }


    //Player selects his/her card for the game
    public void chooseCard(Player player, String card)throws InvalidInputException{
        if(!getPlayers().contains(player) || !gameGods.contains(card) ){throw new InvalidInputException();}

                becomeDivinity(player,card);
                gameGods.remove(card);

    }

    //Selection of the starting player for the game
    public void chooseStarter(Player player)throws InvalidInputException{
        if(!getPlayers().contains(player)){throw new InvalidInputException();}

        //If there is Athena Card we attach other players as observers

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

    //Player places his/her two workers on the map at the beginning of the game
    public void placeWorkers(Player player, Box box1, Box box2) throws InvalidBoxException, InvalidInputException {
        if(!getPlayers().contains(player) || !box1.getMap().equals(map)  || !box2.getMap().equals(map)){throw new InvalidInputException();}

        player.setWorker1(box1);
        player.setWorker2(box2);
    }


    //Returns all the pairs PlayerID-Player
    public ArrayList<Pair> getPairs() {
        ArrayList<Pair> p = pairs;
        return p;
    }

    //Returns all the PlayerIDs in the game
    public ArrayList<String> getIDs() {
        ArrayList<String> i = IDs;
        return i;
    }


    public void setVirtualView(VirtualView view){
        this.virtualView = view;
    }

    //Method used to send an MVEvent
     public void notify(MVEvent event){
        event.manage(virtualView);
   }

    //Method used for updating all the views
    public void gameUpdate(){
        for(int i = 0; i < getIDs().size(); i++){

            GameUpdatingEvent event = new GameUpdatingEvent(getIDs().get(i),this);
            this.notify(event);
        }

    }

}





