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

import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.MotionEvent;
import android.view.View;
import com.softwerry.checkers.CheckerBoardView;
import com.softwerry.checkers.GameEnum;
import com.softwerry.checkers.Sprite;
import com.softwerry.checkers.R;
import com.softwerry.checkers.SimpleCheckersActivity;

/**
 * Test for Checkers Activity
 */
public class CheckersActivityTest extends ActivityInstrumentationTestCase2<SimpleCheckersActivity> {

    private SimpleCheckersActivity checkersActivity;
    private CheckerBoardView checkerBoardView;

    public CheckersActivityTest() {
        super(SimpleCheckersActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        checkersActivity = getActivity();
        checkerBoardView = (CheckerBoardView) checkersActivity.findViewById(R.id.checkerView);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @MediumTest
    public void testPreconditions() {
        assertNotNull("Game view is null.", checkerBoardView);
    }

    /**
     * Test a simple click
     */
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
                Sprite.RED_CHECKER_H,
                checkerBoardView.gameEngine.getState()[clickRow][clickCol]);

        assertSame("Next move is not highlighted.",
                Sprite.EMPTY_NEXT,
                checkerBoardView.gameEngine.getState()[clickRow - 1][clickCol - 1]);
        assertSame("Next move is not highlighted.",
                Sprite.EMPTY_NEXT,
                checkerBoardView.gameEngine.getState()[clickRow + 1][clickCol - 1]);
        decorView.invalidate();
    }

    /**
     * Utility function for translating checker board row into x coordinate
     *
     * @param row
     * @return
     */
    public int getXlocForRow(int row) {
        return checkerBoardView.tx
                + checkerBoardView.squareWidth * row
                + (int) (checkerBoardView.squareWidth / 2.0);
    }

    /**
     * Utility function for translating checker board col into y coordinate
     *
     * @param col
     * @return
     */
    public int getYlocForCol(int col) {
        return checkerBoardView.ty
                + checkerBoardView.squareWidth * col
                + (int) (checkerBoardView.squareWidth / 2.0);
    }

    /**
     * Testing game score string visibility
     */
    @UiThreadTest
    public void testScoreStrings() {
        // default score
        assertTrue("Red score string invalid",
                SimpleCheckersActivity.redScore.getText().toString()
                .equals("Red score: 0"));

        assertTrue("Black score string invalid",
                SimpleCheckersActivity.blackScore.getText().toString()
                .equals("Black score: 0"));
    }

    /**
     * Testing end of game alert
     */
    @UiThreadTest
    public void testBlackWinAlert() {
        final View decorView = checkersActivity.getWindow().getDecorView();

        checkerBoardView.gameEngine.board
                = checkerBoardView.gameEngine.GenEmptyBoardState();
        checkerBoardView.gameEngine.currentPlayer = GameEnum.BLACK;
        checkerBoardView.gameEngine.board[3][4] = Sprite.BLACK_CHECKER;
        checkerBoardView.gameEngine.board[2][5] = Sprite.RED_CHECKER;

        decorView.dispatchTouchEvent(MotionEvent.obtain(
                1000, 1000, MotionEvent.ACTION_DOWN,
                getXlocForRow(3), getYlocForCol(4), 0));

        decorView.dispatchTouchEvent(MotionEvent.obtain(
                1000, 1000, MotionEvent.ACTION_DOWN,
                getXlocForRow(1), getYlocForCol(6), 0));

        AlertDialog gameOver = checkerBoardView.currentAlertDialog;
        assertNotNull("Alert is not shown.", gameOver);
    }

    /**
     * Testing end of game alert
     */
    @UiThreadTest
    public void testRedWinAlert() {
        final View decorView = checkersActivity.getWindow().getDecorView();

        checkerBoardView.gameEngine.board
                = checkerBoardView.gameEngine.GenEmptyBoardState();
        checkerBoardView.gameEngine.board[2][5] = Sprite.RED_CHECKER;
        checkerBoardView.gameEngine.board[3][4] = Sprite.BLACK_CHECKER;

        decorView.dispatchTouchEvent(MotionEvent.obtain(
                1000, 1000, MotionEvent.ACTION_DOWN,
                getXlocForRow(2), getYlocForCol(5), 0));

        decorView.dispatchTouchEvent(MotionEvent.obtain(
                1000, 1000, MotionEvent.ACTION_DOWN,
                getXlocForRow(4), getYlocForCol(3), 0));

        AlertDialog gameOver = checkerBoardView.currentAlertDialog;
        assertNotNull("Alert is not shown.", gameOver);
    }
}
