package HomeWork_06.ClientSide.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatScreenController {
    //Панель контактов
    @FXML
    VBox contactBar;

    //Окно авторизации
    @FXML
    VBox upperPanel;
    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button logIn;
    @FXML
    Button signUp;
    @FXML
    Label textLabel;

    //Окно чата
    @FXML
    ScrollPane chatPanel;
    @FXML
    VBox chatArea;
    @FXML
    BorderPane chatBorderPane;
    @FXML
    VBox chatWall;
    @FXML
    HBox bottomPanel;
    @FXML
    VBox mainVBox;
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

            chatPanel.setVisible(true);
            chatPanel.setManaged(true);
            chatArea.setVisible(true);
            chatArea.setManaged(true);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
        } else {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);

            chatPanel.setVisible(false);
            chatPanel.setManaged(false);
            chatPanel.setMinHeight(300);
            chatPanel.setMaxHeight(300);
            chatArea.setVisible(false);
            chatArea.setManaged(false);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
        }
    }

    public ChatScreenController() {
        this.mainVBox = new VBox(2);
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

    //Событие при нажатии на кнопку Log in на экране авторизации
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

    //Событие при нажатии на кнопку Sign up на экране авторизации
    public void tryAddUser(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            //TODO Сделать нормальную форму аутентификации, чтобы пользователь мог ввести и свой ник нейм
            //Повторяется логин для того чтобы в базу данных записать и ник который будет равен логину
            outputStream.writeUTF("#addUser " + loginField.getText() + " " + passwordField.getText() + " " + loginField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                HBox hBoxMsg = new HBox();
                hBoxMsg.setAlignment(Pos.CENTER_RIGHT);
                hBoxMsg.setMinWidth(250);
                Label label = new Label(message);
                hBoxMsg.getChildren().add(label);
                chatArea.getChildren().add(hBoxMsg);
            }
        });
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
                        //Экран авторизации
                        while (true) {
                            String str = inputStream.readUTF();
                            if (str.startsWith("/authok")) {
                                setAuthorized(true);
                                break;
                            }
                            if (str.startsWith("#registrOk")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        textLabel.setText("New user registered, please authorize");
                                    }
                                });
                            }
                            if (str.startsWith("#registrFail")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        textLabel.setText("This nickname has already registered");
                                    }
                                });
                            }
                            if (str.startsWith("/authalready")) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        textLabel.setText("User is already log in");
                                    }
                                });
                            }
                        }
                        //Экран чата
                        while (true) {
                            String message = inputStream.readUTF();
                            if (message.startsWith("#end")) {
                                outputStream.writeUTF("/end");
                                break;
                            }
                            if (message.startsWith("#newblackpeople")) {
                                String[] tokens = message.split(" ");
                                if (tokens[1] != null) {
                                    drawMessage("<kiki_bot>: " + "user with nickname \"" + tokens[1] + "\" added to blacklist");
                                }
                                continue;
                            }
                            if (message.startsWith("#youarenotblack")) {
                                drawMessage("<kiki_bot>: " + "this user can not be added to blacklist, " +
                                        "because it is you are");
                                continue;
                            }
                            if (message.startsWith("#useralrexblack")) {
                                String[] tokens = message.split(" ");
                                if (tokens[1] != null) {
                                    drawMessage("<kiki_bot>: " + "user with nickname \"" + tokens[1] + "\" already exist in your blacklist");
                                }
                                continue;
                            }
                            if (message.startsWith("#mymessage")) {
                                String[] tokens = message.split(" ", 2);
                                drawMessage(tokens[1]);
                                continue;
                            }
                            drawMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                            setAuthorized(false);
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
}
