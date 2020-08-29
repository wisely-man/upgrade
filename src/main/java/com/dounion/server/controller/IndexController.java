package com.dounion.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.dounion.server.core.base.Constant;
import com.dounion.server.core.base.ServiceInfo;
import com.dounion.server.core.helper.SpringApp;
import com.dounion.server.core.request.HandlerMappingConfig;
import com.dounion.server.core.request.ResponseBuilder;
import com.dounion.server.core.request.annotation.RequestMapping;
import com.dounion.server.core.request.annotation.ResponseType;
import com.dounion.server.eum.ResponseTypeEnum;
import com.dounion.server.task.SubscribeTask;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
public class IndexController {

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 功能列表页
     * @return
     */
    @RequestMapping(name = "功能列表", value = "/list")
    public String list(){
        return "list.html";
    }


    /**
     * 查询当前所有功能信息
     * @return
     */
    @RequestMapping(value = "/list.json")
    @ResponseType(ResponseTypeEnum.JSON)
    public Object listJson(){
        return ResponseBuilder.buildSuccess(HandlerMappingConfig.mapping);
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(name="首页", value = "/index")
    public String index(){
        return "index.html";
    }


    /**
     * 获取当前主机信息
     * @return
     */
    @RequestMapping("/index.json")
    @ResponseType(ResponseTypeEnum.JSON)
    public Object indexJson(){
        return ResponseBuilder.buildSuccess(SpringApp.getInstance().getBean(ServiceInfo.class));
    }

    /**
     * serviceInfo.json文件下载
     */
    @RequestMapping("/index/download.file")
    @ResponseType(ResponseTypeEnum.FILE)
    public File download() {
        return new File(Constant.CONG_PATH+"serviceInfo.json");
    }

    /**
     * serviceInfo.json文件下载
     */
    @RequestMapping("/index/update.json")
    @ResponseType(ResponseTypeEnum.JSON)
    public Object updateJSON(File file) {

        if(file == null){
            return ResponseBuilder.buildError("文件上传失败");
        }

        ServiceInfo serviceInfo = SpringApp.getInstance().getBean(ServiceInfo.class);

        String message = ResponseBuilder.SUCCESS_MESSAGE;
        try {
            // 获取上传文件信息
            ServiceInfo newInfo = JSONObject.parseObject(Files.readAllBytes(file.toPath()), ServiceInfo.class);

            if(!StringUtils.equals(serviceInfo.getLocalIp(), newInfo.getLocalIp()) ||
                    serviceInfo.getPort() != newInfo.getPort()){
                message = "警告：文件已更新，修改IP或PORT需要重启服务";
            }

            // 生成文件
            newInfo.toFile();
            // 刷新服务信息
            BeanUtils.copyProperties(newInfo, serviceInfo);
            serviceInfo.setMaster(newInfo.getMaster());
            serviceInfo.setStandBy(newInfo.getStandBy());
            serviceInfo.reloadLocalService();

            // 向主机刷新服务订阅信息
            new SubscribeTask().start();

        } catch (IOException e) {
            return ResponseBuilder.buildError("文件解析失败");
        }
        return ResponseBuilder.buildSuccess(message);
    }

}
