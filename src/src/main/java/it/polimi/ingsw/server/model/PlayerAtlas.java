package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;

public class PlayerAtlas extends PlayerNotAthena {

            public PlayerAtlas(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Atlas");
            }

            /**
             * Method to perform the special construction available to players with Atlas as God,
             * in addition to the standard construction method
             * @param box selected box to perform the construction in
             * @param worker selected player to perform the construction
             * @throws WrongConstructionException if the construction isn't valid
             */
            public void buildAtlas(Box box, Worker worker) throws WrongConstructionException {
                if ((!worker.getBox().getNeighbours().contains(box)) || box.hasDome()) {
                    throw new WrongConstructionException();
                } else {
                    box.setDome(true);
                }
            }
        }
