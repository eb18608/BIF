package com.bioinspiredflight.sensor;

import android.app.Activity;
import android.content.Context;

import com.bioinspiredflight.utilities.LevelHandler;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SensorFileHandler {

    public static ArrayList<SensorContent.SensorItem> readSensorFile(String fileName, Activity activity){
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

    public static void writeSensorStatusFile(Context context, String fileName,
                                 TreeMap<Sensor, Boolean> table){
        try {
            //Create a new file if it doesn't exist, otherwise do nothing
            new File(context.getFilesDir(), fileName).createNewFile();
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(fileName));
            int data = 1;
            for (Map.Entry<Sensor, Boolean> entry : table.entrySet()){
                //do stuff
                if (entry.getValue()){
                    data = (data << 1) + 1;
                } else {
                    data = data << 1;
                }
            }
            stream.writeInt(data);
            stream.close();
        } catch (Exception e){
            System.out.printf("Failed to write to file: %s\n", fileName);
            e.printStackTrace();
        }
    }

    public static void readSensorStatusFile(Context context, String fileName,
                                            TreeMap<Sensor, Boolean> table){
        try {
            File f = new File(context.getFilesDir(), fileName);
            DataInputStream stream = new DataInputStream(new FileInputStream(f));
            int data = stream.readInt();
            stream.close();
            Sensor[] sensors = Sensor.values();
            for (int i = sensors.length - 1; i >= 0; i--){
                if ((data & 1) == 1){
                    table.put(sensors[i], true);
                } else {
                    table.put(sensors[i], false);
                }
                data = data >> 1;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
