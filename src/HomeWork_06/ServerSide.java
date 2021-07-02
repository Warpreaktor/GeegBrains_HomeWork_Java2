package HomeWork_06;

import HomeWork_06.ClientSide.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
            this.server = new ServerSocket(port);
            System.out.println("Запущен новый сервер");

            while (true) {
                this.socket = server.accept();
                System.out.println("Клиент подключился");
                clients.add(new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String msg){
        for(ClientHandler itr: clients){
            itr.sendMessage(msg);
        }
    }
}
