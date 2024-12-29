package com.vision.SlaveControl.client;

import java.io.IOException;

import com.vision.SlaveControl.resource.Controller.slaveController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SlaveControllerUI extends Application {
    private EchoClient echoClient;

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize EchoClient and start the connection
        echoClient = new EchoClient();
        echoClient.startConnection("127.0.0.1", 8888);

        // Start the socket communication in a separate thread
        new Thread(echoClient::socketCommunication).start();

        // VBox root = new VBox();
        // Button updateButton = new Button("更新参数并通知主控");

        // updateButton.setOnAction(event -> updateParameterAndNotifyMaster());

        // root.getChildren().add(updateButton);

        // Scene scene = new Scene(root, 300, 200);
        // primaryStage.setTitle("从控机");
        // primaryStage.setScene(scene);
        // primaryStage.show();
        //Pane root = FXMLLoader.load(getClass().getResource("../resource/fxml/slaveGUI.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resource/fxml/slaveGUI.fxml"));
        Pane root = loader.load();
        slaveController controller = loader.getController();
    
        // Pass the existing EchoClient to the controller
        controller.setEchoClient(echoClient);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("从控机");
        stage.setScene(scene);

        Image icon = new Image(getClass().getResourceAsStream("/com/vision/SlaveControl/resource/pic/icon.png"));
        stage.getIcons().add(icon);

        stage.show();
    }

    public void updateParameterAndNotifyMaster() {
        
        //传入消息
        String updatedParameter = "新参数值";
        System.out.println("参数更新为: " + updatedParameter);

        // Notify master control
        echoClient.updateParameterAndNotifyMaster(updatedParameter);
    }

    @Override
    public void stop() throws Exception {
        echoClient.stopConnection();
        super.stop();
    } 
}