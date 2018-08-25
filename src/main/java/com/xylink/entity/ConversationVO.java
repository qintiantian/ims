package com.xylink.entity;

/**
 * Created by konglk on 2018/8/25.
 */
public class ConversationVO extends BaseEntity {
    private String conversationId;
    private String userId;
    private String destId;
    private Integer conversationType;
    private Integer status;
    private Long ts;

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

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public Integer getConversationType() {
        return conversationType;
    }

    public void setConversationType(Integer conversationType) {
        this.conversationType = conversationType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "ConversationVO{" +
                "userId='" + userId + '\'' +
                ", destId='" + destId + '\'' +
                '}';
    }
}
