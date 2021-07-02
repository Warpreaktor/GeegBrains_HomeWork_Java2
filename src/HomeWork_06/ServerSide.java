package HomeWork_06;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerSide {
    ServerSocket server;
    Socket socket;
    PrintWriter outPut;

    public ServerSide(int port) {
        try {
            this.server = new ServerSocket(port);
            System.out.println("Запущен новый сервер");

            this.socket = server.accept();
            System.out.println("Клиент подключился");

            outPut = new PrintWriter(socket.getOutputStream(), true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                server.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start(){
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (true){
                String message = scanner.nextLine();
                System.out.println("Client " + message);

                if (finish(message)) break;

                answer(message);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean finish(String command){
        if (command.equals("/end")){
            return true;
        }else {
            return false;
        }
    }

    private void answer(String request){
        outPut.println("---> " + request);
    }

    public static void main(String[] args) {
        ServerSide server = new ServerSide(8044);
        server.start();
    }
}
