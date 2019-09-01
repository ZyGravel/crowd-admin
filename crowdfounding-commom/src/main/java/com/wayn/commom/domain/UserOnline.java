package com.wayn.commom.domain;

import com.wayn.commom.enums.OnlineStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class UserOnline {

    private String id;

    private String userId;

    private String username;

    private String deptName;

    /**
     * 用户主机地址
     */
    private String host;


    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;


    /**
     * 在线状态
     */
    private OnlineStatus status = OnlineStatus.ON_LINE;

    /**
     * session创建时间
     */
    private Date startTimestamp;
    /**
     * session最后访问时间
     */
    private Date lastAccessTime;

    /**
     * 超时时间
     */
    private Long timeout;

    /**
     * 备份的当前用户会话
     */
    private String onlineSession;

    public String getId() {
        return id;
    }

    public UserOnline setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserOnline setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserOnline setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getHost() {
        return host;
    }

    public UserOnline setHost(String host) {
        this.host = host;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public UserOnline setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public String getOs() {
        return os;
    }

    public UserOnline setOs(String os) {
        this.os = os;
        return this;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public UserOnline setStatus(OnlineStatus status) {
        this.status = status;
        return this;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public UserOnline setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
        return this;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public UserOnline setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public UserOnline setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getOnlineSession() {
        return onlineSession;
    }

    public UserOnline setOnlineSession(String onlineSession) {
        this.onlineSession = onlineSession;
        return this;
    }

    public String getDeptName() {
        return deptName;
    }

    public UserOnline setDeptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("userId", userId)
                .append("username", username)
                .append("deptName", deptName)
                .append("host", host)
                .append("browser", browser)
                .append("os", os)
                .append("status", status)
                .append("startTimestamp", startTimestamp)
                .append("lastAccessTime", lastAccessTime)
                .append("timeout", timeout)
                .append("onlineSession", onlineSession)
                .toString();
    }
}
