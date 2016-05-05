package com.maharjan411.tictactoe.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.maharjan411.tictactoe.game.Cell;

/**
 * Created by binod on 4/27/16.
 */
public class Empty extends Cell {

    Paint ePaint;

    public Empty(int x, int y) {
        super(x, y);

        ePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        ePaint.setColor(Color.WHITE);
    }

    @Override
    public void draw(Canvas g, Resources res, int x, int y, int w, int h) {
        g.drawRect(x*w, y*h, (x*w)+w, (y*h)+h,ePaint);
    }
}
