package HomeWork_02;

public class HomeWork_02 {
    public static void main(String[] args) {
        String[][] testArray = new String[4][5];
        try {
            checkArray(testArray);
            System.out.println("Всё гуд, проходи");
        }
        catch(MyArraySizeException e){
            e.printStackTrace();
        }
    }

    /**
     * 1. Напишите метод, на вход которого подаётся двумерный строковый массив размером 4х4,
     * при подаче массива другого размера необходимо бросить исключение MyArraySizeException.
     */
    public static void checkArray(String[][] myArray) throws MyArraySizeException{
        if (myArray.length != 4){
            throw new MyArraySizeException();
        }
        for (int i = 0; i < myArray.length; i++) {
            if (myArray[i].length != 4) throw new MyArraySizeException();
        }
    }
}
