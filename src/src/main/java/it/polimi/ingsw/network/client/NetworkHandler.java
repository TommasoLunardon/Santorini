
package it.polimi.ingsw.network.client;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.network.events.vcevents.VCPingEvent;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.network.events.mvevents.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Class Network Handler handles the client's communications with the server.
 */

public class NetworkHandler implements VCEventSender {

    private SocketConnection connection;
    private String ID;

    private int counter = 0;

    private long startTime = 10000;
    private long offsetTime = 10000;


    public NetworkHandler(){}

    public void setConnection(SocketConnection con){
        this.connection = con;
        ID = con.getUsername();
    }

    @Override
    public void send(Event event) {}

    @Override
    public void send(VCEvent event) throws IOException, SocketTimeoutException {
        String m = event.toString();
        Message message = new Message(m);
        connection.sendClientMessage(message);
    }


    public Message receiveMessage(){
        connection.run();
        return connection.getMessage();

    }

    /**
     * Method used to deserialize the specific events GameUpdatingEvent and PlayerJoinedEvent
     * @param message is the  String to deserialize
     * @return the Event received
     */
    public Object deserialization(String message){
        try {
            byte[] b = Base64.decode(message);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Method used to check that the clients are still connected to the server.
     */
    public void sendPing(){

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                VCPingEvent event = new VCPingEvent(connection.getUsername());
                try {
                    send(event);
                } catch (IOException e)
                {
                    System.out.println("Sorry but the connection went down and the game ended");
                    System.exit(0);
                }
                counter++;
            }
        };

        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, startTime, offsetTime);
        } catch (Exception e) {
            System.out.println("Sorry but the connection went down and the game ended");
            System.exit(0);
        }

    }
}
