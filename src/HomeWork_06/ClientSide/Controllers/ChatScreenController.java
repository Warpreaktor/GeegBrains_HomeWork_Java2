package HomeWork_06.ClientSide.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatScreenController implements Initializable {
    @FXML
    HBox hBox;
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

    public ChatScreenController(){
        this.vBox = new VBox(2);
        Image sendImg = new Image("HomeWork_06/ClientSide/resources/send_button.png");
        sendButton = new Button("   ", new ImageView(sendImg));
    }

    public void sendMsg(ActionEvent actionEvent) {
        textArea.appendText(textField.getText() + "\n");
        textField.clear();
        textField.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
