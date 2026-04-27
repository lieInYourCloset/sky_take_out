package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {

    /**
     * 新建用户
     *
     *
     */
    @Insert("insert into user (openid, create_time) values (#{openid}, #{createTime})")
    void saveUser(User user);

    /**
     * 根据openid查询用户信息
     *
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User getUserByOpenId(String openId);
}
