import entity.Message;
import entity.Point;
import entity.ResultShot;

import java.io.*;
import java.net.*;
import java.util.Objects;

import static entity.Globals.safeReceive;

public class Client {

    private final String host;
    private final int port;
    private final Game game;
    private String lastSentMessage;
    private String lastGetMessage;


    public Client(String host, int port, Game game) {
        this.host = host;
        this.port = port;
        this.game = game;
    }

    public void run() {

        System.out.println("Connecting to " + host + ":" + port);

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(1000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            String message = "start;A1\n";
            int cnt = 0;
            Point myShot = new Point(0, 0);
            out.write(message);
            lastSentMessage = message;
            out.flush();
            System.out.println("Sent to server: " + message);
            while (cnt < 100 && !game.isFinished()) {
                Message gotten = safeReceive(in, out, lastSentMessage);
                String response = gotten.toString();
                if (Objects.equals(response, lastGetMessage)) {
                    System.out.println("Received from server again: " + response);
                    continue;
                } else {
                    lastGetMessage = response;
                    System.out.println("Received from server: " + response);
                }
                game.saveShotResult(gotten.getCommand(), myShot);
                if (gotten.getCommand() != ResultShot.LAST_KILLED) {
                    ResultShot resultShot = game.takeShot(gotten.getCoordinate());
                    myShot = game.calcShot();
                    Message answer = new Message(resultShot, myShot);
                    out.write(answer.toString());
                    lastSentMessage = answer.toString();
                    out.flush();
                    System.out.println("Sent to server: " + answer);
                }
                if (cnt % 12 == 0) {
                    Thread.sleep(2500);
                }
                cnt++;
            }
            if (game.isWin()) {
                System.out.println("Wygrana\n");
            } else {
                System.out.println("Przegrana\n");
            }
            game.getEnemyBoard().print();
            System.out.println();
            game.getBoard().print();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
