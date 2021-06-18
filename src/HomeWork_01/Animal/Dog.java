package HomeWork_01.Animal;

import java.util.Random;

public class Dog extends Animal {
    public Dog(String name) {
        super("Пёс",
                name,
                new Random().nextInt(2500)+500,
                new Random().nextInt(1)+1,
                new Random().nextInt(500)+250,
                new Random().nextInt(80)+50,
                new Random().nextInt(3)+3);
    }
}
