package entity;

import lombok.Data;

@Data
public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "P(" + x + "," + y + ')';
    }
}
