
package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;
import it.polimi.ingsw.server.model.*;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Controller implements Observer{
    private Game game;
    private VirtualView view;
    private Server server;
    public final JsonHelper helper = new JsonHelper();

    public Game getGame(){
        Game g = game;
        return g;
    }

    public void setGame(Game g){
        this.game = g;
    }

    public void setServer(Server server) {
        this.server = server;
    }


    public Controller(VirtualView view){

        this.view = view;
        view.setController(this);
    }

    //Method used to receive BoxSelectedEvent
    public Box receiveBoxSelectedEvent(String ID) throws InvalidSenderException {

        Message m = server.listen();
        String message = server.receivedMessage(m);
        BoxSelectedEvent event = (BoxSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getBox();
        }
    }

    public String receiveCardSelectedEvent(String ID) throws InvalidSenderException {

        Message m = server.listen();
        String message = server.receivedMessage(m);
        CardSelectedEvent event = (CardSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCard();
        }
    }

    public ArrayList<String> receiveGodsSelectedEvent(String ID) throws InvalidSenderException {

        Message m = server.listen();
        String message = server.receivedMessage(m);
        GodsSelectedEvent event = (GodsSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getGods();
        }
    }


    public int receiveNumPlayersSelectedEvent(String ID) throws InvalidSenderException {

        Message m = server.listen();
        String message = server.receivedMessage(m);
        NumPlayersSelectedEvent event = (NumPlayersSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getNum();
        }
    }

    public Object[]  receivePlayerDataEnteredEvent(String ID) throws InvalidSenderException {

        Message m = server.listen();
        String message = server.receivedMessage(m);
        PlayerDataEnteredEvent event = (PlayerDataEnteredEvent) helper.deserialization(message);

        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getData();
        }
    }

    public String receiveStarterSelectedEvent(String ID) throws InvalidSenderException{
        Message m = server.listen();
        String message = server.receivedMessage(m);
        StarterSelectedEvent event = (StarterSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getStarter();
        }
    }


    public boolean receiveWithGodsSelectedEvent(String ID) throws InvalidSenderException{
        Message m = server.listen();
        String message = server.receivedMessage(m);
        WithGodsSelectedEvent event = (WithGodsSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public Worker receiveWorkerSelectedEvent(String ID) throws InvalidSenderException{
        Message m = server.listen();
        String message = server.receivedMessage(m);
        WorkerSelectedEvent event = (WorkerSelectedEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getWorker();
        }
    }

    public boolean receiveUseofSpecialPowerEvent(String ID) throws InvalidSenderException{
        Message m = server.listen();
        String message = server.receivedMessage(m);
        UseofSpecialPowerEvent event = (UseofSpecialPowerEvent) helper.deserialization(message);
        if(!event.getOrigin().equals(ID)){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }





    @Override
    public void update(Observable o, Object arg) {

    }


}
