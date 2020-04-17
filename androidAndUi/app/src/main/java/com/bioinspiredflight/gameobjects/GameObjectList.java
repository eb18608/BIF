package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.physics.Movement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class GameObjectList {

    private ReentrantLock lock = new ReentrantLock();

    ArrayList<GameObject> list = new ArrayList<>();
    ArrayList<ObjectiveObject> objectiveList = new ArrayList<ObjectiveObject>();

    public ArrayList<ObjectiveObject> getObjectiveList(){
        return this.objectiveList;
    }
    public ArrayList<GameObject> getList() { return this.list; }
    public void addToObjectiveList(ObjectiveObject objective){
        this.objectiveList.add(objective);
    }

    public GameObjectList(){

    }

    public void clear(){
        try {
            lock.lock();
            list.clear();
            objectiveList.clear();
        } finally {
            lock.unlock();
        }
    }

    public void add(GameObject gameObject){
        try {
            lock.lock();
            list.add(gameObject);
        } finally {
            lock.unlock();
        }

    }

    public int size(){
        try {
            lock.lock();
            return list.size();
        } finally {
            lock.unlock();
        }

    }

    public GameObject get(int index){
        try {
            lock.lock();
            return list.get(index);
        } finally {
            lock.unlock();
        }
    }

    public DroneObject getDrone(){
        try {
            lock.lock();
            boolean droneFound = false;
            int i = 0;
            while (i < list.size() && !droneFound){
                droneFound = list.get(i).isDrone();
                if (!droneFound){
                    i++;
                }
            }
            if (droneFound){
                return (DroneObject)list.get(i);
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }


    }


    public int checkForCollisions(Movement movingObject){
        try {
            lock.lock();
            int i;
            for (i = 0; i < list.size() && movingObject.collided == false; i++) {
                GameObject gameObject = list.get(i);
                if (!gameObject.isDrone()) {
                    movingObject.isCollision(movingObject, gameObject);
                    if (movingObject.collided) {
                        i--;
                    }
                }
            }
            return i;
        } finally {
            lock.unlock();
        }
    }

    public void drawAllGameObjects3D(){
        try {
            lock.lock();
            for (GameObject gameObject : list){
                gameObject.draw3D();
            }
        } finally {
            lock.unlock();
        }

    }

    public void drawAllGameObjects2D(){
        try {
            lock.lock();
            for (GameObject gameObject : list){
                gameObject.draw2D();
            }
        } finally {
            lock.unlock();
        }

    }

    public void drawNonDroneGameObjects3D(){
        try {
            lock.lock();
            for (GameObject gameObject : list){
                if (!gameObject.isDrone()){
                    gameObject.draw3D();
                }
            }
        } finally {
            lock.unlock();
        }

    }

}
