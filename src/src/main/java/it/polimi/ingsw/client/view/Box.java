package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;

/**
 * Abstract class Box used to manage the client's view.
 */

public abstract class Box {

    public abstract int getLevel();

    public abstract String[][] getBox();

    public abstract void setWorker(PlayerColor color) throws InputFailedException;

    public abstract void printBox(int line);

    public abstract void buildStructure();

    public abstract void setLevel(int level);

    public abstract void buildDome();

    public abstract int getDimension();

    public abstract void setBoxWhitOutWorker();
}
