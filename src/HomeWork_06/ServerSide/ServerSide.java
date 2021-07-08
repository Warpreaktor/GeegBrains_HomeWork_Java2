package HomeWork_06.ServerSide;

import HomeWork_06.AuthService;
import HomeWork_06.ClientSide.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            AuthService.conect();
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
    }

    public void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    //Сервис доставки сообщений клиентам
    public void broadcastMessage(String msg, String from) {
        if (msg.startsWith("@")) {
            if (msg.length() < 1 || msg.indexOf(32) == -1) {
                return;
            }
            String nick = msg.substring(1, msg.indexOf(32, 1));
            String personalMsg = msg.substring(msg.indexOf(32));
            if (personalMsg == null) return;
            for (ClientHandler itr : clients) {
                if (itr.getNick().equals(nick) || itr. getNick().equals(from)) {
                    itr.sendMessage(from + ": " + personalMsg);
                }
            }
            return;
        }
        for (ClientHandler itr : clients) {
            itr.sendMessage(msg);
        }
    }

    public boolean userIsSignIn(String userNick) {
        for (ClientHandler cl : clients) {
            if (cl.getNick().equals(userNick)) {
                return true;
            }
        }
        return false;
    }
}
