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

package com.bioinspiredflight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.bioinspiredflight.sensor.SensorContent;
import com.bioinspiredflight.sensor.SensorListActivity;
import com.bioinspiredflight.utilities.AchievementsFileHandler;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

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
        Button aboutButton = findViewById(R.id.aboutButton);
        final Context context = this;
        aboutButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("aboutButton", "Pressed!");
                WebView view = (WebView) LayoutInflater.from(context).inflate(R.layout.dialog_licenses, null);
                view.loadUrl("file:///android_asset/licenses.html");
                AlertDialog mAlertDialog = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
                        .setTitle(getString(R.string.title_licenses))
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
        boolean fileAlreadyExists =
                AchievementsFileHandler.checkIfFileExists(getApplicationContext(), achievementsFileName);
        if (!fileAlreadyExists){
            //Toast.makeText(this, "Creating new achievements file", Toast.LENGTH_SHORT).show();
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
