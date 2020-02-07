package com.bioinspiredflight.utilities;

import android.content.Context;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public abstract class FileHandler {

    public static void createTestFile(Context context){
        File testFile = new File(context.getFilesDir(), "testing.txt");
        try {
            System.out.println(testFile.createNewFile());
            FileWriter writer = new FileWriter(testFile);
            writer.write("In the first age, in the first battle, when the shadows first lengthened,\nOne stood.\n");
            writer.close();
            FileInputStream inputStream = context.openFileInput("testing.txt");
            InputStreamReader inputStreamReader = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            }
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                builder.append(line).append('\n');
                line = reader.readLine();
            }
            reader.close();
            String contents = builder.toString();
            System.out.printf(contents);
        } catch (FileNotFoundException e){
            System.out.println("Dude wtf");
        } catch (IOException e){
            System.out.println("Seriously wtf");
        }
    }

    public TreeMap<String, String> readFile(Context context, String file){
        System.out.println("Reading file...");
        TreeMap<String,String> schedule = new TreeMap<>();
        try {
            FileInputStream inputStream = context.openFileInput(file);
            InputStreamReader reader = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            }
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);
            for (CSVRecord record : parser){
                //enter data from record into hashtable,
                //converting the first field into a Date object
            }
            parser.close();
            reader.close();
        } catch (Exception e){

        }
        return schedule;
    }

    public void writeFile(Context context, String file, TreeMap<String, String> schedule){
        System.out.println("Writing file...");
        try {
            //Create a new file if it doesn't exist, otherwise do nothing
            File f = new File(context.getFilesDir(), file);
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write("Time,Activity\n");
            for (Map.Entry<String, String> entry : schedule.entrySet()){
                //do stuff
            }
            writer.close();
        } catch (Exception e){

        }
    }

}
