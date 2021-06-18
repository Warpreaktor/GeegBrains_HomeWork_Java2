package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

public class Cross extends Obstacle {
    int dist;

    public Cross(int dist) {
        this.dist = dist;
        this.type = "Дорога";
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.run(dist);
    }

    @Override
    public void presentation() {
        System.out.println("Впереди дорога длинной в " + dist + " м.\n");
    }
}
