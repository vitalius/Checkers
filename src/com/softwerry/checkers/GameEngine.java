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

/**
 * Handles player input and checker game rules
 */
public final class GameEngine {

    /**
     * records current player info
     */
    public class CheckerPieces {

        public Sprite checkerPiece;
        public int row;
        public int col;
    }

    public Sprite[][] board;
    public int squaresPerSide = 8;
    public Sprite currentPlayer;
    public GameEnum gameState;

    public GameEngine() {
        StartNewGame(squaresPerSide);
        currentPlayer = Sprite.RED_CHECKER;
    }

    public GameEngine(int size) {
        StartNewGame(size);
        currentPlayer = Sprite.RED_CHECKER;
    }

    public GameEngine(Sprite firstPlayer, int size) {
        StartNewGame(size);
        currentPlayer = firstPlayer;
    }

    public GameEngine(Sprite firstPlayer) {
        StartNewGame(squaresPerSide);
        currentPlayer = firstPlayer;
    }

    public void StartNewGame(int size) {
        gameState = GameEnum.PLAY;
        squaresPerSide = size;
        board = GenEmptyBoardState();
        InitCheckers();
    }

    /**
     * Process click event
     *
     * @param row
     * @param col
     * @return
     */
    public boolean Click(int row, int col) {
        if (!isValid(row, col) || gameState != GameEnum.PLAY) {
            return false;
        }

        // clicked score square?
        if (At(row, col) == Sprite.SCORE) {
            CheckerPieces p = findPlayer();
            Set(p.row, p.col, Sprite.EMPTY);
            Set(p.row + (int) ((Math.round(row - p.row) / 2.0)),
                    p.col + (int) (Math.round((col - p.col) / 2.0)), Sprite.EMPTY);
            Set(row, col, p.checkerPiece);
            Upgrade(row, col);
            removeHints();
            currentPlayer = currentPlayer == Sprite.BLACK_CHECKER
                    ? Sprite.RED_CHECKER : Sprite.BLACK_CHECKER;
            return true;
        }

        // clicked highlighted square?
        if (At(row, col) == Sprite.EMPTY_NEXT) {
            CheckerPieces p = findPlayer();
            Set(p.row, p.col, Sprite.EMPTY);
            Set(row, col, p.checkerPiece);
            Upgrade(row, col);
            removeHints();
            currentPlayer = currentPlayer == Sprite.BLACK_CHECKER
                    ? Sprite.RED_CHECKER : Sprite.BLACK_CHECKER;
            return true;
        }

        // clicked a checker?
        if (At(row, col) == Sprite.BLACK_CHECKER
                && currentPlayer == Sprite.BLACK_CHECKER) {
            removeHints();
            Set(row, col, Sprite.BLACK_CHECKER_H);
            renderHints(row, col);
        } else if (At(row, col) == Sprite.BLACK_CHECKER_H) {
            Set(row, col, Sprite.BLACK_CHECKER);
            removeHints();
        } else if (At(row, col) == Sprite.RED_CHECKER
                && currentPlayer == Sprite.RED_CHECKER) {
            removeHints();
            Set(row, col, Sprite.RED_CHECKER_H);
            renderHints(row, col);
        } else if (At(row, col) == Sprite.RED_CHECKER_H) {
            Set(row, col, Sprite.RED_CHECKER);
            removeHints();
        } else if (At(row, col) == Sprite.RED_CHECKER_S
                && currentPlayer == Sprite.RED_CHECKER) {
            removeHints();
            Set(row, col, Sprite.RED_CHECKER_S_H);
            renderHints(row, col);
        } else if (At(row, col) == Sprite.RED_CHECKER_S_H) {
            Set(row, col, Sprite.RED_CHECKER_S);
            removeHints();
        } else if (At(row, col) == Sprite.BLACK_CHECKER_S
                && currentPlayer == Sprite.BLACK_CHECKER) {
            removeHints();
            Set(row, col, Sprite.BLACK_CHECKER_S_H);
            renderHints(row, col);
        } else if (At(row, col) == Sprite.BLACK_CHECKER_S_H) {
            Set(row, col, Sprite.BLACK_CHECKER_S);
            removeHints();
        }
        return false;
    }

    public boolean isValid(int row, int col) {
        return row > -1 && row < squaresPerSide && col > -1 && col < squaresPerSide;
    }

    /**
     * Returns square at a given
     *
     * @param row in the checker board
     * @param col in the checker board
     * @return Sprite
     */
    public Sprite At(int row, int col) {
        if (isValid(row, col)) {
            return board[row][col];
        } else {
            return Sprite.INVALID;
        }
    }

    public boolean Set(int row, int col, Sprite s) {
        if (isValid(row, col)) {
            board[row][col] = s;
            return true;
        }
        return false;
    }

    public boolean isEmpty(int row, int col) {
        return At(row, col) == Sprite.EMPTY;
    }

