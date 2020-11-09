package com.dao.account;

import com.model.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年11月03日 14:28:00
 */

@Mapper
public interface AccountMapper {

    @Insert("insert into account(user_id, money) values (#{userId}, #{money})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public void insert(Account account);
}
