//   #  |  Full Name	        | ID
//   ---|-----------------------|---------
//   1  |  Kiana Nezafat Yazdi	| 101488042
//   2  |  Karina Vetlugina	    | 101501883
//   3  |  Max Tran	            | 101490785
//   4  |  Arkadii Akopian	    | 101513972

import java.util.Arrays;

public class MovesAnalyzer {
    public static int[][] offenseValueMove = {
            {2, 1, 1, 50},
            {2, 2, 0, 100},
            {3, 1, 1, 500},
            {3, 2, 0, 2000},
            {4, 1, 1, 10000},
            {4, 2, 0, 100000},
            {5, 0, 2, 1000000},
            {5, 1, 1, 1000000},
            {5, 2, 0, 1000000}
    };

    public static int[][] defenseValueMove = {
            {1, 2, 5},
            {1, 1, 10},
            {2, 2, 60},
            {2, 1, 120},
            {3, 2, 500},
            {3, 1, 2500},
            {4, 2, 500000},
            {4, 1, 500000}
    };

    public static int[][] weights = {
            {0, 0, 0}, // 1x0, 1x1, 1x2
            {1, 2, 3}, // 2x0, 2x1, 2x2
            {5, 10, 15}, // 3x0, 3x1, 3x2
            {20, 50, 100}, // 4x0, 4x1, 4x2
            {5000, 5000, 5000}  // 5x0, 5x1, 5x2
    };

    public static int[][] arrayWithDeltaValuesToFindHalfAdjacentCells = {
            {-1, -1},
            {0, -1},
            {1, -1},
            {1, 0}
    };

    public static int[][] moves;
    public static int numItems = 0;

    private static boolean moveIsWithinBorders(Board boardObject, int x, int y) {
        if (x < boardObject.row && x >= 0 && y < boardObject.col && y >= 0) {
            return true;
        }

        return false;
    }

    private static boolean moveIsValid(Board boardObject, int x, int y) {
        if (moveIsWithinBorders(boardObject, x, y) && boardObject.board[y][x] == boardObject.EMPTY) {
            return true;
        }

        return false;
    }

    public static int[][] getPossibleMoves(Board boardObject, char symbol) {

        moves = new int[boardObject.movesLeft][3];
        numItems = 0;
        for (int x = 0; x < boardObject.col; x++) {
            for (int y = 0; y < boardObject.row; y++) {

                if (moveIsValid(boardObject, x, y)) {
                    int score = evaluateMove(boardObject, symbol, x, y);

                    moves[numItems][0] = x;
                    moves[numItems][1] = y;
                    moves[numItems][2] = score;
                    numItems++;
                }

            }
        }

        // sort based on values
        sortPossibleMovesUsingInsertionSort(moves);

        moves = splitPossibleMovesBasedOnValue(moves);


        return moves;
    }

    public static int[][] splitPossibleMovesBasedOnValue(int[][] moves) {
        int[][] valArray = new int[0][3];
        int[][] arr = new int[0][3];
        int ind1, ind2;
        ind1 = ind2 = 0;
        for (int i = 0; i < moves.length; i++) {
            if (moves[i][2] > 0) {
                valArray = Arrays.copyOf(valArray, valArray.length + 1);
                valArray[ind1] = new int[3];
                valArray[ind1][0] = moves[i][0];
                valArray[ind1][1] = moves[i][1];
                valArray[ind1][2] = moves[i][2];
                ind1++;
            } else {
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[ind2] = new int[3];
                arr[ind2][0] = moves[i][0];
                arr[ind2][1] = moves[i][1];
                arr[ind2][2] = moves[i][2];
                ind2++;
            }
        }

        if (valArray.length != 0) {
            return valArray;
        }
        return arr;
    }

    public static void sortPossibleMovesUsingInsertionSort(int[][] moves) {
        for (int start = 1; start < moves.length; start++) {
            int[] temp = moves[start];
            int prev = start - 1;

            while (prev >= 0 && moves[prev][2] < temp[2]) {
                moves[prev + 1] = moves[prev];
                prev--;
            }

            moves[prev + 1] = temp;
        }
    }

    public static boolean checkWinningCondition(Board boardObject, char symbol, int x, int y) {
        for (int i = 0; i < arrayWithDeltaValuesToFindHalfAdjacentCells.length; i++) {
            int dx = arrayWithDeltaValuesToFindHalfAdjacentCells[i][0];
            int dy = arrayWithDeltaValuesToFindHalfAdjacentCells[i][1];

            int chainLength = 1;

            int resultHalf1 = countChainLengthForWinning(boardObject, symbol, x, y, dx, dy, 0);

            int resultHalf2 = countChainLengthForWinning(boardObject, symbol, x, y, dx * -1, dy * -1, 0);

            chainLength += resultHalf1 + resultHalf2;

            if (chainLength == 5) {
                return true;
            }
        }

        return false;
    }


    public static boolean isAnyoneWin(Board boardObject) {
        for (int i = 0; i < boardObject.col; i++) {
            for (int j = 0; j < boardObject.row; j++) {
                if (boardObject.board[i][j] != boardObject.EMPTY) {
                    if (checkWinningCondition(boardObject, boardObject.board[i][j], i, j)) return true;
                }
            }
        }
        return false;

    }

    private static int evaluateMove(Board boardObject, char symbol, int x, int y) {
        int totalValue = 0;

        totalValue += evaluateOffense(boardObject, symbol, x, y);
        totalValue += evaluateDefense(boardObject, symbol, x, y);

        return totalValue;
    }

