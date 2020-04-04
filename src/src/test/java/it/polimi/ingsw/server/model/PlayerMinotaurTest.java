package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import it.polimi.ingsw.server.model.exceptions.WrongMovementException;
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
    void moveMinotaur1() throws InvalidIndicesException, InvalidBoxException, WrongMovementException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(0,2);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        player.moveMinotaur(player.getWorkers().get(0),box2);

        assertEquals(player.getWorkers().get(0).getBox(),box2);
        assertEquals(worker2.getBox(),box3);
        assertFalse(box1.hasWorker());
        assertEquals(box2.getWorker(),player.getWorkers().get(0));
        assertEquals(box3.getWorker(),worker2);

    }

    //Test Case where the movement isn't correct (the following box is occupied)
    @Test
    void moveMinotaur2() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(0,2);
        box3.setDome(true);
        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        try{player.moveMinotaur(player.getWorkers().get(0),box2);}catch(WrongMovementException e){
            System.out.println("Correct!");
        }
    }

    //Test Case where the movement isn't correct (the following box doesn't exist in the map)
    @Test
    void moveMinotaur3() throws InvalidIndicesException, InvalidBoxException, WrongMovementException, WorkerNotExistException {
        Box box1 = map.getBox(0,3);
        Box box2 = map.getBox(0,4);

        Worker worker2 = new Worker(new Player("a",3,PlayerColor.RED,map), box2);
        player.setWorker1(box1);

        try{player.moveMinotaur(player.getWorkers().get(0),box2);}catch(InvalidIndicesException e){
            System.out.println("Correct!");
        }
    }
}