import entity.*;

import java.io.*;
import java.net.*;
import java.util.Objects;

import static entity.Globals.safeReceive;

public class Server {

    private final int port;
    private final Game game;
    private String lastSentMessage;
    private String lastGetMessage;


    public Server(int port, Game game) {
        this.port = port;
        this.game = game;
    }

    public void run() {

        System.out.println("Server listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {
            socket.setSoTimeout(1000);

            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            int cnt = 0;
            Point myShot = new Point(0, 0);

            while (cnt < 100 && !game.isFinished()) {
                Message gotten = safeReceive(in, out, lastSentMessage);
                String response = gotten.toString();
                if (Objects.equals(response, lastGetMessage)) {
                    continue;
                } else {
                    lastGetMessage = response;
                }
                System.out.println("Received from client: " + response);
                if (cnt != 0) {
                    game.saveShotResult(gotten.getCommand(), myShot);
                }
                if (gotten.getCommand() != ResultShot.LAST_KILLED) {
                    ResultShot resultShot = game.takeShot(gotten.getCoordinate());
                    myShot = game.calcShot();
                    Message answer = new Message(resultShot, myShot);
                    out.write(answer.toString());
                    lastSentMessage = answer.toString();
                    out.flush();
                    System.out.println("Sent to client: " + answer);
                }
                cnt++;
                if (cnt % 8 == 0) {
                    Thread.sleep(2000);
                }
            }
            if (game.isWin()) {
                System.out.println("Wygrana\n");
            } else {
                System.out.println("Przegrana\n");
            }
            game.getEnemyBoard().print();
            System.out.println();
            game.getBoard().print();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
