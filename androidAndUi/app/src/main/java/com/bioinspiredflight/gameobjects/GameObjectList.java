/**The MIT License (MIT)
 *
 * Copyright © 2020 Bio-Inspired Flight Lab, University of Bristol
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bioinspiredflight.gameobjects;

import androidx.annotation.NonNull;

import com.bioinspiredflight.physics.Movement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class GameObjectList implements Iterable<GameObject>{

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


    public void checkForCollisions(Movement movingObject, ArrayList<GameObject> collidingObjects){
        try {
            lock.lock();
            collidingObjects.clear();
            int i;
            for (i = 0; i < list.size(); i++) {
                GameObject gameObject = list.get(i);
                if (!gameObject.isDrone()) {

                    if (movingObject.isCollision(movingObject, gameObject)) {
                        collidingObjects.add(gameObject);
                        //movingObject.collided = false;
                    }
                }
            }
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

    @NonNull
    @Override
    public Iterator<GameObject> iterator() {
        return this.list.iterator();
    }
}
