package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.NotValidLevelException;
import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;
/**
 * The class PlayerDemeter represents a player using Demeter as god
 */
public class PlayerDemeter extends PlayerNotAthena {

            public PlayerDemeter(String id, Integer age, PlayerColor color, Map map) {
                super(id, age, map, color, 4, "Demeter");
            }

            /**
             * Method to perform the special construction available to players with Demeter as God,
             * in addition to the standard construction method
             * @param box1 first selected box to perform the construction in
             * @param box2 second selected box to perform the construction in
             * @param worker selected player to perform the construction
             * @throws WrongConstructionException if the construction isn't valid
             */
            public void buildDemeter(Worker worker, Box box1, Box box2) throws WrongConstructionException{

                if (box1.equals(box2)) {
                    throw new WrongConstructionException();
                }

                worker.build(box1);
                try {
                    worker.build(box2);
                }catch(WrongConstructionException e){
                    if(box1.hasDome()){
                        box1.setDome(false);
                        try {
                            box1.setLevel(3);
                        } catch (NotValidLevelException ignored) {}
                    }else{
                        int oldLevel = box1.getLevel();
                        try {
                            box1.setLevel(oldLevel- 1);
                        } catch (NotValidLevelException ignored){}
                    }
                    throw new WrongConstructionException();
                }

            }
}
