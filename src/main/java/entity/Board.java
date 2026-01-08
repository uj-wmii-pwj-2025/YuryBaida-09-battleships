    package entity;

    import lombok.Data;

    import java.util.*;

    @Data
    public class Board {

        private char[][] grid;
        private final Map<Point, Ship> shipsByPoints = new HashMap<>();
        private final List<Ship> ships = new LinkedList<>();
        private int killed;

        public Board(String map) {
            collectAllShips(map);
            for (Ship ship : ships) {
                System.out.println(ship.getDesks());
            }
        }

        public ResultShot shoot(Point aim) {
            if (!shipsByPoints.containsKey(aim)) {
                grid[aim.getX()][aim.getY()] = '~';
                return ResultShot.MISS;
            } else {
                grid[aim.getX()][aim.getY()] = '@';
                Ship damagedShip = shipsByPoints.get(aim);
                damagedShip.shoot(aim);
                if (damagedShip.isKilled()) {
                    killed++;
                    if (killed == Globals.TOTAL) {
                        return ResultShot.LAST_KILLED;
                    }
                    return ResultShot.KILLED;
                } else {
                    return ResultShot.DAMAGED;
                }
            }
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

        private void collectAllShips(String map) {
            grid = new char[10][10]; // Преобразуем строку в 10×10
            for (int i = 0; i < 100; i++) {
                grid[i / 10][i % 10] = map.charAt(i);
            }
            int[] deltaRow = {1, -1, 0, 0};
            int[] deltaCol = {0, 0, 1, -1};
            boolean[][] visited = new boolean[10][10];
            Map<Point, Ship> result = new HashMap<>();
            for (int r = 0; r < 10; r++) {
                for (int c = 0; c < 10; c++) {
                    if (grid[r][c] != '#' || visited[r][c]) continue;
                    Ship ship = new Ship();
                    Deque<Point> stack = new ArrayDeque<>();
                    stack.push(new Point(r, c));
                    visited[r][c] = true;
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
                    for (Point point : ship.getDesks()) {
                        shipsByPoints.put(point, ship);
                    }
                    ships.add(ship);
                }
            }
        }
    }
