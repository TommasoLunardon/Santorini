package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidBoxException;
import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDemeterTest {
    private PlayerDemeter player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new PlayerDemeter("a", 13, PlayerColor.YELLOW, map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    // Test Case where the construction is correct
    @Test
    void buildDemeter1() throws InvalidIndicesException, InvalidBoxException, WrongConstructionException {
        Box box1= map.getBox(0,0);
        Box box2 =map.getBox(0,1);
        Box box3 =map.getBox(1,0);
        player.setWorker1(box1);

        player.buildDemeter(player.getWorkers().get(0),box2,box3);
        assertEquals(1, box2.getLevel());
        assertEquals(1, box3.getLevel());
    }


    //Test Case where the construction isn't correct (Two boxes are the same)
    @Test
    void buildDemeter2() throws InvalidIndicesException, InvalidBoxException {
        Box box1= map.getBox(0,0);
        Box box2 =map.getBox(0,1);
        Box box3 =map.getBox(0,1);
        player.setWorker1(box1);

        try{player.buildDemeter(player.getWorkers().get(0),box2,box3);}catch(WrongConstructionException e){
            System.out.println("Correct!");
        }
    }
}