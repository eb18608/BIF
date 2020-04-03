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
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SensorFileHandler {

    private static String sensorsEquippedFileName = "sensorsEquipped.bin";
    private static String sensorsUnlockedFileName = "sensorsUnlocked.bin";

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
                                    Double.parseDouble(record.get(3)),
                                    Double.parseDouble(record.get(4)),
                                    Double.parseDouble(record.get(5)),
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
                    data = data * 2;    //equivalent to << 1 but we wanna make sure it works the way we intend it to and sets the LSB to 0
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
        } catch (FileNotFoundException e){
            //Create new file if file not found
            System.out.println("Creating new " + fileName + " file");
            populateTable(table);
            writeSensorStatusFile(context, fileName, table);
        } catch (EOFException e){
            new File(context.getFilesDir(), fileName).delete();
            populateTable(table);
            writeSensorStatusFile(context, fileName, table);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void populateTable(TreeMap<Sensor, Boolean> table){
        Sensor[] sensors = Sensor.values();
        for (Sensor sensor : sensors){
            table.put(sensor, false);
        }
    }

    public static void getSensorsEquipped(Context context, TreeMap<Sensor, Boolean> table){
        readSensorStatusFile(context, sensorsEquippedFileName, table);
    }

    public static void getSensorsUnlocked(Context context, TreeMap<Sensor, Boolean> table){
        readSensorStatusFile(context, sensorsUnlockedFileName, table);
    }

    public static void setSensorsEquipped(Context context, TreeMap<Sensor, Boolean> table){
        writeSensorStatusFile(context, sensorsEquippedFileName, table);
    }

    public static void setSensorsUnlocked(Context context, TreeMap<Sensor, Boolean> table){
        writeSensorStatusFile(context, sensorsUnlockedFileName, table);
    }

}
