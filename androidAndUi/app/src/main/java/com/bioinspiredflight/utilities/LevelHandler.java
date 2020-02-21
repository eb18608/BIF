package com.bioinspiredflight.utilities;

import com.bioinspiredflight.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.TreeMap;

import processing.core.PVector;

public abstract class LevelHandler {

    public static void changeLevel(ArrayList<GameObject> gameObjects, String fileName){
        gameObjects.clear();
        // populate gameObjects from the file of that file name
    }

    private static TreeMap<String, PVector> readLevelFile(String fileName){
        TreeMap<String, PVector> table = new TreeMap<>();
        return table;
    }

}
