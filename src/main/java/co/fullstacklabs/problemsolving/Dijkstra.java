package co.fullstacklabs.problemsolving;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Dijkstra {

    private static final int[][] board = new int[][]{
            { 42, 51, 22, 10, 0 },
            { 2, 50, 7, 6, 15 },
            { 4, 36, 8, 30, 20 },
            { 0, 40, 10, 100, 1 },
    };

    private static final int ROW = board.length;
    private static final int COL = board[0].length;
    private static final int[] dx = new int[]{-1, 0, 1, 0};
    private static final int[] dy = new int[]{0, 1, 0, -1};

    private static final int[][] dist = new int[ROW][COL];
    private static final boolean[][] visited = new boolean[ROW][COL];

    private static class Cell {
        int x, y, cost;

        public Cell(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }

    public static void main(String[] args) {
        dijkstra();
        System.out.println("The shortest path from [0][0] to the last item in the 2D array is: " + dist[ROW - 1][COL - 1]);
    }

    private static void dijkstra() {
        for (int i = 0; i < ROW; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[0][0] = board[0][0];

        PriorityQueue<Cell> pq = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        pq.offer(new Cell(0, 0, board[0][0]));

        while (!pq.isEmpty()) {
            Cell cell = pq.poll();

            int x = cell.x;
            int y = cell.y;
            int cost = cell.cost;

            if (x == ROW - 1 && y == COL - 1) {
                break;
            }

            if (visited[x][y]) {
                continue;
            }
            visited[x][y] = true;

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < ROW && newY >= 0 && newY < COL) {
                    int newCost = cost + board[newX][newY];
                    if (newCost < dist[newX][newY]) {
                        dist[newX][newY] = newCost;
                        pq.offer(new Cell(newX, newY, newCost));
                    }
                }
            }
        }
    }
}
