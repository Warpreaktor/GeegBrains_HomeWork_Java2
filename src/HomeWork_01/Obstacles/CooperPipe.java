package HomeWork_01.Obstacles;

import HomeWork_01.Marathon.Competitor;
import HomeWork_01.Obstacles.Base.Obstacle;

import java.util.Random;

public class CooperPipe extends Obstacle {
    private PipeAngle[] pipeAngles; //количество изгибов в трубе

    public CooperPipe(int angles) {
        this.type = "Медные трубы";
        pipeAngles = new PipeAngle[angles];
        for (int i = 0; i < pipeAngles.length; i++) {
            pipeAngles[i] = new PipeAngle();
        }
    }

    @Override
    public void doIt(Competitor competitor) {
        for (PipeAngle pipeAngle : pipeAngles) {
            competitor.crawl(pipeAngle.pipeWidth);
        }
    }

    public class PipeAngle {
        int pipeWidth;  //Ширина трубы

        public PipeAngle() {
            Random random = new Random();
            this.pipeWidth = 60 + random.nextInt(140);
        }

    }

    @Override
    public void presentation() {
        System.out.println("Впереди " + type + " посмотрим что там внутри\n");
    }
}
