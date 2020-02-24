package com.bioinspiredflight.utilities;

import android.app.Activity;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.gameobjects.Interactable;
import com.bioinspiredflight.gameobjects.GameObjectList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import processing.core.PApplet;

public class LevelHandler {

    private Activity activity;

    public LevelHandler(Activity activity){
        this.activity = activity;
    }

    public void changeLevel(GameSketch sketch, GameObjectList gameObjects, String fileName){
        gameObjects.clear();
        TreeMap<String, Data> table = readLevelFile(fileName);
        // populate gameObjects from the file of that file name
        for (Map.Entry<String, Data> entry : table.entrySet()){
            //entity stuff here
            System.out.println(entry.toString());
            gameObjects.add(createGameObject(sketch, entry));
        }

    }

    private GameObject createGameObject(GameSketch sketch, Map.Entry<String, Data> entry){
        //Optional<GameObject> gameObject = Optional.empty();
        GameObject gameObject = null;
        Data data = entry.getValue();
        String key = entry.getKey();
        if (key.startsWith("drone")){
            gameObject =
                    new DroneObject(sketch, data.getX(), data.getY(), data.getZ(), data.getScale());
        } else if (key.startsWith("building")){
            gameObject =
                    new BuildingObject(sketch, data.getX(), data.getY(), data.getZ(), data.getScale());
        } else if (key.startsWith("objective")){

        }
        return gameObject;
    }

    private TreeMap<String, Data> readLevelFile(String fileName){
        TreeMap<String, Data> table = new TreeMap<>();
        try {
            InputStream inputStream = activity.getAssets().open(fileName);
            InputStreamReader reader = null;
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord record : parser){
                //extract initial starting pos and scale from file
                float x = Float.parseFloat(record.get(1));
                float y = Integer.parseInt(record.get(2));
                float z = Integer.parseInt(record.get(3));
                float scale = Integer.parseInt(record.get(4));
                Data data = new Data(x, y, z, scale);
                System.out.printf("%f, %f, %f, %f\n", x, y, z, scale);
                //put string and PVector pair into table
                table.put(record.get(0), data);
            }
            parser.close();
            reader.close();
        } catch (Exception e){
            System.out.printf("Failed to read file: %s\n", fileName);
            String path = new File("test").getPath();
            System.out.println(path);
            e.printStackTrace();
        }
        return table;
    }

    private class Data {

        private float x, y, z, scale;

        public Data (float x, float y, float z, float scale){
            this.x = x;
            this.y = y;
            this.z = z;
            this.scale = scale;
        }

        public float getX(){
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }

        public float getScale() {
            return scale;
        }

    }

}
