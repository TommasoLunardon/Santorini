package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.PlayerColor;

import java.io.IOException;

public class PlayerDataEnteredEvent extends VCEvent {

    private String ID;
    private  int age;
    private PlayerColor color;

    public PlayerDataEnteredEvent(String origin, String ID, int age, PlayerColor color) {
        super(origin);
        this.ID = ID;
        this.age = age;
        this.color = color;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }

    public String getID() {
        return ID;
    }

    public int getAge() {
        return age;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Object[] getData(){
        Object[] data = new Object[4];
        data[0] = getID();
        data[1] = getAge();
        data[2] = getColor();
        return data;
    }
}
