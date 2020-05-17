package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.NotValidLevelException;
import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEphaestusTest {

    private Map map;
    private PlayerHephaestus player;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerHephaestus("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case where the construction is correct
    @Test
    void buildEphaestus1() throws InvalidIndicesException, InvalidBoxException, NotValidLevelException, WrongConstructionException {
        Box box1= map.getBox(0,0);
        Box box2 =map.getBox(0,1);

        player.setWorker1(box1);
        player.buildHephaestus(player.getWorkers().get(0),box2);

        assertEquals(2, box2.getLevel());

    }

    //Test Case where the construction isn't correct (The second construction is a Dome)
    @Test
    void buildEphaestus2() throws InvalidIndicesException, InvalidBoxException, NotValidLevelException {
        Box box1= map.getBox(0,0);
        Box box2 =map.getBox(0,1);
        box2.setLevel(2);

        player.setWorker1(box1);
        try{player.buildHephaestus(player.getWorkers().get(0),box2);}catch(WrongConstructionException e){
            System.out.println("Correct!");
        }
    }
}