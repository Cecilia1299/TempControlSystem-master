package com.vision.SlaveControl.resource.definedClass;

public class centerData {
    // 定义中央空调的状态枚举
    public enum AirConditioningState {
        PENDING, // 待机状态
        WORKING // 工作状态
    }

    // 默认目标温度
    private double defaultTargetTemperature;
    // 中央空调的当前状态
    private AirConditioningState currentState;

    // 构造函数，初始化默认目标温度和初始状态
    public centerData(double defaultTargetTemperature, AirConditioningState initialState) {
        this.defaultTargetTemperature = defaultTargetTemperature;
        this.currentState = initialState;
    }

    // 获取默认目标温度
    public double getDefaultTargetTemperature() {
        return defaultTargetTemperature;
    }

    // 设置默认目标温度
    public void setDefaultTargetTemperature(double defaultTargetTemperature) {
        this.defaultTargetTemperature = defaultTargetTemperature;
    }

    // 获取当前状态
    public AirConditioningState getCurrentState() {
        return currentState;
    }

    // 设置当前状态
    public void setCurrentState(AirConditioningState currentState) {
        this.currentState = currentState;
    }
}
