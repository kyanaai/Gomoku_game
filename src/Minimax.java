//   #  |  Full Name	        | ID
//   ---|-----------------------|---------
//   1  |  Kiana Nezafat Yazdi	| 101488042
//   2  |  Karina Vetlugina	    | 101501883
//   3  |  Max Tran	            | 101490785
//   4  |  Arkadii Akopian	    | 101513972

public class Minimax {

    public int[] findBestMove(Board board, Player ai, Player player) {

        int bestX, bestY;
        bestX = bestY = -1;
        int depth = 6;
        int final_depth = Math.min(board.movesLeft, depth);

        if (board.movesLeft == board.col * board.row
                || (board.movesLeft == board.col * board.row - 1
                && board.makeAIMove(board.col / 2, board.row / 2, ai.getSymbol()))) {
            bestX = (board.col / 2);
            bestY = (board.row / 2);
            board.undoAIMove(bestX, bestY, ai.getSymbol());
            return new int[]{bestX, bestY};
        }

        int[][] possibleMoves = MovesAnalyzer.getPossibleMoves(board, ai.getSymbol());
        int value = Integer.MIN_VALUE;

        for (int i = 0; i < possibleMoves.length; i++) {

            board.makeAIMove(possibleMoves[i][0], possibleMoves[i][1], ai.getSymbol());
            int score = minimax(board, final_depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false, ai, player,
                    ai);
            board.undoAIMove(possibleMoves[i][0], possibleMoves[i][1], ai.getSymbol());

            if (score > value) {
                value = score;
                bestX = possibleMoves[i][0];
                bestY = possibleMoves[i][1];
            }
        }

        return new int[]{bestX, bestY};
    }


    public int minimax(Board board, int depth, int alpha, int beta, boolean maximizer, Player ai, Player player, Player currentPlayer) {

        // base case
        if (depth == 0 || MovesAnalyzer.isAnyoneWin(board)) {
            return MovesAnalyzer.evaluateBoardState(board, currentPlayer.getSymbol());
        }
        // maximizer true
        int value;
        int[][] possibleMoves;
        if (maximizer) {
            value = Integer.MIN_VALUE;
            possibleMoves = MovesAnalyzer.getPossibleMoves(board, ai.getSymbol());

            for (int i = 0; i < possibleMoves.length; i++) {

                int x = possibleMoves[i][0];
                int y = possibleMoves[i][1];

                board.makeAIMove(x, y, ai.getSymbol());
                int temp = minimax(board, depth - 1, alpha, beta, false, ai, player, ai);
                board.undoAIMove(x, y, ai.getSymbol());

                value = Math.max(value, temp);
                alpha = Math.max(alpha, value);

                if (beta <= alpha) break;
            }

        }
        // maximizer false
        else {
            value = Integer.MAX_VALUE;
            possibleMoves = MovesAnalyzer.getPossibleMoves(board, player.getSymbol());

            for (int i = 0; i < possibleMoves.length; i++) {

                int x = possibleMoves[i][0];
                int y = possibleMoves[i][1];

                board.makeAIMove(x, y, player.getSymbol());
                int temp = minimax(board, depth - 1, alpha, beta, true, ai, player, player);
                board.undoAIMove(x, y, player.getSymbol());

                value = Math.min(value, temp);
                beta = Math.min(beta, value);

                if (beta <= alpha) break;
            }
        }
        return value;

    }

}