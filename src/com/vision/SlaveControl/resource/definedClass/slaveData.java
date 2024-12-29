package com.vision.SlaveControl.resource.definedClass;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class slaveData {
    private SimpleIntegerProperty id;
    private SimpleStringProperty status;
    private SimpleDoubleProperty roomTemp;
    private SimpleDoubleProperty targetTemp;
    private float tempRate;

    public slaveData(int id, String status,double roomTemp, double targetTemp) {
        this.id = new SimpleIntegerProperty(id);
        this.status = new SimpleStringProperty(status);
        this.roomTemp = new SimpleDoubleProperty(roomTemp);
        this.targetTemp = new SimpleDoubleProperty(targetTemp);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public double getRoomTemp() {
        return roomTemp.get();
    }

    public void setRoomTemp(double roomTemp) {
        this.roomTemp.set(roomTemp);
    }

    public SimpleDoubleProperty roomTempProperty() {
        return roomTemp;
    }

    public double getTargetTemp() {
        return targetTemp.get();
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp.set(targetTemp);
    }

    public SimpleDoubleProperty targetTempProperty() {
        return targetTemp;
    }

    public boolean isAchieveTarget(){
        // System.out.println("当前温度: " + roomTemp.get() + ", 目标温度: " + targetTemp.get());
        //false:进入制冷状态 true 无操作
        System.out.println("当前状态"+status.get());
        if(targetTemp.get() <roomTemp.get()){
            return false;
        }
        return true;
    }
}
