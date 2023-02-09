package co.fullstacklabs.problemsolving.challenge3;


import java.util.*;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge3 {

    public static int findLessCostPath(int[][] board) {

        int cols = board.length;
        int rows = board[0].length;

        int[][] temp = new int[cols][rows];
        temp[0][0] = board[0][0];

        for (int i = 1; i < cols; i++) {
            temp[i][0] = temp[i - 1][0] + board[i][0];
        }

        for (int j = 1; j < rows; j++) {
            temp[0][j] = temp[0][j - 1] + board[0][j];
        }

        for (int i = 1; i < cols; i++) {
            for (int j = 1; j < rows; j++) {
                int min = Math.min(temp[i - 1][j], temp[i][j - 1]);
                temp[i][j] = board[i][j] + min;
//                System.out.println("Cost position - (i:" + i + "; j:" + j + ") = " + board[i][j]);
//                System.out.println("Temp position - (i:" + i + "; j:" + j + ") = " + temp[i][j]);
//                System.out.println();
            }
        }

//        System.out.println(board[board.length - 1][board[0].length - 1]);

        return temp[cols - 1][rows - 1] - board[board.length - 1][board[0].length - 1];
    }

    public static void main(String args[]) {
        int[][] cost = {
                { 61, 86, 59, 80, 71, 70, 99, 55 },
                { 48, 49, 85, 9, 50, 93, 40, 0 },
                { 34, 61, 26, 32, 11, 18, 2, 1 },
                { 51, 76, 65, 91, 74, 39, 91, 77 },
                { 78, 96, 33, 49, 94, 75, 47, 29 },
                { 96, 55, 74, 39, 28, 88, 57, 4 },
                { 65, 13, 86, 95, 69, 88, 1, 88 },
                { 85, 7, 30, 74, 40, 78, 3, 75 },
        };
        int result = findLessCostPath(cost);
        System.out.println(result);
    }

}




