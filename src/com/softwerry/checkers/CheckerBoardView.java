/*
 * The MIT License
 *
 * Copyright 2014 Vitaliy Pavlenko.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.softwerry.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Checker board View
 */
public class CheckerBoardView extends View {

    private final BoardAssetFactory squareFactory;
    private final int squaresPerSide = 8;
    public final int squareWidth;
    private final Paint paint = new Paint();

    public GameEngine gameEngine;
    public CheckerAI randomAI;

    public int tx = 0;
    public int ty = 0;

    private static final String TAG = "CheckerBoardView";

    /**
     * Instantiate board View, given the screen size and activity context
     *
     * @param context
     * @param width screen width in pixels
     * @param height screen height in pixels
     */
    public CheckerBoardView(Context context, int width, int height) {
        super(context);

        int minSide = Math.min(width, height);
        squareWidth = (int) Math.floor(minSide / (float) squaresPerSide);
        squareFactory = new BoardAssetFactory(squareWidth);
        gameEngine = new GameEngine();
        randomAI = new CheckerAI();

        // find translation points for centering the board
        tx = (int) Math.round(width / 2.0 - squareWidth * squaresPerSide / 2.0);
        ty = (int) Math.round(height / 2.0 - squareWidth * squaresPerSide / 2.0);

        // capture square touching
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int row = getSquareRow(event);
                    int col = getSquareCol(event);
                    if (gameEngine.Click(row, col)) {
                        randomAI.RandomMove(gameEngine, false);
                    }
                }
                v.invalidate();
                return true;
            }

            private int getSquareRow(MotionEvent event) {
                float xPos = event.getRawX();
                int x = (int) Math.floor((xPos - tx - squareWidth / 2.0) / (float) squareWidth);
                if (x < 0 || x >= squaresPerSide) {
                    return -1;
                }
                return x;
            }

            private int getSquareCol(MotionEvent event) {
                float yPos = event.getRawY();
                int y = (int) Math.floor((yPos - ty - squareWidth / 2.0) / (float) squareWidth);
                if (y < 0 || y >= squaresPerSide) {
                    return -1;
                }
                return y;
            }
        });
    }

    /**
     * Render game board
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                canvas.drawBitmap(squareFactory.GetSquare(gameEngine.getState()[row][col]),
                        tx + row * squareWidth, ty + col * squareWidth, paint);
            }
        }

        //canvas.drawBitmap(squareFactory.GenBlackScore(gameEngine),
        //        tx, ty - squareWidth, paint);
        //canvas.drawBitmap(squareFactory.GenRedScore(gameEngine),
        //        tx, ty + squaresPerSide * squareWidth, paint);
    }
}
