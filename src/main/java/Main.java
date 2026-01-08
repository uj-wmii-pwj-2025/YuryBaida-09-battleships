import entity.Arguments;
import entity.Board;
import entity.EnemyBoard;

public class Main {

    public static void main(String[] args) {

        Arguments parsed = new Arguments(args);
        Board board = new Board(parsed.getMapFile());
        System.out.println("=== My board ===");
        board.print();

        switch (parsed.getMode()) {
            case SERVER -> {
                Server server = new Server(parsed.getPort(), new Game(board, new EnemyBoard()));
                server.run();
            }
            case CLIENT -> {
                Client client = new Client(parsed.getHost(), parsed.getPort(), new Game(board, new EnemyBoard()));
                client.run();
            }
        }
    }
}
