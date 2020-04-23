package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPanTest {
    private Map map;
    private PlayerPan player;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerPan("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case to check the correct additional win condition assignment
    @Test
    void move1() throws InvalidIndicesException, InvalidBoxException, NotValidLevelException, InvalidMovementException, WorkerNotExistException, WrongMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        box1.setLevel(3);
        player.setWorker1(box1);
        player.move(player.getWorkers().get(0),box2);

        assertTrue(player.isWinner());

    }

    //Test Case to check that the additional win condition is assigned only when the condition is satisfied
    @Test
    void move2() throws InvalidIndicesException, InvalidBoxException, InvalidMovementException, WorkerNotExistException, WrongMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);

        player.setWorker1(box1);
        player.move(player.getWorkers().get(0),box2);

        assertFalse(player.isWinner());

    }
}