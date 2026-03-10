//   #  |  Full Name	        | ID
//   ---|-----------------------|---------
//   1  |  Kiana Nezafat Yazdi	| 101488042
//   2  |  Karina Vetlugina	    | 101501883
//   3  |  Max Tran	            | 101490785
//   4  |  Arkadii Akopian	    | 101513972

import java.util.Scanner;

public class Game {

    public Board board;
    private char WHITE = 'W';
    private char BLACK = 'B';
    private static Scanner scanner = new Scanner(System.in);
    private String AI_NAME = "AI";
    private Player currentPlayer;
    private Minimax minimax = new Minimax();
    private boolean isGameOver = false;

    public void app() {

        clearScreen();
        int mode;
        mainMenu();
        mode = validateUserInput(1, 2, "Enter your choice between 1 and 2:");

        clearScreen();
        if (mode == 1) { // Human vs Human

            clearScreen();
            printSplitter("-", 35);
            System.out.println("Game mode Human vs Human");
            printSplitter("-", 35);
            Player player1 = new Player();
            player1.setName(validateUserInput("Enter Player 1 name: "));
            clearScreen();
            player1.setSymbol(validateUserInput(WHITE, BLACK, "Choose color for Player 1 [W, B]"));
            Player player2 = new Player();
            clearScreen();
            player2.setName(validateUserInput("Enter Player 2 name: "));

            if (player1.getSymbol() == WHITE) {
                player2.setSymbol(BLACK);
                gameStart(mode, player2, player1);
            } else {
                player2.setSymbol(WHITE);
                gameStart(mode, player1, player2);
            }


        } else { // Human vs AI

            clearScreen();
            printSplitter("-", 35);
            System.out.println("Game mode Human vs AI");
            printSplitter("-", 35);
            Player player1 = new Player();
            player1.setName(validateUserInput("Enter Player 1 name: "));
            clearScreen();
            player1.setSymbol(validateUserInput(WHITE, BLACK, "Choose color for Player 1 [W, B]"));
            Player AI = new Player();
            clearScreen();
            AI.setName(AI_NAME);

            if (player1.getSymbol() == WHITE) {
                AI.setSymbol(BLACK);
                gameStart(mode, AI, player1);
            } else {
                AI.setSymbol(WHITE);
                gameStart(mode, player1, AI);
            }


        }

    }

    public void gameStart(int mode, Player player1, Player player2) {
        clearScreen();
        board = new Board();
        board.printBoard();
        isGameOver = false;
        if (mode == 1) { // Human VS Human
            currentPlayer = player1; // player 1 goes first by default
            while (board.movesLeft > 0 && !isGameOver) { // check that game is not over
                currentPlayer = makeMove(currentPlayer, player1, player2);
                clearScreen();
                board.printBoard();
            }
            renderFinalMessage();
        } else { //Human vs AI

            currentPlayer = player1;

            // Game loop for Human vs AI
            while (board.movesLeft > 0 && !isGameOver) { // check that game is not over
                if (!currentPlayer.getName().equals(AI_NAME)) { // Player's turn (human)
                    currentPlayer = makeMove(currentPlayer, player1, player2);
                } else { // AI's turn
                    currentPlayer = makeAIMove(currentPlayer, player1, player2);
                }
                clearScreen();
                board.printBoard();
            }
            renderFinalMessage();
        }

    }

    private Player makeAIMove(Player currentPlayer, Player player1, Player player2) {
        System.out.println("AI's turn (" + currentPlayer.getSymbol() + ")");

        int[] bestMove = minimax.findBestMove(board, currentPlayer, player1);
        if (board.makeAIMove(bestMove[0], bestMove[1], currentPlayer.getSymbol())) {
            if (MovesAnalyzer.checkWinningCondition(board, currentPlayer.getSymbol(), bestMove[0], bestMove[1])) {
                isGameOver = true;
                return currentPlayer;
            }
        } else {
            return currentPlayer;
        }

        // change turns
        return (currentPlayer == player1) ? player2 : player1;
    }


    private Player makeMove(Player currentPlayer, Player player1, Player player2) {
        System.out.println("Player - " + currentPlayer.getName() + "'s turn (" + currentPlayer.getSymbol() + ")");
        int col = validateUserInput(1, board.col, "Choose column number (1 - " + board.col + ") :");
        int row = validateUserInput(1, board.col, "Choose row number (1 - " + board.row + ") :");

        if (board.makeMove(col, row, currentPlayer.getSymbol())) {
            if (MovesAnalyzer.checkWinningCondition(board, currentPlayer.getSymbol(), col - 1, row - 1)) {
                // current player won
                isGameOver = true;
                return currentPlayer;
            }
        } else {
            printSplitter("-", 90);
            System.out.println("The cell at column " + col + " and row " + row + " is already occupied. Please choose a different cell.");
            printSplitter("-", 90);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return currentPlayer;
        }

        // change turns
        return currentPlayer == player1 ? player2 : player1;
    }

    private void renderFinalMessage() {
        if (board.movesLeft == 0 && !isGameOver) {
            // tie
            printSplitter("-", 35);
            System.out.println("The game has ended in a tie. No winner this time!");
            printSplitter("-", 35);
        } else {
            printSplitter("-", 35);
            System.out.println("Player " + currentPlayer.getName() + " has won!");
            printSplitter("-", 35);
        }
    }

    public void mainMenu() {
        printSplitter("-", 35);
        System.out.println("Welcome to the Gomoku game");
        System.out.println("1. Human VS Human");
        System.out.println("2. Human VS AI");
        printSplitter("-", 35);
    }

    private void printSplitter(String symbol, int numRepetition) {
        System.out.println(symbol.repeat(numRepetition));
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private int validateUserInput(int min, int max, String msg) {
        while (true) {
            try {
                System.out.println(msg);
                String input = scanner.nextLine();
                int res = Integer.parseInt(input);
                if (res >= min && res <= max) {
                    return res;
                }
                System.out.println("Invalid input! Please choose options between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please choose options between " + min + " and " + max);
            }
        }
    }

    private String validateUserInput(String msg) {
        System.out.println(msg);
        String input = scanner.nextLine();
        while (input.isEmpty()) {
            System.out.println(msg);
            input = scanner.nextLine();
        }
        return input;
    }

    private char validateUserInput(char W, char B, String msg) {
        System.out.println(msg);
        String input = scanner.nextLine();
        while (input.length() != 1 || (input.charAt(0) != W && input.charAt(0) != B)) {
            System.out.println("Invalid input! Please chose options " + W + " or " + B);
            System.out.println(msg);
            input = scanner.nextLine();
        }
        return input.charAt(0);
    }
}
