package it.polimi.ingsw.network.server;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.ConnectionRequest;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


/**
 * Class VirtualView that sends MVEvents to the Client, and receives VCEvent from the Network Handler.
 */

public class VirtualView implements MVEventSender {
    private Controller controller;
    private Server server;




    public VirtualView(){}

    public void setServer(Server server) {
        this.server = server;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void removeController(){
        this.controller = null;
    }



    @Override
    public void send(Event event) {

    }

    @Override
    public void send(MVEvent event) {
        String target = event.getTarget();
        String m = event.toString();
        Message message = new Message(m);
        server.sendMessage(target,message);

    }


    public Box receiveBoxSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        BoxSelectedEvent event = (BoxSelectedEvent) deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getBox();
        }
    }

    public String receiveCardSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        CardSelectedEvent event = (CardSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCard();
        }
    }

    public ArrayList<String> receiveGodsSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        GodsSelectedEvent event = (GodsSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getGods();
        }
    }


    public int receiveNumPlayersSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        NumPlayersSelectedEvent event = (NumPlayersSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getNum();
        }
    }

    public Object[]  receivePlayerDataEnteredEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        PlayerDataEnteredEvent event = (PlayerDataEnteredEvent) JsonHelper.deserialization(message);

        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getData();
        }
    }

    public String receiveStarterSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        StarterSelectedEvent event = (StarterSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getStarter();
        }
    }


    public boolean receiveWithGodsSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        WithGodsSelectedEvent event = (WithGodsSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public Worker receiveWorkerSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        WorkerSelectedEvent event = (WorkerSelectedEvent) deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getWorker();
        }
    }

    public boolean receiveUseofSpecialPowerEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        UseofSpecialPowerEvent event = (UseofSpecialPowerEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public PlayerColor receiveColorSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        ColorSelectedEvent event = (ColorSelectedEvent) JsonHelper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getColor();
        }
    }



    public String receiveConnectionRequest(){
        String message = server.listen();
        ConnectionRequest request = (ConnectionRequest) JsonHelper.deserialization(message);
        return request.getSenderUsername();
    }

    /**
     * Method used to deserialize the specific events BoxSelectedEvent and WorkerSelectedEvent
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


