package com.bioinspiredflight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bioinspiredflight.utilities.FileHandler;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AchievementsActivity extends AppCompatActivity {

    private final String achievementsFileName = "achievements.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
        TextView text1, text2, spacing;
        TableRow currentRow;
        final int padding = 18;
        int rowNum = 0;


        TableRow.LayoutParams statusRowParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        statusRowParams.weight = 1.0f;
        statusRowParams.gravity = Gravity.RIGHT;


        for (Map.Entry<String, String> entry : achievementsTable.entrySet()){
            System.out.println(entry.toString());
            string1 = entry.getKey();
            string2 = entry.getValue();
            text1 = new TextView(this);
            text1.setText(string1);
            text1.setGravity(Gravity.LEFT);
            text2 = new TextView(this);
            text2.setText(string2);
            text2.setLayoutParams(statusRowParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                text2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }
            currentRow = new TableRow(this);
            currentRow.addView(text1);
            currentRow.addView(text2);
            currentRow.setPadding(padding, padding, padding, padding);
            if (rowNum != 0){
                currentRow.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                currentRow.setBackgroundColor(Color.parseColor("#eeeeff"));
            }


            tableLayout.addView(currentRow);
            rowNum++;
        }
    }

    private TreeMap<String, String> getAchievementsTable(){
        boolean fileAlreadyExists =
                FileHandler.checkIfFileExists(getApplicationContext(), achievementsFileName);
        TreeMap<String, String> achievementsTable;
        achievementsTable =
                FileHandler.readFile(getApplicationContext(), achievementsFileName);
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
