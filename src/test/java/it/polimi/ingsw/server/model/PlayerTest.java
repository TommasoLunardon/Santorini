package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;
    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
        player = new Player("a", 11,PlayerColor.YELLOW,map);
    }

    @AfterEach
    void tearDown() {
        map = null;
        player = null;
    }

    //Test Case with correct execution of the method
    @Test
    void setWorker_correct() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        Box box = map.getBox(0,0);

        player.setWorker1(box);

        assertEquals(box.getWorker(),player.getWorkers().get(0));
    }

    //Test Case with box == null;
    @Test
    void setWorker_NullPointerException() throws InvalidBoxException {
       Box box = null;
        try{player.setWorker1(box);}catch(NullPointerException e){
            System.out.println("CORRECT");
        }
    }

    //Test Case with worker's Map not valid;
    @Test
    void setWorker_NotValidMap() throws InvalidIndicesException {
        Map map1 = new Map();
        Box box = map1.getBox(0,0);

        try{player.setWorker1(box);}catch(InvalidBoxException e){
            System.out.println(e);
        }
    }

    //Single Test Case
    @Test
    void getPlayerID() {
        assertEquals("a", player.getPlayerID());
    }

    //Single Test Case
    @Test
    void getPlayerAge() {
        assertEquals(11, player.getPlayerAge());
    }

    //Single Test Case
    @Test
    void getColor() {
        assertEquals(PlayerColor.YELLOW, player.getColor());
    }

    //Single Test Case
    @Test
    void getWorkers() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(1,1);

        player.setWorker1(box1);
        player.setWorker2(box2);

        assertEquals(box1.getWorker(),player.getWorkers().get(0));
        assertEquals(box2.getWorker(),player.getWorkers().get(1));
    }



    //Test Case where the Player can move
    @Test
    void canMove_true() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(1,1);

        player.setWorker1(box1);
        player.setWorker2(box2);

        assertTrue(player.canMove());
    }

    //Test Case where the Player can't move
    @Test
    void canMove_false() throws InvalidBoxException, InvalidIndicesException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);

        ArrayList<Box> n1 = box1.getNeighbours();
        ArrayList<Box> n2 = box2.getNeighbours();
        for (int i = 0; i< n1.size(); i++){
            n1.get(i).setDome(true);
            n2.get(i).setDome(true);
        }

        player.setWorker1(box1);
        player.setWorker2(box2);

        assertFalse(player.canMove());
    }

    //Test Case where the player can build
    @Test
    void canBuild_true() throws InvalidIndicesException, InvalidBoxException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(1,1);

        player.setWorker1(box1);
        player.setWorker2(box2);

        assertTrue(player.canBuild());
    }

    //Test Case where the player can't build
    @Test
    void canBuild_false() throws InvalidBoxException, InvalidIndicesException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(4,4);

        ArrayList<Box> n1 = box1.getNeighbours();
        ArrayList<Box> n2 = box2.getNeighbours();
        for (int i = 0; i< n1.size(); i++){
            n1.get(i).setDome(true);
            n2.get(i).setDome(true);
        }

        player.setWorker1(box1);
        player.setWorker2(box2);

        assertFalse(player.canBuild());
    }

    //Test Case where the movement is correct
    @Test
    void move_correct() throws InvalidIndicesException, InvalidBoxException, InvalidMovementException, AthenaConditionException, WrongMovementException, WorkerNotExistException {
        Box box1 = map.getBox(0,0);
        Box box2 = map.getBox(0,1);


        player.setWorker1(box1);

        player.move(player.getWorkers().get(0),box2 );

        assertEquals(player.getWorkers().get(0).getBox(), box2);
        assertFalse(map.getBox(0,0).hasWorker());
    }

    //Test Case where the movement isn't correct (Worker doesn't belong to the player)
    @Test
    void move_WrongWorker() throws InvalidIndicesException, InvalidMovementException, AthenaConditionException {
        Worker w = new Worker(new Player("b", 15, PlayerColor.BLUE,map), map.getBox(2,2));

        try{player.move(w,map.getBox(1,2));}catch(InvalidMovementException e){
            System.out.println("correct response");
        } catch (WorkerNotExistException e) {
            e.printStackTrace();
        } catch (WrongMovementException e) {
            e.printStackTrace();
        }
    }

    //Test case where the construction is correct
    @Test
    void build_correct() throws InvalidIndicesException, InvalidBoxException, InvalidConstructionException, WrongConstructionException {
        Box box1 = map.getBox(0,0);

        player.setWorker1(box1);
        int oldlevel = map.getBox(0,1).getLevel();

        player.build(player.getWorkers().get(0), map.getBox(0,1));

        assertTrue(map.getBox(0,1).getLevel() == oldlevel+1);
    }

    //Test case where the construction isn't correct (Worker doesn't belong to the player)
    @Test
    void build_WrongWorker() throws InvalidIndicesException, WrongConstructionException {
        Worker w = new Worker(new Player("b", 15, PlayerColor.BLUE,map), map.getBox(2,2));

        try{player.build(w,map.getBox(1,2));}catch(InvalidConstructionException  e){
            System.out.println("correct response");
        }
    }

    //Test Case where the operation is performed correctly
    @Test
    void removeWorkers_correct() throws InvalidIndicesException, InvalidBoxException, WorkerNotExistException {
        player.setWorker1(map.getBox(1,1));
        player.setWorker2(map.getBox(0,0));

        player.removeWorkers();

        assertFalse(map.getBox(1,1).hasWorker());
        assertFalse(map.getBox(0,0).hasWorker());


    }

    //Test Case where the player doesn't have workers
    @Test
    void removeWorkers_NoWorkers(){

        try{player.removeWorkers();}catch (WorkerNotExistException e){
            System.out.println("Correct Response");
        }

    }


}