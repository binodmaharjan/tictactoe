package com.maharjan411.tictactoe.bluetooth;

/**
 * Created by binod on 5/10/16.
 */
public class BluetoothMsg {
    boolean turn;
    int x;
    int y;
    int player;
    boolean restart;


    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    @Override
    public String toString() {
        return "BluetoothMsg{" +
                "turn=" + turn +
                ", x=" + x +
                ", y=" + y +
                ", player=" + player +
                '}';
    }
}
