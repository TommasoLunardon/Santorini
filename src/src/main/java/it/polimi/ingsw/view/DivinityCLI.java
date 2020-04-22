package it.polimi.ingsw.view;

/**
 * These class create a support an some methods for others class DivinityCLI
 * @author Gabriele Gatti
 */

public class DivinityCLI {
    private String name;
    private String description;
    public DivinityCLI(String name, String description){
        this.name=name;
        this.description =description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void printDivinity(){
        System.out.println("\t Name: " + this.name);
        System.out.println("\t Description: " + this.description);
    }

}