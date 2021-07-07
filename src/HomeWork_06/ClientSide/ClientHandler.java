package HomeWork_06.ClientSide;

import HomeWork_06.AuthService;
import HomeWork_06.ServerSide.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private ServerSide server;
    private Socket socket;
    private String nick;

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
                        //Цикл с аутентификацией
                        while (true) {
                            String str = inputStream.readUTF();
                            if (str.startsWith("/auth")) {
                                String[] tokens = str.split(" ");
                                String currentNick = AuthService.authentication(tokens[1], tokens[2]);
                                if (currentNick != null) {
                                    sendMessage("/authok");
                                    nick = currentNick;
                                    server.subscribe(ClientHandler.this);
                                    break;
                                }else {
                                    sendMessage("Unknown login\\password");
                                }
                            }
                        }
                        //Цикл отвечающий за взаимодействие
                        while (true) {
                            String message = inputStream.readUTF();
                            System.out.println(this + "--> " + message);
                            if (finish(message)) {
                                outputStream.writeUTF("/serverClosed");
                                break;
                            }
                            server.broadcastMessage(nick + " : " + message);
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
}
