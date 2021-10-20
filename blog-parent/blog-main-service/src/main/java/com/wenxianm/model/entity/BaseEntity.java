package com.wenxianm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Author cwx
 * @Date 2021/10/9 16:10
 **/
@Data
public class BaseEntity implements Serializable {

    private String id;

    private Date createTime;

    private Date modifyTime;
}
