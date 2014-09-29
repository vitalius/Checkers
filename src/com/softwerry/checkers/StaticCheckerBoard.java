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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Testing a static checker board -- checker board position, sizing, etc
 */
public class StaticCheckerBoard extends View {

    public Bitmap baseBoard;

    private final Paint paint = new Paint();
    private int tx = 0;
    private int ty = 0;
    
    private static final int CELLS_PER_SIDE = 8;

    /**
     * Instantiate board View, given the screen size and activity context
     *
     * @param context
     * @param width
     * @param height
     */
    public StaticCheckerBoard(Context context, int width, int height) {
        super(context);
        setFocusable(true);

        // Build board
        Bitmap board = buildMaxBoard(width, height);
        drawCheckerPattern(board);
        // find translation points for centering the board
        tx = (int) Math.round(width / 2.0 - board.getWidth() / 2.0);
        ty = (int) Math.round(height / 2.0 - board.getHeight() / 2.0);

        baseBoard = board;
    }

    /**
     * Build base board bitmap
     *
     * @param screen_pixel_width
     * @param screen_pixel_height
     * @return
     */
    private Bitmap buildMaxBoard(int screen_pixel_width, int screen_pixel_height) {
        int minSide = Math.min(screen_pixel_width, screen_pixel_height);
        Bitmap.Config conf = Bitmap.Config.ARGB_4444;
        Bitmap bmp = Bitmap.createBitmap(minSide, minSide, conf);
        return bmp;
    }

    /**
     * Add checker board pattern to a bitmap board
     *
     * @param board blank base board
     */
    private void drawCheckerPattern(Bitmap board) {

        // initialize paint colors
        Paint blackPaint = new Paint(Paint.DITHER_FLAG);
        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setColor(Color.BLACK);

        Paint whitePaint = new Paint(Paint.DITHER_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        Canvas boardCanvas = new Canvas(board);

        // size of each box
        int cellSize = (int) Math.floor(board.getWidth() / (float) CELLS_PER_SIDE);

        for (int row = 0; row < CELLS_PER_SIDE; row++) {
            for (int col = 0; col < CELLS_PER_SIDE; col++) {
                if ((row % 2 == 0 && col % 2 == 0)
                        || (row % 2 != 0 && col % 2 != 0)) {
                    boardCanvas.drawRect(row * cellSize, col * cellSize,
                            (row + 1) * cellSize, (col + 1) * cellSize, blackPaint);
                } else {
                    boardCanvas.drawRect(row * cellSize, col * cellSize,
                            (row + 1) * cellSize, (col + 1) * cellSize, whitePaint);
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(baseBoard, tx, ty, paint);
    }
}
