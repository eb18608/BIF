package com.bioinspiredflight.sensor;

import android.content.Intent;
import android.os.Bundle;

import com.bioinspiredflight.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;

import static com.bioinspiredflight.sensor.SensorDetailFragment.ARG_ITEM_ID;

/**
 * An activity representing a single Sensor detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SensorListActivity}.
 */
public class SensorDetailActivity extends AppCompatActivity {

    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        if (bundle.getString(ARG_ITEM_ID) != null){
            id = bundle.getString(ARG_ITEM_ID);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with unlock/equip action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
                //Sensor sensor = SensorContent.idToSensor(id);
                SensorContent.SensorItem item = SensorContent.ITEM_MAP.get(id);
                if (item.isUnlocked() == false){
                    item.setUnlocked(true);
                    Snackbar.make(view, "Sensor unlocked!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (item.isEquipped() == false){
                    item.setEquipped(true);
                    Snackbar.make(view, "Sensor equipped!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    System.out.println("b");
                } else {
                    item.setEquipped(false);
                    Snackbar.make(view, "Sensor unequipped!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    System.out.println("c");
                }
                SensorFileHandler.writeSensorStatusFile(getApplicationContext(), SensorFileHandler.sensorsUnlockedFileName);
                SensorFileHandler.writeSensorStatusFile(getApplicationContext(), SensorFileHandler.sensorsEquippedFileName);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            System.out.println("reee");
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    getIntent().getStringExtra(ARG_ITEM_ID));
            System.out.println(arguments.toString());
            SensorDetailFragment fragment = new SensorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sensor_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, SensorListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
