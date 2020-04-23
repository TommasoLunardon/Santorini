package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.Event;
import it.polimi.ingsw.network.events.EventSender;
import it.polimi.ingsw.network.events.MVEventSender;
import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.mvevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.controller.Controller;

import java.util.Observable;


/*
 Class VirtualView that sends MVEvent to the CLI, and receives VCEvent from the Network Handler, sending them to the Controller to be managed.
 */

public class VirtualView extends Observable implements MVEventSender {
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



    @Override
    public void send(Event event) {

    }


    @Override
    public void send(CardSelectionEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    public void send(CommunicationEvent event){
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(GameCreatedEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(GameUpdatingEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(GodCardsSelectedEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(InvalidConstructionEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(InvalidInputEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(InvalidMovementEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }


    @Override
    public void send(LoserPlayerEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(MatchStartsEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(PlayerJoinedEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }


    @Override
    public void send(StarterSelectionEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    @Override
    public void send(WinnerPlayerEvent event) {
        String target = event.getTarget();
        String m = helper.serialization(event);
        Message message = new Message(m);
        server.sendMessage(target,message);

    }



    //Method used to send a VCEvent to the controller
    public void notify(VCEvent event){
        event.manage((EventSender) controller);
    }



}


