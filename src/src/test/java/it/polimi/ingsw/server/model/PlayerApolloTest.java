package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerApolloTest {
    private PlayerApollo player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerApollo("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case where the special Apollo movement works correctly
    @Test
    void move1() throws InvalidBoxException, InvalidIndicesException, WorkerNotExistException, WrongMovementException, AthenaConditionException, InvalidMovementException {
        Box box1 = map.getBox(0,0);

        Box box2 = map.getBox(0,1);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        player.move(player.getWorkers().get(0),box2);

        assertEquals(player.getWorkers().get(0).getBox(),box2);
        assertEquals(worker2.getBox(),box1);
        assertEquals(box1.getWorker(),worker2);
        assertEquals(box2.getWorker(),player.getWorkers().get(0));
    }


    //Test Case where the selected box doesn't have a worker
    @Test
    void moveApollo2() throws InvalidIndicesException, InvalidBoxException, AthenaConditionException, WorkerNotExistException {

        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        player.setWorker1(box1);

        try{player.move(player.getWorkers().get(0),box2);}catch(WrongMovementException | InvalidMovementException e) {
            System.out.println("Correct!");
        }

    }


    //Test Case where the selected box has a worker of the Apollo Player
    @Test
    void moveApollo3() throws InvalidBoxException, InvalidIndicesException, WorkerNotExistException, AthenaConditionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        player.setWorker1(box1);
        player.setWorker2(box2);

        try{player.move(player.getWorkers().get(0),box2);}catch(WrongMovementException | InvalidMovementException e) {
            System.out.println("Correct!");
        }
    }



    //Test Case to verify the additional movement possibilities for Apollo
    @Test
    void canMove1() throws InvalidIndicesException, WorkerNotExistException, InvalidBoxException {
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

    //Test Case to verify the additional movement possibilities for Apollo (Case where the movement isn't possible)
    @Test
    void canMove2() throws InvalidIndicesException, WorkerNotExistException, InvalidBoxException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(1,0);
        map.getBox(0,1).setDome(true);
        map.getBox(1,1).setDome(true);
        player.setWorker1(box1);
        player.setWorker2(box2);

        map.getBox(2,1).setDome(true);
        map.getBox(2,0).setDome(true);


        assertFalse(player.canMove());

    }


}