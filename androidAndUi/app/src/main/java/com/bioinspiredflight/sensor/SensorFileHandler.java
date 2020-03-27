package com.bioinspiredflight.sensor;

import android.app.Activity;

import com.bioinspiredflight.utilities.LevelHandler;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeMap;

public class SensorFileHandler {

    public static ArrayList<SensorContent.SensorItem> readFile(String fileName, Activity activity){
        ArrayList<SensorContent.SensorItem> list = new ArrayList<>();
        try {
            InputStream inputStream = activity.getAssets().open(fileName);
            InputStreamReader reader = null;
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord record : parser){
                try {
                    SensorContent.SensorItem data =
                            new SensorContent.SensorItem(
                                    record.get(0),
                                    record.get(1),
                                    record.get(2),
                                    Integer.parseInt(record.get(3)),
                                    Integer.parseInt(record.get(4)),
                                    Integer.parseInt(record.get(5)),
                                    record.get(6),
                                    record.get(7));
                    list.add(data);
                } catch (NumberFormatException e){

                }
            }
            parser.close();
            reader.close();
        } catch (Exception e){
            System.out.printf("Failed to read file: %s\n", fileName);
            e.printStackTrace();
        }
        return list;
    }
}
