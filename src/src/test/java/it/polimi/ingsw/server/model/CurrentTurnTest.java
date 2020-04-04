package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.NotValidPlayersException;
import it.polimi.ingsw.server.model.exceptions.PlayerNotValidException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CurrentTurnTest {

    private CurrentTurn currentTurn;
    private Map map;

    @BeforeEach
    void setUp() {
         map = new Map();
        currentTurn = new CurrentTurn(map);
    }

    @AfterEach
    void tearDown() {
        currentTurn = null;
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getWorkers() {
    }

    //Single test case to verify the correct result of the method
    @Test
    void getMap() {
        Map check = map;
        assertEquals(check,currentTurn.getMap());
    }

    //Test Case in which the assignment is correct
    @Test
    void setActivePlayer1() throws NotValidPlayersException, PlayerNotValidException {
        Map map =currentTurn.getMap();
        Player p1 = new Player("a",10,PlayerColor.YELLOW,map);
        currentTurn.addPlayer(p1);
        currentTurn.setActivePlayer(p1);

        assertTrue(currentTurn.getActivePlayer().equals(p1));
    }

    //Test Case in which the player doesn't belong to the game
    @Test
    void setActivePlayer2() {

        Player p1 = new Player("a",10,PlayerColor.YELLOW,new Map());

        try{currentTurn.setActivePlayer(p1);}catch(PlayerNotValidException e){
            System.out.println("Correct Response!");
        }

    }

    //Test Case to check the correct return of the active player
    @Test
    void getActivePlayer() throws PlayerNotValidException, NotValidPlayersException {
        Map map =currentTurn.getMap();
        Player p1 = new Player("a",10,PlayerColor.YELLOW,map);
        currentTurn.addPlayer(p1);
        currentTurn.setActivePlayer(p1);
        assertTrue(currentTurn.getActivePlayer().equals(p1));
    }

    //Single Test Case to verify the correct elimination of both the player and its workers
    @Test
    void removePlayer() throws NotValidPlayersException {
        Map map =currentTurn.getMap();
        Player p1 = new Player("a",10,PlayerColor.YELLOW,map);
        currentTurn.addPlayer(p1);

        currentTurn.removePlayer(p1);
        assertTrue(currentTurn.getPlayers().size()==0);
        assertTrue(currentTurn.getWorkers().size()==0);
    }

    //Test Case with correct input value
    @Test
    void addPlayer1() throws NotValidPlayersException {
        Map map =currentTurn.getMap();
        Player p1 = new Player("a",10,PlayerColor.YELLOW,map);
        currentTurn.addPlayer(p1);
        ArrayList<Worker> workers = p1.getWorkers();

        assertEquals(currentTurn.getPlayers().get(0),p1);
        assertEquals(currentTurn.getWorkers(),workers);

    }

    //Test Case with wrong value of player.map
    @Test
    void addPlayer2() {
        Map map =new Map();
        Player p1 = new Player("a",10,PlayerColor.YELLOW,map);
        try{currentTurn.addPlayer(p1);}catch(NotValidPlayersException e){
            System.out.println("Correct Response!");
        }




    }
}