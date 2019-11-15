package com.bioinspiredflight;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public void init(){
        //Play button will switch to game activity
        Button playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i("playButton", "Pressed!");
                /*
                Toast.makeText(getApplicationContext(), "Not implemented yet",
                        Toast.LENGTH_SHORT)
                        .show();
                 */
                Intent playGame = new Intent(MainActivity.this, GameActivity.class);
                startActivity(playGame);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

}
