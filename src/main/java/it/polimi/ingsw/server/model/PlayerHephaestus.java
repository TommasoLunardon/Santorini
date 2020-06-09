package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.WrongConstructionException;
/**
 * The class PlayerHephaestus represents a player using Hephaestus as god
 */
public class PlayerHephaestus extends PlayerNotAthena {

            public PlayerHephaestus(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Ephaestus");
            }

            /**
             * Method to perform the special construction available to players with Ephaestus as God,
             * in addition to the standard construction method
             * @param box selected box to perform the construction in
             * @param worker selected player to perform the construction
             * @throws WrongConstructionException if the construction isn't valid
             */
            public void buildHephaestus(Worker worker, Box box) throws WrongConstructionException {

                if (box.getLevel() >= 2) {
                    throw new WrongConstructionException();
                }
                    worker.build(box);
                    worker.build(box);
            }

        }
