package com.jgorecki.snakegame;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button newGameButton, continueButton, exitButton, snakeSpeedPlusButton, snakeSpeedMinusButton, boardSizePlusButton, boardSizeMinusButton;
    TextView snakeSpeedTextView, boardSizeTextView, lastScoreTextView, highScoreTextView;
    private boolean continueButtonVisibility;
    private int boardWidth;
    private int boardHeight;
    private int snakeSpeed;
    private String[] boardSizes;
    private int boardSizesIterator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initializeViewComponents();
        setBoardSizes();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        setContinueButtonVisibility(continueButtonVisibility);
        snakeSpeedTextView.setText(String.valueOf(this.snakeSpeed));
        boardSizeTextView.setText(this.boardWidth + "x" + this.boardHeight);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }


    @Override
    public void onClick (View v) {
        if (v.equals(newGameButton)) {
           startGame(true);
        } else if (v.equals(continueButton)) {
            startGame(false);
        } else if (v.equals(exitButton)) {
            finish();
        }  else if (v.equals(snakeSpeedPlusButton)) {
            changeSnakeSpeed(1);
        } else if (v.equals(snakeSpeedMinusButton)) {
            changeSnakeSpeed(-1);
        } else if (v.equals(boardSizePlusButton)) {
            this.boardSizesIterator++;
            changeBoardSize();
        } else if (v.equals(boardSizeMinusButton)) {
            this.boardSizesIterator--;
            changeBoardSize();
        }
    }

    private void changeBoardSize () {
        if (this.boardSizesIterator > this.boardSizes.length-1) {
            this.boardSizesIterator = this.boardSizes.length-1;
        } else if (this.boardSizesIterator < 0) {
            this.boardSizesIterator = 0;
        }
        String size = this.boardSizes[this.boardSizesIterator];
        String[] splitSize = size.split("x");
        this.boardWidth = Integer.valueOf(splitSize[0]);
        this.boardHeight = Integer.valueOf(splitSize[1]);
        boardSizeTextView.setText(size);
    }

    private void setBoardSizes () {
        this.boardSizes = new String[] {
                "15x15",
                "25x25",
                "35x35"
        };
    }


    private void changeSnakeSpeed (int difference) {
        if (this.snakeSpeed+difference > 10) {
            this.snakeSpeed = 10;
        } else if (this.snakeSpeed+difference < 1) {
            this.snakeSpeed = 1;
        } else {
            this.snakeSpeed += difference;
        }
        snakeSpeedTextView.setText(String.valueOf(this.snakeSpeed));
    }

    private void startGame (boolean ifStartNewGame) {
        Intent intent = new Intent(this, GameplayActivity.class);
        intent.putExtra("boardWidth", boardWidth);
        intent.putExtra("boardHeight", boardHeight);
        intent.putExtra("snakeSpeed", snakeSpeed);
        intent.putExtra("ifStartNewGame", ifStartNewGame);
        startActivity(intent);
    }


    private void setContinueButtonVisibility(boolean visibility) {
        if (visibility) {
            continueButton.setVisibility(View.VISIBLE);
        } else {
            continueButton.setVisibility(View.GONE);
        }
    }

    private void loadData () {
        SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        this.continueButtonVisibility = !sharedPref.getBoolean("doesSnakeBitesItself", false);
        this.snakeSpeed = sharedPref.getInt("snakeSpeed", 3);
        this.boardWidth = sharedPref.getInt("boardWidth", 15);
        this.boardHeight = sharedPref.getInt("boardHeight", 15);
        this.boardSizesIterator = sharedPref.getInt("boardSizesIterator", 0);
        lastScoreTextView.setText(String.valueOf(sharedPref.getInt("lastScore", 0)));
        highScoreTextView.setText(String.valueOf(sharedPref.getInt("highScore", 0)));
    }

    private void saveData () {
        SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("snakeSpeed", this.snakeSpeed);
        editor.putInt("boardWidth", this.boardWidth);
        editor.putInt("boardHeight", this.boardHeight);
        editor.putInt("boardSizesIterator", this.boardSizesIterator);
        editor.commit();
    }

    private void initializeViewComponents () {
        newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this);
        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        snakeSpeedPlusButton = findViewById(R.id.snakeSpeedPlusButton);
        snakeSpeedPlusButton.setOnClickListener(this);
        snakeSpeedMinusButton = findViewById(R.id.snakeSpeedMinusButton);
        snakeSpeedMinusButton.setOnClickListener(this);
        snakeSpeedTextView = findViewById(R.id.snakeSpeedValueTextView);
        boardSizePlusButton = findViewById(R.id.boardSizePlusButton);
        boardSizePlusButton.setOnClickListener(this);
        boardSizeMinusButton = findViewById(R.id.boardSizeMinusButton);
        boardSizeMinusButton.setOnClickListener(this);
        boardSizeTextView = findViewById(R.id.boardSizeTextView);
        lastScoreTextView = findViewById(R.id.lastScoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);
    }

}