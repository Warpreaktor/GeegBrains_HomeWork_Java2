package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

public class Water extends Obstacle {
    int length;

    public Water(int length) {
        this.length = length;
        this.type = "Река";

    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.swim(length);
    }

    @Override
    public void presentation() {
        System.out.println("Посмотрим как хорошо вы плаваете! Впереди река длинной в " + length + " м.");

    }
}
