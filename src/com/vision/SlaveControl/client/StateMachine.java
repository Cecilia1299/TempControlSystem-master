package com.vision.SlaveControl.client;

import com.vision.SlaveControl.resource.definedClass.slaveData;

//状态机 应该1s更新一次！
public class StateMachine {
    public enum StateMachineState {
        INITIALIZING,//初始化
        PENDING,//待机
        WORK,//工作
        ACTION,//用户操作
        CLOSE,//关闭
        FAILED;//失败
    
        // 示例方法：检查是否可以从当前状态转换到目标状态
        public boolean canTransitionTo(StateMachineState targetState) {
            switch (this) {
                case INITIALIZING:
                    return targetState == WORK || targetState == PENDING;
                case WORK:
                    return targetState == WORK ||targetState == PENDING || targetState == CLOSE ;
                case FAILED:
                    return false;//TO DO
                case PENDING:
                    return targetState == WORK || targetState == CLOSE;
                case ACTION:
                    return targetState == WORK || targetState == CLOSE;
                case CLOSE:
                    return targetState == INITIALIZING;
                default:
                    throw new IllegalStateException("Unknown state: " + name());
            }
        }
    }

    //private Slave slave;
    private StateMachineState state = StateMachineState.INITIALIZING;
    private slaveData slave;

    public StateMachine(slaveData slave){
        //this.slave = slave;
        this.slave = slave;
    }

    public void executeCurrentState(String message) {
        String[] parts = message.split("\\|");//INITIALIZING:id:targetTemp
        String id = parts[0];
        String currentTemp = parts[1];
        String targetTemp = parts[2];
        String status = parts[3];
        switch (state) {
            case INITIALIZING:
                System.out.println("初始化");
                //slave.setId(id);
                slave.setId(Integer.parseInt(id));
                slave.setRoomTemp(Double.parseDouble(currentTemp));
                slave.setTargetTemp(Double.parseDouble(targetTemp));
                slave.setStatus(status);
                if(slave.isAchieveTarget()){
                    slave.setStatus("待机");//待机模式
                    transitionTo(StateMachineState.PENDING);
                }else{
                    slave.setStatus("制冷");//工作模式
                    transitionTo(StateMachineState.WORK);
                }
                System.out.println(slave.toString());
                break;
            case PENDING:
                System.out.println("待机中");
                slave.setRoomTemp(Double.parseDouble(currentTemp));
                slave.setTargetTemp(Double.parseDouble(targetTemp));
                System.out.println(currentTemp+"|"+targetTemp+"|"+slave.getStatus());
                //1s刷新,温度以及状态
                if(slave.isAchieveTarget()){
                    slave.setStatus("待机");//待机模式
                    transitionTo(StateMachineState.PENDING);
                }else{
                    slave.setStatus("制冷");//工作模式    //不知道为什么从待机没办法进入工作模式
                    transitionTo(StateMachineState.WORK);
                }
                break;
            case WORK:
                System.out.println("工作中");
                slave.setRoomTemp(Double.parseDouble(currentTemp));
                slave.setTargetTemp(Double.parseDouble(targetTemp));
                //5s刷新,发送温度以及状态
                if(slave.isAchieveTarget()){
                    slave.setStatus("待机");//待机模式
                    transitionTo(StateMachineState.PENDING);
                }else{
                    slave.setStatus("制冷");//工作模式
                    transitionTo(StateMachineState.WORK);
                }
                break;
            case ACTION:
                String[] parts_4 = message.split(":");//ACTION:id:targetTemp
                String targetTemp_4 = parts_4[2];
                slave.setTargetTemp(Double.parseDouble(targetTemp_4));//设置目标温度
                System.out.println("用户更改了面板");
                //从控机改变了目标温度或开关
                if(slave.isAchieveTarget()){
                    slave.setStatus("待机");//待机模式
                    transitionTo(StateMachineState.PENDING);
                }else{
                    slave.setStatus("制冷");//工作模式
                    transitionTo(StateMachineState.WORK);
                }
            case CLOSE:
                System.out.println("关闭");
                break;
            case FAILED:
                System.out.println("System is in failed state.");
                // 添加失败状态的逻辑
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }
    }

    public void transitionTo(StateMachineState targetState) {
        if (state.canTransitionTo(targetState)) {
            state = targetState;
        } else {
            throw new IllegalStateException("Cannot transition from " + state + " to " + targetState);
        }
    }

}

//调用
// StateMachine sm = new StateMachine();
// sm.executeCurrentState(); // 执行初始化逻辑
// sm.transitionTo(StateMachineState.READY);
// sm.executeCurrentState(); // 执行 READY 状态逻辑
