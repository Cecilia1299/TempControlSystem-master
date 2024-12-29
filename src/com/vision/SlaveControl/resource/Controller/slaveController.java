package com.vision.SlaveControl.resource.Controller;

import com.vision.SlaveControl.client.EchoClient;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class slaveController {

    @FXML
    private TextField portField;

    @FXML
    private TextField roomTempField;

    @FXML
    private TextField targetTempField;

    @FXML
    private Button confirmButton_slave;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    private double currentTemp;

    // 添加currentTemp变量
    @FXML
    private void initialize() {
        disableAllControls();
        // 监听目标温度文本字段的变化
        targetTempField.textProperty().addListener((observable, oldValue, newValue) -> updateCurrentTemp());
    }

    @FXML
    private void startAction() {
        enableAllControls();
    }

    @FXML
    private void stopAction() {
        disableAllControls();
    }

    private void enableAllControls() {
        portField.setDisable(false);
        roomTempField.setDisable(false);
        targetTempField.setDisable(false);
        confirmButton_slave.setDisable(false);
        stopButton.setDisable(false);
    }

    private void disableAllControls() {
        portField.setDisable(true);
        roomTempField.setDisable(true);
        targetTempField.setDisable(true);
        confirmButton_slave.setDisable(true);
        startButton.setDisable(false);
    }

    @FXML
    private void confirmAction() {
        System.out.println("确定按钮被点击");
    }

    public String getPortNumber() {
        return portField.getText();
    }

    public double getTargetTemperature() {
        return Double.parseDouble(targetTempField.getText());
    }

    public double getRoomTemperature() {
        return currentTemp; // 返回当前温度
    }

    private void updateCurrentTemp() {
        try {
            currentTemp = Double.parseDouble(targetTempField.getText());
            System.out.println("目标温度已更新为: " + currentTemp); // 打印目标温度
        } catch (NumberFormatException e) {
            System.err.println("输入的目标温度不是一个有效的数字");
            currentTemp = 0.0; // 如果输入无效，设置为0.0或其他默认值
        }
    }

    private EchoClient echoClient; // Reference to the existing EchoClient

    public void setEchoClient(EchoClient echoClient) {
        this.echoClient = echoClient;
    }

    public void updateParameterAndNotifyMaster(String message) {
        if (echoClient != null) {
            echoClient.updateParameterAndNotifyMaster(message);
        } else {
            System.err.println("EchoClient is not initialized.");
        }
    }
}