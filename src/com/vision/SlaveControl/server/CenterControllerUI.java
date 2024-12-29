package com.vision.SlaveControl.server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import com.vision.SlaveControl.resource.Controller.*;

public class CenterControllerUI extends Application {
    private EchoMultiServer echoServer;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the server and start it in a separate thread
        echoServer = new EchoMultiServer();
        new Thread(() -> {
            try {
                echoServer.start(8888);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Load the GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resource/fxml/centerGUI.fxml"));
        Pane root = loader.load();

        // Optionally, if you have a controller that needs a reference to the server
        centerController controller = loader.getController();
        controller.setEchoServer(echoServer);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("主控机");
        stage.setScene(scene);

        // Set the application icon
        Image icon = new Image(getClass().getResourceAsStream("/com/vision/SlaveControl/resource/pic/icon.png"));
        stage.getIcons().add(icon);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        echoServer.stop(); // Stop the server when the application is closed
        super.stop();
    }
}
