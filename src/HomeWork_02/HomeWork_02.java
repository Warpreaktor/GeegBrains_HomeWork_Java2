package HomeWork_02;

public class HomeWork_02 {
    public static void main(String[] args) {
        String[][] testArray = new String[4][4];
        try {
            checkArray(testArray);
            System.out.println("Всё гуд, проходи.");
        }
        catch(MyArraySizeException e){
            e.printStackTrace();
        }
        catch (MyArrayDataException e){
            e.printStackTrace();
        }
    }

    /**
     * 1. Напишите метод, на вход которого подаётся двумерный строковый массив размером 4х4,
     * при подаче массива другого размера необходимо бросить исключение MyArraySizeException.
     */
    public static void checkArray(String[][] myArray) throws MyArraySizeException, MyArrayDataException{
        if (myArray.length != 4){
            throw new MyArraySizeException();
        }
        for (int i = 0; i < myArray.length; i++) {
            if (myArray[i].length != 4) throw new MyArraySizeException();
        }

        /**
         * 2. Далее метод должен пройтись по всем элементам массива, преобразовать в int, и просуммировать.
         * Если в каком-то элементе массива преобразование не удалось (например, в ячейке лежит символ или текст вместо числа),
         * должно быть брошено исключение MyArrayDataException,
         * с детализацией в какой именно ячейке лежат неверные данные.
         */

        for (int i = 0; i < myArray.length; i++) {
            for (int j = 0; j < myArray[i].length; j++) {
                try {
                    int num = Integer.parseInt(myArray[i][j]);
                }
                catch (NumberFormatException e){
                    throw new MyArrayDataException();
                }
            }
        }
    }
}
