package com.jgorecki.snakegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class GameplayActivity extends AppCompatActivity implements View.OnClickListener {

    GameBoard gameBoard;
    Snake snake;
    String directionOfMovement;
    private int boardWidth;
    private int boardHeight;
    private int snakeSpeed = 10;
    Handler handler = new Handler();
    Runnable runnable;
    Button upButton, downButton, rightButton, leftButton;
    boolean ifStartNewGame = false;
    TextView endGameTextView, scoreTextView;
    boolean movedone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initializeButtons();
        loadDataFromIntent();
        gameBoard = findViewById(R.id.gameBoard);
        createNewSnake();

    }

    @Override
    protected void onResume() {
        if (!ifStartNewGame) {
            loadStateOfTheSnake();
        }
        scoreTextView.setText(String.valueOf(snake.getSnakeScore()));
        startSnakeMovement();
        super.onResume();
    }


    private void loadDataFromIntent() {
        Intent intent = getIntent();
        this.boardWidth = intent.getIntExtra("boardWidth", 15);
        this.boardHeight = intent.getIntExtra("boardHeight", 15);
        this.snakeSpeed = intent.getIntExtra("snakeSpeed", 3);
        this.ifStartNewGame = intent.getBooleanExtra("ifStartNewGame", true);
    }


    @Override
    protected void onPause() {
        saveStateOfTheSnake();
        super.onPause();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(upButton)) {
            goUp();
        } else if (v.equals(downButton)) {
            goDown();
        } else if (v.equals(rightButton)) {
            goRight();
        } else if (v.equals(leftButton)) {
            goLeft();
        }

    }

    private void stopSnakeMovement() {
        handler.removeCallbacks(runnable);
    }

    private void startSnakeMovement() {
        handler.postDelayed(runnable = () -> {
            handler.postDelayed(runnable, 1000/this.snakeSpeed);
            snake.moveSnake(directionOfMovement);
            gameBoard.drawSnakeOnGameBoard(snake);
            scoreTextView.setText(String.valueOf(snake.getSnakeScore()));
            if (snake.doesSnakeBitesItself()) {
                gameOver();
            }
            movedone = false;
        }, 0);
    }

    private void gameOver() {
        stopSnakeMovement();
        endGameTextView.setVisibility(View.VISIBLE);
        saveScore();
        handler.postDelayed(runnable = () -> {
            handler.postDelayed(runnable, 1200);
            finish();
        }, 1200);
    }

    private void saveStateOfTheSnake () {
            String snake = new Gson().toJson(this.snake);
            SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("snake", snake);
            editor.putBoolean("doesSnakeBitesItself", this.snake.doesSnakeBitesItself());
            editor.commit();
    }

    private void saveScore () {
        SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt("highScore", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("lastScore", snake.getSnakeScore());
        if (snake.getSnakeScore() > highScore) {
            highScore = snake.getSnakeScore();
        }
        editor.putInt("highScore", highScore);
        editor.commit();
    }

    private void loadStateOfTheSnake () {
        SharedPreferences sharedPref = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        String snake = sharedPref.getString("snake", "");
        if (!snake.equals("")) {
            this.snake = new Gson().fromJson(snake, Snake.class);
            this.directionOfMovement = this.snake.getDirectionOfMovement();
            this.snakeSpeed = this.snake.getSnakeSpeed();
        }
    }

    private void createNewSnake () {
        this.snake = new Snake(this.boardWidth, this.boardHeight, this.snakeSpeed);
        directionOfMovement = snake.getDirectionOfMovement();
    }

    private void initializeButtons () {
        upButton = findViewById(R.id.buttonUp);
        downButton = findViewById(R.id.buttonDown);
        rightButton = findViewById(R.id.buttonRight);
        leftButton = findViewById(R.id.buttonLeft);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        endGameTextView = findViewById(R.id.endGameTextView);
        scoreTextView = findViewById(R.id.score);

    }


    private void goUp () {
        if (!movedone) {
            if (directionOfMovement.equals("Down")) {
                directionOfMovement = "Down";
            } else {
                directionOfMovement = "Up";
            }
            movedone = true;
        }
    }

    private void goDown () {
        if (!movedone) {
            if (directionOfMovement.equals("Up")) {
                directionOfMovement = "Up";
            } else {
                directionOfMovement = "Down";
            }
            movedone = true;
        }
    }

    private void goRight () {
        if (!movedone) {
            if (directionOfMovement.equals("Left")) {
                directionOfMovement = "Left";
            } else {
                directionOfMovement = "Right";
            }
            movedone = true;
        }
    }

    private void goLeft () {
        if (!movedone) {
            if (directionOfMovement.equals("Right")) {
                directionOfMovement = "Right";
            } else {
                directionOfMovement = "Left";
            }
            movedone = true;
        }
    }

}