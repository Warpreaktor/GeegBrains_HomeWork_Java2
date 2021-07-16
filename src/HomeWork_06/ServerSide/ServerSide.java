package HomeWork_06.ServerSide;

import HomeWork_06.AuthService;
import HomeWork_06.ClientSide.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class ServerSide {
    private Vector<ClientHandler> clients;

    ServerSocket server;
    Socket socket;
    DataInputStream input;
    DataOutputStream outPut;

    public ServerSide(int port) {
        this.clients = new Vector<>();
        try {
            AuthService.connect();
            this.server = new ServerSocket(port);
            System.out.println("Запущен новый сервер");

            while (true) {
                this.socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AuthService.disconnect();
        }
    }

    public void subscribe(ClientHandler client) {
        clients.add(client);
        //Загружаем входящему клиенту всех текущих пользователей чата
        for(ClientHandler cli: clients) {
            if (cli != client) {
                client.addContact(cli);
            }
        }
        //Загружаем всем пользователям чата вошедшего клиента
        for(ClientHandler cli: clients) {
            if (cli != client) {
                cli.addContact(client);
            }
        }
        client.setBlacklist(AuthService.getClientBlackList(client.getNick()));
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
        AuthService.updateBlackList(client);
    }

    //Сервис доставки сообщений клиентам
    public void broadcastMessage(String msg, String fromNick) {
        if (msg.startsWith("@")) {
            if (msg.length() < 1 || msg.indexOf(32) == -1) {
                return;
            }
            String toNick = msg.substring(1, msg.indexOf(32, 1));
            if (isInBlackList(toNick, fromNick)) return;
            String personalMsg = msg.substring(msg.indexOf(32));
            if (personalMsg == null) return;
            for (ClientHandler itr : clients) {
                if (itr.getNick().equals(toNick) || itr. getNick().equals(fromNick)) {
                    itr.sendMessage(fromNick + ": " + personalMsg);
                }
            }
            return;
        }
        for (ClientHandler client : clients) {
            if (isInBlackList(client.getNick(), fromNick)) {
                continue;
            }
            if (client.getNick().equals(fromNick)) {
                client.sendMessage("#mymessage " + msg);
            }else {
                System.out.println("send = " + msg);
                client.sendMessage(fromNick + ": " + msg);
            }
        }
    }

    //Проверка пользователя черным списком.
    private boolean isInBlackList(String toNick, String fromNick){
        //TODO Слабое место. При большом объеме подключенных клиентов, выборка может быть не быстрой,
        // но думаю, что этот код выдержит тысячу другую пользователей при их черных списках в сотню записей у каждого.
        // Еще можно эту часть вынести на клиента и тогда мы будем фильтровать только все входящие по отправителю.
        for(ClientHandler client: clients) {
            if (client.getNick().equals(toNick)) {
                for(String blackNames: client.getBlacklist()){
                    if (blackNames.equals(fromNick)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean userIsLogged(String userNick) {
        for (ClientHandler cl : clients) {
            if (cl.getNick().equals(userNick)) {
                return true;
            }
        }
        return false;
    }

}
