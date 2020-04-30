
package it.polimi.ingsw.network.client;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.events.vcevents.CardSelectedEvent;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Class Network Handler handles the client's communications with the server.
 */

public class NetworkHandler implements VCEventSender {

    public final JsonHelper helper = new JsonHelper();
    private Client view;
    private SocketConnection connection;
    private String ID;


    public NetworkHandler(){}

    /**
     *  Client connects to the network handler
     * @param client SocketConnection used to connect
     * @param ID Username of the client
     * @param view client's view
     */
    public void clientConnects(SocketConnection client, String ID, Client view){
        connection = client;
        this.ID = ID;
    }


    @Override
    public void send(Event event) {}

    @Override
    public void send(VCEvent event) throws IOException {
        String m = event.toString();
        Message message = new Message(m);

        connection.sendClientMessage(message);
    }


    public String receiveCardSelectionEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        CardSelectionEvent event = (CardSelectionEvent) JsonHelper.deserialization(message.getContent());
        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getSelectedGod();
        }

    }

    public String receiveCommunicationEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        CommunicationEvent event = (CommunicationEvent) JsonHelper.deserialization(message.getContent());
        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getMessage();
        }

    }

    public Object[] receiveGameCreatedEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        GameCreatedEvent event = (GameCreatedEvent) JsonHelper.deserialization(message.getContent());
        Object[] conditions = new Object[2];
        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            conditions[0] = event.getNum();
            conditions[1] = event.getWithGods();
            return conditions;
        }

    }

    public Game receiveGameUpdatingEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        GameUpdatingEvent event = (GameUpdatingEvent) deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getGame();
        }

    }


    public ArrayList<String> receiveGodCardsSelectedEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        GodCardsSelectedEvent event = (GodCardsSelectedEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getSelectedCards();
        }

    }


    //Returns TRUE when received
    public boolean receiveInvalidConstructionEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        InvalidConstructionEvent event = (InvalidConstructionEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return true;
        }

    }


    public Exception receiveInvalidInputEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        InvalidInputEvent event = (InvalidInputEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getException();
        }

    }


    //Returns TRUE when received
    public boolean receiveInvalidMovementEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        InvalidMovementEvent event = (InvalidMovementEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return true;
        }

    }

    //Returns TRUE when received
    public boolean receiveLoserPlayerEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        LoserPlayerEvent event = (LoserPlayerEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return true;
        }

    }

    public void receiveMatchStartsEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        MatchStartsEvent event = (MatchStartsEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            System.out.println("The match starts!");
        }

    }

    public Player receivePlayerJoinedEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        PlayerJoinedEvent event = (PlayerJoinedEvent) deserialization(message.getContent());


        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getP();
        }

    }

    public String receiveStarterSelectionEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        StarterSelectionEvent event = (StarterSelectionEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getStarter();
        }

    }


    //Returns TRUE if the player has won
    public boolean receiveWinnerPlayerEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        WinnerPlayerEvent event = (WinnerPlayerEvent) JsonHelper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return true;
        }

    }

    /**
     * Method used to deserialize the specific events GameUpdatingEvent and PlayerJoinedEvent
     * @param message is the  String to deserialize
     * @return the Event received
     */
    public Object deserialization(String message){
        try {
            byte b[] = Base64.decode(message);
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            System.out.println("Not valid message");
            return null;
        }

    }

}
