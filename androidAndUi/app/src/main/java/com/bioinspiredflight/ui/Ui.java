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

package com.bioinspiredflight.ui;

import android.app.Activity;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.utilities.LevelHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Optional;

import processing.android.CompatUtils;

public class Ui {

    private Menu menu;
    private ArrayList<View> widgets;
    private GameActivity.GameSketchObserver obs;
    private PlayerControls controls;
    private GameStatus gameStatus = GameStatus.STARTUP;

    public Ui(final GameActivity gameActivity, InputToOutput io) {
        setupWidgets(gameActivity, io);
    }

    public Ui(final GameActivity gameActivity, InputToOutput io, boolean sliderToggle) {
        setupWidgets(gameActivity, io);

    }

    private void setupWidgets(final GameActivity gameActivity, InputToOutput io){
        DisplayMetrics metrics = new DisplayMetrics();
        gameActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widgets = new ArrayList<>();

        menu = new Menu(gameActivity, widgets, metrics);
        controls = new PlayerControls(gameActivity, widgets, io);
    }

    public void drawUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.addView(widget);
            System.out.printf("Added %s\n", widget.toString());
        }
    }

    public void removeUi(RelativeLayout frame){
        for (View widget : widgets){
            frame.removeView(widget);
            System.out.printf("Removed %s\n", widget.toString());
        }
    }

    public void startTimer(long seconds){
        menu.startTimer(seconds);
    }

    public void updateTimer(){
        menu.updateTimer();
    }

    public void revealMenu(){
        menu.show();
        controls.hidePlayerControls();
    }

    public void hideMenu(){
        menu.hide();
        controls.showPlayerControls();
    }

    public void showLevelSelect(boolean initial, Activity gameActivity){
        controls.hidePlayerControls();
        menu.showLevelSelect(initial, gameActivity);
    }

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    public GameStatus getGameStatus(){
        return gameStatus;
    }

    public void setObs(GameActivity.GameSketchObserver obs) {
        this.obs = obs;
    }

    public class PlayerControls {
        private Joystick testJoystick;
        private Slider testSlider;
        private InputToOutput io;
        private UiSurfaceView uiSurfaceView;

        public PlayerControls(GameActivity gameActivity, ArrayList<View> widgets, InputToOutput io){
            testJoystick = new Joystick(gameActivity);
            testJoystick.setId(CompatUtils.getUniqueViewId());
            testJoystick.addJoystickListener(new InputToOutput());
            widgets.add(testJoystick);
            testSlider = new Slider(gameActivity);
            testSlider.setId(CompatUtils.getUniqueViewId());
            widgets.add(testSlider);
            this.io = io;
            uiSurfaceView = new UiSurfaceView(gameActivity, testJoystick, testSlider, io);
            widgets.add(uiSurfaceView);
            this.io.setView(uiSurfaceView);
        }

        public void hidePlayerControls(){
            uiSurfaceView.setVisibility(View.GONE);
        }

        public void showPlayerControls(){
            uiSurfaceView.setVisibility(View.VISIBLE);
        }

    }

    public class Menu {
        private TextView levelCompleteText;
        private Button returnButton;
        private Button newLevelButton;
        private FloatingActionButton pauseButton;
        private ArrayList<Button> levelSelectButtonList;
        private Button levelBackButton;
        private TextView timerText;
        private long startTime = 0;
        private long endTime = 0;
        private long pauseTime;
        private boolean timing;


        public Menu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            setupTimer(gameActivity, widgets, metrics);
            setupMenu(gameActivity, widgets, metrics);
            setupLevelSelect(gameActivity, widgets, metrics);
            setupPauseButton(gameActivity, widgets, metrics);
        }

        private void setupTimer(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            final int sideMargin = metrics.widthPixels / 4;
            final int topMargin = metrics.heightPixels / 20;
            final int bottomMargin = 3 * (metrics.heightPixels / 10);
            final int buttonHeight = metrics.heightPixels / 5;
            timerText = new TextView(gameActivity);
            timerText.setTextSize(metrics.heightPixels / 50);
            timerText.setTextColor(Color.BLACK);
            timerText.setShadowLayer(2, 0, 0, Color.WHITE);
            timerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            RelativeLayout.LayoutParams timerParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            timerParams.topMargin = topMargin;
            timerText.setLayoutParams(timerParams);
            widgets.add(timerText);
        }

        /**
         * Start a timer that ends after some number of seconds
         * @param seconds
         */
        private void startTimer(long seconds){
            startTime = System.currentTimeMillis();
            endTime = startTime + (seconds * 1000);
            timing = true;
            timerText.setVisibility(View.VISIBLE);
        }

        private void pauseTimer(){
            pauseTime = System.currentTimeMillis();
        }

        private void resumeTimer(){
            endTime = endTime + (System.currentTimeMillis() - pauseTime);
        }

        private void resetTimer(){
            startTime = 0;
            endTime = 0;
            timing = false;
            timerText.setVisibility(View.GONE);
            timerText.invalidate();
        }

        private void updateTimer(){
            if (timing) {
                long timeLeft = endTime - System.currentTimeMillis();
                long seconds;
                long minutes;
                String secondString;
                String minuteString;
                if (timeLeft <= 0) {
                    seconds = 0;
                    minutes = 0;
                    obs.updateGameOver();
                } else {
                    seconds = timeLeft / 1000;
                    minutes = seconds / 60;
                    seconds %= 60;
                }
                secondString = Long.toString(seconds);
                minuteString = Long.toString(minutes);
                if (seconds < 10) {
                    secondString = "0" + secondString;
                }
                if (minutes < 10) {
                    minuteString = "0" + minuteString;
                }
                if (seconds < 30 && minutes == 0){
                    timerText.setTextColor(Color.RED);
                }
                if (seconds < 10 && minutes == 0){
                    timerText.setShadowLayer(20, 0, 0, 0xFFFF3333);
                }
                timerText.setText(minuteString + ":" + secondString);
                timerText.invalidate();
            }
        }

        private void setupMenu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            final int sideMargin = metrics.widthPixels / 4;
            final int topMargin = 3 * (metrics.heightPixels / 10);
            final int bottomMargin = 3 * (metrics.heightPixels / 10);
            final int buttonHeight = metrics.heightPixels / 5;

            levelCompleteText = new TextView(gameActivity);
            levelCompleteText.setTextSize(metrics.heightPixels / 25);
            levelCompleteText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            RelativeLayout.LayoutParams textParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            textParams.leftMargin = sideMargin;
            textParams.rightMargin = sideMargin;
            textParams.topMargin = topMargin - buttonHeight;
            textParams.height = buttonHeight;
            levelCompleteText.setLayoutParams(textParams);

            newLevelButton = new Button(gameActivity);
            RelativeLayout.LayoutParams newLevelLayoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);

            newLevelLayoutParams.leftMargin = sideMargin;
            newLevelLayoutParams.rightMargin = sideMargin;
            newLevelLayoutParams.topMargin = topMargin;
            newLevelLayoutParams.height = buttonHeight;

            newLevelButton.setLayoutParams(newLevelLayoutParams);
            newLevelButton.setText("Change Level");
            newLevelButton.setId(CompatUtils.getUniqueViewId());
            newLevelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    Toast.makeText(gameActivity.getApplicationContext(), "Switching to level select menu",
                            Toast.LENGTH_SHORT)
                            .show();*/
                    hide();
                    showLevelSelect();
                }
            });

            returnButton = new Button(gameActivity);
            RelativeLayout.LayoutParams returnLayoutParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            returnLayoutParams.leftMargin = sideMargin;
            returnLayoutParams.rightMargin = sideMargin;
            returnLayoutParams.topMargin = topMargin + buttonHeight;
            returnLayoutParams.height = buttonHeight;
            returnButton.setLayoutParams(returnLayoutParams);
            returnButton.setTextColor(Color.RED);
            returnButton.setText("Exit");
            returnButton.setId(CompatUtils.getUniqueViewId());
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameActivity.finish();
                }
            });
            widgets.add(levelCompleteText);
            widgets.add(newLevelButton);
            widgets.add(returnButton);
            returnButton.setVisibility(View.GONE);
            newLevelButton.setVisibility(View.GONE);
            newLevelButton.setEnabled(true);
            returnButton.setEnabled(true);
        }

        private void setupPauseButton(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            pauseButton = new FloatingActionButton(gameActivity);
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean paused = obs.togglePauseSketch();
                    if (gameStatus == GameStatus.IN_PROGRESS){
                        if (paused){
                            pauseButton.setImageResource(android.R.drawable.ic_media_play);
                            pauseTimer();
                            revealMenu();
                        } else {
                            resumeTimer();
                            hideMenu();
                        }
                    }
                    pauseButton.invalidate();
                }
            });
            RelativeLayout.LayoutParams pauseButtonParams =
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            pauseButtonParams.leftMargin = metrics.widthPixels / 20;
            pauseButtonParams.topMargin = metrics.heightPixels / 20;
            pauseButton.setLayoutParams(pauseButtonParams);
            widgets.add(pauseButton);
        }

        public void setupLevelSelect(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            levelSelectButtonList = new ArrayList<>();
            final int sideMargin = metrics.widthPixels / 4;
            final int topMargin = 2 * (metrics.heightPixels / 10);
            final int bottomMargin = 3 * (metrics.heightPixels / 10);
            final int buttonWidth = metrics.widthPixels / 20;
            final int buttonHeight = metrics.heightPixels / 8;
            final int gap = metrics.widthPixels / 200;
            Optional<String[]> levelList = LevelHandler.getLevelDirectory(gameActivity);
            int numOfCols = 0;
            int numOfRows = 0;
            if (levelList.isPresent()){
                String[] list = levelList.get();
                for (final String item : list){

                    final Button levelButton = new Button(gameActivity);
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin =  sideMargin + (numOfCols * (buttonWidth + gap) * 2) ;
                    params.height = buttonHeight;
                    params.topMargin = topMargin + (numOfRows * buttonHeight);
                    levelButton.setLayoutParams(params);
                    levelButton.setText(item.replace(".csv", ""));
                    levelButton.setId(CompatUtils.getUniqueViewId());

                    levelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideLevelSelect();
                            gameStatus = GameStatus.LOADING;
                            resetTimer();
                            obs.updateGameSketch(item);
                        }
                    });

                    levelButton.setEnabled(true);
                    levelButton.setVisibility(View.GONE);
                    levelSelectButtonList.add(levelButton);
                    widgets.add(levelButton);
                    numOfCols++;
                    if (numOfCols > 5){
                        numOfRows++;
                        numOfCols = 0;
                    }
                }
                levelBackButton = new Button(gameActivity);
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin =  sideMargin + (numOfCols * (buttonWidth + gap) * 2);
                params.height = buttonHeight;
                params.topMargin = topMargin + (numOfRows * buttonHeight);
                levelBackButton.setLayoutParams(params);
                levelBackButton.setText("Back");
                levelBackButton.setTextColor(0xffff0000);
                levelBackButton.setId(CompatUtils.getUniqueViewId());

                levelBackButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideLevelSelect();
                        show();
                    }
                });

                levelBackButton.setEnabled(true);
                levelBackButton.setVisibility(View.GONE);
                levelSelectButtonList.add(levelBackButton);
                widgets.add(levelBackButton);
            }
        }

        private void updateLevelCompletedText(){
            if (gameStatus == GameStatus.COMPLETED){
                levelCompleteText.setText("LEVEL COMPLETE");
                levelCompleteText.setTextColor(0xff33ee33);
                levelCompleteText.setShadowLayer(20, 0, 0, 0xff33ee33);
            } else if (gameStatus == GameStatus.GAME_OVER){
                levelCompleteText.setText("GAME OVER");
                levelCompleteText.setTextColor(0xffff3333);
                levelCompleteText.setShadowLayer(20, 0, 0, 0xffff3333);
            }
        }

        private void showLevelSelect(){
            for (Button button : levelSelectButtonList){
                button.setVisibility(View.VISIBLE);
            }
            levelBackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideLevelSelect();
                    show();
                }
            });
            invalidateMenu();
        }

        private void showLevelSelect(boolean initial, final Activity gameActivity){
            for (Button button : levelSelectButtonList){
                button.setVisibility(View.VISIBLE);
            }
            if (initial){
                returnButton.setVisibility(View.VISIBLE);
                levelBackButton.setVisibility(View.GONE);
                pauseButton.setEnabled(false);
                pauseButton.setImageResource(android.R.drawable.btn_radio);
            }
            invalidateMenu();
        }

        private void hideLevelSelect(){
            for (Button button : levelSelectButtonList){
                button.setVisibility(View.GONE);
            }
            invalidateMenu();
        }

        public void show(){
            if (gameStatus == GameStatus.COMPLETED || gameStatus == GameStatus.GAME_OVER){
                pauseButton.setEnabled(false);
                pauseButton.setImageResource(android.R.drawable.btn_radio);
                updateLevelCompletedText();
                levelCompleteText.setVisibility(View.VISIBLE);
            }
            newLevelButton.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.VISIBLE);
            invalidateMenu();
        }

        public void hide(){
            if (gameStatus == GameStatus.IN_PROGRESS){
                hideLevelSelect();
                pauseButton.setEnabled(true);
                pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
            levelCompleteText.setVisibility(View.GONE);
            newLevelButton.setVisibility(View.GONE);
            returnButton.setVisibility(View.GONE);
            invalidateMenu();

        }

        // Makes sure everything in the menu is updated visually
        private void invalidateMenu(){
            levelCompleteText.invalidate();
            pauseButton.invalidate();
            newLevelButton.invalidate();
            returnButton.invalidate();
            for (Button button : levelSelectButtonList){
                button.invalidate();
            }
        }
    }

    public enum GameStatus{
        STARTUP,
        LOADING,
        IN_PROGRESS,
        COMPLETED,
        GAME_OVER
    }

}
