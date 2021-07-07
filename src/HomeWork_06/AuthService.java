package HomeWork_06;

import java.sql.*;

public class AuthService {
    //Переменная хранит подключение к базе
    private static Connection connection;
    //В переменной хранится состояние запроса к базе. По сути это SELECT
    private static Statement statement;

    public static void conect(){
        try {
            //Инициализация драйвера JDBC
            Class.forName("org.sqlite.JDBC");
            //Устанавливаем соединение с базой
            connection = DriverManager.getConnection("jdbc:sqlite:src/HomeWork_06/database.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static String authentication(String login, String password){
        //Пишем запрос в бд
        String sqlRequest = String.format("SELECT nickname FROM users WHERE login = '%s' AND password = '%s'", login, password);
        try {
            //Получаем результат запроса и сохраняем его
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }



    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
