package HomeWork_06.ClientSide;

import HomeWork_06.AuthService;
import HomeWork_06.ServerSide.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Класс, по сути, отождествляется с подключённым к серверу клиенту.
 * Объект существует пока есть соединение.
 */
public class ClientHandler {
    private ServerSide server;
    private Socket socket;

    //Client information
    private String login;
    private String nick;
    private LinkedList<String> blacklist; //лист забаненых никнеймов
    private LinkedList<String> contactList; //лист контактов

    DataInputStream inputStream;
    DataOutputStream outputStream;

    //Инициализация происходит после успешного подключения клиента к серверу
    public ClientHandler(ServerSide server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.contactList = new LinkedList<>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Цикл с аутентификацией
                        while (true) {
                            //Читаем входящий поток на сокет
                            String str = inputStream.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                //Запрос на авторизацию, сверка логина\пароля с БД.
                                String currentNick = AuthService.authentication(tokens[1], tokens[2]);
                                if (currentNick != null) {
                                    if (server.userIsLogged(currentNick)){
                                        sendMessage("/authalready");
                                        continue;
                                    }
                                    nick = currentNick;
                                    sendMessage("#authok");
                                    server.subscribe(ClientHandler.this);
                                    break;
                                }else {
                                    sendMessage("Unknown login\\password");
                                }
                            }
                            //Запрос на регистрацию нового юзера.
                            if (str.startsWith("#addUser")) {
                                String[] tokens = str.split(" ");
                                String currentNick = tokens[1];
                                if (currentNick != null) {
                                    //TODO Здесь должно быть три токена логин, пароль и ник. Для того нужно подготовить
                                    // пользовательский интерфейс
                                    try {
                                        AuthService.addUser(tokens[1], tokens[2], tokens[1]);
                                        sendMessage("#registrOk");
                                    } catch (SQLException e) {
                                        sendMessage("#registrFail");
                                    }
                                }else {
                                    sendMessage("Unknown login\\password");
                                }
                            }
                        }

                        //Цикл отвечающий за взаимодействие
                        while (true) {
                            String message = inputStream.readUTF();
                            if (message.startsWith("#end")) {
                                outputStream.writeUTF("#end");
                                System.out.println("#serverClosed");
                                break;
                            }
                            if (message.startsWith("#black")) {
                                String[] tokens = message.split(" ");
                                if (tokens[1] != null) {
                                    if (tokens[1].replaceAll("\n", "").equals(nick)){
                                        outputStream.writeUTF("#youarenotblack");
                                    }else {
                                        addUserToLocalBlackList(tokens[1]);
                                    }
                                }
                            }
                            server.broadcastMessage(message, nick);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                            inputStream.close();
                            outputStream.close();
                            server.unsubscribe(ClientHandler.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    public LinkedList<String> getBlacklist() {
        LinkedList<String> copyBlacklist = new LinkedList<>();
        for(String str: blacklist){
            copyBlacklist.add(str);
        }
        return copyBlacklist;
    }

    public void setBlacklist(LinkedList<String> blacklist) {
        this.blacklist = blacklist;
    }

    private void addUserToLocalBlackList(String nick){
        for(String blackName : blacklist) {
            if (blackName.equals(nick)){
                sendMessage("#useralrexblack " + nick);
                return;
            }
        }
        try {
            outputStream.writeUTF("#newblackpeople " + nick);
            AuthService.updateBlackList(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        blacklist.add(nick);
    }

    public LinkedList<String> getContactList() {
        return contactList;
    }

    public void addContact(ClientHandler client){
        contactList.add(client.getNick());
        sendMessage("#addcontact " + client.getNick());
    }

    public void showNick(String nick){
        sendMessage("#showmynick " + nick);
    }
}
