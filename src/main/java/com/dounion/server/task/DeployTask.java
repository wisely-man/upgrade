package com.dounion.server.task;

import com.dounion.server.core.base.AppInfo;
import com.dounion.server.core.base.BaseTask;
import com.dounion.server.core.base.Constant;
import com.dounion.server.core.base.ServiceInfo;
import com.dounion.server.core.task.annotation.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 部署后台任务
 *      本地任务
 */
@Task(Constant.TASk_DEPLOY)
public class DeployTask extends BaseTask {

    private final static Logger logger = LoggerFactory.getLogger(DeployTask.class);

    @Autowired
    private ServiceInfo serviceInfo;

    @Override
    public String getTaskName() {
        return "部署后台任务";
    }

    @Override
    protected void execute() {

        // some logic about do upgrade

        // 获取本地服务列表
        List<AppInfo> localServices = serviceInfo.getLocalServiceList();
        if(CollectionUtils.isEmpty(localServices)){
            logger.info("local service list is empty, deploy task exit");
            return;
        }

        // called scripts here

    }
}
