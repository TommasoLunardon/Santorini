package it.polimi.ingsw.client.view.divinities;

/**
 * These class create a support an some methods for others class DivinityCLI
 * @author Gabriele Gatti
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