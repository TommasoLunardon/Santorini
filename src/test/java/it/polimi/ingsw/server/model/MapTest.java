package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
    void getBox_correct() throws Exception {
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
    void getBox_Exception(){
        try{map.getBox(5,5); }catch (Exception e) {
            e.printStackTrace();
        }
        try{map.getBox(-1,-1); }catch (Exception e) {
            e.printStackTrace();
        }
    }

}