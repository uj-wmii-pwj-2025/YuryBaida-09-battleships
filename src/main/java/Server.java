import entity.*;

import java.io.*;
import java.net.*;

public class Server {

    private final int port;
    private final Game game;

    public Server(int port, Game game) {
        this.port = port;
        this.game = game;
    }

    public void run() {
        System.out.println("Server listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept()) {

            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            int cnt = 0;
            Point myShot = new Point(0, 0);
            while (cnt < 100 || !game.isFinished()) {

                String response = in.readLine();
                System.out.println("Received from client: " + response);
                Message gotten = new Message(response);
                System.out.println("Received from client msg: " + gotten);

                if (cnt != 0) {
                    game.saveShotResult(gotten.getCommand(), myShot);
                }
                if (gotten.getCommand() != ResultShot.LAST_KILLED) {
                    ResultShot resultShot = game.takeShot(gotten.getCoordinate());
                    myShot = game.calcShot();
                    Message answer = new Message(resultShot, myShot);
                    response = answer.toString();
                    out.write(response);
                    out.flush();
                    System.out.println("Sent to client: " + response);
                } else {
                    game.setFinished(true);
                    game.setWin(true);
                }
                cnt++;
                if (cnt % 5 == 0) {
                    game.getBoard().print();
                }
            }
            if (game.isWin()) {
                System.out.println("Wygrana\n");
            } else {
                System.out.println("Przegrana\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
