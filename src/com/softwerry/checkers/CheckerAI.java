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

import java.util.LinkedList;
import java.util.Random;

/**
 * Checkers AI engine
 */
public final class CheckerAI {

    /**
     * Very dump AI, randomly picks a checker and randomly moves it
     * @param e current game which contains current game state
     * @param isRed flag for red or black side
     */
    public void RandomMove(GameEngine e, boolean isRed) {
        Random rn = new Random();

        // click random checker that can move
        LinkedList<int[]> playPieces = playablePieces(e, isRed);
        if (playPieces.size() < 1) {
            return;
        }

        int[] piece = playPieces.get(rn.nextInt(playPieces.size()));
        e.Click(piece[0], piece[1]);

        // click random next move
        LinkedList<int[]> nextSteps = nextStep(e);
        if (nextSteps.size() < 1) {
            return;
        }
        int[] next = nextSteps.get(rn.nextInt(nextSteps.size()));
        e.Click(next[0], next[1]);
    }

    /**
     * Returns all currently highlighted next possible steps on the board
     * @param e current game which contains current game state
     * @return 
     */
    public LinkedList<int[]> nextStep(GameEngine e) {
        LinkedList<int[]> result = new LinkedList<int[]>();
        for (int row = 0; row < e.squaresPerSide; row++) {
            for (int col = 0; col < e.squaresPerSide; col++) {
                if (e.At(row, col) == GameEnum.EMPTY_NEXT
                        || e.At(row, col) == GameEnum.SCORE) {
                    result.add(new int[]{row, col});
                }
            }
        }
        return result;
    }

    /**
     * Find all pieces that can move
     *
     * @param e current game which contains current game state
     * @param isRed flag for red or black side
     * @return
     */
    public LinkedList<int[]> playablePieces(GameEngine e, boolean isRed) {
        LinkedList<int[]> result = new LinkedList<int[]>();
        for (int row = 0; row < e.squaresPerSide; row++) {
            for (int col = 0; col < e.squaresPerSide; col++) {
                if (isRed && isRed(e, row, col)
                        && validRedMoves(e, row, col)) {
                    result.add(new int[]{row, col});
                }
                if (!isRed && isBlack(e, row, col)
                        && validBlackMoves(e, row, col)) {
                    result.add(new int[]{row, col});
                }
            }
        }
        return result;
    }

    /**
     * Return true if a black checker at row, col has possible moves
     *
     * @param e current game which contains current game state
     * @param row
     * @param col
     * @return
     */
    public boolean validBlackMoves(GameEngine e, int row, int col) {
        // check possible moves to empty square
        if (e.isEmpty(row - 1, col + 1)) {
            return true;
        }
        if (e.isEmpty(row + 1, col + 1)) {
            return true;
        }
        if (e.At(row, col) == GameEnum.BLACK_CHECKER_S) {
            if (e.isEmpty(row - 1, col - 1)) {
                return true;
            }
            if (e.isEmpty(row + 1, col - 1)) {
                return true;
            }
        }

        // check jump moves
        if (isRed(e, row - 1, col + 1) && e.isEmpty(row - 2, col + 2)) {
            return true;
        }
        if (isRed(e, row + 1, col + 1) && e.isEmpty(row + 2, col + 2)) {
            return true;
        }
        if (e.At(row, col) == GameEnum.BLACK_CHECKER_S) {
            if (isRed(e, row - 1, col - 1) && e.isEmpty(row - 2, col - 2)) {
                return true;
            }
            if (isRed(e, row + 1, col - 1) && e.isEmpty(row + 2, col - 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if a red checker at row, col has possible moves
     *
     * @param e current game which contains current game state
     * @param row
     * @param col
     * @return
     */
    public boolean validRedMoves(GameEngine e, int row, int col) {
        // check possible moves to empty square
        if (e.isEmpty(row - 1, col - 1)) {
            return true;
        }
        if (e.isEmpty(row + 1, col - 1)) {
            return true;
        }
        if (e.At(row, col) == GameEnum.RED_CHECKER_S) {
            if (e.isEmpty(row - 1, col + 1)) {
                return true;
            }
            if (e.isEmpty(row + 1, col + 1)) {
                return true;
            }
        }

        // check jump moves
        if (isBlack(e, row - 1, col - 1) && e.isEmpty(row - 2, col - 2)) {
            return true;
        }
        if (isBlack(e, row + 1, col - 1) && e.isEmpty(row + 2, col - 2)) {
            return true;
        }
        if (e.At(row, col) == GameEnum.RED_CHECKER_S) {
            if (isBlack(e, row - 1, col - 1) && e.isEmpty(row - 2, col - 2)) {
                return true;
            }
            if (isBlack(e, row + 1, col - 1) && e.isEmpty(row + 2, col - 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is given cell a Red checker
     *
     * @param e current game which contains current game state
     * @param row
     * @param col
     * @return
     */
    public boolean isRed(GameEngine e, int row, int col) {
        if (e.At(row, col) == GameEnum.RED_CHECKER
                || e.At(row, col) == GameEnum.RED_CHECKER_S) {
            return true;
        }
        return false;
    }

    /**
     * Is given cell a Black checker
     *
     * @param e current game which contains current game state
     * @param row
     * @param col
     * @return
     */
    public boolean isBlack(GameEngine e, int row, int col) {
        if (e.At(row, col) == GameEnum.BLACK_CHECKER
                || e.At(row, col) == GameEnum.BLACK_CHECKER_S) {
            return true;
        }
        return false;
    }
}
