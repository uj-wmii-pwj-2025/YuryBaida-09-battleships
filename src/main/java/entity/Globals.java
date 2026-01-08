package entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class Globals {

    public static final int TOTAL = 10;
    public static final int MAX_MISTAKES = 3;

    public static Message safeReceive(BufferedReader in, BufferedWriter out, String lastSent) throws IOException {
        int retry = 0;

        while (retry < MAX_MISTAKES) {
            try {
                String line = in.readLine();
                if (line == null) {
                    throw new IOException("null received");
                }
                Message ans = new Message(line);
                return ans;

            } catch (SocketTimeoutException e) {
                System.out.println("Timeout — ponownie wysyłam swoją ostatnią wiadomość");
            } catch (IllegalArgumentException e) {
                System.out.println("Nieznana komenda — ponownie wysyłam swoją ostatnią wiadomość");
            } catch (IOException e) {
                System.out.println("Błąd odczytu — ponownie wysyłam swoją ostatnią wiadomość");
            }
            out.write(lastSent);
            System.out.println("Sent again: " + lastSent);
            out.flush();
            retry++;
        }

        System.out.println("Błąd komunikacji");
        System.exit(1);
        return null;
    }

}
