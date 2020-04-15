package it.polimi.ingsw.network.server;

import it.polimi.ingsw.server.model.Game;

import java.util.logging.Logger;

public class Server {
    static final int SOCKET_PORT = 1010;
    public static final Logger LOGGER = Logger.getLogger("Server");

    private Game game;

    public void server(){
        (new SocketServer(this, SOCKET_PORT)).start();


    }

    public static void main(String[] args) {
        new Server();

    }
    void login(String username, Connection connection) { }

}
