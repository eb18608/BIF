package com.bioinspiredflight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bioinspiredflight.utilities.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AchievementsActivity extends AppCompatActivity {

    private final String achievementsFileName = "achievements.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        TableLayout tableLayout = findViewById(R.id.achievementsTable);
        TreeMap<String, String> achievementsTable = getAchievementsTable();
        displayAchievements(tableLayout, achievementsTable);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeButtonEnabled(true);
        }
        //FileHandler.createTestFile(getApplicationContext());
    }

    private void displayAchievements(TableLayout tableLayout,
                                     TreeMap<String, String> achievementsTable){
        String string1, string2;
        TextView text1, text2;
        TableRow currentRow;
        System.out.println(tableLayout.getChildCount());
        int rowNum = 0;
        for (Map.Entry<String, String> entry : achievementsTable.entrySet()){
            string1 = entry.getKey();
            string2 = entry.getValue();
            text1 = new TextView(getApplicationContext());
            text1.setText(string1);
            text2 = new TextView(getApplicationContext());
            text2.setText(string2);
            currentRow = (TableRow) tableLayout.getChildAt(rowNum);
            currentRow.addView(text1);
            currentRow.addView(text2);

            rowNum++;
        }
    }

    private TreeMap<String, String> getAchievementsTable(){
        new File(getApplicationContext().getFilesDir(), achievementsFileName).delete();
        boolean fileAlreadyExists =
                FileHandler.checkIfFileExists(getApplicationContext(), achievementsFileName);
        TreeMap<String, String> achievementsTable;
        if (fileAlreadyExists){
            Toast.makeText(this, "Achievements exists", Toast.LENGTH_SHORT).show();
            achievementsTable =
                    FileHandler.readFile(getApplicationContext(), achievementsFileName);
        } else {
            Toast.makeText(this, "Creating new achievements file", Toast.LENGTH_SHORT).show();
            achievementsTable = createNewAchievementsTable();
            FileHandler.writeFile(
                    getApplicationContext(),
                    achievementsFileName,
                    new TreeMap<String, String>(),
                    "Achievement",
                    "Status");
        }
        return achievementsTable;
    }

    private TreeMap<String, String> createNewAchievementsTable(){
        TreeMap<String, String> achievementsTable = new TreeMap<>();
        achievementsTable.put("Lorem ipsum", "...");
        achievementsTable.put("Say Coloradooooo", "I'M A GIRAFFE");
        achievementsTable.put(
                "They are rage, brutal and without mercy. But you...you will be worse.",
                "Rip and tear, until it is DONE.");
        return achievementsTable;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String[] fileList = getApplicationContext().fileList();
        System.out.println(fileList.toString());
        for (String fileName : fileList){
            System.out.println(fileName);
        }
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }
}
