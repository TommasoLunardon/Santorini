package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerNotAthenaTest {
    private PlayerNotAthena player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerNotAthena("a", 11, map, PlayerColor.YELLOW, 2, "Apollo");

    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case to check the particular case of AthenaConditionException, otherwise the method works as a normal movement
    @Test
    void move_AthenaException() throws InvalidIndicesException, InvalidBoxException, NotValidLevelException, InvalidMovementException, WorkerNotExistException{
        player.update(false);
        Box box1 = map.getBox(0, 0);

        player.setWorker1(box1);
        map.getBox(0, 1).setLevel(2);

        try {
            player.move(player.getWorkers().get(0), map.getBox(0, 1));
        } catch (AthenaConditionException | WrongMovementException e) {
            System.out.println("correct");
        }
    }

    //Test Case to verify the additional constraints for PlayerNotAthena (Can Move)
    @Test
    void canMove_true() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException, NotValidLevelException {
        player.update(false);
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);
        map.getBox(0,1).setLevel(2);
        map.getBox(1,1).setLevel(2);
        map.getBox(1,0).setLevel(2);


        map.getBox(3,3).setLevel(2);
        map.getBox(3,4).setLevel(2);
        player.setWorker1(box1);
        player.setWorker2(box2);

        assertTrue(player.canMove());

    }


    //Test Case to verify the additional constraints for PlayerNotAthena (Can't Move)
    @Test
    void canMove_false() throws InvalidIndicesException, NotValidLevelException, InvalidBoxException, WorkerNotExistException {
        player.update(false);
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);
        map.getBox(0,1).setLevel(2);
        map.getBox(1,1).setLevel(2);
        map.getBox(1,0).setLevel(2);


        map.getBox(3,3).setLevel(2);
        map.getBox(3,4).setLevel(2);
        map.getBox(4,3).setLevel(2);
        player.setWorker1(box1);
        player.setWorker2(box2);

        assertFalse(player.canMove());

    }

}