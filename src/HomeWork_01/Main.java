package HomeWork_01;

import HomeWork_01.Animal.Cat;
import HomeWork_01.Animal.Dog;
import HomeWork_01.Human.Human;
import HomeWork_01.Marathon.Course;
import HomeWork_01.Marathon.Team;

public class Main {
    public static void main(String[] args) {
        Course c = new Course(5); // Создаем полосу препятствий
        Team team =
                new Team("Невороятный успех",
                new Human("Матвей"),
                new Dog("Амиго"),
                new Cat("Буська")); // Создаем команду
        c.justDoIt(team); // Просим команду пройти полосу
        team.showResults(); // Показываем результаты
    }
}