package com.maharjan411.tictactoe.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by binod on 4/27/16.
 */
public class Ball extends Mark {



    public Ball(int x, int y) {
        super(x, y);
        setText("0");
        setTextColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas g, Resources res, int x, int y, int w, int h) {
        super.draw(g, res, x, y, w, h);
    }
}
