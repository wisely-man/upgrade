package com.dounion.server.task;

import com.dounion.server.core.base.BaseTask;
import com.dounion.server.core.base.Constant;
import com.dounion.server.core.exception.BusinessException;
import com.dounion.server.core.helper.DataHelper;
import com.dounion.server.core.task.TaskHandler;
import com.dounion.server.core.task.annotation.Task;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 任务链后台任务
 *      when a sub task cause a exception,
 *      the linked tasks will be stopped
 */
@Task(Constant.TASK_CHAIN)
public class ChainTask extends BaseTask {

    @Override
    public String getTaskName() {
        return "任务链后台任务";
    }


    @Override
    protected void execute() throws Exception {
        Map<String, Object> params = super.getParams();

        List<BaseTask> tasks = (List<BaseTask>) params.get(Constant.TASK_CHAIN_NAMES);
        if (CollectionUtils.isEmpty(tasks)) {
            logger.warn("【{}】 taskNames check failed", this);
            return;
        }
        Long delay = (Long) params.get(Constant.TASK_CHAIN_DELAY);
        if (delay == null) {
            delay = 0l;
        }

        this.setProgressJustStart(); // progress

        int i = 0;
        ConcurrentHashMap<String, Object> temp;
        for (BaseTask task : tasks) {
            temp = new ConcurrentHashMap<>();
            temp.putAll(params);
            Future<Integer> future = TaskHandler.callTaskBlock(task, temp, delay);
            Integer id = future.get();
            if(id == null){
                throw new BusinessException("Chain task execute failed");
            }
            this.setProgress(DataHelper.percent(tasks.size(), (i+1)));
            logger.debug("Chain task completed, sub task is 【id:{}, name:{}】",
                    id, task.getTaskName());
            i++;
        }
    }
}
