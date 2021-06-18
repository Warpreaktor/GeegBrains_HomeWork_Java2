package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

public class Fire extends Obstacle {
    int radius;

    public Fire(int length) {
        this.radius = length;
        this.type = "Огненный лес";

    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.fire(radius);
    }

    @Override
    public void presentation() {
        System.out.println("О нет! Горящий лес! Нужно пройти сквозь него. Радиус пожара " + radius + " м.\n");
    }
}
