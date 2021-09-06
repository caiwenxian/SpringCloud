package generate;

import generate.TSong;

public interface TSongDao {
    int deleteByPrimaryKey(String id);

    int insert(TSong record);

    int insertSelective(TSong record);

    TSong selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TSong record);

    int updateByPrimaryKey(TSong record);
}