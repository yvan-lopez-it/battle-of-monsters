package co.fullstacklabs.problemsolving.challenge3;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge3 {

	public static int findLessCostPath(int[][] board) {

		int cols = board.length;
		int rows = board[0].length;

		int[][] tempCost = new int[board.length][board[0].length];
		tempCost [0][0] = board[0][0];

		for (int i = 1; i < cols; i++) {
			tempCost[i][0] = tempCost[i - 1][0] + board[i][0];
		}

		for (int j = 1; j < rows; j++) {
			tempCost[0][j] = tempCost[0][j - 1] + board[0][j];
		}

		for (int i = 1; i < cols; i++) {
			for (int j = 1; j < rows; j++) {
				int min = Math.min(tempCost[i - 1][j], tempCost[i][j - 1]);
				tempCost[i][j] = board[i][j] + min;
			}
		}

		return tempCost[cols - 1][rows - 1] - board[board.length - 1][board[0].length - 1];
	}

}
