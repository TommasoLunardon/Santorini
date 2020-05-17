package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.JsonHelper;
import it.polimi.ingsw.network.events.*;
import it.polimi.ingsw.network.events.mvevents.MVPingEvent;
import it.polimi.ingsw.network.events.vcevents.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.exceptions.InvalidSenderException;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class VirtualView that sends MVEvents to the Client, and receives VCEvent from the Network Handler.
 */

public class VirtualView implements MVEventSender, Serializable {
    private Controller controller;
    private Server server;

    private int counter = 0;

    private long startTime = 10000;
    private long offsetTime = 10000;


    public VirtualView(){}

    public void setServer(Server server) {
        this.server = server;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void send(Event event) {}

    @Override
    public void send(MVEvent event) throws SocketTimeoutException {
        String target = event.getTarget();
        String m = event.toString();
        Message message = new Message(m);
        server.sendMessage(target,message);

    }

    public int[] receiveBoxSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.receive(ID);
        BoxSelectedEvent event = (BoxSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getBox();
        }
    }

    public String receiveCardSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.receive(ID);
        CardSelectedEvent event = (CardSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getCard();
        }
    }

    public ArrayList<String> receiveGodsSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.receive(ID);
        GodsSelectedEvent event = (GodsSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getGods();
        }
    }

    public int receiveNumPlayersSelectedEvent(String ID) throws InvalidSenderException {

        String message = server.receive(ID);
        NumPlayersSelectedEvent event = (NumPlayersSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getNum();
        }
    }

    public Object[]  receivePlayerDataEnteredEvent(String ID) throws InvalidSenderException {

        String message = server.receive(ID);
        PlayerDataEnteredEvent event = (PlayerDataEnteredEvent) JsonHelper.deserialization(message);

        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getData();
        }
    }

    public String receiveStarterSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.receive(ID);
        StarterSelectedEvent event = (StarterSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getStarter();
        }
    }

    public boolean receiveWithGodsSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.receive(ID);
        WithGodsSelectedEvent event = (WithGodsSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    public int receiveWorkerSelectedEvent(String ID) throws InvalidSenderException{

        String message = server.receive(ID);
        WorkerSelectedEvent event = (WorkerSelectedEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getWorker();
        }
    }

    public boolean receiveUseofSpecialPowerEvent(String ID) throws InvalidSenderException{

        String message = server.receive(ID);
        UseofSpecialPowerEvent event = (UseofSpecialPowerEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }


    public boolean receiveRestartConfirmationEvent(String ID) throws InvalidSenderException {
        String message = server.receive(ID);
        RestartConfirmationEvent event = (RestartConfirmationEvent) JsonHelper.deserialization(message);
        if(!(event != null && event.getOrigin().equals(ID))){
            throw new InvalidSenderException();
        }
        else{
            return event.getCondition();
        }
    }

    /**
     * Method used to check that the clients are still connected to the server.
     */
    public void sendPing(String ID){

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                MVPingEvent event = new MVPingEvent(ID);
                try {
                    send(event);
                } catch (SocketTimeoutException e ) {
                    System.out.println("Connection ended");
                    System.exit(0);
                }
                counter++;
            }
        };

        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, startTime, offsetTime);
        } catch (Exception e ) {
            System.out.println("Connection ended");
            System.exit(0);
        }

    }

}