    private void Upgrade(int row, int col) {
        if ((At(row, col) == Sprite.BLACK_CHECKER
                || At(row, col) == Sprite.BLACK_CHECKER_H)
                && col == squaresPerSide - 1) {
            Set(row, col, Sprite.BLACK_CHECKER_S);
        }
        if ((At(row, col) == Sprite.RED_CHECKER
                || At(row, col) == Sprite.RED_CHECKER_H)
                && col == 0) {
            Set(row, col, Sprite.RED_CHECKER_S);
        }
    }

    private void renderHints(int row, int col) {
        // show potential moves for REGULAR CHECKER, here direction matters
        if (At(row, col) == Sprite.BLACK_CHECKER_H) {
            if (isEmpty(row + 1, col + 1)) {
                Set(row + 1, col + 1, Sprite.EMPTY_NEXT);
            }
            if (isEmpty(row - 1, col + 1)) {
                Set(row - 1, col + 1, Sprite.EMPTY_NEXT);
            }
        }

        if (At(row, col) == Sprite.RED_CHECKER_H) {
            if (isEmpty(row + 1, col - 1)) {
                Set(row + 1, col - 1, Sprite.EMPTY_NEXT);
            }
            if (isEmpty(row - 1, col - 1)) {
                Set(row - 1, col - 1, Sprite.EMPTY_NEXT);
            }
        }

        // show winning moves for REGULAR CHECKERS
        if (At(row, col) == Sprite.BLACK_CHECKER_H) {
            if (isEmpty(row + 2, col + 2) && (At(row + 1, col + 1) == Sprite.RED_CHECKER)
                    || At(row + 1, col + 1) == Sprite.RED_CHECKER_S) {
                Set(row + 2, col + 2, Sprite.SCORE);
            }
            if (isEmpty(row - 2, col + 2) && (At(row - 1, col + 1) == Sprite.RED_CHECKER
                    || At(row - 1, col + 1) == Sprite.RED_CHECKER_S)) {
                Set(row - 2, col + 2, Sprite.SCORE);
            }
        }

        if (At(row, col) == Sprite.RED_CHECKER_H) {
            if (isEmpty(row + 2, col - 2) && (At(row + 1, col - 1) == Sprite.BLACK_CHECKER
                    || At(row + 1, col - 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row + 2, col - 2, Sprite.SCORE);

            }
            if (isEmpty(row - 2, col - 2) && (At(row - 1, col - 1) == Sprite.BLACK_CHECKER
                    || At(row - 1, col - 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row - 2, col - 2, Sprite.SCORE);
            }
        }

        // show potential moves for SUPER CHECKER, direction doesn't matter
        if (At(row, col) == Sprite.RED_CHECKER_S_H
                || At(row, col) == Sprite.BLACK_CHECKER_S_H) {
            if (isEmpty(row + 1, col - 1)) {
                Set(row + 1, col - 1, Sprite.EMPTY_NEXT);
            }
            if (isEmpty(row - 1, col - 1)) {
                Set(row - 1, col - 1, Sprite.EMPTY_NEXT);
            }
            if (isEmpty(row + 1, col + 1)) {
                Set(row + 1, col + 1, Sprite.EMPTY_NEXT);
            }
            if (isEmpty(row - 1, col + 1)) {
                Set(row - 1, col + 1, Sprite.EMPTY_NEXT);
            }
        }

        // show winning moves for SUPER CHECKERS
        if (At(row, col) == Sprite.BLACK_CHECKER_S_H) {
            if (isEmpty(row + 2, col + 2) && (At(row + 1, col + 1) == Sprite.RED_CHECKER
                    || At(row + 1, col + 1) == Sprite.RED_CHECKER_S)) {
                Set(row + 2, col + 2, Sprite.SCORE);
            }
            if (isEmpty(row - 2, col + 2) && (At(row - 1, col + 1) == Sprite.RED_CHECKER
                    || At(row - 1, col + 1) == Sprite.RED_CHECKER_S)) {
                Set(row - 2, col + 2, Sprite.SCORE);
            }
            if (isEmpty(row + 2, col - 2) && (At(row + 1, col - 1) == Sprite.RED_CHECKER
                    || At(row + 1, col - 1) == Sprite.RED_CHECKER_S)) {
                Set(row + 2, col - 2, Sprite.SCORE);
            }
            if (isEmpty(row - 2, col - 2) && (At(row - 1, col - 1) == Sprite.RED_CHECKER
                    || At(row - 1, col - 1) == Sprite.RED_CHECKER_S)) {
                Set(row - 2, col - 2, Sprite.SCORE);
            }
        }

        if (At(row, col) == Sprite.RED_CHECKER_S_H) {
            if (isEmpty(row + 2, col + 2) && (At(row + 1, col + 1) == Sprite.BLACK_CHECKER
                    || At(row + 1, col + 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row + 2, col + 2, Sprite.SCORE);
            }
            if (isEmpty(row - 2, col + 2) && (At(row - 1, col + 1) == Sprite.BLACK_CHECKER
                    || At(row - 1, col + 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row - 2, col + 2, Sprite.SCORE);
            }
            if (isEmpty(row + 2, col - 2) && (At(row + 1, col - 1) == Sprite.BLACK_CHECKER
                    || At(row + 1, col - 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row + 2, col - 2, Sprite.SCORE);
            }
            if (isEmpty(row - 2, col - 2) && (At(row - 1, col - 1) == Sprite.BLACK_CHECKER
                    || At(row - 1, col - 1) == Sprite.BLACK_CHECKER_S)) {
                Set(row - 2, col - 2, Sprite.SCORE);
            }
        }
    }

    private void removeHints() {
        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                if ((row % 2 == 1 && col % 2 == 0) || (row % 2 != 1 && col % 2 != 0)) {
                    if (At(row, col) == Sprite.EMPTY_NEXT
                            || At(row, col) == Sprite.SCORE) {
                        Set(row, col, Sprite.EMPTY);
                    }
                    if (At(row, col) == Sprite.BLACK_CHECKER_H) {
                        Set(row, col, Sprite.BLACK_CHECKER);
                    }
                    if (At(row, col) == Sprite.RED_CHECKER_H) {
                        Set(row, col, Sprite.RED_CHECKER);
                    }
                    if (At(row, col) == Sprite.BLACK_CHECKER_S_H) {
                        Set(row, col, Sprite.BLACK_CHECKER_S);
                    }
                    if (At(row, col) == Sprite.RED_CHECKER_S_H) {
                        Set(row, col, Sprite.RED_CHECKER_S);
                    }
                }
            }
        }
    }

    private CheckerPieces findFirst(Sprite g) {
        CheckerPieces result = new CheckerPieces();

        result.checkerPiece = g;
        result.col = -1;
        result.row = -1;

        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                if (At(row, col) == g) {
                    result.col = col;
                    result.row = row;
                }
            }
        }
        return result;
    }

    private CheckerPieces findPlayer() {
        for (Sprite g : new Sprite[]{
            Sprite.BLACK_CHECKER_H,
            Sprite.BLACK_CHECKER_S_H,
            Sprite.RED_CHECKER_H,
            Sprite.RED_CHECKER_S_H}) {
            CheckerPieces p = findFirst(g);
            if (p.col != -1) {
                return p;
            }
        }
        return null;
    }

    /**
     * Build checker board pattern
     *
     * @return
     */
    public Sprite[][] GenEmptyBoardState() {
        Sprite[][] board = new Sprite[squaresPerSide][squaresPerSide];
        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                if ((row % 2 == 1 && col % 2 == 0) || (row % 2 != 1 && col % 2 != 0)) {
                    board[row][col] = Sprite.EMPTY;
                } else {
                    board[row][col] = Sprite.INVALID;
                }
            }
        }
        return board;
    }

    /**
     * Place checker pieces
     */
    public void InitCheckers() {
        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                if ((row % 2 == 1 && col % 2 == 0) || (row % 2 != 1 && col % 2 != 0)) {
                    if (row < 3) {
                        board[col][row] = Sprite.BLACK_CHECKER;
                    }
                    if (row > 4) {
                        board[col][row] = Sprite.RED_CHECKER;
                    }
                }
            }
        }
    }

    /**
     * Returns current game state
     *
     * @return
     */
    public Sprite[][] getState() {
        return board;
    }

    /**
     * Is given cell a Red checker
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isRed(int row, int col) {
        return At(row, col) == Sprite.RED_CHECKER
                || At(row, col) == Sprite.RED_CHECKER_H
                || At(row, col) == Sprite.RED_CHECKER_S
                || At(row, col) == Sprite.RED_CHECKER_S_H;
    }

    /**
     * Is given cell a Black checker
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isBlack(int row, int col) {
        return At(row, col) == Sprite.BLACK_CHECKER
                || At(row, col) == Sprite.BLACK_CHECKER_H
                || At(row, col) == Sprite.BLACK_CHECKER_S
                || At(row, col) == Sprite.BLACK_CHECKER_S_H;
    }

    /**
     * Returns side score
     *
     * @param side
     * @return
     */
    public int getScore(GameEnum side) {
        int score = 12;
        for (int row = 0; row < squaresPerSide; row++) {
            for (int col = 0; col < squaresPerSide; col++) {
                if (side == GameEnum.RED && isRed(row, col)) {
                    score--;
                } else if (side == GameEnum.BLACK && isBlack(row, col)) {
                    score--;
                }
            }
        }

        if (score > 11) {
            gameState = GameEnum.GAME_OVER;
        }

        return score;
    }
}
