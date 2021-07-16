package HomeWork_06.ClientSide;

import HomeWork_06.ClientSide.Controllers.ChatScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WChat extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("HomeWork_06/ClientSide/fxml/ChatScreen.fxml"));
        primaryStage.setTitle("kIkI");
        //ChatScreenController chatScreenController = new ChatScreenController();
        Scene scene = new Scene(root, 650, 700);
        primaryStage.setMinHeight(360);
        primaryStage.setMinWidth(550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
