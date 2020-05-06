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

import android.app.Activity;

import processing.core.PShape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 *
 */
public class SensorContent {

    /**
     * An array of sensor items.
     */
    public static final List<SensorItem> ITEMS = new ArrayList<>();

    /**
     * A map of sensor items, by ID.
     */
    public static final Map<String, SensorItem> ITEM_MAP = new HashMap<>();

    public static void populateList(String fileName, Activity activity){
        ITEMS.clear();
        ArrayList<SensorItem> list = SensorFileHandler.readSensorFile(fileName, activity);
        for (SensorItem item : list){
            addItem(item);
        }
    }

    private static void addItem(SensorItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    /**
     * An item representing a piece of content.
     */
    public static class SensorItem {
        private final String id;
        private final String purpose;
        private final String inspiration;
        private final double power, weight, price;
        private final String imageFileName;
        private final String details;
        private boolean equipped, unlocked;
        private String bodyFilePath;

        public SensorItem(String id, String purpose, String inspiration,
                          double power, double weight, double price,
                          String imageFileName, String details, String bodyfilepath) {
            this.id = id;
            this.purpose = purpose;
            this.inspiration = inspiration;
            this.power = power;
            this.weight = weight;
            this.price = price;
            this.imageFileName = imageFileName;
            this.details = details;
            this.equipped = false;
            this.unlocked = false;
            this.bodyFilePath = bodyfilepath;
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

        public double getPower() {
            return power;
        }

        public double getWeight() {
            return weight;
        }

        public double getPrice() {
            return price;
        }

        public String getImageFileName() {
            return imageFileName;
        }

        public boolean isEquipped() {
            return equipped;
        }

        public void setEquipped(boolean equipped) {
            this.equipped = equipped;
        }

        public boolean isUnlocked() {
            return unlocked;
        }

        public void setUnlocked(boolean unlocked) {
            this.unlocked = unlocked;
        }

        public String getBodyfilePath() { return bodyFilePath; }

    }
}
