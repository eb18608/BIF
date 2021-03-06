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

package com.bioinspiredflight.utilities;

import android.app.Activity;

import com.bioinspiredflight.GameSketch;
import com.bioinspiredflight.gameobjects.AirStreamObject;
import com.bioinspiredflight.gameobjects.AirVentObject;
import com.bioinspiredflight.gameobjects.ApartmentsObject;
import com.bioinspiredflight.gameobjects.BuildingObject;
import com.bioinspiredflight.gameobjects.CollectibleObject;
import com.bioinspiredflight.gameobjects.CollectionPointObject;
import com.bioinspiredflight.gameobjects.DroneObject;
import com.bioinspiredflight.gameobjects.FuelObject;
import com.bioinspiredflight.gameobjects.GameObject;
import com.bioinspiredflight.gameobjects.GameObjectList;
import com.bioinspiredflight.gameobjects.LoopObject;
import com.bioinspiredflight.gameobjects.ObjectiveObject;
import com.bioinspiredflight.gameobjects.HelipadObject;
import com.bioinspiredflight.gameobjects.SearchlightObject;
import com.bioinspiredflight.gameobjects.SkyscraperObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import processing.core.PImage;

public class LevelHandler {

    private Activity activity;
    private boolean isTimed;
    private long timeLimitSeconds;
    private int floorW, floorD;
    private String floorImageFilepath;

    public LevelHandler(Activity activity){
        this.activity = activity;
    }

    public void changeLevel(GameSketch sketch, GameObjectList gameObjects, String fileName){
        isTimed = false;
        for (GameObject gameObject : gameObjects){
            gameObject.setCollisionsEnabled(false);
        }
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
        GameObject gameObject = null;
        Data data = entry.getValue();
        String key = entry.getKey();
        int id;
        if (key.startsWith("drone")){
            id = Integer.parseInt(key.replace("drone", ""));
            gameObject =
                    new DroneObject(sketch, sketch.getDroneBodyShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("building")){
            id = Integer.parseInt(key.replace("building", ""));
            gameObject =
                    new BuildingObject(sketch, sketch.getBuildingShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("skyscraper")){
            id = Integer.parseInt(key.replace("skyscraper", ""));
            gameObject =
                    new SkyscraperObject(sketch, sketch.getSkyscraperShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("apartments")){
            id = Integer.parseInt(key.replace("apartments", ""));
            gameObject =
                    new ApartmentsObject(sketch, sketch.getApartmentsShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("objective")){
            id = Integer.parseInt(key.replace("objective", ""));
            gameObject =
                    new ObjectiveObject(sketch, sketch.getObjectiveShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("loop")){
            id = Integer.parseInt(key.replace("loop", ""));
            gameObject =
                    new LoopObject(sketch, sketch.getOuterLoopShape(), sketch.getInnerLoopShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("helipad")){
            id = Integer.parseInt(key.replace("helipad", ""));
            gameObject =
                    new HelipadObject(sketch, sketch.getHelipadShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("collection")){
            id = Integer.parseInt(key.replace("collection", ""));
            gameObject =
                    new CollectionPointObject(sketch, sketch.getCollectionPointShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("collectible")){
            id = Integer.parseInt(key.replace("collectible", ""));
            gameObject =
                    new CollectibleObject(sketch, sketch.getCollectibleShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id);
        } else if (key.startsWith("airvent")){
            id = Integer.parseInt(key.replace("airvent", ""));
            gameObject =
                    new AirVentObject(sketch, sketch.getAirVentShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("airstream")){
            id = Integer.parseInt(key.replace("airstream", ""));
            gameObject =
                    new AirStreamObject(sketch, sketch.getAirStreamShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), data.getRotation(), id, data.getFinx(), data.getFinz());
        } else if (key.startsWith("fuel")){
            id = Integer.parseInt(key.replace("fuel", ""));
            gameObject =
                    new FuelObject(sketch, sketch.getFuelShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id);
        } else if (key.startsWith("searchlight")){
            id = Integer.parseInt(key.replace("searchlight", ""));
            gameObject =
                    new SearchlightObject(sketch, sketch.getSearchlightShape(), data.getX(), data.getY(), data.getZ(), data.getScale(), id, data.getFinx(), data.getFinz());
        }
        return gameObject;
    }

    public long getTimeLimitSeconds(){
        if (isTimed){
            return timeLimitSeconds;
        } else {
            return -1;
        }
    }

    private TreeMap<String, Data> readLevelFile(String fileName){
        TreeMap<String, Data> table = new TreeMap<>();
        try {
            InputStream inputStream = activity.getAssets().open("levels/" + fileName);
            InputStreamReader reader = null;
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord record : parser){
                //extract initial starting pos and scale from file
                System.out.println(record.toString());
                if (record.size() >= 7){
                    float x = Float.parseFloat(record.get(1));
                    float y = Integer.parseInt(record.get(2));
                    float z = Integer.parseInt(record.get(3));
                    float rot = Float.parseFloat(record.get(4));
                    float scale = Integer.parseInt(record.get(5));
                    int finx = Integer.parseInt(record.get(6));
                    int finz = Integer.parseInt(record.get(7));
                    Data data = new Data(x, y, z, rot, scale, finx, finz);
                    //put string and PVector pair into table
                    table.put(record.get(0), data);
                } else if (record.get(0).equals("timer")){
                    isTimed = true;
                    timeLimitSeconds = Long.parseLong(record.get(1));
                } else if (record.get(0).equals("levelspecs")) {
                    floorImageFilepath = record.get(1);
                    floorW = Integer.parseInt(record.get(2));
                    floorD = Integer.parseInt(record.get(3));
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

        private float x, y, z, scale, rot;
        private int finx, finz;

        public Data (float x, float y, float z, float rot, float scale, int finx, int finz){
            this.x = x;
            this.y = y;
            this.z = z;
            this.rot = rot;
            this.scale = scale;
            this.finx = finx;
            this.finz = finz;
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

        public float getRotation() { return rot; }

        public float getScale() { return scale; }

        public int getFinx() { return finx; }

        public int getFinz() { return finz; }

    }

    public static Optional<String[]> getLevelDirectory(Activity activity){
        try {
            String[] fileNameList = activity.getAssets().list("levels");
            return Optional.of(fileNameList);
        } catch (IOException e){
            return Optional.empty();
        }
    }

    public int getFloorW() { return floorW; }
    public int getFloorD() { return floorD; }
    public String getFloorImageFilepath() { return floorImageFilepath; }

}
