import entity.Message;
import entity.Point;
import entity.ResultShot;

import java.io.*;
import java.net.*;

public class Client {

    private final String host;
    private final int port;
    private final Game game;

    public Client(String host, int port, Game game) {
        this.host = host;
        this.port = port;
        this.game = game;
    }

    public void run() {
        System.out.println("Connecting to " + host + ":" + port);

        try (Socket socket = new Socket(host, port)) {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            String msg = "start;A1\n";
            int cnt = 0;
            Point myShot = new Point(0, 0);
            while (cnt < 100 || !game.isFinished()) {
                out.write(msg);
                out.flush();
                System.out.println("Sent to server: " + msg);
                String response = in.readLine();
                System.out.println("Received from server: " + response);
                Message gotten = new Message(response);
                System.out.println("Received from server msg: " + gotten);
                game.saveShotResult(gotten.getCommand(), myShot);

                if (gotten.getCommand() != ResultShot.LAST_KILLED) {
                    ResultShot resultShot = game.takeShot(gotten.getCoordinate());
                    if (resultShot == ResultShot.LAST_KILLED) {
                        game.setFinished(true);
                        Message answer = new Message(resultShot, myShot);
                        out.write(answer.toString());
                        out.flush();
                        System.out.println("Sent to server: " + msg);
                    } else {
                        myShot = game.calcShot();
                        Message answer = new Message(resultShot, myShot);
                        System.out.println("Ready to server: " + answer);
                        msg = answer.toString();
                    }
                } else {
                    game.setFinished(true);
                    game.setWin(true);
                }
                if (cnt % 5 == 0) {
                    game.getBoard().print();
                }
                cnt++;
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
