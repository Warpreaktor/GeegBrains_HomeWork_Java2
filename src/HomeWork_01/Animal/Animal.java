package HomeWork_01.Animal;

import HomeWork_01.Marathon.Competitor;

import java.util.Random;

public class Animal implements Competitor {
    private String type;
    private String name;
    int maxRunDistance;
    int maxJumpHeight;
    int maxSwimDistance;
    private final int SIZE;
    private int hitpoint;

    boolean onDistance;

    public Animal(String type, String name,
                  int maxRunDistance,
                  int maxJumpHeight,
                  int maxSwimDistance,
                  int size,
                  int hitpoint) {
        this.type = type;
        this.name = name;
        this.maxRunDistance = maxRunDistance;
        this.maxJumpHeight = maxJumpHeight;
        this.maxSwimDistance = maxSwimDistance;
        this.onDistance = true;
        this.SIZE = size;
        this.hitpoint = hitpoint;
    }

    @Override
    public void run(int dist) {
        if (!isOnDistance()){
            return;
        }

        if (dist <= maxRunDistance) {
            System.out.println(type + " " + name + " хорошо справился с кроссом");
        } else {
            System.out.println(type + " " + name + " пробежал " + maxRunDistance + " он измождён и больше не хочет жить");
            onDistance = false;
        }
    }

    @Override
    public void jump(int height) {
        if (!isOnDistance()){
            return;
        }

        if (height <= maxJumpHeight) {
            System.out.println(type + " " + name + " удачно перепрыгнул через стену");
        } else {
            System.out.println(type + " " + name + " прыгнул на " + maxJumpHeight + ", не смог перепрыгнуть стену и просто умер");
            onDistance = false;
        }
    }

    @Override
    public void crawl(int pipeWidth) {
        if (!isOnDistance()){
            return;
        }
        if (pipeWidth >= SIZE) {
            System.out.println(type + " " + name + " лезет по трубе шириной " + pipeWidth + " сантиметров и проползает");
        } else {
            System.out.println(type + " " + name + " застрял в трубе  шириной " + pipeWidth + " сантиметров и умер от голода и страха");
            onDistance = false;
        }
    }

    @Override
    public void swim(int dist) {
        if (!isOnDistance()){
            return;
        }
        if (dist <= maxSwimDistance) {
            System.out.println(type + " " + name + " отлично проплыл");
        } else {
            System.out.println(type + " " + name + " проплыл только " + maxSwimDistance + " метров и утонул");
            onDistance = false;
        }
    }

    @Override
    public void fire(int fireRadius) {
        Random random = new Random();
        while (fireRadius > 0) {
            int distance = random.nextInt(20)+1;
            fireRadius -=distance;
            if (fireRadius <= 0) {
                System.out.println(type + " " + name + " oтлично прошел огненную стену!");
            }else {
                hitpoint--;
                if (hitpoint <= 0) {
                    System.out.println(type + " " + name + " пробежал по лесу лишь " + fireRadius + " метров и сгорел");
                    onDistance = false;
                    return;
                }
            }
        }
    }

    @Override
    public boolean isOnDistance() {
        return onDistance;
    }

    @Override
    public void printInfo() {
        if (onDistance) {
            System.out.println(type + " " + name + " - " + "выжил");
        }else{
            System.out.println(type + " " + name + " - " + "не дошёл до конца");
        }
    }
}
