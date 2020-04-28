package it.polimi.ingsw.network.server;


import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketServer extends Thread {

    private final Server server;
    private final int port;
    private ServerSocket serverSocket;

    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Socket newClientConnection;
            try {
                newClientConnection = serverSocket.accept();
                Logger.getGlobal().info("New client connected.");
                new SocketConnection(this,newClientConnection );
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        }
    }
    static void login(String username, ServerConnection connection){
        Server.login(username,connection);
    }

}
