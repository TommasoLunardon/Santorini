package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerMinotaurTest{
    private Worker worker;
    private PlayerMinotaur player;
    private  Map map;

    @BeforeEach
    void setUp() {
        player = new PlayerMinotaur("aaa", 12, PlayerColor.YELLOW, new Map());
        map = player.getPlayerMap();
        try {
            Box box = new Box(2, 2, map);
            worker = new Worker(player, box);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
        player = null;
        map = null;
        worker = null;
    }


    //Test case in which worker can move (next box is empty)
    @Test
    void canMove_true() throws WorkerNotExistException, InvalidIndicesException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(0);
        Worker enemy = new Worker(new Player("b", 12, PlayerColor.RED, next.getMap()), next);

        assertTrue(worker.canMove(next));
    }

    //Test case in which worker can't move (next box has dome)
    @Test
    void canMove_false_dome() throws WorkerNotExistException, InvalidIndicesException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(0);
        Worker enemy = new Worker(new Player("b", 12, PlayerColor.RED, next.getMap()), next);

        int dirX = next.getPosition()[0] - 2;
        int dirY = next.getPosition()[1] - 2;
        int x = next.getPosition()[0] + dirX;
        int y = next.getPosition()[1] + dirY;
        map.getBox(x,y).setDome(true);


        assertFalse(worker.canMove(next));
    }

    //Test case in which worker can't move (next box has worker)
    @Test
    void canMove_false_worker() throws WorkerNotExistException, InvalidIndicesException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(0);
        Worker enemy = new Worker(new Player("b", 12, PlayerColor.RED, next.getMap()), next);

        int dirX = next.getPosition()[0] - 2;
        int dirY = next.getPosition()[1] - 2;
        int x = next.getPosition()[0] + dirX;
        int y = next.getPosition()[1] + dirY;
        Worker busy = new Worker(new Player("c", 14, PlayerColor.YELLOW, player.getPlayerMap()), map.getBox(x,y));


        assertFalse(worker.canMove(next));
    }
}
