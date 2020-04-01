package com.bioinspiredflight.sensor;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SensorContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<SensorItem> ITEMS = new ArrayList<SensorItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SensorItem> ITEM_MAP = new HashMap<String, SensorItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        /*
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/

        //System.out.println(ITEM_MAP.toString());
    }

    public static void populateList(String fileName, Activity activity){
        ArrayList<SensorItem> list = SensorFileHandler.readSensorFile(fileName, activity);
        for (SensorItem item : list){
            addItem(item);
        }
    }

    private static void addItem(SensorItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    /*
    private static SensorItem createDummyItem(int position) {
        return new SensorItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SensorItem {
        private final String id;
        private final String purpose;
        private final String inspiration;
        private final int power, weight, price;
        private final String imageFileName;
        private final String details;

        public SensorItem(String id, String purpose, String inspiration,
                          int power, int weight, int price,
                          String imageFileName, String details) {
            this.id = id;
            this.purpose = purpose;
            this.inspiration = inspiration;
            this.power = power;
            this.weight = weight;
            this.price = price;
            this.imageFileName = imageFileName;
            this.details = details;
        }

        @Override
        public String toString() {
            return purpose;
        }

        public String getId(){
            return id;
        }

        public String getPurpose(){
            return purpose;
        }

        public String getDetails(){
            return details;
        }

        public String getInspiration() {
            return inspiration;
        }

        public int getPower() {
            return power;
        }

        public int getWeight() {
            return weight;
        }

        public int getPrice() {
            return price;
        }

        public String getImageFileName() {
            return imageFileName;
        }
    }
}
