package com.bioinspiredflight.utilities;

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.gameobjects.ObjectiveObject;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LevelHandlerTest {
    GameSketch testGameSketch = new GameSketch();
    GameActivity testGameActivity = new GameActivity();
    LevelHandler testingLevelHandler = new LevelHandler(testGameActivity);
    String filename = "levelTest.csv";

    @Test
    public void levelHandlerNotNull(){
        assertNotNull(testingLevelHandler);
    }

    @Test
    public void generatedObjectListNotNull(){
        GameObjectList testObjectList = new GameObjectList();
        ArrayList<ObjectiveObject> testObjectiveList = new ArrayList<ObjectiveObject>();
        testingLevelHandler.changeLevel(testGameSketch, testObjectList, filename);
        System.out.println(testObjectiveList);

        assertNotNull(testObjectList);
    }

    @Test
    public void generatedObjectListNotEmpty(){
        GameObjectList emptyGameObjectList = new GameObjectList();
        GameObjectList testObjectList = new GameObjectList();
        ArrayList<ObjectiveObject> testObjectiveList = new ArrayList<ObjectiveObject>();
        GameActivity testGameActivity = new GameActivity();

        testingLevelHandler.changeLevel(testGameSketch, testObjectList, filename);
        assertNotEquals(emptyGameObjectList, testObjectiveList);
    }



}