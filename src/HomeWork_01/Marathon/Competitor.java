package HomeWork_01.Marathon;

import HomeWork_01.Obstacles.CooperPipe;

public interface Competitor {
    void run(int dist);
    void swim(int dist);
    void jump(int height);
    void crawl(CooperPipe cooperPipe);
    boolean isOnDistance();
    void info();
}
