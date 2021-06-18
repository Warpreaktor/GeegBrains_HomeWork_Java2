package HomeWork_01.Marathon;

import HomeWork_01.Obstacles.*;
import HomeWork_01.Obstacles.Base.Obstacle;

import java.util.Random;

public class Course {
    private Obstacle[] obstacles;
    private final int MAX_PIPE_ANGLES = 3;
    private final int MIN_PIPE_ANGLES = 1;
    private final int MAX_WALL_HEIGHT = 5;
    private final int MIN_WALL_HEIGHT = 1;

    public Course(int obstaclesCount) {
        Random random = new Random();
        obstacles = new Obstacle[random.nextInt(obstaclesCount) + 1];
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i] = generateObstacle();
        }
    }

    public void justDoIt(Team tm) {
        Competitor[] team = tm.getCompetitors();
        System.out.println("Полоса препятствий: ");
        for (Obstacle obst : obstacles){
            System.out.println(obst.type);
        }
        System.out.println();
        for (Obstacle obst : obstacles) {
            obst.presentation();
            for (Competitor teamMate : team) {
                obst.doIt(teamMate);
            }
            System.out.println();
            for (int i = 0; i < team.length; i++) {
                if (team[i].isOnDistance()) {
                    break;
                }
                if (i == team.length-1 && !team[i].isOnDistance()) {
                    System.out.println("Все умерли =( \n");
                    return;
                }
            }
        }
    }

    private Obstacle generateObstacle() {
        Random random = new Random();
        //Генерируем трассу
        int rndObstacle = random.nextInt(5);
        switch (rndObstacle) {
            case 0:
                return new CooperPipe(random.nextInt(MAX_PIPE_ANGLES) + MIN_PIPE_ANGLES);
            case 1:
                return new Cross(random.nextInt(1400) + 100);
            case 2:
                return new Fire(random.nextInt(99) + 1);
            case 3:
                return new Wall(random.nextInt(MAX_WALL_HEIGHT) + MIN_WALL_HEIGHT);
            case 4:
                return new Water(random.nextInt(400) + 100);
        }
        return null;
    }
}
