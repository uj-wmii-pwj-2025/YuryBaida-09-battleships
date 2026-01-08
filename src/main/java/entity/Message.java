package entity;

import lombok.Data;

@Data
public class Message {

    ResultShot command;
    Point coordinate;

    public Message(String message) {
        parsMessage(message);
    }

    public Message(ResultShot command, Point point) {
        this.command = command;
        this.coordinate = command == ResultShot.LAST_KILLED ? null : point;
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

    private void parsMessage(String message) {
        message = message.trim();
        if (message.equals("ostatni zatopiony;")) {
            this.command = ResultShot.LAST_KILLED;
            this.coordinate = null;
            return;
        }
        String[] parts = message.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid message format: " + message);
        }

        String leftPart = parts[0].trim();
        String rightPart = parts[1].trim();

        switch (leftPart) {
            case "start" -> this.command = ResultShot.START;
            case "pudło" -> this.command = ResultShot.MISS;
            case "trafiony" -> this.command = ResultShot.DAMAGED;
            case "trafiony zatopiony" -> this.command = ResultShot.KILLED;
            default -> throw new IllegalArgumentException("Unknown command: " + leftPart);
        }

        if (rightPart.isEmpty()) {
            this.coordinate = null;
        } else {
            char letter = rightPart.charAt(0);
            int number = Integer.parseInt(rightPart.substring(1));
            this.coordinate = new Point(letter - 'A', number - 1);
        }
    }
}
