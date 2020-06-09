package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;
/**
 * The class PlayerPrometheus represents a player using Prometheus as god
 */
public class PlayerPrometheus extends PlayerNotAthena {

            public PlayerPrometheus(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Prometheus");
            }

    /**
     * Method to perform the special movement available to players with Prometheus as God,
     * in addition to the standard movement method
     * @param box1 box to build in
     * @param box2 box to move in
     * @param worker selected worker to perform the movement
     * @throws Exception if the movement isn't valid
     */
    public void movePrometheus(Box box1, Box box2, Worker worker) throws PrometheusMovementException, WrongConstructionException, InvalidConstructionException, NotValidLevelException {
        try {
            Box startingBox = worker.getBox();
            int startingLevel = startingBox.getLevel();

            super.build(worker, box1);
            try {
                super.move(worker, box2);
            } catch (InvalidMovementException | AthenaConditionException | WrongMovementException | WorkerNotExistException | InvalidIndicesException e) {
                try {
                    box1.setLevel(box1.getLevel() - 1);
                } catch (NotValidLevelException ignored) {}
                box1.setDome(false);
                throw new PrometheusMovementException();
            }
            if (box2.getLevel() > startingLevel) {
                box1.setLevel(box1.getLevel() - 1);
                box1.setDome(false);
                super.move(worker, startingBox);
                throw new PrometheusMovementException();
            }
        } catch (AthenaConditionException | WrongMovementException | WorkerNotExistException | InvalidIndicesException | InvalidMovementException ignored) {}

    }
}
