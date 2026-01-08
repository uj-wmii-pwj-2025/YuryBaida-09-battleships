package entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Ship {

    private Set<Point> desks = new HashSet<>();
    private Set<Point> hitDesks = new HashSet<>();

    public void shoot(Point aim) {
        hitDesks.add(aim);
    }

    public boolean isKilled() {
        System.out.println("Desks:" + desks);
        System.out.println("Hited:" + hitDesks);
        return hitDesks.size() == desks.size();
    }
}
