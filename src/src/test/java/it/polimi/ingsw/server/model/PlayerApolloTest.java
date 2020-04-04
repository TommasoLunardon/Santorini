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
    void moveApollo1() throws InvalidBoxException, InvalidIndicesException, WorkerNotExistException, WrongMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,0);

        Box box2 = map.getBox(0,1);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        player.moveApollo(player.getWorkers().get(0),box2);

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

        try{player.moveApollo(player.getWorkers().get(0),box2);}catch(WrongMovementException e) {
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

        try{player.moveApollo(player.getWorkers().get(0),box2);}catch(WrongMovementException e) {
            System.out.println("Correct!");
        }
    }





}