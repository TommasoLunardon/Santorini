package it.polimi.ingsw.client.view.divinities;

/**
 * Class used to describe Minotaur Card to the client
 */
public class Minotaur extends Divinity {
    public Minotaur(){
        super("Minotaur","Your worker can move in a opponent worker's box\n only if the next box in same direction is free, the opponent's worker must move in that box");
    }
}