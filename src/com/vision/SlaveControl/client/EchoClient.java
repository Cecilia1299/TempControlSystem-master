package com.vision.SlaveControl.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vision.SlaveControl.client.StateMachine.StateMachineState;
import com.vision.SlaveControl.resource.definedClass.slaveData;

import javafx.application.Platform;

public class EchoClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private double currentTemp = 22.5; // 初始温度  后续改为随机数
    private double TargetTemp=100; //标识，为了读取中央空调默认的目标温度
    private double tempRate = 0.0;    // 初始温度变化速率 (°C/s)

    private boolean isStateMachineStarted = false;  // 标志位，控制是否启动状态机更新
    private boolean isTemperatureUpdateStarted = false;  // 标志位，避免重复启动降温
    private ScheduledExecutorService scheduler;  // 线程池，确保只创建一次
    private slaveData slave = new slaveData(0, "初始化", currentTemp, TargetTemp); // 初始化 slaveData

    // 创建状态机对象
    private StateMachine sm = new StateMachine(slave);

    // 启动连接
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送消息
    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        return in.readLine(); // 阻塞等待服务器返回
    }

    // 停止连接
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    // 处理消息
    public void socketCommunication() {
        try {
            long lastSentTime = System.currentTimeMillis();

            // 开机初始化
            String initialResponse = sendMessage("hello server");
            System.out.println("开始发送接受消息");
            System.out.println("Server: " + initialResponse);
            
            // 启动状态机定时更新
            startStateMachineUpdate();

            // 进入交互循环
            while (true) {
                // 定时发送温控数据
                if (System.currentTimeMillis() - lastSentTime >= 5000) {
                    int roomId = 1;  // 房间ID
                    String status = slave.getStatus(); // 当前状态

                    String message = roomId + "|" + currentTemp + "|" + TargetTemp + "|" + status;
                    String response = sendMessage(message);  // 发送消息并获取主控机的响应
                    
                    // // 启动状态机更新
                    // if (!isStateMachineStarted) {
                    //     isStateMachineStarted = true;  // 收到信息后标记
                    //     startStateMachineUpdate(); // 收到信息后启动状态机
                    // }
                    // 解析主控机响应并获取温度变化速率
                    String[] responseParts = response.split("\\|");
                    if (responseParts.length > 1) {
                        try {
                            TargetTemp = Double.parseDouble(responseParts[1]);
                            tempRate = Double.parseDouble(responseParts[2]);
                            System.out.println("从控室温的下降速率 (°C/s): " + tempRate);
                            System.out.println("目标温度: " + TargetTemp);
                            if(currentTemp != TargetTemp && !isTemperatureUpdateStarted) {
                                startTemperatureUpdate();  // 启动温度更新
                                System.out.println("....降温线程开启");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("无法解析温度变化速率，格式错误: " + responseParts[1]);
                        }
                    } else {
                        System.out.println("主控机返回数据格式错误: " + response);
                    }

                    System.out.println("发送消息: " + message);
                    System.out.println("主控机响应: " + response);
                
                    lastSentTime = System.currentTimeMillis();  // 更新发送时间
                }

                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                stopConnection();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // 启动定时任务：定期更新状态机
    public void startStateMachineUpdate() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    // System.out.println("test");
                    sm.executeCurrentState(slave.getId() + "|" + currentTemp + "|" + TargetTemp + "|" + slave.getStatus());
                }
            }, 0, 1, TimeUnit.SECONDS);  // 0秒延迟后每秒执行一次
        }
    }

    // 温度更新：主控送风降温
    public void updateTemperature() {
        currentTemp = Double.parseDouble(String.format("%.3f", currentTemp - tempRate));
        System.out.println("...降温中，当前温度：" + currentTemp);

        // 如果温度低于目标温度，停止更新
        if (currentTemp <= TargetTemp) {
            currentTemp = TargetTemp;
            System.out.println("温度已达到目标，停止更新");
            stopTemperatureUpdate();  // 停止定时任务
        }
    }

    // 启动定时任务：修改当前温度
    public void startTemperatureUpdate() {
        if (!isTemperatureUpdateStarted) {  // 确保只启动一次
            isTemperatureUpdateStarted = true;

            // 使用 ScheduledExecutorService 每秒更新一次温度
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    updateTemperature();
                }
            }, 0, 1, TimeUnit.SECONDS);  // 0秒延迟后每秒执行一次
        }
    }

    // 停止定时任务
    public void stopTemperatureUpdate() {
        if (isTemperatureUpdateStarted) {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();  // 停止定时任务  //后续要解决 好像不能这样关闭
            }
            isTemperatureUpdateStarted = false;  // 重置标志位
            System.out.println("降温任务已停止");
        }
    }

    // // 消息处理逻辑  没用到？
    // private void handleServerMessage(String message, StateMachine sm) {
    //     if (message.startsWith("START")) {
    //         start(message, sm);
    //     } else if (message.startsWith("SET")) {
    //         set(message, sm);
    //     } else if (message.startsWith("UPDATE")) {
    //         update(message, sm);
    //     } else if (message.startsWith("CLOSE")) {
    //         close(message, sm);
    //     } else {
    //         System.out.println("Unknown command from server: " + message);
    //     }
    // }

    // 开机
    public void start(String message, StateMachine sm) {
        System.out.println("Initializing...");
        sm.executeCurrentState(message);
        System.out.println("System initialized.");
    }

    // 设置目标温度
    public void set(String message, StateMachine sm) {
        System.out.println("Setting target...");
        sm.transitionTo(StateMachineState.ACTION);
        String[] parts = message.split(":");
        if (parts.length > 1) {
            try {
                double targetTemperature = Double.parseDouble(parts[1]);
                System.out.println("Target temperature set to: " + targetTemperature);
            } catch (NumberFormatException e) {
                System.out.println("Invalid temperature value.");
            }
        }
    }

    // 关机
    public void close(String message, StateMachine sm) {
        System.out.println("Shutting down...");
        sm.transitionTo(StateMachineState.CLOSE);
        System.out.println("System closed.");
    }

    // 更新状态
    public void update(String message, StateMachine sm) {
        System.out.println("Updating state...");
        sm.executeCurrentState(message);
        System.out.println("State updated.");
    }

    // 更新参数并通知主控
    public void updateParameterAndNotifyMaster(String updatedParameter) {
        messageQueue.offer("更新参数消息: " + updatedParameter);
    }

    public void notifyUI(String message) {
        Platform.runLater(() -> {
            System.out.println(message); // Or send this to your UI
        });
    }
}
