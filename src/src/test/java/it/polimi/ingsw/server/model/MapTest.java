package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tommaso Lunardon
 */

class MapTest {

    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @AfterEach
    void tearDown() {
        map = null;
    }

    //Test Case with correct parameters
    @Test
    void getBox1() throws Exception {
        ArrayList<Box> boxes = new ArrayList<Box>();
        for (int i = 0; i<5; i++){
            for(int j = 0; j<5; j++) {
                boxes.add(map.getBox(i,j));
            }
        }
        assertEquals(25,boxes.size());
    }


    //Test Case with wrong parameters
    @Test
    void getBox2() throws Exception {
        try{map.getBox(5,5); }catch (Exception e) {
            e.printStackTrace();
        }
        try{map.getBox(-1,-1); }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Test Case where Map doesn't contain any building completed
    @Test
    void getCompletedBuildings1() {
        assertEquals(0,map.getCompletedBuildings());
    }

    //Test Case where Map contains some buildings completed
    @Test
    void getCompletedBuildings2() {

        for(int n = 0; n<3; n++) {
            try {
                map.getBox(2, n).setLevel(4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assertEquals(3,map.getCompletedBuildings());
    }
}