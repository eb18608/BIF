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

    public final static String sensorsEquippedFileName = "sensorsEquipped.csv";
    public final static String sensorsUnlockedFileName = "sensorsUnlocked.csv";

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
                                    record.get(7),
                                    record.get(8));
                    if (record.get(0).equals("Camera") || record.get(0).equals("IMU (gyroscopes and accelerometers)")){
                        data.setEquipped(true);
                        data.setUnlocked(true);
                    }
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

    public static ArrayList<SensorContent.SensorItem> readSensorStatusFile(Context context, String fileName){
        ArrayList<SensorContent.SensorItem> list = new ArrayList<>();
        try {
            File f = new File(context.getFilesDir(), fileName);
            f.createNewFile();
            FileInputStream inputStream = new FileInputStream(f);
            InputStreamReader reader = null;
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord record : parser){
                boolean status = false;
                if (record.get(1).equals("true")){
                    status = true;
                }
                if (record.get(0).equals("Camera") || record.get(0).equals("IMU (gyroscopes and accelerometers)")){
                    SensorContent.ITEM_MAP.get(record.get(0)).setEquipped(true);
                    SensorContent.ITEM_MAP.get(record.get(0)).setUnlocked(true);
                } else if (fileName.equals(sensorsEquippedFileName)){
                    SensorContent.ITEM_MAP.get(record.get(0)).setEquipped(status);
                } else if (fileName.equals(sensorsUnlockedFileName)){
                    SensorContent.ITEM_MAP.get(record.get(0)).setUnlocked(status);
                }
            }
            parser.close();
            reader.close();
        } catch (FileNotFoundException e){
            //Create new file if file not found
            System.out.println("Creating new " + fileName + " file");
            writeSensorStatusFile(context, fileName);
        } catch (Exception e){
            System.out.printf("Failed to read file: %s\n", fileName);
            e.printStackTrace();
        }
        return list;
    }

    public static void writeSensorStatusFile(Context context, String fileName){
        try {
            //Create a new file if it doesn't exist, otherwise do nothing
            File f = new File(context.getFilesDir(), fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String str = "";
            for (Map.Entry<String, SensorContent.SensorItem> entry : SensorContent.ITEM_MAP.entrySet()){
                //do stuff
                str = entry.getValue().getId() + ",";
                if (fileName.equals(sensorsEquippedFileName)){
                    str += Boolean.toString(entry.getValue().isEquipped());
                } else if (fileName.equals(sensorsUnlockedFileName)){
                    str += Boolean.toString(entry.getValue().isUnlocked());
                }
                str += "\n";
                writer.write(str);
            }
            writer.close();
        } catch (Exception e){
            System.out.printf("Failed to write to file: %s\n", fileName);
            e.printStackTrace();
        }
    }

}
