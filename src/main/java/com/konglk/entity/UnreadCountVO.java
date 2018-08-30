package com.konglk.entity;

/**
 * Created by konglk on 2018/8/30.
 */
public class UnreadCountVO extends BaseEntity {

    private String unreadCountId;
    private String conversationId;
    private String userId;
    private Integer unreadCount;
    private String lastMsgId;

    public String getUnreadCountId() {
        return unreadCountId;
    }

    public void setUnreadCountId(String unreadCountId) {
        this.unreadCountId = unreadCountId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(String lastMsgId) {
        this.lastMsgId = lastMsgId;
    }
}
