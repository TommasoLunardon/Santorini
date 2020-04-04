package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.exceptions.*;

import java.util.ArrayList;

/**
 * @author Gabriele Gatti, Tommaso Lunardon
 * Subclasses of Player, used to play games with Divinity Cards
 */

    class PlayerDivinity extends Player{
    private String godName;
    private Integer nPlayer;

    protected PlayerDivinity(String id, Integer age, Map map, PlayerColor color, Integer nPlayer, String godName){
        super(id, age, color, map);
        this.nPlayer = nPlayer;
        this.godName = godName;
    }

    /**
     *
     * @return The name of the God's Card assigned to the player
     */
    protected String getGodName(){

        return godName;
    }

    /**
     *
     * @return the  accepted number of players in the game for the specific card used
     */
    protected Integer getnPlayer(){
        int nPlayer1 = this.nPlayer;
        return nPlayer1;
    }
}

    class PlayerNotAthena extends PlayerDivinity{

        private boolean freeMovement;

        public PlayerNotAthena(String id, Integer age, Map map, PlayerColor color, Integer nPlayer, String godName){

            super(id, age, map, color,  nPlayer, godName);

            freeMovement = true;
        }

        /**
         * Override of the movement method, to satisfy the eventual Athena's restriction
         * @param worker is the worker that will perform the movement
         * @param nextBox is the box in which the worker will move
         * @throws AthenaConditionException if the Athena Condition isn't followed
         */
        public void move(Worker worker, Box nextBox) throws AthenaConditionException, InvalidMovementException {
            if (checkFreeMovement()){

                super.move(worker,nextBox);
            }
            else{
                int oldLevel = worker.getBox().getLevel();

                try{
                    worker.move(nextBox);
                }catch (Exception e){
                    System.out.println(e);;
                }
                if(oldLevel < worker.getBox().getLevel()){
                    throw new AthenaConditionException();
                }

            }
        }

        /**
         * Method used to update the Athena Condition (Pattern Observer)
         * @param condition
         */
        public void update(boolean condition){

            freeMovement = condition;
        }

        protected Boolean checkFreeMovement(){

            Boolean p = this.freeMovement;
            return p;
        }
    }

    class PlayerApollo extends PlayerNotAthena {

        public PlayerApollo(String id, Integer age, PlayerColor color, Map map) {

            super(id, age, map, color, 4, "Apollo");
        }

        /**
         * Method to perform the special movement available only to players having Apollo as God,
         * in addition to the standard movement
         * @param worker selected worker to perform the movement
         * @param nextBox selected Box to move in
         * @throws WrongMovementException if the movement isn't valid
         */
        public void moveApollo(Worker worker, Box nextBox) throws WrongMovementException, AthenaConditionException, WorkerNotExistException {

            if (!checkFreeMovement() && worker.getBox().getLevel() < nextBox.getLevel()) {
                throw new AthenaConditionException();
            }

            if (!worker.getBox().getNeighbours().contains(nextBox) || nextBox.hasDome() || nextBox.getLevel() > worker.getBox().getLevel() + 1) {
                throw new WrongMovementException();
            }

            if (!nextBox.hasWorker() || this.getWorkers().contains(nextBox.getWorker())) {
                throw new WrongMovementException();
            }

            try{Worker enemy = nextBox.getWorker();

            Box oldBox = worker.getBox();

            nextBox.setWorker(worker);
            oldBox.setWorker(enemy);
            worker.setBox(nextBox);
            enemy.setBox(oldBox);

            if (worker.getBox().getLevel() == 3) {
                setWinner(true);
            }
            }catch ( WorkerNotExistException e){
                System.out.println(e);
            }
        }
    }

        class PlayerArthemis extends PlayerNotAthena {

            public PlayerArthemis(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Arthemis");
            }

            /**
             * Method to perform the special movement available to player with Arthemis as God,
             * in addition to the standard movement method
             * @param nextBox1 first box to move in
             * @param nextBox2 second box to move in
             * @param worker selected worker to perform the movement
             * @throws WrongMovementException if the movement isn't valid
             */
            public void moveArthemis(Box nextBox1, Box nextBox2, Worker worker) throws WrongMovementException {

                Box initialBox = worker.getBox();
                try {
                    worker.move(nextBox1);
                } catch (WrongMovementException e) {
                    System.out.println(e);;
                }
                try {
                    worker.move(nextBox2);

                    if (nextBox2.equals(initialBox)) {
                        throw new WrongMovementException();
                    }
                } catch (WrongMovementException e) {
                    System.out.println(e);;
                }
            }


        }

        class PlayerAtlas extends PlayerNotAthena {

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

        class PlayerDemeter extends PlayerNotAthena {

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
            public void buildDemeter(Worker worker, Box box1, Box box2) throws WrongConstructionException {

                if (box1.equals(box2)) {
                    throw new WrongConstructionException();}

                try {
                    worker.build(box1);
                } catch (WrongConstructionException e) {
                    System.out.println(e);;
                }

                try {
                    worker.build(box2);

                } catch (WrongConstructionException e) {
                    System.out.println(e);;
                }
            }

        }

        class PlayerEphaestus extends PlayerNotAthena {

            public PlayerEphaestus(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Ephaestus");
            }

            /**
             * Method to perform the special construction available to players with Ephaestus as God,
             * in addition to the standard construction method
             * @param box selected box to perform the construction in
             * @param worker selected player to perform the construction
             * @throws WrongConstructionException if the construction isn't valid
             */
            public void buildEphaestus(Worker worker, Box box) throws WrongConstructionException {
                try {
                    worker.build(box);
                    worker.build(box);

                    if (box.hasDome()) {
                        throw new WrongConstructionException();
                    }
                } catch (WrongConstructionException e) {
                    System.out.println(e);
                }

            }

        }

        class PlayerMinotaur extends PlayerNotAthena {

            public PlayerMinotaur(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Minotaur");
            }

            /**
             * Method to perform the special movement available to players with Minotaur as god,
             * in addition to the standard movement
             * @param worker is the selected worker to perform the movement
             * @param nextBox is the selected box to move in
             * @throws WrongMovementException if the movement isn't valid
             */
            public void moveMinotaur(Worker worker, Box nextBox) throws WrongMovementException, WorkerNotExistException, InvalidIndicesException {

                if (!checkFreeMovement() && worker.getBox().getLevel() < nextBox.getLevel()) {
                    throw new WrongMovementException();
                }

                if (!worker.getBox().getNeighbours().contains(nextBox) || nextBox.hasDome() || nextBox.getLevel() > worker.getBox().getLevel() + 1) {
                    throw new WrongMovementException();
                }

                if (!nextBox.hasWorker()||this.getWorkers().contains(nextBox.getWorker())) {
                    throw new WrongMovementException();
                }

                Worker enemy = nextBox.getWorker();
                Box oldBox = worker.getBox();
                int dirX = nextBox.getPosition()[0] - oldBox.getPosition()[0];
                int dirY = nextBox.getPosition()[1] - oldBox.getPosition()[1];
                Box nextBox2 = playerMap.getBox(nextBox.getPosition()[0] + dirX, nextBox.getPosition()[1] + dirY);

                if (nextBox2.hasWorker() || nextBox2.hasDome()) {
                    throw new WrongMovementException();
                }

                nextBox.setWorker(worker);
                nextBox2.setWorker(enemy);
                worker.setBox(nextBox);
                enemy.setBox(nextBox2);
                oldBox.removeWorker();

                if (worker.getBox().getLevel() == 3) {
                    setWinner(true);
                }

            }
        }

        class PlayerPan extends PlayerNotAthena {

            public PlayerPan(String id, Integer age, PlayerColor color, Map map) {
                super(id, age, map, color, 4, "Pan");
            }

            /**
             * Method for movement that takes into account the additional win condition
             * assigned to players with Pan as God
             * @param worker is the worker that will perform the movement
             * @param nextBox is the box in which the worker will move
             * @throws InvalidMovementException if the movement isn't valid
             */
            public void move(Worker worker, Box nextBox) throws InvalidMovementException {
                int oldLevel = worker.getBox().getLevel();

                try {
                    super.move(worker, nextBox);
                } catch (AthenaConditionException e) {
                    System.out.println(e);
                }
                if (worker.getBox().getLevel() <= oldLevel - 2) {
                    setWinner(true);
                }

            }

        }

        class PlayerPrometheus extends PlayerNotAthena {

            public PlayerPrometheus(String id, Integer age, PlayerColor color, Map map) {

                super(id, age, map, color, 4, "Prometheus");
            }

            //leave management of Prometheus Ability to controller
            //  "turnPrometheus" ==> build(), move() [CHECK NOT UPPER LEVEL], build()
        }

        class PlayerAthena extends PlayerDivinity {

            private ArrayList<PlayerNotAthena> observer = new ArrayList<PlayerNotAthena>();

            public PlayerAthena(String id, Integer age, PlayerColor color, Map map) {
                super(id, age, map, color, 4, "Athena");
            }

            /**
             *Method to assign other players as Observers of Athena Condition
             * @param observersList is the list of players playing the same game as the one having Athena as god
             */
            public void attach(ArrayList<PlayerNotAthena> observersList) {
                for (int x = 0; x < observersList.size(); x++) {
                    observer.add(x, observersList.get(x));
                }
            }

            /**
             * Method that overrides the standard movement and assigns the Athena Condition if
             * the level is increased
             * @param worker is the worker that will perform the movement
             * @param nextBox is the box in which the worker will move
             */
            public void move( Worker worker, Box nextBox) throws InvalidMovementException, AthenaConditionException {
                int oldLevel = worker.getBox().getLevel();
                    super.move(worker,nextBox);

                    if (worker.getBox().getLevel() > oldLevel) {
                        notifyPlayers(false);
                    }
                    else {
                        notifyPlayers(true);
                    }


            }

            /**
             * Method used to update the Athena Condition an notify the Observers
             * @param condition
             */
            private void notifyPlayers(boolean condition) {
                for (int i = 0; i < observer.size(); i++) {
                    observer.get(i).update(condition);
                }
            }
        }


