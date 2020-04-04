package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.InvalidIndicesException;
import it.polimi.ingsw.server.model.exceptions.NotValidBoxException;

/**
 * @author Tommaso Lunardon
 */

public class Map {

    private Box[][] map;

    /**
     * Map Constructor
     * creates the Box matrix
     */
    public Map(){
        this.map = new Box [5][5];
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                try {
                    map[i][j] = new Box(i, j, this);
                } catch (NotValidBoxException e) {
                    System.out.println(e);;}
            }

        }
    }

    /**
     *
     * @param x = latitude of box
     * @param y = longitude of box
     * @return Box at position (x,y) in the matrix
     * @throws InvalidIndicesException if (x,y) isn't a position in the matrix
     */
    public Box getBox(int x, int y) throws InvalidIndicesException {

        if(x<0 || y<0 || x>4 || y>4) {throw new InvalidIndicesException();}
        Box box;
        box = map[x][y];
        return box;

    }

    /**
     *
     * @return number of completed buildings in the map
     */
    public int getCompletedBuildings() {
        int x = 0;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++) {
                try {
                    if (getBox(i, j).getLevel() == 4) {
                        x++;
                    }
                } catch (InvalidIndicesException e) {
                    System.out.println(e);;
                }
            }
        }
        return x;
    }
}
