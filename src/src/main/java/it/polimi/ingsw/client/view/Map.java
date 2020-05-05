package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

/**
 * Abstract class Map used to manage the client's view.
 */
public abstract class Map {

    public abstract void setWorker(int x, int y, PlayerColor color) throws InputFailedException;

    public abstract void build(int boxX, int boxY) throws InputFailedException;

    public abstract void buildDome(int boxX, int boxY);

    public abstract void moveWorker(int boxXgo, int boxYgo, Worker worker) throws InputFailedException;

    public abstract void printMap();

    public abstract BoxCLI getBoxCLI(int x, int y);
}
