package it.polimi.ingsw.server.model;

public class PlayerPrometheus extends PlayerNotAthena {

            public PlayerPrometheus(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Prometheus");
            }

            // Management of Prometheus Ability left to controller
            //  "turnPrometheus" ==> build(), move() [CHECK NOT UPPER LEVEL], build()
        }
