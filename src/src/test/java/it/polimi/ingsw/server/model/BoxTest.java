package it.polimi.ingsw.server.model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tommaso Lunardon
 */
class BoxTest {

    private Box box;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        try{box = new Box(1,2, map);}catch(Exception e){
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
        box = null;
    }

    //Test Case resulting true
    @Test
    void equals1(){
        assertTrue(box.equals(box));
    }

    //Test Case resulting false
    @Test
    void equals2() throws Exception{
        Box wrong = new Box(0,0,map);
        assertFalse(box.equals(wrong));

    }

    //Test case with no worker
    @Test
    void hasWorker1() {
        assertFalse(box.hasWorker());
    }

    //Test case with worker
    @Test
    void hasWorker2() {
        box.setWorker(new Worker(new Player("a", 12, PlayerColor.RED, new Map()),this.box));
        assertTrue(box.hasWorker());
    }

    // Test case with level=0
    @Test
    void getLevel1() {
        assertEquals(0,box.getLevel());
    }

    // Test case with level=4
    @Test
    void getLevel2() {
        try{box.setLevel(3);}catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(3,box.getLevel());
    }

    //Test Case to check that the method works, the correctness of parameters x,y already checked in the constructor
    @Test
    void getPosition() {
        assertEquals(1,box.getPosition()[0]);
        assertEquals(2,box.getPosition()[1]);
    }

    //Test Case to verify the correct disposition of data in the matrix (the logic implementation is the same in getNeighbours())
    @Test
    void getNeighboursMatrix() throws Exception {
        Box[][] neighbours = box.getNeighboursMatrix();
        int x = box.getPosition()[0];
        int y = box.getPosition()[1];
        for (int i =0; i<3; i++){
            for (int j =0; j<3; j++){
                if(!(j==1 && i ==1)){
                assertTrue(neighbours[i][j].equals(map.getBox(x-1+i,y-1+j)));}
            }
        }
    }

    //Test Case in which all 8 possible neighbours exist
    @Test
    void getNeighbours1() {
        ArrayList<Box> neighbours = box.getNeighbours();
        int x = box.getPosition()[0];
        int y = box.getPosition()[1];

        assertEquals(8,neighbours.size());

        for(int i=0; i<3;i++){
            try{assertTrue(neighbours.contains(map.getBox(x-1+i,y)));}catch (Exception e){
                e.printStackTrace();
            }

            try{assertTrue(neighbours.contains(map.getBox(x-1+i,y-1)));}catch (Exception e){
                e.printStackTrace();
            }

            try{assertTrue(neighbours.contains(map.getBox(x-1+i,y+1)));}catch (Exception e){
                e.printStackTrace();
            }

            try{assertTrue(neighbours.contains(map.getBox(x,y-1+i)));}catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    //Case Test for critical situation in which box is at one corner of the map
    @Test
    void getNeighbours3(){
        try{box = new Box(0,0, map);}catch(Exception e){
            e.printStackTrace();
        }
        assertEquals(3,box.getNeighbours().size());

    }

    //Test case with no dome
    @Test
    void hasDome1() {
        assertFalse(box.hasDome());
    }

    //Test case with dome
    @Test
    void hasDome2() {
        box.setDome(true);
        assertTrue(box.hasDome());
    }

    //Test Case with no worker
    @Test
    void getWorker() {
        try{box.getWorker();}catch(Exception e){
            System.out.println("Correct Response!");
        }
    }

    //Test Case with worker
    @Test
    void getWorker2() {
        Worker w = new Worker(new Player("aaa", 12, PlayerColor.RED,new Map()),this.box);
        box.setWorker(w);
        try{ assertEquals(w,box.getWorker());}catch(Exception e){
            e.printStackTrace();
        }

    }

    //Test Case with correct level value
    @Test
    void setLevel1() {
        try{box.setLevel(3);}catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(3,box.getLevel());
    }

    //Test Case with level value < 0
    @Test
    void setLevel2() {
        try{box.setLevel(-1);}catch (Exception e){
            System.out.println("Correct Response!");
        }

    }

    //Test Case with level value > 4
    @Test
    void setLevel3() {
        try{box.setLevel(5);}catch (Exception e){
            System.out.println("Correct Response!");
        }
    }

    //Single Test Case to verify correctness of the assignment
    @Test
    void setDome() {
        box.setDome(true);
        assertTrue(box.hasDome());
    }

    //Test Case with correct input
    @Test
    void setWorker1() {
        Worker w = new Worker(new Player("aaa", 12,PlayerColor.RED, new Map()),this.box);
        try{box.setWorker(w);}catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    //Test Case with worker==null
    @Test
    void setWorker2() {
        Worker w = null;
        try{box.setWorker(w);}catch (NullPointerException e){
            System.out.println("Correct Response!");
        }
    }

    //Single Test Case to verify correctness of the removing
    @Test
    void removeWorker1() {
        Worker w = new Worker(new Player("aaa", 12, PlayerColor.YELLOW, new Map()),this.box);
        box.setWorker(w);
        box.removeWorker();
        assertFalse(box.hasWorker());
    }

}