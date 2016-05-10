package com.maharjan411.tictactoe.bluetooth;

import java.util.Arrays;

/**
 * Created by binod on 5/9/16.
 */
public class BoardDTO {
    private boolean turn;
    private int[][]board;

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "BoardDTO{" +
                "turn=" + turn +
                ", board=" + Arrays.toString(board) +
                '}';
    }
}
