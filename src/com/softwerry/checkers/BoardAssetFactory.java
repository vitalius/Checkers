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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.EnumMap;
import java.util.Map;

/**
 * Generates and caches bitmap images for various board pieces
 */
public class BoardAssetFactory {

    private final int sideLength;
    private final Map<GameEnum, Bitmap> cache = new EnumMap<GameEnum, Bitmap>(GameEnum.class);
    private final float fHalfSide;
    private final float fCheckerR;

    public BoardAssetFactory(int s) {
        sideLength = s;
        fHalfSide = sideLength / 2.0f;
        fCheckerR = fHalfSide * 0.8f;
    }

    public Bitmap BlackSquare() {
        if (cache.containsKey(GameEnum.EMPTY)) {
            return cache.get(GameEnum.EMPTY);
        }
        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.DKGRAY);

        cache.put(GameEnum.EMPTY, square);
        return square;
    }

    public Bitmap WhiteSquare() {
        if (cache.containsKey(GameEnum.INVALID)) {
            return cache.get(GameEnum.INVALID);
        }
        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.WHITE);

        cache.put(GameEnum.INVALID, square);
        return square;
    }

    public Bitmap NextMoveSquare() {
        if (cache.containsKey(GameEnum.EMPTY_NEXT)) {
            return cache.get(GameEnum.EMPTY_NEXT);
        }
        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.GRAY);

        cache.put(GameEnum.EMPTY_NEXT, square);
        return square;
    }

    public Bitmap ScoreSquare() {
        if (cache.containsKey(GameEnum.SCORE)) {
            return cache.get(GameEnum.SCORE);
        }
        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.LTGRAY);

        cache.put(GameEnum.SCORE, square);
        return square;
    }

    public Bitmap RedChecker() {
        if (cache.containsKey(GameEnum.RED_CHECKER)) {
            return cache.get(GameEnum.RED_CHECKER);
        }

        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.DKGRAY);

        Paint red = new Paint(Paint.ANTI_ALIAS_FLAG);
        red.setStyle(Paint.Style.FILL);
        red.setColor(Color.RED);
        red.setShadowLayer(1.5f, 0.0f, 2.0f, Color.BLACK);
        Canvas c = new Canvas(square);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, red);

        Paint white = new Paint(Paint.ANTI_ALIAS_FLAG);
        white.setStyle(Paint.Style.STROKE);
        white.setColor(Color.LTGRAY);
        white.setStrokeWidth(1.5f);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, white);

        cache.put(GameEnum.RED_CHECKER, square);
        return square;
    }

    public Bitmap RedCheckerHighlighted() {
        if (cache.containsKey(GameEnum.RED_CHECKER_H)) {
            return cache.get(GameEnum.RED_CHECKER_H);
        }

        // start with red checker
        Bitmap square = Bitmap.createBitmap(RedChecker());
        Canvas c = new Canvas(square);

        // highlight
        Paint highlight = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlight.setStyle(Paint.Style.STROKE);
        highlight.setStrokeWidth(1.5f);
        highlight.setColor(Color.WHITE);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, highlight);

        cache.put(GameEnum.RED_CHECKER_H, square);
        return square;
    }

    public Bitmap RedSuperChecker() {
        if (cache.containsKey(GameEnum.RED_CHECKER_S)) {
            return cache.get(GameEnum.RED_CHECKER_S);
        }
        // start with red checker
        Bitmap square = Bitmap.createBitmap(RedChecker());
        Canvas c = new Canvas(square);

        // mark
        Paint superMark = new Paint(Paint.ANTI_ALIAS_FLAG);
        superMark.setStyle(Paint.Style.STROKE);
        superMark.setStrokeWidth(2.0f);
        superMark.setColor(Color.YELLOW);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR * 0.1f, superMark);

        cache.put(GameEnum.RED_CHECKER_S, square);
        return square;
    }

    public Bitmap RedSuperCheckerHighlighted() {
        if (cache.containsKey(GameEnum.RED_CHECKER_S_H)) {
            return cache.get(GameEnum.RED_CHECKER_S_H);
        }
        // start with red super checker
        Bitmap square = Bitmap.createBitmap(RedSuperChecker());
        Canvas c = new Canvas(square);

        // highlight
        Paint highlight = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlight.setStyle(Paint.Style.STROKE);
        highlight.setStrokeWidth(1.5f);
        highlight.setColor(Color.WHITE);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, highlight);

        cache.put(GameEnum.RED_CHECKER_S_H, square);
        return square;
    }

    public Bitmap BlackChecker() {
        if (cache.containsKey(GameEnum.BLACK_CHECKER)) {
            return cache.get(GameEnum.BLACK_CHECKER);
        }
        Bitmap square = Bitmap.createBitmap(
                sideLength, sideLength, Bitmap.Config.ARGB_8888);
        square.eraseColor(Color.DKGRAY);

        Paint black = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setStyle(Paint.Style.FILL);
        black.setColor(Color.BLACK);
        black.setShadowLayer(1.5f, 0.0f, 2.0f, Color.BLACK);
        Canvas c = new Canvas(square);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, black);

        Paint border = new Paint(Paint.ANTI_ALIAS_FLAG);
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.LTGRAY);
        border.setStrokeWidth(1.5f);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, border);

        cache.put(GameEnum.BLACK_CHECKER, square);
        return square;
    }

    public Bitmap BlackCheckerHighlighted() {
        if (cache.containsKey(GameEnum.BLACK_CHECKER_H)) {
            return cache.get(GameEnum.BLACK_CHECKER_H);
        }

        // start with black checker
        Bitmap square = Bitmap.createBitmap(BlackChecker());
        Canvas c = new Canvas(square);

        // highlight
        Paint highlight = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlight.setStyle(Paint.Style.STROKE);
        highlight.setStrokeWidth(1.5f);
        highlight.setColor(Color.WHITE);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, highlight);

        cache.put(GameEnum.BLACK_CHECKER_H, square);
        return square;
    }

    public Bitmap BlackSuperChecker() {
        if (cache.containsKey(GameEnum.BLACK_CHECKER_S)) {
            return cache.get(GameEnum.BLACK_CHECKER_S);
        }

        // start with black checker
        Bitmap square = Bitmap.createBitmap(BlackChecker());
        Canvas c = new Canvas(square);

        // mark
        Paint superMark = new Paint(Paint.ANTI_ALIAS_FLAG);
        superMark.setStyle(Paint.Style.STROKE);
        superMark.setStrokeWidth(2.0f);
        superMark.setColor(Color.YELLOW);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR * 0.1f, superMark);

        cache.put(GameEnum.BLACK_CHECKER_S, square);
        return square;
    }

    public Bitmap BlackSuperCheckerHighlighted() {
        if (cache.containsKey(GameEnum.BLACK_CHECKER_S_H)) {
            return cache.get(GameEnum.BLACK_CHECKER_S_H);
        }

        // start with black checker
        Bitmap square = Bitmap.createBitmap(BlackSuperChecker());
        Canvas c = new Canvas(square);

        // highlight
        Paint highlight = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlight.setStyle(Paint.Style.STROKE);
        highlight.setStrokeWidth(1.5f);
        highlight.setColor(Color.WHITE);
        c.drawCircle(fHalfSide, fHalfSide, fCheckerR, highlight);

        cache.put(GameEnum.BLACK_CHECKER_S_H, square);
        return square;
    }

    public Bitmap GetSquare(GameEnum e) {
        if (e == GameEnum.INVALID) {
            return WhiteSquare();
        }

        if (e == GameEnum.SCORE) {
            return ScoreSquare();
        }

        if (e == GameEnum.RED_CHECKER) {
            return RedChecker();
        }

        if (e == GameEnum.RED_CHECKER_S) {
            return RedSuperChecker();
        }

        if (e == GameEnum.RED_CHECKER_H) {
            return RedCheckerHighlighted();
        }

        if (e == GameEnum.RED_CHECKER_S_H) {
            return RedSuperCheckerHighlighted();
        }

        if (e == GameEnum.BLACK_CHECKER) {
            return BlackChecker();
        }

        if (e == GameEnum.BLACK_CHECKER_H) {
            return BlackCheckerHighlighted();
        }

        if (e == GameEnum.BLACK_CHECKER_S) {
            return BlackSuperChecker();
        }

        if (e == GameEnum.BLACK_CHECKER_S_H) {
            return BlackSuperCheckerHighlighted();
        }

        if (e == GameEnum.EMPTY_NEXT) {
            return NextMoveSquare();
        }
        return BlackSquare();
    }

    public Bitmap GenBlackScore(GameEngine ge) {
        Bitmap score = Bitmap.createBitmap(
                sideLength * 8, sideLength, Bitmap.Config.ARGB_8888);
        score.eraseColor(Color.BLACK);

        Canvas c = new Canvas(score);
        Paint scorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setStrokeWidth(2.0f);
        scorePaint.setColor(Color.YELLOW);

        for (int i = 0; i < 8; i++) {
            c.drawRect(
                    fHalfSide *0.5f + (i * fHalfSide ),
                    fHalfSide,
                    1f + (i * fHalfSide),
                    10f,
                    scorePaint);
        }

        return score;
    }

    public Bitmap GenRedScore(GameEngine ge) {
        Bitmap score = Bitmap.createBitmap(
                sideLength * 8, sideLength, Bitmap.Config.ARGB_8888);
        score.eraseColor(Color.LTGRAY);
        return score;
    }
}
