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

    public static void addUser(String login, String pass, String nick) {
        try {
            String query = "INSERT INTO users (login, password, nickname) VALUES ('%s', '%s', ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, login);
            ps.setInt(2, pass.hashCode());
            ps.setString(3, nick);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        try {
            ResultSet rs = statement.executeQuery("SELECT nickname, password FROM users WHERE login = '" + login + "'");
            int myHash = pass.hashCode();
            // 106438208
            if (rs.next()) {
                String nick = rs.getString(1);
                int dbHash = rs.getInt(2);
                if (myHash == dbHash) {
                    return nick;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
