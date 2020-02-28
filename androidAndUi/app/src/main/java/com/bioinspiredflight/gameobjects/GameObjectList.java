package com.bioinspiredflight.gameobjects;

import com.bioinspiredflight.physics.Movement;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameObjectList {

    ArrayList<GameObject> list = new ArrayList<>();
    ArrayList<ObjectiveObject> objectiveList = new ArrayList<ObjectiveObject>();

    public ArrayList<ObjectiveObject> getObjectiveList(){
        return this.objectiveList;
    }
    public void addToObjectiveList(ObjectiveObject objective){
        this.objectiveList.add(objective);
    }


    public GameObjectList(){

    }

    public void clear(){
        list.clear();
    }

    public void add(GameObject gameObject){
        list.add(gameObject);
    }

    public int size(){
        return list.size();
    }

    public GameObject get(int index){
        return list.get(index);
    }

    public DroneObject getDrone(){
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

    }


    public int checkForCollisions(Movement movingObject){
        int i;
        for (i = 0; i < list.size() && movingObject.collided == false ; i++) {
            GameObject gameObject = list.get(i);
            if (!gameObject.isDrone()){
                movingObject.isCollision(movingObject, gameObject);
                if (movingObject.collided){
                    i--;
                }
            }
        }
        return i;
    }

    public void drawAllGameObjects3D(){
        for (GameObject gameObject : list){
            gameObject.draw3D();
        }
    }

    public void drawAllGameObjects2D(){
        for (GameObject gameObject : list){
            gameObject.draw2D();
        }
    }

    public void drawNonDroneGameObjects3D(){
        for (GameObject gameObject : list){
            if (!gameObject.isDrone()){
                gameObject.draw3D();
            }
        }
    }

}
