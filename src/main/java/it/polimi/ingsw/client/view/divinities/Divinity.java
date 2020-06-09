package it.polimi.ingsw.client.view.divinities;

/**
 * Class used to describe a generic Divinity Card to the client
 */
public class Divinity {
    private String name;
    private String description;
    public Divinity(String name, String description){
        this.name=name;
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }


}