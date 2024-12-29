package com.vision.SlaveControl.server;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.concurrent.*;

public class EchoMultiServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool; // 线程池优化
    private static double CenterTargetTemp=22;
    public void start(int port) throws IOException {
        threadPool = Executors.newFixedThreadPool(10); // 限制线程池大小为10
        System.out.println("Server started on port: " + port);

        while (true) {
            serverSocket = new ServerSocket(port); // 新建ServerSocket，指定Port
            new EchoMultiHandler(serverSocket.accept()).start();
            serverSocket.close();
        }

    }

    public void stop() throws IOException {
        serverSocket.close();
        threadPool.shutdown(); // 关闭线程池
    }

    private static class EchoMultiHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoMultiHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    // 解析客户端请求
                    String[] parts = inputLine.split("\\|");
                    if (parts.length == 4) {
                        String roomId = parts[0];
                        double currentTemp = Double.parseDouble(parts[1]);
                        double targetTemp = Double.parseDouble(parts[2]);
                        if(targetTemp==100){
                            targetTemp=CenterTargetTemp;
                        }
                        String status = parts[3];
                        
                        // 计算温度变化率
                        double tempRate = calculateTemperatureChangeRate(currentTemp, targetTemp);

                        // 返回温度变化率
                        out.println(roomId + "|" +targetTemp+ "|" +tempRate);
                    }
                    else{ out.println(inputLine);}
                    System.out.println(inputLine);
                }
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                System.err.println("Error in client communication: " + e.getMessage());
            } 
        }
    }

    private static double calculateTemperatureChangeRate(double currentTemp, double targetTemp) {
        // 计算温度差 Δt
        double deltaT = currentTemp - targetTemp;
        // 如果温度差大于0,才进行降温
        if (deltaT > 0) {
            // 计算温度变化率  以C°/s做单位
            double tempRate = Math.sqrt(deltaT) / 60;
            // 保留3位小数
            return Double.parseDouble(String.format("%.3f", tempRate)); 
        }
            
        // 如果不需要降温，则返回 0.0
        return 0.0;
    }

    public static void main(String[] args) throws IOException {
        EchoMultiServer server = new EchoMultiServer();
        server.start(8888);// 启动服务器，指定端口
    }
}