    private static int evaluateOffense(Board boardObject, char symbol, int x, int y) {
        int value = 0;

        for (int i = 0; i < arrayWithDeltaValuesToFindHalfAdjacentCells.length; i++) {
            int dx = arrayWithDeltaValuesToFindHalfAdjacentCells[i][0];
            int dy = arrayWithDeltaValuesToFindHalfAdjacentCells[i][1];

            int chainLength = 1;
            int openEdges = 0;

            // check if this function returns -1, assign it to 0
            int[] resultHalf1 = countChainLengthAndEdgeStatusForMove(boardObject, symbol, x, y, dx, dy, 0);

            // Opposite direction
            int[] resultHalf2 = countChainLengthAndEdgeStatusForMove(boardObject, symbol, x, y, dx * -1, dy * -1, 0);

            chainLength += resultHalf1[0] + resultHalf2[0];
            openEdges = resultHalf1[1] + resultHalf2[1];
            int closedEdges = 2 - openEdges;

            if (chainLength == 0) {
                continue;
            }

            for (int j = 0; j < offenseValueMove.length; j++) {
                if (chainLength == offenseValueMove[j][0] && openEdges == offenseValueMove[j][1] &&
                        closedEdges == offenseValueMove[j][2]) {

                    value += offenseValueMove[j][3];
                    break;
                }
            }
        }

        return value;
    }

    private static int evaluateDefense(Board boardObject, char symbol, int x, int y) {
        int value = 0;
        symbol = symbol == 'B' ? 'W' : 'B';

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int chainLength = 0;
                int closedEdges = 2;

                int[] result = countChainLengthAndEdgeStatusForMove(boardObject, symbol, x, y, dx, dy, 0);
                closedEdges -= result[1];
                chainLength += result[0];

                if (chainLength == 0) {
                    continue;
                }

                for (int j = 0; j < defenseValueMove.length; j++) {
                    if (chainLength == defenseValueMove[j][0] && closedEdges == defenseValueMove[j][1]) {
                        value += defenseValueMove[j][2];
                        break;
                    }
                }
            }
        }

        return value;
    }

    private static int countChainLengthForWinning(Board boardObject, char symbol, int x, int y, int dx, int dy, int chainLength) {

        if (moveIsWithinBorders(boardObject, x + dx, y + dy) && boardObject.board[y + dy][x + dx] == symbol) {
            return countChainLengthForWinning(boardObject, symbol, x + dx, y + dy, dx, dy, chainLength + 1);
        }

        return chainLength;
    }

    // Returns an array [chain length, open edges]
    private static int[] countChainLengthAndEdgeStatusForMove(Board boardObject, char symbol, int x, int y, int dx, int dy, int chainLength) {

        if (moveIsWithinBorders(boardObject, x + dx, y + dy)) {
            if (boardObject.board[y + dy][x + dx] == boardObject.EMPTY) {
                return new int[]{chainLength, 1};
            }

            if (boardObject.board[y + dy][x + dx] == symbol) {
                return countChainLengthAndEdgeStatusForMove(boardObject, symbol, x + dx, y + dy, dx, dy, chainLength + 1);
            }

            // if (boardObject.board[y + dy][x + dx] == opponent's symbol)
            return new int[]{chainLength, 0};
        }

        return new int[]{chainLength, 0};

    }

    // Returns an array [chain length, open edges]
    private static int[] countChainLengthAndEdgeStatusForBoard(Board boardObject, char symbol, int x, int y, int dx, int dy, int chainLength) {

        if (moveIsWithinBorders(boardObject, x + dx, y + dy)) {


            if (boardObject.board[y + dy][x + dx] == boardObject.EMPTY) {
                return new int[]{chainLength, 1};
            }

            if (boardObject.board[y + dy][x + dx] == symbol) {

                return countChainLengthAndEdgeStatusForBoard(boardObject, symbol, x + dx, y + dy, dx, dy, chainLength + 1);
            }

            // if (boardObject.board[y + dy][x + dx] == opponent's symbol)
            return new int[]{chainLength, 0};
        }

        return new int[]{chainLength, 0};

    }

    public static int evaluateBoardState(Board boardObject, char symbol) {
        char opponentSymbol = symbol == 'B' ? 'W' : 'B';

        return (int) (evaluateSymbolBoardState(boardObject, symbol) - evaluateSymbolBoardState(boardObject, opponentSymbol) * 1.5);
    }

    // Formula: sum(WI)
    private static int evaluateSymbolBoardState(Board boardObject, char symbol) {
        int totalValue = 0;



        for (int x = 0; x < boardObject.col; x++) {
            for (int y = 0; y < boardObject.row; y++) {
                if (moveIsWithinBorders(boardObject, x, y) && boardObject.board[y][x] == symbol) {
                    for (int i = 0; i < arrayWithDeltaValuesToFindHalfAdjacentCells.length; i++) {
                        int dx = arrayWithDeltaValuesToFindHalfAdjacentCells[i][0];
                        int dy = arrayWithDeltaValuesToFindHalfAdjacentCells[i][1];


                        int chainLength = 1;
                        int openEdges = 0;

                        int[] resultHalf1 = countChainLengthAndEdgeStatusForBoard(boardObject, symbol, x, y, dx, dy, 0);
                        // Opposite direction
                        int[] resultHalf2 = countChainLengthAndEdgeStatusForBoard(boardObject, symbol, x, y, dx * -1, dy * -1, 0);

                        chainLength += resultHalf1[0] + resultHalf2[0];
                        openEdges = resultHalf1[1] + resultHalf2[1];

                        if (chainLength > 5) {
                            break;
                        }

                        // Formula
                        totalValue += weights[chainLength - 1][openEdges] * chainLength * (openEdges + 1);

                    }
                }
            }
        }

        return totalValue;
    }


}
