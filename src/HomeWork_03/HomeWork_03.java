package HomeWork_03;

import java.util.HashMap;
import java.util.Random;

public class HomeWork_03 {
    private static String[] words = new String[]{
            "собака", "встреча", "лидер", "мгновение", "ряд", "долг",
            "поддержка", "артист", "вопрос", "монастырь", "определение",
            "признак", "закон", "знак", "корабль", "слуга", "невесомость"};

    public static void main(String[] args) {
        /**
         * 1. Создать массив с набором слов (10-20 слов, должны встречаться повторяющиеся).
         * Найти и вывести список уникальных слов, из которых состоит массив (дубликаты не считаем).
         * Посчитать сколько раз встречается каждое слово.
         */
        HomeWork_03 utils = new HomeWork_03();
        HashMap<String, Integer> uniqueWords = new HashMap();

        String[] words = new String[40];
        for (int i = 0; i < words.length; i++) {
            words[i] = utils.getRandomWord();
            Integer count = uniqueWords.get(words[i]);
            uniqueWords.put(words[i], count == null ? 1 : count +1);
        }
        System.out.println(uniqueWords);
    }

        private String getRandomWord(){
            Random random = new Random();
            return words[random.nextInt(words.length)];
        }
}
