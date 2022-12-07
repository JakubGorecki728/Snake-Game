package com.jgorecki.snakegame;

import java.io.Serializable;
import java.util.Random;

public class Snake {
    private int boardWidth;
    private int boardHeight;
    private int snakeSpeed;
    private int snakeHeadXPosition;
    private int snakeHeadYPosition;
    private int foodXPosition;
    private int foodYPosition;
    private int snakeLengthOnStart = 5;
    private int scoredPoints = 0;
    private int snakeLengthBuffor = snakeLengthOnStart-1;
    private int[] snakeBodyXPosition;
    private int[] snakeBodyYPosition;
    private String[] snakeBodyPartsOrientation;
    private String[] SnakeBodyPartsDirectionOfMovement;
    private String directionOfMovement = "Up";
    private boolean snakeBitesItself = false;

    public Snake (int boardWidth, int boardHeight, int snakeSpeed) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.snakeHeadXPosition = boardWidth/2+1;
        this.snakeHeadYPosition = boardHeight/2+1;
        this.snakeSpeed = snakeSpeed;
    }


    public void moveSnake (String directionOfMovement) {
        changeDirectionOfMovement(directionOfMovement);
        outOfBorderControl();
        growthControl();
        foodControl();
        selfCollisionControl();
    }

    private void buildBodyOfSnake (int x, int y, String direction) {
        int extend = 0;
        if (snakeLengthBuffor > 0) {
            extend = 1;
        }
        if (snakeBodyXPosition == null) {
            snakeBodyXPosition = new int[]{x};
            snakeBodyYPosition = new int[]{y};
            SnakeBodyPartsDirectionOfMovement = new String[]{direction};
        } else {
            int[] oldx = snakeBodyXPosition;
            int[] oldy = snakeBodyYPosition;
            String[] oldDir = SnakeBodyPartsDirectionOfMovement;
            int[] newx = new int[oldx.length+extend];
            int[] newy = new int[oldy.length+extend];
            String[] newDir = new String[oldDir.length+extend];
            for(int i=0; i< newx.length-1; i++) {
                newx[i] = oldx[i+1-extend];
                newy[i] = oldy[i+1-extend];
                newDir[i] = oldDir[i+1-extend];
            }
            newx[newx.length-1] = x;
            newy[newy.length-1] = y;
            newDir[newDir.length-1] = direction;
            snakeBodyXPosition = newx;
            snakeBodyYPosition = newy;
            SnakeBodyPartsDirectionOfMovement = newDir;
        }

    }

    private void setSnakeBodyPartsOrientation () {
        String[] orientation = new String[snakeBodyXPosition.length];
        for(int i=0; i< orientation.length-1; i++) {
            orientation[i] = SnakeBodyPartsDirectionOfMovement[i]+SnakeBodyPartsDirectionOfMovement[i+1];
        }
        orientation[orientation.length-1] = SnakeBodyPartsDirectionOfMovement[orientation.length-1]+this.directionOfMovement;
        this.snakeBodyPartsOrientation = orientation;
    }

    private void outOfBorderControl() {
        if (this.snakeHeadXPosition < 1) {
            this.snakeHeadXPosition = boardWidth;
        } else if (this.snakeHeadXPosition > boardWidth) {
            this.snakeHeadXPosition = 1;
        } else if (this.snakeHeadYPosition < 1) {
            this.snakeHeadYPosition = boardHeight;
        } else if (this.snakeHeadYPosition > boardHeight) {
            this.snakeHeadYPosition = 1;
        }
    }

    private void foodControl () {
        if (foodXPosition == 0 && foodYPosition == 0) {
            makeNewFood();
        }

        if (checkIntersection(snakeHeadXPosition, snakeHeadYPosition, foodXPosition, foodYPosition)) {
            makeNewFood();
            snakeLengthBuffor ++;
            scoredPoints ++;
        }
    }

    private void makeNewFood () {
       boolean makeFoodAgain;
        do {
            makeFoodAgain = false;
            Random r = new Random();
            this.foodXPosition = 1 + r.nextInt(this.boardWidth);
            this.foodYPosition = 1 + r.nextInt(this.boardHeight);
            boolean headIntersection = checkIntersection(foodXPosition, foodYPosition, snakeHeadXPosition, snakeHeadYPosition);
            boolean bodyIntersection = checkIntersection(foodXPosition, foodYPosition, snakeBodyXPosition, snakeBodyYPosition);
            if (headIntersection || bodyIntersection) {
                makeFoodAgain = true;
            }
        }
        while (makeFoodAgain);
    }

    private void growthControl () {
        if (snakeLengthBuffor > 0) {
            snakeLengthBuffor--;
        }
    }

    private void selfCollisionControl() {
        this.snakeBitesItself = checkIntersection(snakeHeadXPosition, snakeHeadYPosition, snakeBodyXPosition, snakeBodyYPosition);
    }

    private boolean checkIntersection (int intX, int intY, int[] intArrayX, int[] intArrayY) {
        boolean ifIntersects = false;
        for (int i = 0; i < intArrayX.length; i++) {
            boolean ifXIntersects = false;
            boolean ifYIntersects = false;
            if (intX == intArrayX[i]) {
                ifXIntersects = true;
            }
            if (intY == intArrayY[i]) {
                ifYIntersects = true;
            }
            if (ifXIntersects && ifYIntersects) {
                ifIntersects = true;
                break;
            }
        }
        return ifIntersects;
    }

    private boolean checkIntersection (int int1X, int int1Y, int int2X, int int2Y) {
        boolean ifIntersects = false;
        if (int1X == int2X && int1Y == int2Y) ifIntersects = true;
        return ifIntersects;
    }

    private void changeDirectionOfMovement (String directionOfMovement) {
        buildBodyOfSnake(this.snakeHeadXPosition, this.snakeHeadYPosition, this.directionOfMovement);
        this.directionOfMovement = directionOfMovement;
        setSnakeBodyPartsOrientation();
        switch (this.directionOfMovement) {
            case "Up":
                this.snakeHeadYPosition--;
                break;
            case "Down":
                this.snakeHeadYPosition++;
                break;
            case "Right":
                this.snakeHeadXPosition++;
                break;
            case "Left":
                this.snakeHeadXPosition--;
                break;
        }
    }

    public int getXPosition () {
        return this.snakeHeadXPosition;
    }

    public int getYPosition () {
        return this.snakeHeadYPosition;
    }

    public int getXfoodPosition () {
        return this.foodXPosition;
    }

    public int getYfoodPosition () {
        return this.foodYPosition;
    }

    public int getBoardWidth () {
        return this.boardWidth;
    }

    public int getBoardHeight () {
        return this.boardHeight;
    }

    public int[] getSnakeXBodyPosition () {
        return this.snakeBodyXPosition;
    }

    public int[] getSnakeYBodyPosition () {
        return this.snakeBodyYPosition;
    }

    public String[] getSnakeBodyPartsOrientation () {
        return this.snakeBodyPartsOrientation;
    }

    public boolean doesSnakeBitesItself () {
        return this.snakeBitesItself;
    }

    public String getDirectionOfMovement() {
        return this.directionOfMovement;
    }

    public int getSnakeSpeed () {return this.snakeSpeed;}

    public int getSnakeScore () {
        return this.scoredPoints;
    }
}
