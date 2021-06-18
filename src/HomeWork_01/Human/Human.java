package HomeWork_01.Human;


import HomeWork_01.Marathon.Competitor;

import java.util.Random;

public class Human implements Competitor {
    String name;

    int maxRunDistance;
    int maxJumpHeight;
    int maxSwimDistance;
    private final int SIZE;
    private int hitpoint;

    boolean onDistance;

    @Override
    public boolean isOnDistance() {
        return onDistance;
    }

    public Human(String name) {
        Random random = new Random();
        this.name = name;
        this.maxRunDistance = 500 + random.nextInt(1000);
        this.maxJumpHeight = random.nextInt(3);
        this.maxSwimDistance = 50 + random.nextInt(500);
        this.SIZE = 100 + random.nextInt(100);
        this.hitpoint = 5 + random.nextInt(5);

        this.onDistance = true;
    }

    @Override
    public void run(int dist) {
        if (!isOnDistance()){
            return;
        }

        if (dist <= maxRunDistance) {
            System.out.println(name + " хорошо справился с кроссом");
        } else {
            System.out.println(name + " пробежал " + maxRunDistance + " он измождён и больше не хочет жить");
            onDistance = false;
        }
    }

    @Override
    public void jump(int height) {
        if (!isOnDistance()){
            return;
        }
        if (height <= maxJumpHeight) {
            System.out.println(name + " удачно перепрыгнул через стену");
        } else {
            System.out.println(name + " прыгнул на " + maxJumpHeight + ", не смог перепрыгнуть стену и просто умер");
            onDistance = false;
        }
    }

    @Override
    public void crawl(int pipeWidth) {
        if (!isOnDistance()){
            return;
        }
        if (pipeWidth >= SIZE) {
            System.out.println(name + " проползает в трубе шириной всего " + pipeWidth + " сантиметров");
        } else {
            System.out.println(name + " застрял в трубе шириной " + pipeWidth + " и умер от страха и голода");
            onDistance = false;
        }
    }

    @Override
    public void swim(int dist) {
        if (!isOnDistance()){
            return;
        }
        if (dist <= maxSwimDistance) {
            System.out.println(name + " отлично проплыл");
        } else {
            System.out.println(name + " проплыл только " + maxSwimDistance + " метров и утонул");
            onDistance = false;
        }
    }

    /**
     * Каждый цикл участник преодалевает некоторое расстояние сквозь огонь,
     * если огонь еще не закончился он получает урон. Если жизней не осталось, то всё, давай до свидания.
     */
    @Override
    public void fire(int fireRadius) {
        if (!isOnDistance()){
            return;
        }
        Random random = new Random();
        while (fireRadius > 0) {
            int distance = random.nextInt(20)+1;
            fireRadius -=distance;
            if (fireRadius <= 0) {
                System.out.println(name + " Отлично прошел огненную стену!");
            }else {
                hitpoint--;
                if (hitpoint <= 0) {
                    System.out.println(name + " пробежал по лесу лишь " + fireRadius + " метров и сгорел");
                    onDistance = false;
                    return;
                }
            }
        }
    }

    @Override
    public void printInfo() {
        if (onDistance) {
            System.out.println(name + " - " + " выжил");
        }else{
            System.out.println(name + " - " + " не дошёл до конца");
        }
    }
}