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
package com.softwerry.checkers.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.MotionEvent;
import android.view.View;
import com.softwerry.checkers.GameEnum;
import com.softwerry.checkers.SimpleCheckersActivity;

/**
 * Test for Checkers Activity
 */
public class CheckersActivityTest extends ActivityInstrumentationTestCase2<SimpleCheckersActivity> {

    private SimpleCheckersActivity checkersActivity;

    public CheckersActivityTest() {
        super(SimpleCheckersActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        checkersActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @MediumTest
    public void testPreconditions() {
        assertNotNull("Game view is null.", checkersActivity.board);
    }

    @UiThreadTest
    public void testCheckerClick() {
        final View decorView = checkersActivity.getWindow().getDecorView();

        int clickRow = 2;
        int clickCol = 5;

        int xPos = getXlocForRow(clickRow);
        int yPos = getYlocForCol(clickCol);

        MotionEvent click = MotionEvent.obtain(
                1000, 1000, MotionEvent.ACTION_DOWN, xPos, yPos, 0);
        decorView.dispatchTouchEvent(click);

        assertSame("First checker is not selected.",
                GameEnum.RED_CHECKER_H,
                checkersActivity.board.gameEngine.getState()[clickRow][clickCol]);

        assertSame("Next move is not highlighted.",
                GameEnum.EMPTY_NEXT,
                checkersActivity.board.gameEngine.getState()[clickRow - 1][clickCol - 1]);
        assertSame("Next move is not highlighted.",
                GameEnum.EMPTY_NEXT,
                checkersActivity.board.gameEngine.getState()[clickRow + 1][clickCol - 1]);
        decorView.invalidate();
    }

    public int getXlocForRow(int row) {
        return checkersActivity.board.tx
                + checkersActivity.board.squareWidth * row
                + (int) (checkersActivity.board.squareWidth / 2.0);
    }

    public int getYlocForCol(int col) {
        return checkersActivity.board.ty
                + checkersActivity.board.squareWidth * col
                + (int) (checkersActivity.board.squareWidth / 2.0);
    }
}
