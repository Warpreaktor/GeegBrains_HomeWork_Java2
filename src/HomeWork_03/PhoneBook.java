package HomeWork_03;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 2. Написать простой класс ТелефонныйСправочник, который хранит в себе список фамилий и телефонных номеров.
 * В этот телефонный справочник с помощью метода add() можно добавлять записи. С помощью метода get() искать номер телефона по фамилии.
 * Следует учесть, что под одной фамилией может быть несколько телефонов, тогда при запросе такой фамилии должны выводиться все телефоны.
 */

public class PhoneBook {
    public static void main(String[] args) {
        //Тест
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Максим Евдокимов", "999-66-55");
        phoneBook.add("Алим Желябужский", "555-13-00");
        phoneBook.add("Демид Городенский", "8(952)931-7777");
        phoneBook.add("Евстигней Звенигородский", "(321)45-67-89");
        phoneBook.add("Северин Голосов", "(901)701-34-51");

        System.out.println(phoneBook.get("Евстигней Звенигородский"));

        phoneBook.add("Евстигней Звенигородский", "+7(907)405-74-51");

        System.out.println(phoneBook.get("Евстигней Звенигородский"));
    }

    HashMap <String, ArrayList<String>> phonesMap;

    public PhoneBook() {
        this.phonesMap = new HashMap<>();
    }

    public void add(String name, String phoneNumber){
        if (phonesMap.get(name) == null){
            phonesMap.put(name, new ArrayList<>());
            phonesMap.get(name).add(phoneNumber);
        } else{
            phonesMap.get(name).add(phoneNumber);
        }
    }

    public ArrayList get(String name){
        return phonesMap.get(name);
    }
}
