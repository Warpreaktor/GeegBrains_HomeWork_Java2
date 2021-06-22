package HomeWork_02;

public class MyArrayDataException extends Exception{
    private static String message = "int не int. Давай до свидания!";
    public MyArrayDataException() {
        super(message);
    }
}
