package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

import java.util.Random;

public class CooperPipe extends Obstacle {
    private PipeAngle[] pipeAngles; //количество поворотов в трубе

    public CooperPipe(int angles) {
        pipeAngles = new PipeAngle[angles];
        for (int i = 0; i < pipeAngles.length; i++) {
            pipeAngles[i] = new PipeAngle();
        }
    }

    @Override
    public void doIt(Competitor competitor) {
        competitor.crawl(this);
    }

    private class PipeAngle {
        int anglesDegree;  //угол поворота

        public PipeAngle() {
            Random random = new Random();
            this.anglesDegree = 60 + random.nextInt(140);
        }
    }
}
