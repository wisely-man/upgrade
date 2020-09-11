package com.dounion.server.core.route;


import com.dounion.server.core.base.Constant;
import com.dounion.server.core.helper.ConfigurationHelper;
import com.dounion.server.entity.DownloadRouteRecord;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 资源下载分发控制器
 */
public class RouteHandler {

    // private constructor
    private RouteHandler() {
    }

    private static Object LOCK1 = new Object();
    private static Object LOCK2 = new Object();
    private static Object LOCK3 = new Object();

    final static Integer MAX_COUNT = ConfigurationHelper.getInt(Constant.CONF_DOWNLOAD_MAX_COUNT, -1);
    final static Long MAX_ROUTE_TIME = ConfigurationHelper.getLong(Constant.CONF_DOWNLOAD_MAX_ROUTE_TIME, -1);


    /**
     * 本地下载数
     */
    public final static ConcurrentHashMap<String, AtomicInteger>
            LOCAL_COUNTER_MAP = new ConcurrentHashMap<>();

    /**
     * 已注册的路由表
     *      key:path
     *      value:routes
     */
    public final static ConcurrentHashMap<String, List<DownloadRouteRecord>>
            ROUTE_INFO_MAP = new ConcurrentHashMap<>();


    /**
     * 路由队列集合
     *  key:path
     *  value:queue
     */
    public final static Map<String, BlockingQueue<DownloadRouteRecord>> ROUTE_QUEUE_MAP = new HashMap<>();


    // ================================================ local count  ===================================================

    /**
     * 获取本地指定下载资源的下载数AtomicInteger对象
     *
     * @param path
     * @return
     */
    private static AtomicInteger get(String path) {
        AtomicInteger integer = LOCAL_COUNTER_MAP.get(path);
        if (integer == null) {
            synchronized (LOCK1) {
                if (integer == null) {
                    integer = new AtomicInteger(0);
                    LOCAL_COUNTER_MAP.put(path, integer);
                }
                integer = LOCAL_COUNTER_MAP.get(path);
            }
        }
        return integer;
    }


    /**
     * 获取本地资源当前下载数
     *
     * @param path
     * @return
     */
    public static Integer getCount(String path) {
        return get(path).get();
    }


    /**
     * 本地资源 计数+1
     *
     * @param path
     * @return
     */
    public static Integer countDown(String path) {
        return get(path).addAndGet(1);
    }


    /**
     * 本地资源 计数-1
     *
     * @param path
     * @return
     */
    public static Integer reduction(String path) {
        return get(path).decrementAndGet();
    }


    // ================================================ route operation ================================================



    /**
     * 路由注册
     * @param path
     * @param record
     */
    public static void routeRegister(String path, DownloadRouteRecord record) {
        List<DownloadRouteRecord> records = ROUTE_INFO_MAP.get(path);
        if(records == null){
            synchronized (LOCK2) {
                if(records == null){
                    records = Collections.synchronizedList(new LinkedList<DownloadRouteRecord>());
                    ROUTE_INFO_MAP.put(path, records);
                }
                records = ROUTE_INFO_MAP.get(path);
            }
        }

        records.add(record);
    }

    /**
     * 路由注销
     * @param path
     */
    public static void routeCancel(String path, String host) {
        List<DownloadRouteRecord> records = ROUTE_INFO_MAP.get(path);
        if(CollectionUtils.isEmpty(records)){
            return;
        }

        int removeInx = -1;
        for(int i=0; i<records.size(); i++){
            if(StringUtils.equals(records.get(i).getHost(), host)){
                removeInx = i;
                break;
            }
        }

        if(removeInx > -1){
            records.remove(removeInx);
        }
    }

    // ================================================ for download operation =========================================


    /**
     * 判断是否需要新的下载路由
     *
     * @param url
     * @return
     */
    public static String getNewUrl(String url) {

        if (MAX_COUNT <= 0) {
            // 小于等于0为不限制
            return null;
        }

        // 当前下载数未超过最大限制
        if (getCount(url) < MAX_COUNT) {
            return null;
        }

        return routeQueueOperation(url);
    }

    /**
     * 路由处理
     * @param url
     * @return
     */
    private static String routeQueueOperation(String url) {

        BlockingQueue<DownloadRouteRecord> queue = ROUTE_QUEUE_MAP.get(url);

        // queue 为 null 初始化数据
        if(queue == null){
            synchronized (LOCK3) {
                if(queue == null){
                    queue = new LinkedBlockingQueue<>();
                    ROUTE_QUEUE_MAP.put(url, queue);
                }
                queue = ROUTE_QUEUE_MAP.get(url);
            }

            // 遍历添加队列
            List<DownloadRouteRecord> records = ROUTE_INFO_MAP.get(url);
            if(CollectionUtils.isEmpty(records)){
                return null;
            }
            for(DownloadRouteRecord route : records){
                if(MAX_ROUTE_TIME > 0 && System.currentTimeMillis()-MAX_ROUTE_TIME<0){
                    continue;
                }
                queue.add(route);
            }
        }

        DownloadRouteRecord record = queue.poll();
        if(record == null){
            return null;
        }

        if(MAX_ROUTE_TIME>0 && System.currentTimeMillis()-MAX_ROUTE_TIME<=0){
            return null;
        }

        return record.getDownloadPath();
    }
}
