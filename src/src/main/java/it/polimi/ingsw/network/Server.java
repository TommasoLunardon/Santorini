package it.polimi.ingsw.network;

public class Server {
    static final int SOCKET_PORT = 1111;

    public server(){
        (new SocketServer(this, SOCKET_PORT)).start();

    }

    public static void main(String[] args) {
        new Server();
    }
}
