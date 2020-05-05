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

public abstract class AchievementsFileHandler {

    public static void createTestFile(Context context){
        File testFile = new File(context.getFilesDir(), "testing.txt");
        try {
            System.out.println(testFile.createNewFile());
            FileWriter writer = new FileWriter(testFile);
            writer.write("In the first age, in the first battle, when the shadows first lengthened,\nOne stood.\n");
            writer.close();
            FileInputStream inputStream = context.openFileInput("testing.txt");
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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
            reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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
                                 TreeMap<String, String> table){
        System.out.println("Writing file...");
        try {
            //Create a new file if it doesn't exist, otherwise do nothing
            File f = new File(context.getFilesDir(), fileName);
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String str;
            //writer.write(str);
            //System.out.printf("Size of entry set: %d\n", table.entrySet().size());
            for (Map.Entry<String, String> entry : table.entrySet()){
                //do stuff
                str = entry.getKey() + "," + entry.getValue() + "\n";
                writer.write(str);
                System.out.printf("%s", str);
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
