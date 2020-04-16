package com.bioinspiredflight.ui;

import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bioinspiredflight.GameActivity;
import com.bioinspiredflight.R;
import com.bioinspiredflight.utilities.LevelHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

import processing.android.CompatUtils;

public class Ui {

    private PauseMenu pauseMenu;
    private ArrayList<View> widgets;
    private GameActivity.GameSketchObserver obs;
    private PlayerControls controls;
    private GameStatus gameStatus = GameStatus.IN_PROGRESS;
    //private final FrameLayout frame;
    //private final AppCompatActivity gameActivity;

    public Ui(final GameActivity gameActivity, InputToOutput io) {
        //layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity, io);
    }

    public Ui(final GameActivity gameActivity, InputToOutput io, boolean sliderToggle) {
        //layout = new TableLayout(gameActivity);
        setupWidgets(gameActivity, io);

    }

    private void setupWidgets(final GameActivity gameActivity, InputToOutput io){
        DisplayMetrics metrics = new DisplayMetrics();
        gameActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widgets = new ArrayList<>();

        pauseMenu = new PauseMenu(gameActivity, widgets, metrics);
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

    public void revealMenu(){
        pauseMenu.revealMenu();

    }

    public void hideMenu(){
        pauseMenu.hideMenu(true);

    }

    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
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

    }

    public class PauseMenu {
        private TextView levelCompleteText;
        private Button returnButton;
        private Button newLevelButton;
        private FloatingActionButton pauseButton;
        private ArrayList<Button> levelSelectButtonList;

        public PauseMenu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            setupMenu(gameActivity, widgets, metrics);
            setupLevelSelect(gameActivity, widgets, metrics);
            setupPauseButton(gameActivity, widgets, metrics);
        }

        private void setupMenu(final GameActivity gameActivity, ArrayList<View> widgets, DisplayMetrics metrics){
            final int sideMargin = metrics.widthPixels / 4;
            final int topMargin = 3 * (metrics.heightPixels / 10);
            final int bottomMargin = 3 * (metrics.heightPixels / 10);
            final int buttonHeight = metrics.heightPixels / 5;

            levelCompleteText = new TextView(gameActivity);
            levelCompleteText.setText("LEVEL COMPLETE");
            levelCompleteText.setTextSize(metrics.heightPixels / 25);
            levelCompleteText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            levelCompleteText.setTextColor(0xff33ff33);
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
                    Toast.makeText(gameActivity.getApplicationContext(), "Switching to level select menu",
                            Toast.LENGTH_SHORT)
                            .show();
                    hideMenu(false);
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
            returnButton.setText("Exit");
            returnButton.setId(CompatUtils.getUniqueViewId());
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(gameActivity.getApplicationContext(), "Exiting",
                            Toast.LENGTH_SHORT)
                            .show();
                    gameActivity.finish();
                    //obs.updateGameSketch();

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
                    Toast.makeText(gameActivity, "Pause button pressed!", pauseButton.getId()).show();
                    boolean paused = obs.togglePauseSketch();
                    if (paused){
                        revealMenu();
                    } else {

                        hideMenu(true);
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
                    //levelButton.setHeight(metrics.widthPixels / 20);
                    levelButton.setText(item.replace(".csv", ""));
                    levelButton.setId(CompatUtils.getUniqueViewId());
                    levelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(gameActivity, "Switching to " + item.replace(".csv", ""), levelButton.getId()).show();
                            hideLevelSelect();
                            obs.updateGameSketch(item);
                        }
                    });
                    levelButton.setEnabled(true);
                    //levelSelectLayout.addView(levelButton);
                    levelButton.setVisibility(View.GONE);
                    levelSelectButtonList.add(levelButton);
                    widgets.add(levelButton);
                    numOfCols++;
                    if (numOfCols > 5){
                        numOfRows++;
                        numOfCols = 0;
                    }
                }
                final Button backButton = new Button(gameActivity);
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin =  sideMargin + (numOfCols * (buttonWidth + gap) * 2);
                params.height = buttonHeight;
                params.topMargin = topMargin + (numOfRows * buttonHeight);
                backButton.setLayoutParams(params);
                //levelButton.setHeight(metrics.widthPixels / 20);
                backButton.setText("Back");
                backButton.setTextColor(0xffff0000);
                backButton.setId(CompatUtils.getUniqueViewId());
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(gameActivity, "Switching to pause menu", backButton.getId()).show();
                        hideLevelSelect();
                        revealMenu();
                        //pauseButton.setImageResource(android.R.drawable.ic_media_pause);
                        //pauseButton.invalidate();
                    }
                });
                backButton.setEnabled(true);
                //levelSelectLayout.addView(levelButton);
                backButton.setVisibility(View.GONE);
                levelSelectButtonList.add(backButton);
                widgets.add(backButton);
                //levelSelectLayout.setEnabled(true);
                //widgets.add(levelSelectLayout);
            }
        }

        private void showLevelSelect(){
            for (Button button : levelSelectButtonList){
                button.setVisibility(View.VISIBLE);
            }
            invalidateMenu();
        }

        private void hideLevelSelect(){
            for (Button button : levelSelectButtonList){
                button.setVisibility(View.GONE);
            }
            invalidateMenu();
        }

        public void revealMenu(){
            if (gameStatus == GameStatus.COMPLETED){
                //pauseButton.hide();
                pauseButton.setEnabled(false);
                pauseButton.setImageResource(android.R.drawable.btn_radio);
                levelCompleteText.setVisibility(View.VISIBLE);
            } else {
                pauseButton.setImageResource(android.R.drawable.ic_media_play);
            }
            newLevelButton.setVisibility(View.VISIBLE);
            returnButton.setVisibility(View.VISIBLE);
            invalidateMenu();
        }

        public void hideMenu(boolean playing){
            if (playing){
                pauseButton.setImageResource(android.R.drawable.ic_media_pause);
                hideLevelSelect();
            }
            //pauseButton.show();
            pauseButton.setEnabled(true);
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
        IN_PROGRESS,
        COMPLETED,
        GAME_OVER
    }

}
