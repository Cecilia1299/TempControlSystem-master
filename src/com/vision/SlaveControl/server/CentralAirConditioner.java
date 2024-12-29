package com.vision.SlaveControl.server;

import java.util.HashMap;
import java.util.Map;

public class CentralAirConditioner {
    private Map<String, Slave> unitStateMachines;

    // 构造方法
    public CentralAirConditioner() {
        unitStateMachines = new HashMap<>();
    }

    // 添加从控机
    public void addSlaveUnit(String id, String ipAddr) {
        unitStateMachines.put(id, new Slave(id, ipAddr));
        System.out.println("已添加从控机 [" + id + "]");
    }

    // 切换从控机状态
    // public void changeUnitState(String id, Slave newState) {
    //     Slave stateMachine = unitStateMachines.get(id);
    //     if (stateMachine != null) {
    //         stateMachine.setCurrStatus(newState);
    //     } else {
    //         System.out.println("从控机 [" + id + "] 不存在！");
    //     }
    // }

    // // 获取从控机状态
    // public Slave getUnitState(String id) {
    //     Slave stateMachine = unitStateMachines.get(id);
    //     if (stateMachine != null) {
    //         return stateMachine.getCurrentState();
    //     } else {
    //         System.out.println("从控机 [" + id + "] 不存在！");
    //         return null;
    //     }
    // }
}
