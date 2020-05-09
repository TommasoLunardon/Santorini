package it.polimi.ingsw.client.view.divinities;

/**
 * create a Minotaur instance for out stamp
 * @author Gabriele Gatti
 */
public class Minotaur extends Divinity {
    public Minotaur(){
        super("Minotaur","Your worker can move in a opponent worker's box\n only if the next box in same direction is free, the opponent's worker must move in that box");
    }
}