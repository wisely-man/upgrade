package com.dounion.server.entity;

import com.dounion.server.core.base.BaseEntity;

public class UpgradeRecord extends BaseEntity {

    private Integer id;

    private Integer subscribeId; // 订阅ID

    private String code; // 库点代码

    private String name; // 库点名称

    private String publishUrl; // 发布地址

    private SubscribeInfo subscribe; // 订阅记录实体类

    private Integer versionId; // 版本ID

    private String versionNo; // 版本号

    private String appType; // 应用类型

    private String isForceUpdate; // 是否强制更新 1-是 0-否

    private String publishType; // 发布类型 1-手动发布 2-自动发布

    private VersionInfo version; // 版本记录实体类

    private String notifyStatus; // 通知结果 通知结果 1-成功 0-失败

    private Integer notifyCount; // 通知次数

    private String notifyCountStr; // sql count++ 使用

    private String notifyTime; // 通知时间

    private String upgradeStatus; // 版本升级状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Integer subscribeId) {
        this.subscribeId = subscribeId;
    }

    public SubscribeInfo getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(SubscribeInfo subscribe) {
        this.subscribe = subscribe;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public VersionInfo getVersion() {
        return version;
    }

    public void setVersion(VersionInfo version) {
        this.version = version;
    }

    public String getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(String notifyStatus) {
        this.notifyStatus = notifyStatus == null ? null : notifyStatus.trim();
    }

    public Integer getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Integer notifyCount) {
        this.notifyCount = notifyCount;
    }

    public String getNotifyCountStr() {
        return notifyCountStr;
    }

    public void setNotifyCountStr(String notifyCountStr) {
        this.notifyCountStr = notifyCountStr;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(String upgradeStatus) {
        this.upgradeStatus = upgradeStatus == null ? null : upgradeStatus.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getIsForceUpdate() {
        return isForceUpdate;
    }

    public void setIsForceUpdate(String isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }
}