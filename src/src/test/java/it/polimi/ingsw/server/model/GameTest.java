package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.InvalidInputException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game(2,true);
    }

    @AfterEach
    void tearDown() {
        game = null;
    }


    //Test Case where game is full
    @Test
    void isGameFull_true() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);
        game.addPlayer("abc", 17, PlayerColor.YELLOW);
        assertTrue(game.isGameFull());
    }

    //Test Case where game is not full
    @Test
    void isGameFull_false() {
        assertFalse(game.isGameFull());
    }

    //Test Case where the parameters are correct
    @Test
    void addPlayer_correct() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);

        System.out.println(game.getAvailableColors());
        assertEquals(game.getPlayers().get(0).getColor(), PlayerColor.RED);
        assertEquals("aaa", game.getPlayers().get(0).getPlayerID());
        assertEquals(15,game.getPlayers().get(0).getPlayerAge());

    }

    //Test Case where the color has already been chosen
    @Test
    void addPlayer_wrong_color() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);

        try{game.addPlayer("abc", 17, PlayerColor.RED);}catch (InvalidInputException e) {
            System.out.println("Correct Response");}

    }


    //Test Case where the ID has already been chosen
    @Test
    void addPlayer_wrong_ID() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);

        try{game.addPlayer("aaa", 17, PlayerColor.YELLOW);}catch (InvalidInputException e) {
            System.out.println("Correct Response");}
    }

    //Test Case where the age isn't an acceptable number
    @Test
    void addPlayer_wrong_number() {
        try{game.addPlayer("aaa", 170, PlayerColor.YELLOW);}catch (InvalidInputException e) {
            System.out.println("Correct Response");}
    }

    //Single Test Case to check that all the operations are correct
    @Test
    void becomeDivinity_correct() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);
        game.addPlayer("abc", 17, PlayerColor.YELLOW);

        game.becomeDivinity(game.getPlayers().get(0),"Apollo");

        assertEquals(game.getPlayers().size(),2);
        assertEquals(game.getPlayers().get(1).getColor(), PlayerColor.RED);
        assertEquals("aaa", game.getPlayers().get(1).getPlayerID());
        assertEquals(15,game.getPlayers().get(1).getPlayerAge());
        assertTrue(game.getPlayers().get(1) instanceof PlayerApollo );

        assertEquals(game.getPlayers().get(0).getColor(), PlayerColor.YELLOW);
        assertEquals("abc", game.getPlayers().get(0).getPlayerID());
        assertEquals(17,game.getPlayers().get(0).getPlayerAge());

    }

    //Test Case where the player belongs to the game
    @Test
    void removePlayer_correct() throws InvalidInputException, InvalidIndicesException, InvalidBoxException {
        game.addPlayer("aaa", 15, PlayerColor.RED);
        game.addPlayer("abc", 16, PlayerColor.YELLOW);
        ArrayList<Player> otherPlayers = new ArrayList<>();

        int oldSize = game.getPlayers().size();
        for(int j = 1; j<oldSize; j++) {
                otherPlayers.add(game.getPlayers().get(j));
        }


        game.placeWorkers(game.getPlayers().get(0), game.getMap().getBox(0,0),  game.getMap().getBox(1,0));

        game.removePlayer(game.getPlayers().get(0));

        assertFalse(game.getMap().getBox(0,0).hasWorker());
        assertFalse(game.getMap().getBox(1,0).hasWorker());
        assertEquals(game.getPlayers().size(), (oldSize-1));
        assertTrue(game.getPlayers().containsAll(otherPlayers));


    }


    //Test Case where the player doesn't belong to the game
    @Test
    void removePlayer_Exception() {
        try{
            game.removePlayer(new Player("a",12,PlayerColor.RED, game.getMap()));
        }catch(InvalidInputException e){
            System.out.println("Correct response");
        }
    }

    //Test Case where the input is correct
    @Test
    void selectGodCards_correct() throws InvalidInputException {
        ArrayList<String> cards = new ArrayList<>();
        cards.add("Arthemis");
        cards.add("Apollo");
        game.selectGodCards(cards);

        assertTrue(game.getGods().containsAll(cards));
        assertEquals(game.getGods().size(),cards.size());
    }

    //Test Case where cards aren't valid
    @Test
    void selectGodCards_Exception() {
        ArrayList<String> cards = new ArrayList<>();
        cards.add("Arthemis");
        cards.add("Buddha");
        try{game.selectGodCards(cards);}catch(InvalidInputException e){
            System.out.println("Correct Response");
        }
    }

    //Test Case where cards aren't enough, or are too many
    @Test
    void selectGodCards_Exception_number() {
        ArrayList<String> cards = new ArrayList<>();
        cards.add("Arthemis");
        try{game.selectGodCards(cards);}catch(InvalidInputException e){
            System.out.println("Correct Response");
        }
    }

    //Test Case where the parameters are correct
    @Test
    void chooseCard_correct() throws InvalidInputException {
        game.addPlayer("aaa",15,PlayerColor.RED);
        ArrayList<String> gods = new ArrayList<>();
        gods.addAll(Arrays.asList(new String[]{"Apollo", "Arthemis"}));
        game.selectGodCards(gods);


        game.chooseCard(game.getPlayers().get(0),"Apollo");

        assertTrue(game.getPlayers().get(0) instanceof PlayerApollo);
        assertTrue(game.getGods().contains("Arthemis"));
        assertFalse(game.getGods().contains("Apollo"));

    }

    //Test Case where the player doesn't belong to the game
    @Test
    void chooseCard_Exception_Player() throws InvalidInputException {
        Player p = new Player("a",12,PlayerColor.YELLOW,new Map());
        ArrayList<String> gods = new ArrayList<>();
        gods.addAll(Arrays.asList(new String[]{"Apollo", "Arthemis"}));
        game.selectGodCards(gods);
        try{game.chooseCard(p,"Apollo");}catch (InvalidInputException e){
            System.out.println("Correct Response");
        }
    }

    //Test Case where the god selected isn't available
    @Test
    void chooseCard_Exception_God() throws InvalidInputException {
        game.addPlayer("aaa",15,PlayerColor.RED);
        ArrayList<String> gods = new ArrayList<>();
        gods.addAll(Arrays.asList(new String[]{"Apollo", "Arthemis"}));
        game.selectGodCards(gods);

        try{game.chooseCard(game.getPlayers().get(0),"Pan");}catch (InvalidInputException e){
            System.out.println("Correct Response");
        }
    }

    //Test Case where the player belongs to the game
    @Test
    void chooseStarter_correct() throws InvalidInputException {
        game.addPlayer("aaa", 15, PlayerColor.RED);
        game.addPlayer("abc", 16, PlayerColor.YELLOW);

        game.chooseStarter(game.getPlayers().get(1));

        assertEquals(game.getPlayers().get(0).getColor(), PlayerColor.YELLOW);
        assertEquals("abc", game.getPlayers().get(0).getPlayerID());
        assertEquals(16,game.getPlayers().get(0).getPlayerAge());
    }

    //Test Case where the player doesn't belong to the game
    @Test
    void chooseStarter_Exception() {

        try{game.chooseStarter(new Player("aaa",12,PlayerColor.RED,new Map()));}catch (InvalidInputException e){
            System.out.println("Correct Response");
        }
    }

    //Test Case where the parameters are correct
    @Test
    void placeWorkers_correct() throws InvalidInputException, InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        game.addPlayer("aaa", 14, PlayerColor.RED);
        Box box1 = game.getMap().getBox(0,0);
        Box box2 = game.getMap().getBox(1,0);
        game.placeWorkers(game.getPlayers().get(0),box1,box2);


        Worker w1 = game.getPlayers().get(0).getWorkers().get(0);
        Worker w2 = game.getPlayers().get(0).getWorkers().get(1);

        assertTrue(w1.getBox().equals(box1));
        assertTrue(w2.getBox().equals(box2));
        assertTrue(box1.getWorker().equals(w1));
        assertTrue(box2.getWorker().equals(w2));

    }

    //Test Case where the boxes aren't correct (Don't belong to the map)
    @Test
    void placeWorkers_Exception() throws InvalidInputException, InvalidIndicesException{
        Map m1 = new Map();
        game.addPlayer("aaa", 14, PlayerColor.RED);
        Box box1 = m1.getBox(0,0);
        Box box2 = m1.getBox(1,0);

        try{game.placeWorkers(game.getPlayers().get(0),box1,box2);}catch(InvalidInputException | InvalidBoxException e) {
            System.out.println("Correct Response");
        }
    }

}