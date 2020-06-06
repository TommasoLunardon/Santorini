package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPrometheusTest {
    private PlayerPrometheus player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerPrometheus("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case where the movement works correctly
    @Test
    void movePrometheus_correct() throws InvalidIndicesException, InvalidBoxException, PrometheusMovementException, NotValidLevelException, WrongConstructionException, InvalidConstructionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(1,1);
        player.setWorker1(box1);

        player.movePrometheus(box2, box3, player.getWorkers().get(0));

        assertEquals(player.getWorkers().get(0).getBox(), box3);
        assertFalse(box1.hasWorker());
        assertEquals(1, box2.getLevel());
    }

    //Test Case where the movement doesn't work (Destination box at upper level)
    @Test
    void movePrometheus_wrong_UpLevel() throws InvalidIndicesException, InvalidBoxException, PrometheusMovementException, NotValidLevelException, WrongConstructionException, InvalidConstructionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(1,1);
        box3.setLevel(1);
        player.setWorker1(box1);

        try {
            player.movePrometheus(box2, box3, player.getWorkers().get(0));
        }catch(PrometheusMovementException e){
            assertEquals(player.getWorkers().get(0).getBox(), box1);
            assertEquals(0, box2.getLevel());
        }
    }

    //Test Case where the movement doesn't work (movement not allowed)
    @Test
    void movePrometheus_wrong() throws InvalidIndicesException, InvalidBoxException, PrometheusMovementException, NotValidLevelException, WrongConstructionException, InvalidConstructionException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);
        Box box3 = map.getBox(1,1);
        box3.setDome(true);
        player.setWorker1(box1);

        try {
            player.movePrometheus(box2, box3, player.getWorkers().get(0));
        }catch(PrometheusMovementException e){
            assertEquals(player.getWorkers().get(0).getBox(), box1);
            assertEquals(0, box2.getLevel());
        }

    }

}