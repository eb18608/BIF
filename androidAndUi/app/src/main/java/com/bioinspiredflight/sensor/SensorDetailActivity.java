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

package com.bioinspiredflight.sensor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import com.bioinspiredflight.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.preference.AndroidResources;

import android.view.MenuItem;
import android.widget.ImageView;

import java.lang.reflect.Field;

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

        SensorContent.SensorItem item = SensorContent.ITEM_MAP.get(id);
        Resources r = getResources();
        int res = r.getIdentifier(item.getImageFileName(), "drawable", getPackageName());
        ImageView image = findViewById(R.id.header);
        image.setImageResource(res);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!item.getId().equals("Camera") && !item.getId().equals("IMU (gyroscopes and accelerometers)")){
            updateFabIcon(fab, item.isUnlocked(), item.isEquipped());

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                    updateFabIcon(fab, item.isUnlocked(), item.isEquipped());
                }
            });
        } else {
            fab.hide();
        }


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

    private void updateFabIcon(FloatingActionButton fab, boolean unlocked, boolean equipped){
        if (unlocked){
            if (equipped){
                fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            } else {
                fab.setImageResource(android.R.drawable.ic_menu_add);
            }
        } else {
            fab.setImageResource(android.R.drawable.ic_lock_idle_lock);
        }
        fab.invalidate();
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
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        navigateUpTo(new Intent(this, SensorListActivity.class));
        super.onBackPressed();
    }
}
