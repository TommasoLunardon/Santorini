package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class SocketServer extends Thread {

    //private static Server server;
    private final int port;
    private ServerSocket serverSocket;

    private ArrayList<SocketConnection> clientsConnections;
    private ArrayList<String> users;
    private  Map < String, ServerConnection> clients;

    public SocketServer(int port) {
        //this.server = server;
        this.port = port;

        clientsConnections = new ArrayList<>();
        users = new ArrayList<>();
    }

    public ArrayList<String> getUsers(){
        ArrayList<String> u = users;
        return u;
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
                SocketConnection client = new SocketConnection(this, newClientConnection );
                clientsConnections.add(client);
                client.run();
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        }
    }

    public void login(String username, SocketConnection connection){
        users.add(username);
        clients.put(username, connection);

    }

    public void disconnect(String username){
        clientsConnections.remove(clients.get(username));
        users.remove(username);
        clients.remove(username);

    }

    public void sendMessage(String user, Message message){
        SocketConnection client = (SocketConnection) clients.get(user);
        client.sendServerMessage(message);
    }

    public Message receiveMessage(String user) throws IOException, ClassNotFoundException {
        SocketConnection client = (SocketConnection) clients.get(user);
        return client.receiveMessage();

    }

}
