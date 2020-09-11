package com.dounion.server.dao;

import com.dounion.server.core.base.BaseDao;
import com.dounion.server.entity.SubscribeInfo;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Repository("subscribeInfoMapper")
public class SubscribeInfoMapper extends BaseDao<SubscribeInfo> {

    @Override
    public String getNamespace() {
        return "com.dounion.server.dao.SubscribeInfoMapper";
    }


    public int deleteBySelective(SubscribeInfo record) {
        Assert.notNull(record.getCode(), "code is null,it's dangerous operation...");
        return sql.delete(this.getNamespace() + ".deleteBySelective", record);
    }


    /**
     * 被订阅服务列表查询
     * @param record
     * @return
     */
    public List<Map<String, String>> currentServiceSubscribeQuery(SubscribeInfo record){
        return sql.selectList(this.getNamespace() + ".currentServiceSubscribeQuery", record);
    }

}
