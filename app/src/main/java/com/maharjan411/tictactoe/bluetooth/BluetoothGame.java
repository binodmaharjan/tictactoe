package com.maharjan411.tictactoe.bluetooth;

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

import com.maharjan411.tictactoe.game.Ball;
import com.maharjan411.tictactoe.game.Board;
import com.maharjan411.tictactoe.game.Cell;
import com.maharjan411.tictactoe.game.Cross;
import com.maharjan411.tictactoe.game.Empty;
import com.maharjan411.tictactoe.game.GameActivity;
import com.maharjan411.tictactoe.utils.LogUtil;


/**
 * Created by binod on 4/27/16.
 */
public class BluetoothGame extends View {

    private static final String TAG = BluetoothGame.class.getSimpleName();
    private Context mContext;
    private int screenH, screenW;
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

    private boolean turn = true;

    private int gameMode;

    IMessage mListener;

    public void setListener(IMessage mListener) {
        this.mListener = mListener;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public BluetoothGame(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BluetoothGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BluetoothGame(Context context, AttributeSet attrs, int defStyleAttr) {
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
            case Board.X:
                cross.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
            case Board.O:
                ball.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
            default:
                empty.draw(canvas, mContext.getResources(), i, j, screenW / 3, screenH / 3);
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!turn){

            Toast.makeText(mContext,"Not your turn",Toast.LENGTH_SHORT).show();
            return false;
        }

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

                turn=false;

                if(mListener!=null){
                    mListener.onTouchDetected(true,xCell,yCell,board.getPlayer());
                }
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



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;

        cellHeight = screenH / 3;
        cellWidth = screenW / 3;
        start();
    }


    public void start() {
        board.clearBoard(0);
        win = false;
        isGameOver = false;
        invalidate();
    }

    public void putMark(int x,int y,boolean turn,int player){
        board.putMark(x,y);
        this.turn=turn;
        win = board.isWin(board.getMark(x, y));
        isGameOver = board.isGameCompleted();
        if (isGameOver) {
            Toast.makeText(mContext, " game over", Toast.LENGTH_SHORT);
        }
        board.setPlayer(player);
        invalidate();
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }


    public interface IMessage{
        void onTouchDetected(boolean turn,int x, int y, int player);
    }

}