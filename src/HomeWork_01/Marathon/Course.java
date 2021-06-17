package HomeWork_01.Marathon;

import HomeWork_01.Obstacles.*;
import HomeWork_01.Obstacles.Base.Obstacle;

import java.util.Random;

public class Course {
    private Obstacle[] obstacles;

    public Course(int obstaclesCount) {
        Random random = new Random();
        obstacles = new Obstacle[random.nextInt(obstaclesCount)];
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i] = generateObstacle();
        }
    }

    public void justDoIt(){

    }

    private Obstacle generateObstacle(){
        Random random = new Random();
        //Генерируем трассу
        int rndObstacle = random.nextInt(5);
        switch (rndObstacle){
            case 0: return new CooperPipe(random.nextInt(3)+1);
            case 1: return new Cross(random.nextInt(1400)+100);
            case 2: return new Fire(random.nextInt(99)+1);
            case 3: return new Wall(random.nextInt(9)+1);
            case 4: return new Water(random.nextInt(400)+100);
        }
        return null;
    }
}
