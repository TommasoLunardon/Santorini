package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.WorkerNotExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkerApolloTest {
    private Worker worker;
    private PlayerApollo player;

    @BeforeEach
    void setUp() {
        player = new PlayerApollo("aaa", 12, PlayerColor.YELLOW, new Map());
        Map map = player.getPlayerMap();
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
        worker = null;
    }


    //Test case in which worker can move (box has an enemy worker)
    @Test
    void canMove_true() throws WorkerNotExistException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(0);
        Worker enemy = new Worker(new Player("b", 12, PlayerColor.RED, next.getMap()), next);
        assertTrue(worker.canMove(next));
    }

    //Test case in which worker can't move (box has a worker of the same team)
    @Test
    void canMove_false() throws WorkerNotExistException, InvalidBoxException {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(0);
        player.setWorker2(next);

        assertFalse(worker.canMove(next));
    }

}
