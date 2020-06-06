package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.NotValidLevelException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerNotAthenaTest {
    private Worker worker;
    private PlayerNotAthena player;

    @BeforeEach
    void setUp() {
        player = new PlayerNotAthena("aaa", 12, new Map(), PlayerColor.YELLOW, 2, "Atlas");
        Map map = player.getPlayerMap();
        try{Box box = new Box(2,2,map);
            worker = new Worker(player,box);}
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {

        worker = null;
        player = null;
    }


    //Test case in which worker can move even if there is Athena Condition
    @Test
    void canMove_true() throws NotValidLevelException, WorkerNotExistException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        player.update(false);
        Box next = neighbours.get(0);
        next.setLevel(0);
        assertTrue(worker.canMove(next));
    }

    //Test case in which worker can't move because of Athena Condition
    @Test
    void canMove_false_AthenaCondition() throws WorkerNotExistException, NotValidLevelException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        player.update(false);
        Box next = neighbours.get(0);
        next.setLevel(1);

        assertFalse(worker.canMove(next));
    }
}
