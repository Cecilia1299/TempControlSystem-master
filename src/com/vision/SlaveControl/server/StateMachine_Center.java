package com.vision.SlaveControl.server;

import com.vision.SlaveControl.resource.definedClass.centerData;
import com.vision.SlaveControl.resource.definedClass.centerData.AirConditioningState;

public class StateMachine_Center {
    public enum MasterStateMachineState {
        PENDING, // 待机
        WORK; // 工作

        // 检查是否可以从当前状态转换到目标状态
        public boolean canTransitionTo(MasterStateMachineState targetState) {
            switch (this) {
                case PENDING:
                    return targetState == WORK;
                case WORK:
                    return targetState == PENDING;
                default:
                    throw new IllegalStateException("Unknown state: " + this);
            }
        }
    }

    private MasterStateMachineState state = MasterStateMachineState.PENDING;
    private centerData master;

    // 构造函数名称修正以匹配类名
    public StateMachine_Center(centerData master) {
        this.master = master;
    }

    public void executeCurrentState() {
        switch (state) {
            case PENDING:
                System.out.println("主控待机中");
                // 待机状态的逻辑
                checkAndActivateWorkMode();
                // 更新centerData的状态
                master.setCurrentState(AirConditioningState.PENDING);
                break;
            case WORK:
                System.out.println("主控工作中");
                // 工作状态的逻辑
                checkAndActivatePendingMode();
                // 更新centerData的状态
                master.setCurrentState(AirConditioningState.WORKING);
                break;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }
    }

    public void transitionTo(MasterStateMachineState targetState) {
        if (state.canTransitionTo(targetState)) {
            state = targetState;
            executeCurrentState(); // 状态转换后执行新状态的逻辑
        } else {
            throw new IllegalStateException("Cannot transition from " + state + " to " + targetState);
        }
    }

    private void checkAndActivateWorkMode() {
        // 逻辑检查是否需要转换到工作模式
        boolean needToWork = /* some condition */;
        if (needToWork) {
            transitionTo(MasterStateMachineState.WORK);
        }
    }

    private void checkAndActivatePendingMode() {
        // 逻辑检查是否需要转换到待机模式
        boolean needToPending = /* some condition */;
        if (needToPending) {
            transitionTo(MasterStateMachineState.PENDING);
        }
    }
}
