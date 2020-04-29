package it.polimi.ingsw.network.events.vcevents;

import it.polimi.ingsw.network.events.VCEvent;
import it.polimi.ingsw.network.events.VCEventSender;
import it.polimi.ingsw.server.model.PlayerColor;

import java.io.IOException;

public class PlayerDataEnteredEvent extends VCEvent {

    private  int age;
    private PlayerColor color;

    public PlayerDataEnteredEvent(String origin, int age, PlayerColor color) {
        super(origin);
        this.age = age;
        this.color = color;
    }

    @Override
    public void manage(VCEventSender eventSender) throws IOException {
        eventSender.send(this);

    }


    public int getAge() {
        return age;
    }

    public PlayerColor getColor() {
        return color;
    }

    public Object[] getData(){
        Object[] data = new Object[3];
        data[0] = getAge();
        data[1] = getColor();
        return data;
    }
}
