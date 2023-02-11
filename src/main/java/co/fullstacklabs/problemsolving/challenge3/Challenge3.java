package co.fullstacklabs.problemsolving.challenge3;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge3 {

    private int[][] board;
    private int[][] tempCost;

    public static int findLessCostPath(int[][] board) {

        int rows = board.length;
        int cols = board[0].length;

        Challenge3 challenge3 = new Challenge3();
        challenge3.setBoard(board);
        challenge3.setTempCost(new int[rows][cols]);
        challenge3.setTempCostFirstValue(challenge3.getBoard()[0][0]);

        int[][] tempCost = challenge3.getTempCost();

        for (int i = 1; i < rows; i++) {
            tempCost[i][0] = tempCost[i - 1][0] + board[i][0];
        }

        for (int j = 1; j < cols; j++) {
            tempCost[0][j] = tempCost[0][j - 1] + board[0][j];
        }

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                int min = Math.min(tempCost[i - 1][j], tempCost[i][j - 1]);
                tempCost[i][j] = board[i][j] + min;
            }
        }

        return tempCost[rows - 1][cols - 1] - board[board.length - 1][board[0].length - 1];
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getTempCost() {
        return tempCost;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setTempCost(int[][] tempCost) {
        this.tempCost = tempCost;
    }

    public void setTempCostFirstValue(int firstValue) {
        this.tempCost[0][0] = firstValue;
    }
}
