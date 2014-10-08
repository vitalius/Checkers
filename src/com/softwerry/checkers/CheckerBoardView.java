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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Checker board View
 */
public final class CheckerBoardView extends View {

    private BoardAssetFactory squareFactory;
    private final int squaresPerSide = 8;
    public int squareWidth;
    private final Paint paint = new Paint();
    public AlertDialog currentAlertDialog = null;

    public GameEngine gameEngine;
    public CheckerAI randomAI;

    public int tx = 0;
    public int ty = 0;

    /**
     * Instantiate board View, given the screen size and activity context
     *
     * @param context
     * @param attrs
     */
    public CheckerBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int width = SimpleCheckersActivity.SCREEN_X;
        int height = SimpleCheckersActivity.SCREEN_Y;
        int minSide = Math.min(width, height);
        squareWidth = (int) Math.floor(minSide / (float) squaresPerSide);

        // find translation points for centering the board
        tx = (int) Math.round(width / 2.0 - squareWidth * squaresPerSide / 2.0);
        ty = (int) Math.round(height / 2.0 - squareWidth * squaresPerSide / 2.0);
        squareFactory = new BoardAssetFactory(squareWidth);
        gameEngine = new GameEngine(squaresPerSide);
        randomAI = new CheckerAI();

        AddClickListener();
    }

    /**
     * Add click listener
     */
    public void AddClickListener() {
        // capture square touching
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gameEngine.gameState != GameEnum.PLAY) {
                    return false;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int row = getSquareRow(event);
                    int col = getSquareCol(event);
                    if (gameEngine.Click(row, col)) {
                        if (gameEngine.getScore(GameEnum.RED) > 11) {
                            ShowAlert(v, "Red win!");
                        }
                        randomAI.RandomMove(gameEngine, false);
                        if (gameEngine.getScore(GameEnum.BLACK) > 11) {
                            ShowAlert(v, "Black win!");
                        }
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
     * Display Alert box
     *
     * @param v
     * @param s
     */
    public void ShowAlert(final View v, String s) {
        currentAlertDialog = new AlertDialog.Builder(v.getContext())
                .setTitle(s)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gameEngine = new GameEngine(squaresPerSide);
                        v.invalidate();
                    }
                })
                .setIcon(android.R.drawable.star_on)
                .show();
    }

    /**
     * Resize the board on device flip
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            int width = newConfig.screenWidthDp;
            int height = newConfig.screenHeightDp;
            int minSide = Math.min(width, height);
            squareWidth = (int) Math.floor(minSide / (float) squaresPerSide);

            // find translation points for centering the board
            tx = (int) Math.round(width / 2.0 - squareWidth * squaresPerSide / 2.0);
            ty = (int) Math.round(height / 2.0 - squareWidth * squaresPerSide / 2.0);
            squareFactory = new BoardAssetFactory(squareWidth);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int width = newConfig.screenWidthDp;
            int height = newConfig.screenHeightDp;
            int minSide = Math.min(width, height);
            squareWidth = (int) Math.floor(minSide / (float) squaresPerSide * 0.90);

            // find translation points for centering the board
            tx = (int) Math.round(width / 2.0 - squareWidth * squaresPerSide / 2.0);
            ty = (int) Math.round(height / 2.0 - squareWidth * squaresPerSide / 2.0);
            squareFactory = new BoardAssetFactory(squareWidth);
        }
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

        // update the score
        SimpleCheckersActivity.redScore
                .setText("Red score: " + gameEngine.getScore(GameEnum.RED));
        SimpleCheckersActivity.blackScore
                .setText("Black score: " + gameEngine.getScore(GameEnum.BLACK));
    }
}
