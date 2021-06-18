package HomeWork_01.Obstacles.Base;

import HomeWork_01.Marathon.Competitor;

public abstract class Obstacle {
    public String type;
    public abstract void doIt(Competitor competitor);
    public abstract void presentation();
}