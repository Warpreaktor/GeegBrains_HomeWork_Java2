package HomeWork_06.ClientSide.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatScreenController {
    //Окно авторизации
    @FXML
    VBox upperPanel;
    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button auth;
    @FXML
    Label textLabel;

    //Окно чата
    @FXML
    HBox bottomPanel;
    @FXML
    VBox vBox;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button sendButton;

    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    final String IP_ADDRESS = "localhost";
    final int PORT = 8044;
    private boolean isAuthorized;

    public void setAuthorized(boolean authorized) {
        this.isAuthorized = authorized;
        if (isAuthorized) {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            textArea.setVisible(true);
            textArea.setManaged(true);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            textArea.clear();
        } else {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        }
    }

    public ChatScreenController() {
        this.vBox = new VBox(2);
        Image sendImg = new Image("HomeWork_06/ClientSide/resources/send_button.png");
        sendButton = new Button("   ", new ImageView(sendImg));
    }

    public void sendMsg(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            outputStream.writeUTF(textField.getText() + "\n");
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Событие при нажатии на кнопку Sign in на экране авторизации
    public void tryAuth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            //Считываем введенные пользователем данные и отправляем их на обработку в ClientHandler
            //Эту строчку перехватывает метод connect в параллельном треде.
            outputStream.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Происходит перед попыткой пользователя авторизоваться
    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            setAuthorized(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Авторизация
                        while (true) {
                            String str = inputStream.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            }
                            if (str.startsWith("/authalready")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        textLabel.setText("User is already log in");
                                    }
                                });
                                textArea.appendText(str + "\n");
                            }
                        }

                        while (true) {
                            String message = inputStream.readUTF();
                            if (message.equals("/serverClosed")) {
                                outputStream.writeUTF("/end");
                                break;
                            }
                            textArea.appendText(message + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setAuthorized(false);
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
