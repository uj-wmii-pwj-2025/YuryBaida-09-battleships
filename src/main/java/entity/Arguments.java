package entity;

import lombok.Data;

@Data
public class Arguments {

    private Mode mode;
    private String host;
    private String mapFile;
    private int port;

    public Arguments(String[] args) {
        parseArgs(args);
    }

    private void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode" -> mode = Mode.valueOf(args[++i].toUpperCase());
                case "-host" -> host = args[++i];
                case "-map" -> mapFile = args[++i];
                case "-port" -> port = Integer.parseInt(args[++i]);
                default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }

        if (mode == null || mapFile == null || port == 0)
            throw new IllegalArgumentException("Required args: -mode, -map, -port");

        if (mode == Mode.CLIENT && host == null)
            throw new IllegalArgumentException("Client mode requires -host");
    }
}
