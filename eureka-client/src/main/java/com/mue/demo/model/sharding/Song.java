package com.mue.demo.model.sharding;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * t_song
 * @author 
 */
@Data
@TableName("t_song")
public class Song extends Model<Song> implements Serializable {
    private String id;

    private String songid;

    private String name;

    private String artistid;

    private String mp3url;

    private String origin;

    private Integer num;

    private Date createtime;

    private Date modifytime;

    private static final long serialVersionUID = 1L;
}