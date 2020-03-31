package Progetto;

import javafx.scene.layout.HBox;
import javafx.scene.transform.MatrixType;

import javax.swing.*;
import java.lang.management.BufferPoolMXBean;

import java.util.ArrayList;

    class PlayerDivinity extends Player{
    private String godName;
    private Integer nPlayer;
    protected PlayerDivinity(String id, Integer age, Map map, Integer nPlayer, String godName){
        super(id, age, map);
        this.nPlayer=nPlayer;
        this.godName=godName;
    }
    protected String getGodName(){
        return godName;
    }
    protected Integer getnPlayer(){
        Integer nPlayer1 = this.nPlayer;
        return nPlayer1;
    }
}

    class PlayerNotAthena extends PlayerDivinity{
        private Boolean possiblyLevel;
        public PlayerNotAthena(String id, Integer age, Map map,Integer nPlayer, String godName){
            super(id, age, map, nPlayer, godName);
            possiblyLevel = Boolean.TRUE;
        }

        public void move(Box nextBox, Worker worker) throws Exception, AthenaLevelledException{
            try{
                if ((checkPossiblyLevel()||nextBox.getLevel()<=worker.getBox().getLevel()) && nextBox.getNeighbours().contains(worker.getBox())){
                    worker.move(nextBox);
                }
                else {
                    throw new AthenaLevelledException();
                }
            }catch (Exception e){
                System.out.println("ERROR IN MOVE");
            }
        }

        public void update(){
            possiblyLevel=Boolean.FALSE;
        }
        protected Boolean checkPossiblyLevel(){
            Boolean p = this.possiblyLevel;
            return p;
        }
    }

    class PlayerApollo extends PlayerNotAthena {

        public PlayerApollo(String id, Integer age, Map map) {
            super(id, age, map, 4, "Apollo");
        }

        public void move(Box nextBox, Worker worker) throws Exception, AthenaLevelledException {
                Worker wSpostable;
                if ((nextBox.getLevel()<=worker.getBox().getLevel()) && nextBox.getNeighbours().contains(worker.getBox())){
                    if (checkPossiblyLevel()){
                        if (nextBox.hasWorker()) {
                            wSpostable = nextBox.getWorker();
                            wSpostable.setBox(worker.getBox());
                            nextBox.setWorker(wSpostable);
                        }
                        worker.move(nextBox);
                } else {
                    throw new AthenaLevelledException();
                }
            } else  throw new Exception();
        }
    }

    class PlayerArthemis extends PlayerNotAthena {

        Boolean alreadyMove;
        Box initialBox;

        public PlayerArthemis(String id, Integer age, Map map) {
            super(id, age, map, 4, "Arthemis");
        }

        public void move(Box nextBox, Worker worker) throws Exception, AthenaLevelledException {
            try {
                if( (nextBox.getLevel()<=worker.getBox().getLevel()) && nextBox.getNeighbours().contains(worker.getBox())){
                    if (checkPossiblyLevel()){
                        if (!alreadyMove) {
                            initialBox = worker.getBox();
                            alreadyMove = Boolean.TRUE;
                        }
                        else {
                            if (nextBox == initialBox) {
                                throw new Exception("Can't return to your original box");
                            } else {
                                worker.move(nextBox);
                            }
                        }
                    } else throw new AthenaLevelledException();
                }

            } catch (Exception e) {
                System.out.println("ERROR IN MOVE");
            }
        }

        public void construction(Box box, Worker worker) throws Exception {
            try{
                worker.construction(box);
                alreadyMove = Boolean.FALSE;
            }
            catch (Exception e){

            }
        }
    }

    class PlayerAtlas extends PlayerNotAthena {

        public PlayerAtlas(String id, Integer age, Map map) {
            super(id, age, map, 4, "Atlas");
        }

        public void constructionDome(Box box) throws Exception {
            if (box.hasDome()) throw new Exception("CAN'T BUILD A DOME, THE BOX HAS A STRUCTURE");
            else {
                box.setDome(Boolean.TRUE);
            }
        }
    }

    class PlayerDemeter extends PlayerNotAthena {

        private Box initialCostructionBox;
        private Boolean haveBuild;
        private Boolean stopBuild;
        //always lanch stop Demeter after her turn
        public PlayerDemeter(String id, Integer age, Map map) {
            super(id, age, map, 4, "Demeter");
        }

        public void costruction(Worker worker, Box box) throws Exception {
            try {
                if (!haveBuild) {
                    worker.construction(box);
                    haveBuild = Boolean.TRUE;
                    initialCostructionBox = box;
                } else {
                    if (!(stopBuild) || (box == initialCostructionBox))
                        throw new Exception("Can't Build Here Again");
                    else {
                        worker.construction(box);
                        stopBuild = Boolean.TRUE;
                    }
                }
            } catch (Exception e) {

            }
        }

        public void costructionDome(Box box) throws Exception {
            if (box.hasDome() || box.getLevel() == 0 || stopBuild || initialCostructionBox == box)
                throw new Exception("Build Failed");
            else if (haveBuild) {
                box.setDome(Boolean.TRUE);
                stopBuild = Boolean.TRUE;
            } else {
                box.setDome(Boolean.TRUE);
                haveBuild = Boolean.TRUE;
                initialCostructionBox = box;
            }

        }

        public void stopDemeter(){
            initialCostructionBox=null;
            haveBuild=Boolean.FALSE;
            stopBuild=Boolean.FALSE;
        }
    }

    class PlayerEphaestus extends PlayerNotAthena {

        public PlayerEphaestus(String id, Integer age, Map map) {
            super(id, age, map, 4, "Ephaestus");
        }

        public void costruction (Worker worker, Box box, Boolean twiceBuild) throws Exception{
            worker.construction(box);
            if (twiceBuild) worker.construction(box);
        }

    }

    class PlayerMinotaur extends PlayerNotAthena { //Asterios per gli amici tra cui: Euriale, Atalanta, la sovracitata Artemide, Gudao/Gudako, il Capitano Francis Drake, San re Davide per qualche motivo e MASHUUUUUU

        public PlayerMinotaur(String id, Integer age, Map map) {
            super(id, age, map, 4, "Minotaur");
        }

        public void move(Box nextBox, Worker worker) throws Exception{
            Box[][] map;
            int x=0,y=0;
            Box boxDirNext;
            Worker wSpostable;
            if ((checkPossiblyLevel()||nextBox.getLevel()<=worker.getBox().getLevel()) && nextBox.getNeighbours().contains(worker.getBox())){
                if (nextBox.hasWorker()){
                    map=worker.getBox().getNeighboursMatrix();
                    for ( x=0; x<3; x++){
                        for ( y=0; y<3; y++){
                            if (map[x][y]==nextBox){
                                break;
                            }
                        }
                    }
                    boxDirNext=nextBox.getNeighboursMatrix()[x][y];
                    wSpostable = nextBox.getWorker();
                    wSpostable.setBox(boxDirNext);
                    nextBox.setWorker(null);
                }
                worker.move(nextBox);
            }
            else throw new Exception("Can't move");
        }
    }

    class PlayerPan extends PlayerNotAthena {
        public PlayerPan(String id, Integer age, Map map) {
            super(id, age, map, 4, "Pan");
        }

        public void move(Box nextBox, Worker worker) throws Exception {
            Box oldBox;
            if ((checkPossiblyLevel()||nextBox.getLevel()<=worker.getBox().getLevel()) && nextBox.getNeighbours().contains(worker.getBox())){
                oldBox=worker.getBox();
                worker.move(nextBox);
                if (oldBox.getLevel()>=2+nextBox.getLevel()){
                    worker.setWinner(Boolean.TRUE);
                }
            }
            else {
                throw new Exception("Can't Move");
            }
        }

    }

    class PlayerPrometheus extends PlayerNotAthena {
        private Boolean hasMove;

        public PlayerPrometheus(String id, Integer age, Map map) {
            super(id, age, map, 4, "Prometheus");
        }

        //leave gestion of Prometheus Ability to controller
    }

    class PlayerAthena extends PlayerDivinity {

        private ArrayList<PlayerNotAthena> observer;

        public PlayerAthena(String id, Integer age, Map map) {
            super(id, age, map, 4, "Athena");
        }

        public void attach (ArrayList<PlayerNotAthena> observersList){
            for(int x=0; x<observersList.size(); x++) {
                observer.add(x,observersList.get(x));
            }
        }

        public void move(Box nextBox, Progetto.Worker worker) throws Exception {
            Box oldBox = worker.getBox();
            worker.move(nextBox);
            if (nextBox.getLevel() > oldBox.getLevel()) notifyPlayers();
        }

        private void notifyPlayers() {
            for (int con = 0; con < observer.size(); con++) {
                observer.get(con).update();
            }
        }
    }


