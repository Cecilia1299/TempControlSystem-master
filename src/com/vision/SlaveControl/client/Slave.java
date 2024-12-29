package com.vision.SlaveControl.client;

public class Slave {
    public static final int PENDING = 0;//待机模式
    public static final int WORKING = 1;//工作模式

    private String id;//编号
    private String ipAddr;//ip地址
    private float currentTemp;//目前温度
    private float targetTemp;//目标温度
    private float tempRate;//温度变化率
    private int status;//待机开机模式

    public Slave(String ipAddr) {
        this.ipAddr = ipAddr;
        this.currentTemp = 26.0f;//默认温度
        this.targetTemp = 20.0f;//目标温度
    }

    //id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    //ipAddr
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    //目前温度
    public float getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    }


    //目标温度
    public float getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(float targetTemp) {
        this.targetTemp = targetTemp;
    }

    //温度变化率
    public float getTempRate() {
        return tempRate;
    }

    public void setTempRate(float tempRate){
        this.tempRate = tempRate;
    }

    //打印状态
    public String toString() {
        return "Slave #" + id + "ip: " + ipAddr + " current temperature: " + currentTemp + " targetTemp: " +
                targetTemp;
    }

    // public void addTempChangeDaemon() {
    //     new Timer(1000, new ActionListener() {
    //         public void actionPerformed(ActionEvent e) {
    //             // code below is not a good way
    //             if (workMode == HOT_MODE && currentTemp > 10.0f) {
    //                 currentTemp += power * TEMP_GRID;
    //                 currentTemp -= TEMP_GRID * 0.5;
    //             } else if(workMode == COLD_MODE && currentTemp < 33.0f) {
    //                 currentTemp -= power * TEMP_GRID;
    //                 currentTemp += TEMP_GRID * 0.5;
    //             }
    //             listener.temperatureChanged(currentTemp);
    //         }
    //     }).start();
    // }

    //模式
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAchieveTarget(){
        if(targetTemp < currentTemp){
            return false;
        }
        return true;
    }
}