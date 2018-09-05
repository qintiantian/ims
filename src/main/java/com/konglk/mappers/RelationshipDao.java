package com.konglk.mappers;

import com.konglk.data.UserData;
import com.konglk.entity.RelationshipVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RelationshipDao {

    @Insert("insert into ims_relationship (id, from_user, to_user, relationship_type, main_user, status, createtime, updatetime) values (#{id}, #{fromUser}, #{toUser}, #{relationshipType}, #{mainUser}, #{status}, #{createtime}, #{updatetime})")
    int insertRelationship(RelationshipVO relationshipVO);

    @Select("select * from ims_relationship where (from_user=#{fromUser} and to_user=#{toUser}) or (from_user=#{toUser} and to_user=#{fromUser}) limit 1")
    RelationshipVO selectOneRelationship(@Param("fromUser") String fromUser, @Param("toUser") String toUser);

    /**
     * 通过好友验证
     *
     * @param id
     * @param status
     * @param updatetime
     * @return
     */
    @Update("update ims_relationship set status=#{status}, updatetime=#{updatetime} where from_user=#{fromUser} and to_user=#{toUser}")
    int passRelationship(@Param("fromUser") String fromUser, @Param("toUser") String toUser, @Param("status") Integer status, @Param("updatetime") Long updatetime);

    /**
     * 单向好友删除
     *
     * @param id
     * @param relationshipType
     * @param mainUser
     * @param updatetime
     * @return
     */
    @Update("update ims_relationship set relationship_type=#{relationshipType}, main_user=#{mainUser}, updatetime=#{updatetime} where id=#{id}")
    int delOneWayRelationship(@Param("id") String id, @Param("relationshipType") Integer relationshipType, @Param("mainUser") String mainUser, @Param("updatetime") Long updatetime);

    /**
     * 双向好友删除
     *
     * @param id
     * @return
     */
    @Delete("delete from ims_relationship where id=#{id}")
    int delTwoWayRelationship(@Param("id") String id);

    /**
     * 获取好友列表
     *
     * @param userId
     * @return
     */
    List<UserData> selectRelationshipByUserId(String userId);

    /**
     * 获取新朋友
     * @param userId
     * @return
     */
    List<UserData> selectNewFriendsByUserId(String userId);
}
