package com.bioinspiredflight.sensor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.bioinspiredflight.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * An activity representing a list of Sensors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SensorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SensorListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public static TreeMap<Sensor, Boolean> sensorsEquipped = new TreeMap<>();
    public static TreeMap<Sensor, Boolean> sensorsUnlocked = new TreeMap<>();

    public void updateSensors(){
        for (SensorContent.SensorItem item : SensorContent.ITEM_MAP.values()){
            sensorsUnlocked.put(item.getSensor(), item.isUnlocked());
            sensorsEquipped.put(item.getSensor(), item.isEquipped());
        }
        SensorFileHandler.setSensorsEquipped(getApplicationContext(), sensorsEquipped);
        SensorFileHandler.setSensorsUnlocked(getApplicationContext(), sensorsUnlocked);
    }

    public static void getSensorData(){
        // Inefficient as heck but it's probably fine, will fix later if not
        for (SensorContent.SensorItem item : SensorContent.ITEM_MAP.values()){
            for (Map.Entry<Sensor, Boolean> entry : sensorsEquipped.entrySet()){
                item.setEquipped(entry.getValue());
                item.setUnlocked(sensorsUnlocked.get(item.getSensor()));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        SensorFileHandler.getSensorsEquipped(getApplicationContext(), sensorsEquipped);
        SensorFileHandler.getSensorsUnlocked(getApplicationContext(), sensorsUnlocked);
        getSensorData();

        SensorContent.populateList("Sensors.csv", this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.sensor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.sensor_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, SensorContent.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final SensorListActivity mParentActivity;
        private final List<SensorContent.SensorItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSensorData();
                SensorContent.SensorItem item = (SensorContent.SensorItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(SensorDetailFragment.ARG_ITEM_ID, item.getId());
                    SensorDetailFragment fragment = new SensorDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.sensor_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, SensorDetailActivity.class);
                    intent.putExtra(SensorDetailFragment.ARG_ITEM_ID, item.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(SensorListActivity parent,
                                      List<SensorContent.SensorItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sensor_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getId());
            //holder.mContentView.setText(Boolean.toString(mValues.get(position).getStatus()));
            if (!mValues.get(position).isUnlocked()){
                holder.mContentView.setText("Locked");
            } else if (mValues.get(position).isEquipped()){
                holder.mContentView.setText("Active");
            }
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
