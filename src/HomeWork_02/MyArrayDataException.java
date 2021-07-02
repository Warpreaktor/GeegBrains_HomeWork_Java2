package HomeWork_02;

public class MyArrayDataException extends Exception{
    private static String message = "int не int. Ошибка в ячейках: ";
    public MyArrayDataException(int y, int x) {
        super(message + y + " " + x);
    }
}
