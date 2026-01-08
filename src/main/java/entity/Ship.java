package entity;

import lombok.Data;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

@Data
public class Ship {

    private Set<Point> desks = new HashSet<>();
    private Set<Point> hitDesks = new HashSet<>();

    public void shoot(Point aim) {
        if (!desks.contains(aim)) {
            throw new ConcurrentModificationException("This ship doesn't have the desk.");
        }
        hitDesks.add(aim);
    }

    public boolean isKilled() {
        return hitDesks.size() == desks.size();
    }
}
