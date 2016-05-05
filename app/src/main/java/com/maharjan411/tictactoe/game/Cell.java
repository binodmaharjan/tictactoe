package com.maharjan411.tictactoe.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by binod on 4/27/16.
 */
public  abstract class Cell extends Point {
    public Cell(int x, int y) {
        super(x, y);
    }

    public abstract void draw(Canvas g, Resources res, int x, int y, int w, int h);
}
