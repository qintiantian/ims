package com.konglk.mappers;

import com.konglk.common.Page;
import com.konglk.entity.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by konglk on 2018/8/24.
 */
@Mapper
@Component("userDao")
public interface UserDao {
    int insertUser(UserVO userVO);

    @Select("select * from ims_user where (nickname=#{unique} or cellphone=#{unique}) and status=1")
    UserVO selectUser(String unique);

    @Update("update ims_user set pwd=#{newpwd} where (nickname=#{unique} or cellphone=#{unique}) and status=1")
    void updatePwd(@Param("newpwd") String newpwd, @Param("unique") String unique);

    Map<String, Object> selectUserById(String userId);

    List<UserVO> selectUsersByPage(Page<UserVO> page);

}