package HomeWork_06.ClientSide;

import HomeWork_06.AuthService;
import HomeWork_06.ServerSide.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

    DataInputStream inputStream;
    DataOutputStream outputStream;

    //Инициализация происходит после успешного подключения клиента к серверу
    public ClientHandler(ServerSide server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());

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
                                    if (server.userIsSignIn(currentNick)){
                                        sendMessage("/authalready");
                                        continue;
                                    }
                                    sendMessage("/authok");
                                    nick = currentNick;
                                    server.subscribe(ClientHandler.this);
                                    blacklist = AuthService.getClientBlackList(ClientHandler.this);
                                    break;
                                }else {
                                    sendMessage("Unknown login\\password");
                                }
                            }
                        }
                        //Цикл отвечающий за взаимодействие
                        while (true) {
                            String message = inputStream.readUTF();
                            if (finish(message)) {
                                outputStream.writeUTF("/serverClosed");
                                break;
                            }
                            //TODO Запись в базу данных
                            if (message.startsWith("/blacklist")) {
                                String[] tokens = message.split(" ");
                                blacklist.add(tokens[1]);
                                break;
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

    private boolean finish(String command) {
        if (command.equals("/end")) {
            return true;
        } else {
            return false;
        }
    }

    public String getNick() {
        return nick;
    }

    public LinkedList<String> getBlacklist() {
        return blacklist;
    }
}
