package com.vision.SlaveControl;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


//没有用到

//最开始调用init方法，紧跟着start方法，stop方法是在主窗口关闭之后调用
public class Slave extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {


        Pane root = FXMLLoader.load(getClass().getResource("resource/fxml/slaveGUI.fxml"));

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("从控机");
        stage.setScene(scene);

        Image icon = new Image(getClass().getResourceAsStream("/com/vision/SlaveControl/resource/pic/icon.png"));
        stage.getIcons().add(icon);

        stage.show();
    }
}
