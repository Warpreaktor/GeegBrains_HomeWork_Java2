package HomeWork_06;

import HomeWork_06.ClientSide.ClientHandler;

import java.sql.*;
import java.util.LinkedList;

public class AuthService {
    //Переменная хранит подключение к базе
    private static Connection connection;
    //В переменной хранится состояние запроса к базе. По сути это SELECT
    private static Statement statement;

    public static void connect(){
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

    public static void addUser(String login, String pass, String nick) throws SQLException{
        String nickname = getNickByLoginAndPass(login, pass);
        if (nickname == null){
            try {
                String query = String.format("INSERT INTO users (login, password, nickname) VALUES ('%s', '%s', '%s');", login, pass, nick);
                PreparedStatement ps = connection.prepareStatement(query);
                //Не понимаю зачем здесь этот код. Возможно он и не нужен.
//            ps.setString(1, login);
//            ps.setInt(2, pass.hashCode());
//            ps.setString(3, nick);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (nickname != null){
            if (nickname.equals(login)){
                throw new SQLException("Пользователь с таким никнеймом уже зарегистрирован");
            }
        }

    }

    public static String getNickByLoginAndPass(String login, String pass) {
        try {
            String sqlRequest = String.format("SELECT nickname, password FROM users WHERE login = '%s';", login);
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            int myHash = pass.hashCode();
            if (resultSet.next()) {
                String nick = resultSet.getString(1);
                //int dbHash = rs.getInt(2);
               // if (myHash == dbHash) {
                return nick;
                //}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LinkedList<String> getClientBlackList(String listOwner){
        LinkedList<String> blacklist = new LinkedList<>();
        String sqlRequest = String.format("SELECT nickname_ban FROM blacklist WHERE login_owner = '%s';", listOwner);
        try {
            ResultSet resultSet = statement.executeQuery(sqlRequest);
            while (resultSet.next()){
                blacklist.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //TODO Возникает ошибка             java.sql.SQLException: ResultSet is closed
        return blacklist;
    }

    public static void addUserToBlackList(String nick){

    }
}
