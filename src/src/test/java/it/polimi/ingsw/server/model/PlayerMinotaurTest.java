package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMinotaurTest {
    private Map map;
    private PlayerMinotaur player;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerMinotaur("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case where the movement is correct
    @Test
    void moveMinotaur_correct() throws InvalidIndicesException, InvalidBoxException, WrongMovementException, WorkerNotExistException, InvalidMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(0,2);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        player.move(player.getWorkers().get(0),box2);

        assertEquals(player.getWorkers().get(0).getBox(),box2);
        assertEquals(worker2.getBox(),box3);
        assertFalse(box1.hasWorker());
        assertEquals(box2.getWorker(),player.getWorkers().get(0));
        assertEquals(box3.getWorker(),worker2);

    }

    //Test Case where the movement isn't correct (the following box is occupied)
    @Test
    void moveMinotaur_wrong_occupied() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(0,2);
        box3.setDome(true);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        try{player.move(player.getWorkers().get(0),box2);}catch(WrongMovementException | AthenaConditionException | InvalidMovementException e){
            System.out.println("Correct!");
        }
    }

    //Test Case where the movement isn't correct (the following box doesn't exist in the map)
    @Test
    void moveMinotaur_wrong_noNextBox() throws InvalidIndicesException, InvalidBoxException, WrongMovementException, WorkerNotExistException, InvalidMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,3);
        Box box2 = map.getBox(0,4);

        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        try{player.move(player.getWorkers().get(0),box2);}catch(InvalidIndicesException e){
            System.out.println("Correct!");
        }
    }

    //Test Case to verify the additional movement possibilities for Minotaur
    @Test
    void canMove_correct() throws InvalidIndicesException, WorkerNotExistException, InvalidBoxException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);
        map.getBox(0,1).setDome(true);
        map.getBox(1,1).setDome(true);

        map.getBox(4,3).setDome(true);
        map.getBox(3,3).setDome(true);
        map.getBox(3,4).setDome(true);
        player.setWorker1(box1);
        player.setWorker2(box2);

        Worker w = new Worker(new Player("b",12, PlayerColor.YELLOW,map),map.getBox(1,0));
        assertTrue(player.canMove());
    }

    //Test Case to verify the additional movement possibilities for Minotaur (Case where the movement isn't possible)
    @Test
    void canMove_false_noNextBoxFree() throws InvalidIndicesException, WorkerNotExistException, InvalidBoxException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);
        map.getBox(0,1).setDome(true);
        map.getBox(1,1).setDome(true);

        map.getBox(4,3).setDome(true);
        map.getBox(3,3).setDome(true);
        map.getBox(3,4).setDome(true);
        player.setWorker1(box1);
        player.setWorker2(box2);

        Worker w = new Worker(new Player("b",12, PlayerColor.YELLOW,map),map.getBox(1,0));
        map.getBox(2,0).setDome(true);

        assertFalse(player.canMove());



    }

    //Test Case to verify the additional movement possibilities for Minotaur (Case where the movement isn't possible)
    @Test
    void canMove_false_Dome() throws InvalidIndicesException, WorkerNotExistException, InvalidBoxException {
        Box box1 = map.getBox(1,0);
        Box box2 = map.getBox(4,4);
        map.getBox(0,1).setDome(true);
        map.getBox(1,1).setDome(true);
        map.getBox(2,1).setDome(true);
        map.getBox(2,0).setDome(true);

        map.getBox(4,3).setDome(true);
        map.getBox(3,3).setDome(true);
        map.getBox(3,4).setDome(true);
        player.setWorker1(box1);
        player.setWorker2(box2);

        Worker w = new Worker(new Player("b",12, PlayerColor.YELLOW,map),map.getBox(0,0));
        map.getBox(2,0).setDome(true);

        assertFalse(player.canMove());

    }
}