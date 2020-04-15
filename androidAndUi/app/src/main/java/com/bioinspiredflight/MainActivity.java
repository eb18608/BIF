package com.bioinspiredflight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.bioinspiredflight.sensor.SensorContent;
import com.bioinspiredflight.sensor.SensorListActivity;
import com.bioinspiredflight.utilities.AchievementsFileHandler;

import java.io.File;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private final String achievementsFileName = "achievements.csv";

    public void init(){
        //Play button will switch to game activity
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("playButton", "Pressed!");
                Intent playGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(playGame);
            }
        });
        Button achievementsButton = findViewById(R.id.achievementsButton);
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("achievementsButton", "Pressed!");
                Intent achievements = new Intent(MainActivity.this, AchievementsActivity.class);
                startActivity(achievements);
            }
        });
        Button customizationButton = findViewById(R.id.customizationButton);
        customizationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("settingsButton", "Pressed!");
                Intent customization = new Intent(MainActivity.this, SensorListActivity.class);
                startActivity(customization);
            }
        });
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("settingsButton", "Pressed!");
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
            }
        });
        boolean fileAlreadyExists =
                AchievementsFileHandler.checkIfFileExists(getApplicationContext(), achievementsFileName);
        if (!fileAlreadyExists){
            Toast.makeText(this, "Creating new achievements file", Toast.LENGTH_SHORT).show();
            TreeMap<String, String> achievementsTable = createNewAchievementsTable();
            AchievementsFileHandler.writeFile(
                    getApplicationContext(),
                    achievementsFileName,
                    achievementsTable);
        }
    }

    private TreeMap<String, String> createNewAchievementsTable(){
        new File(getApplicationContext().getFilesDir(), achievementsFileName).delete();
        TreeMap<String, String> achievementsTable = new TreeMap<>();
        achievementsTable.put("Achievements", "Status");
        achievementsTable.put("Lorem ipsum", "...");
        achievementsTable.put("Say Coloradooooo", "I'M A GIRAFFE");
        achievementsTable.put(
                "They are rage. Brutal and without mercy. But you...you will be worse.",
                "Rip and tear until it is DONE.");
        System.out.printf("Size: %d\n", achievementsTable.entrySet().size());
        return achievementsTable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorContent.populateList("Sensors.csv", this);
        SensorListActivity.updateSensorContent(this);
        setContentView(R.layout.activity_main);

        init();

    }

}
