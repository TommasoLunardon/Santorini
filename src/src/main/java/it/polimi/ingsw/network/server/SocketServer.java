package it.polimi.ingsw.network.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketServer extends Thread {

    private Server server;
    private int port;
    private java.net.ServerSocket serverSocket;

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket newClientConnection = serverSocket.accept();
                Logger.getGlobal().info("New client connected.");
                new SocketConnection(this, newClientConnection);
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        }
    }
    void login(String username, SocketConnection connection) {
        server.login(username, connection);
    }
}
