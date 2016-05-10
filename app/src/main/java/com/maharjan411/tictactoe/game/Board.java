package com.maharjan411.tictactoe.game;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by binod on 4/28/16.
 */
public class Board {


    public static final  int X = 1, O = -1; // players

    public static final int EMPTY = 0; // empty cell

    private int board[][] = new int[3][3]; // game board

    private int player; // current player

    private int winingLine[] = new int[4];


    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public Board() {
        clearBoard();
    }

    public void selectPlayer(){
        Random rand=new Random();
        boolean choose=rand.nextBoolean();
    }


    public void clearBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = EMPTY; // every cell should be empty

        Random rand=new Random();
        boolean choose=rand.nextBoolean();
        player = (choose?X:O); // the first player is 'X' or O

    }

    public void clearBoard(int bluetooth) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = EMPTY; // every cell should be empty

        Random rand=new Random();
        boolean choose=rand.nextBoolean();
        player = X; // the first player is 'X' or O

    }

    public int getMark(int i, int j) {
        return board[i][j];
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player){
        this.player=player;
    }



    /**
     * Puts an X or O mark at position i,j.
     */




    public void putMark(int i, int j) throws IllegalArgumentException {
        if ((i < 0) || (i > 2) || (j < 0) || (j > 2))
            throw new IllegalArgumentException("Invalid board position");
        if (board[i][j] != EMPTY)
            throw new IllegalArgumentException("Board position occupied");
        board[i][j] = player; // place the mark for the current player
        player = -player; // switch players (uses fact that O = - X)
    }


    public List<Point> getAvailableStates() {
        List<Point> states = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    states.add(new Point(i, j));
                }
            }
        }
        return states;
    }

    public Point nextWinningMove(int mark) {

        List<Point>availableStates=getAvailableStates();

        for(int i=0;i<availableStates.size();i++){
            Point pt=availableStates.get(i);
            board[pt.x][pt.y]=mark;
            boolean iswin=isWin(mark);
            board[pt.x][pt.y]=EMPTY;
            if(iswin) return pt;
        }

        return null;
    }


    public Point getComputerMove(int mark) {

        // wining move
        Point pt =nextWinningMove(mark);
        if(pt==null){
            // defensive move
            pt=nextWinningMove(-mark);
        }

        if(pt==null){
            List<Point> states = getAvailableStates();
            int move = (int) (Math.random() * states.size());
            return states.get(move);
        }

        return pt;

    }




    public int[] getWiningLine() {
        return winingLine;
    }

    public boolean isWin(int mark) {
        return checkCol(mark) || checkRow(mark) || checkDiagonal(mark);
    }

    private boolean checkRow(int mark) {
        for (int i = 0; i < 3; i++) {
            boolean h = (board[i][0] + (board[i][1]) + (board[i][2]) == mark * 3);
            if (h) {
                winingLine[0] = i;
                winingLine[1] = 0;
                winingLine[2] = i;
                winingLine[3] = 2;
                return true;
            }
        }
        return false;

    }

    private boolean checkCol(int mark) {
        for (int i = 0; i < 3; i++) {
            boolean h = (board[0][i] + (board[1][i]) + (board[2][i]) == mark * 3);
            if (h) {
                winingLine[0] = 0;
                winingLine[1] = i;
                winingLine[2] = 2;
                winingLine[3] = i;
                return true;
            }
        }
        return false;

    }

    private boolean checkDiagonal(int mark) {

        boolean diag = (board[0][0] + board[1][1] + board[2][2] == mark * 3);
        if (diag) {
            winingLine[0] = 0;
            winingLine[1] = 0;
            winingLine[2] = 2;
            winingLine[3] = 2;
            return true;
        }

        boolean revDiag = (board[2][0] + board[1][1] + board[0][2] == mark * 3);
        if (revDiag) {
            winingLine[0] = 2;
            winingLine[1] = 0;
            winingLine[2] = 0;
            winingLine[3] = 2;
            return true;
        }
        return false;

    }


    public int winner() {
        if (isWin(X))
            return (X);
        else if (isWin(O))
            return (O);
        else
            return (0);

    }

    public String getWinner(){
      if(winner()==X){
          return "X wins";
      }else if(winner()==O){
          return "O wins";

      }
        return "draw";
    }

    public boolean isGameCompleted() {
        if (winner() == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) return false;
                }
            }
        }

        return true;
    }


}
