package com.dounion.server.task;

import com.dounion.server.core.base.BaseTask;
import com.dounion.server.core.base.Constant;
import com.dounion.server.core.task.TaskHandler;
import com.dounion.server.core.task.annotation.Task;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 任务链后台任务
 */
@Task(Constant.TASK_CHAIN)
public class ChainTask extends BaseTask {

    @Override
    public String getTaskName() {
        return "任务链后台任务";
    }

    @Override
    protected void execute() throws ExecutionException, InterruptedException {
        Map<String, Object> params = super.getParams();

        String[] taskNames = (String[]) params.get(Constant.TASK_CHAIN_NAMES);
        if (taskNames == null || taskNames.length == 0) {
            logger.warn("【{}】 taskNames check failed", this);
            return;
        }
        Integer delay = (Integer) params.get(Constant.TASK_CHAIN_DELAY);
        if (delay == null) {
            delay = 0;
        }

        for (String taskName : taskNames) {
            Future<Integer> task = TaskHandler.callTaskBlock(taskName, params, delay);
            Integer id = task.get();
            logger.debug("【{}】 completed, sub 【{}】", this, TaskHandler.getTask(id));
        }
    }
}