package it.polimi.ingsw.server;

import it.polimi.ingsw.network.SocketServer;

public class Server {
    static final int SOCKET_PORT = 1111;

    public void server(){
        (new SocketServer(this, SOCKET_PORT)).start();


    }

    public static void main(String[] args) {
        new Server();
    }
}
