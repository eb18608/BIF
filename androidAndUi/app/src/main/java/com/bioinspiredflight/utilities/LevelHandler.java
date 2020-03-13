package com.bioinspiredflight.utilities;

import android.app.Activity;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.gameobjects.LoopObject;
import com.bioinspiredflight.gameobjects.ObjectiveObject;
import com.bioinspiredflight.gameobjects.HelipadObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class LevelHandler {

    private Activity activity;
    //private ArrayList<> objectivesAccomplished; ObjectiveObjects list here

    public LevelHandler(Activity activity){
        this.activity = activity;
        //this.objectivesAccomplished = new ArrayList<>();
    }

    public void changeLevel(GameSketch sketch, GameObjectList gameObjects, String fileName){
        gameObjects.clear();
        TreeMap<String, Data> table = readLevelFile(fileName);
        GameObject gameObject;
        // populate gameObjects from the file of that file name
        for (Map.Entry<String, Data> entry : table.entrySet()) {
            //entity stuff here
            gameObject = createGameObject(sketch, entry);
            gameObjects.add(gameObject);
            if (gameObject instanceof ObjectiveObject) {
                gameObjects.addToObjectiveList((ObjectiveObject)gameObject);
            }
        }
    }

    private GameObject createGameObject(GameSketch sketch, Map.Entry<String, Data> entry){
        //Optional<GameObject> gameObject = Optional.empty();
        GameObject gameObject = null;
        Data data = entry.getValue();
        String key = entry.getKey();
        int id;
        if (key.startsWith("drone")){
            id = Integer.parseInt(key.replace("drone", ""));
            gameObject =
                    new DroneObject(sketch, sketch.getDroneBodyShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("building")){
            id = Integer.parseInt(key.replace("building", ""));
            gameObject =
                    new BuildingObject(sketch, sketch.getBuildingShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("objective")){
            id = Integer.parseInt(key.replace("objective", ""));
            gameObject =
                    new ObjectiveObject(sketch, sketch.getObjectiveShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("loop")){
            id = Integer.parseInt(key.replace("loop", ""));
            gameObject =
                    new LoopObject(sketch, sketch.getOuterLoopShape(), sketch.getInnerLoopShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("helipad")){
            id = Integer.parseInt(key.replace("helipad", ""));
            gameObject =
                    new HelipadObject(sketch, sketch.getHelipadShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
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
                System.out.println(record.toString());
                if (record.size() >= 5){
                    float x = Float.parseFloat(record.get(1));
                    float y = Integer.parseInt(record.get(2));
                    float z = Integer.parseInt(record.get(3));
                    float scale = Integer.parseInt(record.get(4));
                    Data data = new Data(x, y, z, scale);
               // System.out.printf("%f, %f, %f, %f, %f\n", x, y, z, scale);
                    //put string and PVector pair into table
                    table.put(record.get(0), data);
                }
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
