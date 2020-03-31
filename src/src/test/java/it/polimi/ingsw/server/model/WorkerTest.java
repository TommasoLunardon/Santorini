package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    private Worker worker;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("aaa",'y');
        Map map = new Map();
        try{Box box = new Box(2,2,map);
            worker = new Worker(player,box);}
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
        worker = null;
    }

    //Test case in which worker can move
    @Test
    void canMove1() {
        assertTrue(worker.canMove());
    }

    //Test case in which worker can't move
    @Test
    void canMove2() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        int i =0;
        while(i<neighbours.size()){
            neighbours.get(i).setDome(true);
            i++;
        }
        assertFalse(worker.canMove());
    }

    //Test case in which worker can build
    @Test
    void canBuild1() {
        assertTrue(worker.canBuild());
    }

    //Test case in which worker can't build
    @Test
    void canBuild2() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        int i =0;
        while(i<neighbours.size()){
            neighbours.get(i).setDome(true);
            i++;
        }
        assertFalse(worker.canMove());
    }

    //Test Case in which the movement is correct
    @Test
    void move1() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(neighbours.size()-1);
        Box old = worker.getBox();
        try{worker.move(next);}catch (Exception e){
            e.printStackTrace();
        }
        assertEquals(next, worker.getBox());
        assertFalse(old.hasWorker());
    }

    //Test Case in which the movement isn't correct
    @Test
    void move2() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(neighbours.size()-1);
        try{next.setLevel(3);} catch (Exception e) {
            e.printStackTrace();
        }
        try{worker.move(next);}catch (Exception e){
            System.out.println("Correct Response!");
        }
    }

    //Test Case in which the construction is correct
    @Test
    void construction1() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(neighbours.size()-1);
        int oldLevel = next.getLevel();
        try{worker.construction(next);}catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(next.getLevel() == oldLevel+1);

    }

    //Test Case in which the construction isn't correct
    @Test
    void construction2() {
        ArrayList<Box> neighbours = worker.getBox().getNeighbours();
        Box next = neighbours.get(neighbours.size()-1);
        next.setDome(true);
        try{worker.construction(next);}catch (Exception e){
            System.out.println("Correct Response!");
        }
    }

    @Test
    void isWinner() {
    }

    @Test
    void isLoser() {
    }

    //Single Test Case to verify the correct value returned
    @Test
    void getBox() {

    }

    //Single Test Case to verify the correct value returned
    @Test
    void getPlayer() {
    }

    @Test
    void setBox() {
    }
}