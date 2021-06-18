package HomeWork_01.Animal;

import java.util.Random;

public class Cat extends Animal {
    public Cat(String name) {
        super("Кот",
                name,
                new Random().nextInt(1500)+500,
                new Random().nextInt(2)+2,
                new Random().nextInt(150)+50,
                new Random().nextInt(30)+30,
                new Random().nextInt(1)+3);

    }
}