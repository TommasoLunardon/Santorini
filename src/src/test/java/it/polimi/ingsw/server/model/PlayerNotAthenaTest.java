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
    void move() throws InvalidIndicesException, InvalidBoxException, NotValidLevelException, InvalidMovementException {
        player.update(false);
        Box box1 = map.getBox(0, 0);

        player.setWorker1(box1);
        map.getBox(0, 1).setLevel(2);

        try {
            player.move(player.getWorkers().get(0), map.getBox(0, 1));
        } catch (AthenaConditionException e) {
            System.out.println("correct");
        }
    }

}