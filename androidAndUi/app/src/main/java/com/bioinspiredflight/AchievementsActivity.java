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
import android.widget.TextView;
import android.widget.Toast;

import com.bioinspiredflight.utilities.FileHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AchievementsActivity extends AppCompatActivity {

    private ArrayList<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_achievements);
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        textViews = new ArrayList<>();
        TextView txt1 = new TextView(this);
        TextView txt2 = new TextView(this);
        txt1.setText("Lorem");
        txt2.setText("Ipsum");
        textViews.add(txt1);
        textViews.add(txt2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        addContentView(layout, params);
        displayAchievements(layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeButtonEnabled(true);
        }
        FileHandler.createTestFile(getApplicationContext());
    }

    private void displayAchievements(LinearLayout layout){
        for (TextView text : textViews){
            layout.addView(text);
        }
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
