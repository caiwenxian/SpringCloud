package com.dao.user;

import com.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年11月03日 14:25:00
 */

@Mapper
public interface UserMapper {

    @Insert("insert into user(id, name) values (#{id}, #{name})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public void insert(User user);
}
