import entity.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Game {

    private final Board board;
    private final EnemyBoard enemyBoard;
    private Random random = new Random();
    private boolean finished;
    private boolean win;

    public Point calcShot() {
        if (enemyBoard.getAreAvailable().isEmpty()) {
            return null;
        }
        Set<Point> set = enemyBoard.getAreAvailable();
        int randomInt = random.nextInt(set.size());
        int i = 0;
        for (Point point : set) {
            if(i == randomInt) {
                return point;
            }
            i++;
        }
        throw new IllegalStateException("Set is empty");
    }

    public void saveShotResult(ResultShot result, Point aim) {
        switch (result) {
            case MISS -> enemyBoard.miss(aim);
            case DAMAGED -> enemyBoard.damaged(aim);
            case KILLED -> enemyBoard.killed(aim);
            case LAST_KILLED -> {
                enemyBoard.killed(aim);
                finished = true;
                win = true;
                enemyBoard.clearAllWater();
            }
        }
    }

    public ResultShot takeShot(Point point) {
        ResultShot resultShot = board.shot(point);
        if (resultShot == ResultShot.LAST_KILLED) {
            finished = true;
        }
        return resultShot;
    }
}
