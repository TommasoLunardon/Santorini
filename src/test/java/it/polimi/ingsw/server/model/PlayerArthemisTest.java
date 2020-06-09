package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerArthemisTest {
    private PlayerArthemis player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerArthemis("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case where the movement is correct
    @Test
    void moveArthemis_correct() throws InvalidIndicesException, InvalidBoxException, WrongMovementException, WorkerNotExistException, InvalidMovementException, AthenaConditionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(0,2);
        player.setWorker1(box1);

        player.moveArthemis(box2, box3, player.getWorkers().get(0));

        assertEquals(player.getWorkers().get(0).getBox(), box3);
        assertFalse(box1.hasWorker());
        assertFalse(box2.hasWorker());
    }


    //Test Case where the movement isn't correct (The second box is the starting box)
    @Test
    void moveArthemis_wrong_sameBox() throws InvalidIndicesException, InvalidBoxException {

        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = box1;
        player.setWorker1(box1);

        try{player.moveArthemis(box2, box3, player.getWorkers().get(0));}catch(WrongMovementException | InvalidMovementException | WorkerNotExistException | AthenaConditionException e){
            System.out.println("Correct!");
        }
    }
}