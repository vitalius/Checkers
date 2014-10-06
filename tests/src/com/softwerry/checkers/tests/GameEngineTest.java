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

import com.softwerry.checkers.GameEngine;
import com.softwerry.checkers.Sprite;
import junit.framework.TestCase;

/**
 * Test for checkers game engine and game moves
 */
public class GameEngineTest extends TestCase {

    /**
     * Verify simple move of a black checker
     */
    public void testBlackCheckerSimpleMoves() {
        GameEngine checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 2, Sprite.BLACK_CHECKER);

        checkers.Click(1, 2);
        assertSame(Sprite.BLACK_CHECKER_H, checkers.At(1, 2));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(0, 3));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(2, 3));
        assertSame(Sprite.EMPTY, checkers.At(0, 1));
        assertSame(Sprite.EMPTY, checkers.At(2, 1));

        checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 2, Sprite.BLACK_CHECKER);
        checkers.Set(0, 3, Sprite.BLACK_CHECKER);

        checkers.Click(1, 2);
        assertSame(Sprite.BLACK_CHECKER_H, checkers.At(1, 2));
        assertSame(Sprite.BLACK_CHECKER, checkers.At(0, 3));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(2, 3));

        // no moves available
        checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 2, Sprite.BLACK_CHECKER);
        checkers.Set(0, 3, Sprite.BLACK_CHECKER);
        checkers.Set(2, 3, Sprite.BLACK_CHECKER);

        checkers.Click(1, 2);
        assertSame(Sprite.BLACK_CHECKER_H, checkers.At(1, 2));
        assertSame(Sprite.BLACK_CHECKER, checkers.At(0, 3));
        assertSame(Sprite.BLACK_CHECKER, checkers.At(2, 3));

        // no moves backwards
        assertSame(Sprite.EMPTY, checkers.At(0, 1));
        assertSame(Sprite.EMPTY, checkers.At(2, 1));
    }

    /**
     * Verify simple move of a red checker
     */
    public void testRedCheckerSimpleMoves() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 6, Sprite.RED_CHECKER);

        // check for 2 highlighted moves
        checkers.Click(1, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(1, 6));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(0, 5));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(2, 5));
        assertSame(Sprite.EMPTY, checkers.At(0, 7));
        assertSame(Sprite.EMPTY, checkers.At(2, 7));

        checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 6, Sprite.RED_CHECKER);
        checkers.Set(0, 5, Sprite.RED_CHECKER);

        checkers.Click(1, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(1, 6));
        assertSame(Sprite.RED_CHECKER, checkers.At(0, 5));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(2, 5));

        // no moves available
        checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(1, 6, Sprite.RED_CHECKER);
        checkers.Set(0, 5, Sprite.RED_CHECKER);
        checkers.Set(2, 5, Sprite.RED_CHECKER);

        checkers.Click(1, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(1, 6));
        assertSame(Sprite.RED_CHECKER, checkers.At(0, 5));
        assertSame(Sprite.RED_CHECKER, checkers.At(2, 5));

        // no moves backwards
        assertSame(Sprite.EMPTY, checkers.At(0, 7));
        assertSame(Sprite.EMPTY, checkers.At(2, 7));
    }

    /**
     * Verify red checker jumping over black checker
     */
    public void testRedCheckerJumpMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER);

        checkers.Click(3, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(3, 6));
        assertSame(Sprite.SCORE, checkers.At(1, 4));
        assertSame(Sprite.BLACK_CHECKER, checkers.At(2, 5));

        checkers.Click(1, 4);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.RED_CHECKER, checkers.At(1, 4));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify king checker simple move 
     */
    public void testBlackSuperCheckerMove() {
        GameEngine checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(2, 3, Sprite.BLACK_CHECKER_S);

        checkers.Click(2, 3);
        assertSame(Sprite.BLACK_CHECKER_S_H, checkers.At(2, 3));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 2));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 2));

        checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(2, 3, Sprite.BLACK_CHECKER_S);
        checkers.Set(3, 4, Sprite.RED_CHECKER);

        checkers.Click(2, 3);
        assertSame(Sprite.BLACK_CHECKER_S_H, checkers.At(2, 3));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 4));
        assertSame(Sprite.RED_CHECKER, checkers.At(3, 4));
        assertSame(Sprite.SCORE, checkers.At(4, 5));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 2));
    }

    /**
     * Verify red king checker simple move
     */
    public void testRedSuperCheckerMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();

        checkers.Set(2, 5, Sprite.RED_CHECKER_S);

        checkers.Click(2, 5);
        assertSame(Sprite.RED_CHECKER_S_H, checkers.At(2, 5));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 6));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 6));
    }

    /**
     * Verify possible moves for a red king checker at the border
     */
    public void testRedSuperCheckerEdgeMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(2, 7, Sprite.RED_CHECKER_S);

        checkers.Click(2, 7);
        assertSame(Sprite.RED_CHECKER_S_H, checkers.At(2, 7));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 6));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 6));
    }

    /**
     * Verify red checker jumping black checker
     */
    public void testRedBeatsBlackMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER);

        checkers.Click(3, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(3, 6));
        assertSame(Sprite.SCORE, checkers.At(1, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(4, 5));

        checkers.Click(1, 4);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.RED_CHECKER, checkers.At(1, 4));
        assertSame(Sprite.EMPTY, checkers.At(4, 5));
    }

    /**
     * Verify black checker jumping red checker
     */
    public void testBlackBeatsRedMove() {
        GameEngine checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER);

        checkers.Click(2, 5);
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 6));
        assertSame(Sprite.SCORE, checkers.At(4, 7));
        assertSame(Sprite.BLACK_CHECKER_H, checkers.At(2, 5));

        checkers.Click(4, 7);
        assertSame(Sprite.EMPTY, checkers.At(1, 6));
        assertSame(Sprite.BLACK_CHECKER_S, checkers.At(4, 7));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify red king checker jumping black checker
     */
    public void testSuperRedBeatsBlackMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER_S);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER);

        checkers.Click(3, 6);
        assertSame(Sprite.RED_CHECKER_S_H, checkers.At(3, 6));
        assertSame(Sprite.SCORE, checkers.At(1, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(4, 5));

        checkers.Click(1, 4);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.RED_CHECKER_S, checkers.At(1, 4));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify king black checker jumping red checker
     */
    public void testSuperBlackBeatsRedMove() {
        GameEngine checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER_S);

        checkers.Click(2, 5);
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 6));
        assertSame(Sprite.SCORE, checkers.At(4, 7));
        assertSame(Sprite.BLACK_CHECKER_S_H, checkers.At(2, 5));

        checkers.Click(4, 7);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.BLACK_CHECKER_S, checkers.At(4, 7));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify king black checker jumping king red checker
     */
    public void testSuperBlackBeatsSuperRedMove() {
        GameEngine checkers = new GameEngine(Sprite.BLACK_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER_S);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER_S);

        checkers.Click(2, 5);
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 6));
        assertSame(Sprite.SCORE, checkers.At(4, 7));
        assertSame(Sprite.BLACK_CHECKER_S_H, checkers.At(2, 5));

        checkers.Click(4, 7);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.BLACK_CHECKER_S, checkers.At(4, 7));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify king red checker jumping king black checker
     */
    public void testSuperRedBeatsSuperBlackMove() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER_S);
        checkers.Set(2, 5, Sprite.BLACK_CHECKER_S);

        checkers.Click(3, 6);
        assertSame(Sprite.RED_CHECKER_S_H, checkers.At(3, 6));
        assertSame(Sprite.SCORE, checkers.At(1, 4));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(4, 5));

        checkers.Click(1, 4);
        assertSame(Sprite.EMPTY, checkers.At(3, 6));
        assertSame(Sprite.RED_CHECKER_S, checkers.At(1, 4));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
    }

    /**
     * Verify player taking turns. one side can't go twice in a row
     */
    public void testMoveTurns() {
        GameEngine checkers = new GameEngine(Sprite.RED_CHECKER);
        checkers.board = checkers.GenEmptyBoardState();
        checkers.Set(3, 6, Sprite.RED_CHECKER);
        checkers.Set(2, 1, Sprite.BLACK_CHECKER);

        // red - get next moves
        checkers.Click(3, 6);
        assertSame(Sprite.RED_CHECKER_H, checkers.At(3, 6));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(2, 5));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(4, 5));

        // red - moves
        checkers.Click(4, 5);
        assertSame(Sprite.RED_CHECKER, checkers.At(4, 5));
        assertSame(Sprite.EMPTY, checkers.At(2, 5));
        assertSame(Sprite.EMPTY, checkers.At(3, 6));

        // verify can't move again because it's next player turn
        checkers.Click(4, 5);
        assertSame(Sprite.EMPTY, checkers.At(3, 4));
        assertSame(Sprite.EMPTY, checkers.At(5, 4));

        checkers.Click(2, 1);
        assertSame(Sprite.EMPTY_NEXT, checkers.At(1, 2));
        assertSame(Sprite.EMPTY_NEXT, checkers.At(3, 2));
    }
}
