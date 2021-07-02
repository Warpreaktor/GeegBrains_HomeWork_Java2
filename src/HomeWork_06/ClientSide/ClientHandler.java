package HomeWork_06.ClientSide;

import HomeWork_06.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private ServerSide server;
    private Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

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
                                while (true){
                                    String message = inputStream.readUTF();
                                    System.out.println(this + "--> "  + message);
                                    if (finish(message)) break;
                                    server.broadcastMessage(message);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            finally {
                                try {
                                    socket.close();
                                    inputStream.close();
                                    outputStream.close();
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

    public void sendMessage(String message){
        try {
            outputStream.writeUTF(message);
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
}
