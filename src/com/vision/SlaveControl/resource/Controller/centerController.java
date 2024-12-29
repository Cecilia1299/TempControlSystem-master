package com.vision.SlaveControl.resource.Controller;

import com.vision.SlaveControl.resource.definedClass.slaveData;
import com.vision.SlaveControl.server.EchoMultiServer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class centerController {


    private EchoMultiServer echoServer; // 添加对EchoMultiServer的引用

    @FXML
    private TextField defaultTargetTempField;

    @FXML
    private TextField refreshPeriodField;

    @FXML
    private Button confirmButton_center;

    @FXML
    private TableView<slaveData> statusTable;

    @FXML
    private Button openButton;

    @FXML
    private Button closeButton;

    @FXML
    private TableColumn<slaveData, Integer> colId;

    @FXML
    private TableColumn<slaveData, String> colStatus;

    @FXML
    private TableColumn<slaveData, Double> colRoomTemp;

    @FXML
    private TableColumn<slaveData, Double> colTargetTemp;

    @FXML
    public void initialize() {
        // 创建表格数据
        ObservableList<slaveData> data = FXCollections.observableArrayList();
        data.add(new slaveData(1, "Active", 25.0, 27.0));
        data.add(new slaveData(2, "Inactive",  26.0, 27.0));
        // 可以添加更多数据

        // 设置表格数据
        statusTable.setItems(data);

        // 设置列的单元格值工厂
        colId.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getId())
        );
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colRoomTemp.setCellValueFactory(cellData -> cellData.getValue().roomTempProperty().asObject());
        colTargetTemp.setCellValueFactory(cellData -> cellData.getValue().targetTempProperty().asObject());

        disableAllControls();
    }

    @FXML
    private void confirmAction() {
    // 确定按钮的实现逻辑
    System.out.println("确定按钮被点击");
    }


    @FXML
    private void onOpenAction() {
        enableAllControls();
    }

    @FXML
    private void onCloseAction() {
        disableAllControls();
    }

    private void enableAllControls() {
        defaultTargetTempField.setDisable(false);
        confirmButton_center.setDisable(false);
        refreshPeriodField.setDisable(false);
        statusTable.setDisable(false);
    }

    private void disableAllControls() {
        defaultTargetTempField.setDisable(true);
        confirmButton_center.setDisable(true);
        refreshPeriodField.setDisable(true);
        statusTable.setDisable(true);
    }

    // 添加一个方法来设置EchoMultiServer
    public void setEchoServer(EchoMultiServer server) {
        this.echoServer = server;
        // 可以在这里根据需要添加更多的初始化代码
    }
}
