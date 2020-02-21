package com.bioinspiredflight.utilities;

import com.bioinspiredflight.gameobjects.GameObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import processing.core.PVector;

public abstract class LevelHandler {

    public static void changeLevel(ArrayList<GameObject> gameObjects, String fileName){
        gameObjects.clear();
        TreeMap<String, PVector> table = readLevelFile(fileName);
        // populate gameObjects from the file of that file name
        for (Map.Entry<String, PVector> entry : table.entrySet()){
            //entity stuff here
        }

    }

    private static TreeMap<String, PVector> readLevelFile(String fileName){
        TreeMap<String, PVector> table = new TreeMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            InputStreamReader reader = null;
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord record : parser){
                //extract initial PVector from file
                int x = Integer.parseInt(record.get(1));
                int y = Integer.parseInt(record.get(2));
                int z = Integer.parseInt(record.get(3));
                PVector vector = new PVector(x, y, z);
                //put string and PVector pair into table
                table.put(record.get(0), vector);
            }
            parser.close();
            reader.close();
        } catch (Exception e){
            System.out.printf("Failed to read file: %s\n", fileName);
            e.printStackTrace();
        }
        return table;
    }

}
