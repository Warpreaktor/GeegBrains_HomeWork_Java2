package HomeWork_06;

import HomeWork_06.ClientSide.ClientHandler;

import java.sql.*;
import java.util.LinkedList;

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
        //Направляем запрос в бд
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

    //TODO Все обращения в БД желательно вынести в отдельный сервис.
    public static LinkedList<String> getClientBlackList(ClientHandler client){
        LinkedList<String> blacklist = new LinkedList<>();
        String sqlrequest = String.format("SELECT nickname_ban FROM blacklist WHERE login_owner = '%s'", client);
        try {
            ResultSet resultSet = statement.executeQuery(sqlrequest);
            while (resultSet.next()){
                blacklist.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //TODO Возникает ошибка             java.sql.SQLException: ResultSet is closed
        System.out.println(blacklist.getFirst());
        return blacklist;
    }
}
