//   #  |  Full Name	        | ID
//   ---|-----------------------|---------
//   1  |  Kiana Nezafat Yazdi	| 101488042
//   2  |  Karina Vetlugina	    | 101501883
//   3  |  Max Tran	            | 101490785
//   4  |  Arkadii Akopian	    | 101513972

import java.util.Arrays;

public class Board {
    public char[][] board;
    public char EMPTY = '_';
    public int col;
    public int row;
    final private int BOARD_SIZE = 9;
    public int movesLeft;


    public Board() {
        this.col = BOARD_SIZE;
        this.row = BOARD_SIZE;
        movesLeft = col * row;
        this.board = buildBoard();
    }


    private char[][] buildBoard() {
        board = new char[row][col];

        for (int i = 0; i < row; i++) {
            Arrays.fill(board[i], EMPTY);
        }

        return board;
    }

    public void printBoard() {
        String title = "Board";
        int underscoreNum = (row + col + 4 - title.length() - 1) / 2;
        String header = "\n" + "_".repeat(underscoreNum) + title + "_".repeat(underscoreNum) + "\n";

        String boardDisplay = header;
        boardDisplay += " ".repeat(4);

        for (int i = 1; i < col + 1; i++) {
            boardDisplay += i + " ";
        }

        boardDisplay += "\n" + " ".repeat(4) + "_".repeat(col * 2 - 1) + "\n";

        for (int i = 0; i < row; i++) {

            boardDisplay += (i + 1) + " | ";
            for (int j = 0; j < col; j++) {
                boardDisplay += board[i][j] + " ";
            }

            boardDisplay += "\n";
        }

        boardDisplay += "_".repeat(header.length() - 1) + '\n';
        System.out.println(boardDisplay);
    }

    public boolean makeMove(int x, int y, char symbol) {
        x--;
        y--;

        if (x >= 0 && x < row && y >= 0 && y < col && board[y][x] == EMPTY) {
            board[y][x] = symbol;
            movesLeft--;
            return true;
        }

        return false;
    }


    public boolean makeAIMove(int x, int y, char symbol) {
        if (x >= 0 && x < row && y >= 0 && y < col && board[y][x] == EMPTY) {
            board[y][x] = symbol;
            movesLeft--;
            return true;
        }

        return false;
    }

    public boolean undoAIMove(int x, int y, char symbol) {
        if (x >= 0 && x < row && y >= 0 && y < col && board[y][x] == symbol) {
            board[y][x] = EMPTY;
            movesLeft++;
            return true;
        }

        return false;
    }
}