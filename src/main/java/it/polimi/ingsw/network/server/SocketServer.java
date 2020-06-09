package it.polimi.ingsw.network.server;

import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.network.events.vcevents.VCPingEvent;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static it.polimi.ingsw.network.JsonHelper.deserialization;

public class SocketServer extends Thread {

    private final int port;
    private ServerSocket serverSocket;

    private ArrayList<SocketConnection> clientsConnections;
    private ArrayList<String> users;
    private  Map < String, ServerConnection> clients;

    public SocketServer(int port) {
        this.port = port;

        clientsConnections = new ArrayList<>();
        users = new ArrayList<>();
        clients = new HashMap<>();
    }

    public ArrayList<String> getUsers(){
        return users;
    }


    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(20000);
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
                Logger.getGlobal().info("New client tried to connect.");
                SocketConnection client = new SocketConnection(this, newClientConnection );
                clientsConnections.add(client);
                client.run();
                break;
            }catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());

            }
        }
    }

    void login(String username, SocketConnection connection){
        users.add(username);
        clients.put(username, connection);

    }

    public void disconnect(String username){
        SocketConnection client = (SocketConnection) clients.get(username);
        client.disconnect();
        clientsConnections.remove(clients.get(username));
        users.remove(username);
        clients.remove(username);

    }

    public void sendMessage(String user, Message message){
        SocketConnection client = (SocketConnection) clients.get(user);
        client.sendServerMessage(message);
    }

    public Message receiveMessage(String user){
        boolean notPing = false;
        SocketConnection client = (SocketConnection) clients.get(user);
        while (!notPing) {
            try {
                client.run();
                VCPingEvent ping = (VCPingEvent) deserialization(client.getMessage().getContent());
            } catch (ClassCastException | JsonSyntaxException ignored) {
                notPing = true;
            }
        }
        return client.getMessage();

    }

}
