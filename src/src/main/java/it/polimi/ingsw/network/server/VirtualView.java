package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.Box;
import it.polimi.ingsw.server.model.PlayerColor;
import it.polimi.ingsw.server.model.Worker;

import java.util.ArrayList;




/*
 Class VirtualView that sends MVEvent to the CLI, and receives VCEvent from the Network Handler, sending them to the Controller to be managed.
 */

public class VirtualView implements MVEventSender {
    private Controller controller;
    public final JsonHelper helper = new JsonHelper();
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
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    //Method used to receive BoxSelectedEvent
    public Box receiveBoxSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        BoxSelectedEvent event = (BoxSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getBox();
        }
    }

    public String receiveCardSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        CardSelectedEvent event = (CardSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCard();
        }
    }

    public ArrayList<String> receiveGodsSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        GodsSelectedEvent event = (GodsSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getGods();
        }
    }


    public int receiveNumPlayersSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        NumPlayersSelectedEvent event = (NumPlayersSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getNum();
        }
    }

    public Object[]  receivePlayerDataEnteredEvent(String ID) throws InvalidSenderException {

        String message = server.listen();
        PlayerDataEnteredEvent event = (PlayerDataEnteredEvent) helper.deserialization(message);

        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getData();
        }
    }

    public String receiveStarterSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        StarterSelectedEvent event = (StarterSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getStarter();
        }
    }


    public boolean receiveWithGodsSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        WithGodsSelectedEvent event = (WithGodsSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public Worker receiveWorkerSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        WorkerSelectedEvent event = (WorkerSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getWorker();
        }
    }

    public boolean receiveUseofSpecialPowerEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        UseofSpecialPowerEvent event = (UseofSpecialPowerEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public PlayerColor receiveColorSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.listen();
        ColorSelectedEvent event = (ColorSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getColor();
        }
    }





}


