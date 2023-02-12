package co.fullstacklabs.problemsolving;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import lombok.Getter;

public class Challenge3Dijkstra {

  //moves up, right, down, left
  private static final int[] dx = new int[] { -1, 0, 1, 0 };
  private static final int[] dy = new int[] { 0, 1, 0, -1 };

  @Getter
  private static class Cell {

    int x, y, cost;

    public Cell(int x, int y, int cost) {
      this.x = x;
      this.y = y;
      this.cost = cost;
    }
  }

  /*
   * Dijkstra's code
   */
  public static Integer findLessCostPath(int[][] board) {
    int row = board.length;
    int col = board[0].length;

    int[][] distance = new int[row][col];
    boolean[][] visited = new boolean[row][col];

    // By filling the distance array with Integer.MAX_VALUE,
    // it represents that no path has yet been found to the current cell
    // and it provides a large value for comparison when a new path is discovered.
    Arrays.stream(distance).forEach(d -> Arrays.fill(d, Integer.MAX_VALUE));

    distance[0][0] = board[0][0];

    PriorityQueue<Cell> pq = new PriorityQueue<>(
      Comparator.comparingInt(Cell::getCost)
    );
    pq.offer(new Cell(0, 0, board[0][0]));

    while (!pq.isEmpty()) {
      Cell cell = pq.poll();

      int x = cell.x;
      int y = cell.y;
      int cost = cell.cost;

      if (x == row - 1 && y == col - 1) {
        break;
      }

      if (visited[x][y]) {
        continue;
      }
      visited[x][y] = true;

      // This loop is repeated 4 times to check all the possible directions
      IntStream
        .range(0, 4)
        .forEach(i -> {
          // new coordinates
          int newDistanceX = x + dx[i];
          int newDistanceY = y + dy[i];

          // Check if it's inside the board boundaries.
          if (
            newDistanceX >= 0 &&
            newDistanceX < row &&
            newDistanceY >= 0 &&
            newDistanceY < col
          ) {
            int newCost = cost + board[newDistanceX][newDistanceY];
            if (newCost < distance[newDistanceX][newDistanceY]) {
              distance[newDistanceX][newDistanceY] = newCost;
              pq.offer(new Cell(newDistanceX, newDistanceY, newCost));
            }
          }
        });
    }

    return distance[row - 1][col - 1] - board[row - 1][col - 1];
  }

  public static void main(String[] args) {
    int[][] board = new int[][] {
      { 42, 51, 22, 10, 0 },
      { 2, 50, 7, 6, 15 },
      { 4, 36, 8, 30, 20 },
      { 0, 40, 10, 100, 1 },
    };
    System.out.println("The shortest path is: " + findLessCostPath(board));
  }
}
