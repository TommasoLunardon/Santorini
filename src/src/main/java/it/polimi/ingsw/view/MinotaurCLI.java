package it.polimi.ingsw.view;
/**
 * create a Minotaur instance for out stamp
 * @author Gabriele Gatti
 */
public class MinotaurCLI extends DivinityCLI {
    public MinotaurCLI(){
        super("Asterios","Your worker can move in a opponent's worker box\n if only the next box in same direction is free, the opponent's worker have to move in that box (don't care about the level)");
    }
}