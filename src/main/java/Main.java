import entity.Arguments;
import entity.Board;
import entity.EnemyBoard;
import entity.Mode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.net.Authenticator.RequestorType.SERVER;

public class Main {



    public static void main(String[] args) {
        Arguments parsed = parseArgs(args);

        Board board = new Board(readBoard(parsed.mapFile()));
        System.out.println("=== Your board ===");
        board.print();

        switch (parsed.mode()) {
            case SERVER -> {
                Server server = new Server(parsed.port(), new Game(board, new EnemyBoard()));
                server.run();
            }
            case CLIENT -> {
                Client client = new Client(parsed.host(), parsed.port(), new Game(board, new EnemyBoard()));
                client.run();
            }
        }
    }

    private static String readBoard(String file) {
        try {
            return Files.readString(Path.of(file));
        } catch (IOException e) {
            throw new RuntimeException("Can not read: " + file, e);
        }
    }

    private static Arguments parseArgs(String[] args) {
        String mode = null;
        String host = null;
        String map = null;
        Integer port = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode" -> mode = args[++i];
                case "-host" -> host = args[++i];
                case "-map" -> map = args[++i];
                case "-port" -> port = Integer.parseInt(args[++i]);
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }

        if (mode == null || map == null || port == null)
            throw new IllegalArgumentException("Required args: -mode, -map, -port");

        Mode m = switch (mode) {
            case "server" -> Mode.SERVER;
            case "client" -> Mode.CLIENT;
            default -> throw new IllegalArgumentException("Invalid mode: " + mode);
        };

        if (m == Mode.CLIENT && host == null)
            throw new IllegalArgumentException("Client mode requires -host");

        return new Arguments(m, host, map, port);
    }
}
