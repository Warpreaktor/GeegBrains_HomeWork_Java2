package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

public class Wall extends Obstacle {
    int height;

    public Wall(int height) {
        this.height = height;
        this.type = "Стена";

    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.jump(height);
    }

    @Override
    public void presentation() {
        System.out.println("Перед участниками стена высотой в " + height + " м.\n");
    }
}
