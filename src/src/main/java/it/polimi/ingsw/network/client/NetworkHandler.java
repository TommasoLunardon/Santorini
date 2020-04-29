
package it.polimi.ingsw.network.client;

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

import java.io.IOException;
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
        String m = helper.serialization(event);
        Message message = new Message(m);

        connection.sendClientMessage(message);
    }


    public String receiveCardSelectionEvent() throws InvalidSenderException {
        connection.run();
        Message message = connection.getMessage();
        CardSelectionEvent event = (CardSelectionEvent) helper.deserialization(message.getContent());
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
        CommunicationEvent event = (CommunicationEvent) helper.deserialization(message.getContent());
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
        GameCreatedEvent event = (GameCreatedEvent) helper.deserialization(message.getContent());
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
        GameUpdatingEvent event = (GameUpdatingEvent) helper.deserialization(message.getContent());

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
        GodCardsSelectedEvent event = (GodCardsSelectedEvent) helper.deserialization(message.getContent());

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
        InvalidConstructionEvent event = (InvalidConstructionEvent) helper.deserialization(message.getContent());

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
        InvalidInputEvent event = (InvalidInputEvent) helper.deserialization(message.getContent());

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
        InvalidMovementEvent event = (InvalidMovementEvent) helper.deserialization(message.getContent());

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
        LoserPlayerEvent event = (LoserPlayerEvent) helper.deserialization(message.getContent());

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
        MatchStartsEvent event = (MatchStartsEvent) helper.deserialization(message.getContent());

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
        PlayerJoinedEvent event = (PlayerJoinedEvent) helper.deserialization(message.getContent());

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
        StarterSelectionEvent event = (StarterSelectionEvent) helper.deserialization(message.getContent());

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
        WinnerPlayerEvent event = (WinnerPlayerEvent) helper.deserialization(message.getContent());

        if(!event.getTarget().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return true;
        }

    }

}
