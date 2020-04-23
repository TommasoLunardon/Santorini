package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerAthenaTest {
    private Map map;
    private PlayerAthena player;


    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerAthena("a",10,PlayerColor.RED,map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }


    //Test Case where the AthenaCondition is set to true;
    @Test
    void move1() throws InvalidIndicesException, NotValidLevelException, InvalidBoxException, InvalidMovementException, AthenaConditionException, WrongMovementException, WorkerNotExistException {
        ArrayList<PlayerNotAthena> observer = new ArrayList<PlayerNotAthena>();
        PlayerNotAthena player1 = new PlayerPan("a", 13, PlayerColor.YELLOW, map);
        PlayerNotAthena player2 = new PlayerApollo("a", 13, PlayerColor.YELLOW, map);

        observer.add(player1);
        observer.add(player2);

        player.attach(observer);


        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        box2.setLevel(1);
        player.setWorker1(box1);

        player.move(player.getWorkers().get(0),box2);

        assertFalse(player1.checkFreeMovement());
        assertFalse(player2.checkFreeMovement());
    }

    //Test Case where the AthenaCondition is set to false;
    @Test
    void move2() throws InvalidBoxException, NotValidLevelException, InvalidIndicesException, InvalidMovementException, AthenaConditionException, WrongMovementException, WorkerNotExistException {
        ArrayList<PlayerNotAthena> observer = new ArrayList<PlayerNotAthena>();
        PlayerNotAthena player1 = new PlayerPan("a", 13, PlayerColor.YELLOW, map);
        PlayerNotAthena player2 = new PlayerApollo("a", 13, PlayerColor.YELLOW, map);

        observer.add(player1);
        observer.add(player2);

        player.attach(observer);


        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        box1.setLevel(2);
        box2.setLevel(0);
        player.setWorker1(box1);

        player.move(player.getWorkers().get(0),box2);

        assertTrue(player1.checkFreeMovement());
        assertTrue(player2.checkFreeMovement());
    }
}