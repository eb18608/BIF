package com.bioinspiredflight.utilities;

import android.content.Context;
import android.os.Build;

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
import java.util.Optional;
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

    public static TreeMap<String, String> readFile(Context context, String fileName){
        System.out.println("Reading file...");
        TreeMap<String,String> table = new TreeMap<>();
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            InputStreamReader reader = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            }
            CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);
            for (CSVRecord record : parser){
                //enter data from record into hashtable
                System.out.printf("%s, %s\n", record.get(0), record.get(1));
                table.put(record.get(0), record.get(1));
            }
            parser.close();
            reader.close();
        } catch (Exception e){
            System.out.printf("Failed to read file: %s\n", fileName);
            e.printStackTrace();
        }
        return table;
    }

    public static void writeFile(Context context, String fileName,
                                 TreeMap<String, String> table,
                                 String columnName1, String columnName2){
        System.out.println("Writing file...");
        try {
            //Create a new file if it doesn't exist, otherwise do nothing
            File f = new File(context.getFilesDir(), fileName);
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(columnName1 + "," + columnName2 + "\n");
            for (Map.Entry<String, String> entry : table.entrySet()){
                //do stuff
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            writer.close();
        } catch (Exception e){
            System.out.printf("Failed to write to file: %s\n", fileName);
            e.printStackTrace();
        }
    }

    public static boolean checkIfFileExists(Context context, String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return file.isFile();
    }

}
