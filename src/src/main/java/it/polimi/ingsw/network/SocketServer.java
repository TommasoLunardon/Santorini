package it.polimi.ingsw.network;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketServer extends Thread{

    private SocketConnection server;
    private int port;
    private java.net.ServerSocket serverSocket;

    public void SocketServer(SocketConnection server, int port) {
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
}
