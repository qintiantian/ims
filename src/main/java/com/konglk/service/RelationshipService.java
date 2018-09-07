package com.konglk.service;

import com.konglk.data.UserData;
import com.konglk.entity.RelationshipVO;
import com.konglk.enums.RelationshipConfig;
import com.konglk.mappers.RelationshipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelationshipService {
    @Autowired
    private RelationshipDao relationshipDao;

    public void insertRelationship(String fromUser, String toUser) {
        RelationshipVO relationshipVO = new RelationshipVO();
        relationshipVO.setId(UUID.randomUUID().toString());
        relationshipVO.setFromUser(fromUser);
        relationshipVO.setToUser(toUser);
        relationshipVO.setRelationshipType(RelationshipConfig.RelationshipType.TWOWAY.v);
        relationshipVO.setStatus(RelationshipConfig.Status.WAITING.v);
        relationshipVO.setCreatetime(System.currentTimeMillis());
        relationshipVO.setUpdatetime(System.currentTimeMillis());
        this.relationshipDao.insertRelationship(relationshipVO);
    }

    /**
     * 获取好友列表
     *
     * @param userId
     * @return
     */
    public Map<String, List<UserData>> selectRelationshipByUserId(String userId) {
        List<UserData> relationshipList = relationshipDao.selectRelationshipByUserId(userId);
        Map<String, List<UserData>> relationshipMap = new LinkedHashMap<>();
        relationshipList.stream().forEach(relationship -> {
            if (relationshipMap.containsKey(relationship.getFpinyin())) {
                relationshipMap.get(relationship.getFpinyin()).add(relationship);
            } else {
                List<UserData> relationships = new ArrayList<>();
                relationships.add(relationship);
                relationshipMap.put(relationship.getFpinyin(), relationships);
            }
        });
        return relationshipMap;
    }

    /**
     * 获取新朋友
     *
     * @param userId
     * @return
     */
    public List<UserData> selectNewFriendsByUserId(String userId) {
        return relationshipDao.selectNewFriendsByUserId(userId);
    }

    /**
     * 通过好友验证
     *
     * @param id
     * @return
     */
    public int passRelationship(String fromUser, String toUser) {
        return this.relationshipDao.passRelationship(fromUser, toUser, RelationshipConfig.Status.PASS.v, System.currentTimeMillis());
    }

    /**
     * 删除好友
     *
     * @param id
     * @return
     */
    public void delRelationship(String userId, String toUser) {
        RelationshipVO relationship = this.relationshipDao.selectOneRelationship(userId, toUser);
        if (relationship != null) {
            // 删除双向好友
            if (relationship.getRelationshipType() == RelationshipConfig.RelationshipType.ONEWAY.v) {
                this.relationshipDao.delTwoWayRelationship(relationship.getId());
                // 删除单向好友
            } else if (relationship.getRelationshipType() == RelationshipConfig.RelationshipType.TWOWAY.v) {
                String mainUser = "";
                if (relationship.getFromUser().equals(userId)) {
                    mainUser = relationship.getToUser();
                } else if (relationship.getToUser().equals(userId)) {
                    mainUser = relationship.getFromUser();
                }
                this.relationshipDao.delOneWayRelationship(relationship.getId(), RelationshipConfig.RelationshipType.ONEWAY.v, mainUser, System.currentTimeMillis());
            }
        }
    }
}
