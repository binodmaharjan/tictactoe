package com.maharjan411.tictactoe.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.maharjan411.tictactoe.utils.LogUtil;


/**
 * Created by binod on 4/27/16.
 */
public class Game extends View {

    private static final String TAG = Game.class.getSimpleName();
    private static final int EXTENSION = 40;

    private Context mContext;

    private int screenH, screenW;

    private Cell[][] squareCell;

    final int x = 3, y = 3;
    private int cellHeight;
    private int cellWidth;
    private Paint mPaintBlack;
    private Paint mPaintRed;

    private Board board;

    Cell cross = new Cross(0, 0);
    Cell ball = new Ball(0, 0);
    Cell empty = new Empty(0, 0);

    private boolean win;
    private boolean isGameOver;

    private RectF firstRect = new RectF();
    private RectF secondRect = new RectF();

    private boolean turn = false;

    private int gameMode;

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public Game(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();


    }

    private void init() {
        mPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setStrokeWidth(3);

        mPaintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRed.setColor(Color.RED);
        mPaintRed.setStrokeWidth(3);

        board = new Board();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                drawMark(i, j, canvas);
            }
        }

        for (int i = 0; i <= x; i++) {
            canvas.drawLine(cellWidth * i, 0, cellWidth * i, screenH, mPaintBlack);
        }
        for (int i = 0; i <= y; i++) {
            canvas.drawLine(0, cellHeight * i, screenW, cellHeight * i, mPaintBlack);
        }


        if (win) {
            int[] line = board.getWiningLine();

            firstRect.set(line[0] * cellWidth, line[1] * cellHeight, (line[0] * cellWidth) + cellWidth, (line[1] * cellHeight) + cellHeight);
            secondRect.set(line[2] * cellWidth, line[3] * cellHeight, (line[2] * cellWidth) + cellWidth, (line[3] * cellHeight) + cellHeight);

            canvas.drawLine(firstRect.left + cellWidth / 2, firstRect.top + cellHeight / 2, secondRect.left + cellWidth / 2, secondRect.top + cellHeight / 2, mPaintRed);

            Toast.makeText(mContext, board.getWinner(), Toast.LENGTH_SHORT).show();

        }

        if (gameMode == GameActivity.SINGLE_PLAYER) {
            if (board.getPlayer() < 0) {
                if (!isGameOver) {
                    Point pt = board.getComputerMove(board.getPlayer());
                    move(pt.x, pt.y);
                } else {
                    Toast.makeText(mContext, board.getWinner(), Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            if (isGameOver) {
                Toast.makeText(mContext, board.getWinner(), Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void drawMark(int i, int j, Canvas canvas) {
        switch (board.getMark(i, j)) {
            case 1:
                cross.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
            case -1:
                ball.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
            default:
                empty.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        int xCell = (int) (x / cellWidth);
        int yCell = (int) (y / cellHeight);

        LogUtil.i(TAG, "x " + x + " y " + y + " xCell " + xCell + " yCell " + yCell);
        move(xCell, yCell);

        return super.onTouchEvent(event);
    }

    private void move(int xCell, int yCell) {


        if (!win || !board.isGameCompleted()) {

            try {

                board.putMark(xCell, yCell);
                win = board.isWin(board.getMark(xCell, yCell));

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                Toast.makeText(mContext, " already marked", Toast.LENGTH_SHORT).show();
            }
        }
        isGameOver = board.isGameCompleted();
        if (isGameOver) {
            Toast.makeText(mContext, " game over", Toast.LENGTH_SHORT);
        }
        invalidate();
    }

    private void drawBallCross(int xCell, int yCell) {

        if (turn) {
            squareCell[xCell][yCell] = new Cross(squareCell[xCell][yCell].x, squareCell[xCell][yCell].y);
        } else {
            squareCell[xCell][yCell] = new Ball(squareCell[xCell][yCell].x, squareCell[xCell][yCell].y);
        }
    }

    boolean isCellFilled(int x, int y) {
        if (squareCell[x][y] instanceof Cross) return true;
        if (squareCell[x][y] instanceof Ball) return true;
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;

        cellHeight = screenH / 3;
        cellWidth = screenW / 3;
        initCells();
    }

    private void initCells() {
        squareCell = new Cell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                squareCell[i][j] = new Empty(j * cellWidth, i * cellHeight);
            }
        }
    }

    public void start() {
        board.clearBoard();
        win = false;
        isGameOver = false;
        invalidate();
    }
}