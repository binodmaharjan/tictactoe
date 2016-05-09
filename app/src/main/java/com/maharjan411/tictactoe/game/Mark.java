package com.maharjan411.tictactoe.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by binod on 4/27/16.
 */
public class Mark extends Cell {

    Paint mTextPaint = new Paint();
    RectF rect = new RectF();

    String mText = "O";
    private RectF bounds;


    public Mark(int x, int y) {
        super(x, y);
        initPaints();
    }

    public void setTextColor(int color){
        mTextPaint.setColor(color);
    }

    public void setText(String mark){
        this.mText=mark;
    }

    private void initPaints(){
        mTextPaint.setTextSize(100);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextScaleX(1.0f);
    }



    @Override
    public void draw(Canvas g, Resources res, int x, int y, int w, int h) {
        rect.set(x * w, y * h, (x * w) + w, (y * h) + h);
        adjustTextSize(h);
        g.drawText(mText, // Text to display
                bounds.left, bounds.top - mTextPaint.ascent(),
                mTextPaint
        );

    }


    void adjustTextSize(int mViewHeight) {

        float target = (float) mViewHeight * .7f;
        mTextPaint.setTextSize(target);
        bounds = new RectF(rect);
        bounds.right = mTextPaint.measureText(mText, 0, mText.length());
        // measure text height
        bounds.bottom = mTextPaint.descent() - mTextPaint.ascent();
        bounds.left += (rect.width() - bounds.right) / 2.0f;
        bounds.top += (rect.height() - bounds.bottom) / 2.0f;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Mark) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "O";
    }
}
