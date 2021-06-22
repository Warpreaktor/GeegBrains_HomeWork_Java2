package HomeWork_02;

public class MyArraySizeException extends Exception{
       private static String message = "Массив не того размера. Давай до свидания!";

    public MyArraySizeException() {
        super(message);
    }
}
