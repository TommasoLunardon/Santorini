package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerAtlasTest {
    private PlayerAtlas player;
    private Map map;


    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerAtlas("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Single Test Case to verify the correct execution of the construction, the cases of Exception are the same tested
    //for the standard method BUILD
    @Test
    void buildAtlas() throws InvalidIndicesException, InvalidBoxException, WrongConstructionException {
        Box box1= map.getBox(0,0);
        Box box2 =map.getBox(0,1);
        player.setWorker1(box1);

        player.buildAtlas(box2,player.getWorkers().get(0));

        assertTrue(box2.hasDome());
    }
}