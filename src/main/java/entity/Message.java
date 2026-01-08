package entity;

import lombok.Data;

@Data
public class Message {
    ResultShot command;
    Point coordinate;

    public Message(String msg) {
        msg = msg.trim();
        if (msg.equals("ostatni zatopiony")) {
            this.command = ResultShot.LAST_KILLED;
            this.coordinate = null;
            return;
        }

        String[] parts = msg.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid message format: " + msg);
        }

        String cmdPart = parts[0].trim();
        String cdtPart = parts[1].trim();

        switch (cmdPart) {
            case "start" -> this.command = ResultShot.START;
            case "pudło" -> this.command = ResultShot.MISS;
            case "trafiony" -> this.command = ResultShot.DAMAGED;
            case "trafiony zatopiony" -> this.command = ResultShot.KILLED;
            default -> throw new IllegalArgumentException("Unknown command: " + cmdPart);
        }

        if (cdtPart.isEmpty()) {
            this.coordinate = null;
        } else {
            char letter = cdtPart.charAt(0);
            int number = Integer.parseInt(cdtPart.substring(1));

            this.coordinate = new Point(letter - 'A', number - 1);
        }
    }

    public Message(ResultShot command, Point point) {
        this.command = command;
        this.coordinate = point;
    }

    @Override
    public String toString() {
        String cmdPart = "";
        String cdtPart = "";

        switch (command) {
            case START -> cmdPart = "start";
            case MISS -> cmdPart = "pudło";
            case DAMAGED -> cmdPart = "trafiony";
            case KILLED -> cmdPart = "trafiony zatopiony";
            case LAST_KILLED -> cmdPart = "ostatni zatopiony";
        }

        cdtPart = coordinate == null ? ""
                : Character.toString(coordinate.getX() + 'A') + (coordinate.getY() + 1);

        return cmdPart + ";" + cdtPart + "\n";
    }


}
