package com.wenxianm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName BaseEntity
 * @Author cwx
 * @Date 2021/10/9 16:10
 **/
@Data
public class BaseEntity implements Serializable {

    private String id;

    @Column(name = "createTime")
    private LocalDateTime createTime;

    @Column(name = "modifyTime")
    private LocalDateTime modifyTime;
}
