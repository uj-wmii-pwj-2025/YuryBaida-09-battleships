package entity;

import lombok.Getter;

import java.util.*;

public class EnemyBoard {

    private char[][] grid;
    @Getter
    private final Set<Point> areAvailable = new HashSet<>();

    public EnemyBoard() {
        fillQuestions();
    }

    public void miss(Point aim) {
        grid[aim.getX()][aim.getY()] = '.';
        areAvailable.remove(aim);
    }

    public void damaged(Point aim) {
        grid[aim.getX()][aim.getY()] = '#';
        areAvailable.remove(aim);
    }

    public void killed(Point aim) {
        damaged(aim);
        Ship ship = findShip(aim);
        cleanWater(ship);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                sb.append(grid[r][c]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void print() {
        System.out.println(this);
    }

    private void fillQuestions() {
        grid = new char[10][10];
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                grid[r][c] = '?';
                areAvailable.add(new Point(r, c));
            }
        }
    }

    private Ship findShip(Point aim) {
        int[] deltaRow = {1, -1, 0, 0};
        int[] deltaCol = {0, 0, 1, -1};
        boolean[][] visited = new boolean[10][10];
        Deque<Point> stack = new ArrayDeque<>();
        Ship ship = new Ship();
        stack.push(aim);
        while (!stack.isEmpty()) {
            Point point = stack.pop();
            ship.getDesks().add(point);
            for (int k = 0; k < 4; k++) {
                int newRow = point.getX() + deltaRow[k];
                int newCol = point.getY() + deltaCol[k];
                if (newRow < 0 || newRow >= 10 || newCol < 0 || newCol >= 10) continue;
                if (grid[newRow][newCol] != '#') continue;
                if (visited[newRow][newCol]) continue;
                visited[newRow][newCol] = true;
                stack.push(new Point(newRow, newCol));
            }
        }
        return ship;
    }

    private void cleanWater(Ship ship) {
        int[] deltaRow = {1, -1, 0, 0, 1, -1, 1, -1};
        int[] deltaCol = {0, 0, 1, -1, 1, -1, -1, 1};
        for (Point point : ship.getDesks()) {
            for (int k = 0; k < 8; k++) {
                int newRow = point.getX() + deltaRow[k];
                int newCol = point.getY() + deltaCol[k];
                if (newRow < 0 || newRow >= 10 || newCol < 0 || newCol >= 10) continue;
                if (grid[newRow][newCol] == '?') {
                    grid[newRow][newCol] = '.';
                    areAvailable.remove(new Point(newRow, newCol));
                }
            }
        }
    }

    public void clearAllWater() {
        if (!areAvailable.isEmpty()) {
            for (Point point : areAvailable) {
                grid[point.getX()][point.getY()] = '.';
            }
        }
    }
}
