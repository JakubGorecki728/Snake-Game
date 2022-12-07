package com.jgorecki.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class GameBoard extends View {

    private int boardWidth = 1;
    private int boardHeight = 1;
    private int snakeHeadXPosition;
    private int snakeHeadYPosition;
    private int foodXPosition;
    private int foodYPosition;
    private int[] snakeXBodyPosition;
    private int[] snakeYBodyPosition;
    private String[] snakeBodyPartsOrientation;
    private String directionOfMovement = "";

    public GameBoard(Context context) {
        super(context);
    }

    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawSnakeOnGameBoard(Snake snake) {
        this.snakeHeadXPosition = snake.getXPosition();
        this.snakeHeadYPosition = snake.getYPosition();
        this.foodXPosition = snake.getXfoodPosition();
        this.foodYPosition = snake.getYfoodPosition();
        this.boardWidth = snake.getBoardWidth();
        this.boardHeight = snake.getBoardHeight();
        this.snakeXBodyPosition = snake.getSnakeXBodyPosition();
        this.snakeYBodyPosition = snake.getSnakeYBodyPosition();
        this.snakeBodyPartsOrientation = snake.getSnakeBodyPartsOrientation();
        this.directionOfMovement = snake.getDirectionOfMovement();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawSnakeBody(canvas);
        drawSnakeFood(canvas);
        drawSnakeHead(canvas);
        super.onDraw(canvas);
    }

    private void drawSnakeFood (Canvas canvas) {
        int color = Color.RED;
        drawPixel(canvas, color, this.foodXPosition, this.foodYPosition, "");
    }

    private void drawSnakeBody (Canvas canvas) {
        int bodyColor = Color.rgb(120, 189, 67);

        if (snakeXBodyPosition != null ) {
        for (int i=0; i<snakeXBodyPosition.length; i++) {
            int posX = snakeXBodyPosition[i];
            int posY = snakeYBodyPosition[i];
            String orientation = snakeBodyPartsOrientation[i];
            drawSnakeBodyPart(canvas, bodyColor, posX, posY, orientation);
        }
        }
    }

    private void drawSnakeHead (Canvas canvas) {
        int headColor = Color.rgb(70, 160, 45);


        String offsetDirection = "";
        if (this.directionOfMovement.equals("Up")) {
            offsetDirection = "Down";
        } else if (this.directionOfMovement.equals("Down")) {
            offsetDirection = "Up";
        } else if (this.directionOfMovement.equals("Right")) {
            offsetDirection = "Left";
        } else if (this.directionOfMovement.equals("Left")) {
            offsetDirection = "Right";
        }
        drawPixel(canvas, headColor, this.snakeHeadXPosition, this.snakeHeadYPosition, offsetDirection);
    }

    private void drawSnakeBodyPart (Canvas canvas, int color, int positionX, int positionY, String orientation) {
        String rect1 = "";
        String rect2 = "";

        if (orientation.equals("UpUp") || orientation.equals("DownDown")) {
            rect1 = "Up";
            rect2 = "Down";
        } else if (orientation.equals("RightRight") || orientation.equals("LeftLeft")) {
            rect1 = "Right";
            rect2 = "Left";
        } else if (orientation.equals("LeftDown") || orientation.equals("UpRight")) {
            rect1 = "Down";
            rect2 = "Right";
        } else if (orientation.equals("UpLeft") || orientation.equals("RightDown")) {
            rect1 = "Down";
            rect2 = "Left";
        } else if (orientation.equals("DownLeft") || orientation.equals("RightUp")) {
            rect1 = "Up";
            rect2 = "Left";
        } else if (orientation.equals("LeftUp") || orientation.equals("DownRight")) {
            rect1 = "Up";
            rect2 = "Right";
        }

        drawPixel(canvas, color, positionX, positionY, rect1);
        drawPixel(canvas, color, positionX, positionY, rect2);
    }


    private void drawPixel(Canvas canvas, int color, int positionX, int positionY, String directionOfExtend) {
        int xzero = getXZeroPosition();
        int yzero = getYZeroPosition();
        int pixelsize = getPixelSize();
        double bodyWidthToPixelSizeRatio = 0.8;
        double offset = (1-bodyWidthToPixelSizeRatio)/2;
        double offsetUp = offset;
        double offsetDown = offset;
        double offsetRight = offset;
        double offsetLeft = offset;

        if (directionOfExtend.equals("Up")) {
            offsetUp = 0;
        } else if (directionOfExtend.equals("Down")) {
            offsetDown = 0;
        } else if (directionOfExtend.equals("Right")) {
            offsetRight = 0;
        } else if (directionOfExtend.equals("Left")) {
            offsetLeft = 0;
        }

        Paint pixelStyleFill = new Paint();
        pixelStyleFill.setColor(color);
        pixelStyleFill.setStyle(Paint.Style.FILL);

        Rect rectangle = new Rect();
        rectangle.left = (int) (xzero+(positionX-1+offsetLeft)*pixelsize);
        rectangle.top = (int) (yzero+(positionY-1+offsetUp)*pixelsize);
        rectangle.right = (int) (xzero+(positionX-offsetRight)*pixelsize);
        rectangle.bottom = (int) (yzero+(positionY-offsetDown)*pixelsize);
        canvas.drawRect(rectangle, pixelStyleFill);
    }

    private void drawBackground (Canvas canvas) {
        int xzero = getXZeroPosition();
        int yzero = getYZeroPosition();
        int pixelsize = getPixelSize();

        Paint backgroundStyle = new Paint();
        backgroundStyle.setColor(Color.WHITE);
        backgroundStyle.setStyle(Paint.Style.FILL);
        Rect rectangle = new Rect();
        rectangle.left = xzero;
        rectangle.top = yzero;
        rectangle.right = xzero+pixelsize* boardWidth;
        rectangle.bottom = yzero+pixelsize* boardHeight;
        canvas.drawRect(rectangle, backgroundStyle);
    }


    private int getXZeroPosition () {
        double panelWidth = getWidth();
        double x = boardWidth;
        double y = boardHeight;
        int xposition;

        if (x<y) {
            xposition = (int) (panelWidth*(1-(x/y))/2);
        }
        else {
            xposition = 0;
        }
        return xposition;
    }

    private int getYZeroPosition () {
        double panelHeight = getHeight();
        double x = boardWidth;
        double y = boardHeight;
        int yposition;

        if (x>y) {
            yposition = (int) (panelHeight*(1-(y/x))/2);
        }
        else {
            yposition = 0;
        }
        return yposition;
    }

    private int getPixelSize () {
        int size;
        double x = boardWidth;
        double y = boardHeight;
        if (x>y) {
            size = (int) getWidth()/ boardWidth;
        }
        else {
            size = (int) getHeight()/ boardHeight;
        }
        return size;
    }

}
