package com.mue.demo.controller.sharding;

import com.mue.demo.model.sharding.Song;
import com.mue.demo.service.sharding.ISongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName SongController
 * @Description TODO
 * @Author cwx
 * @Date 2021/8/31 17:01
 * @Version 1.0
 **/
@RestController
@RequestMapping("/song")
@Slf4j
public class SongController {

    @Autowired
    ISongService songService;

    @GetMapping("/list")
    public List<Song> list() {
        return songService.listAll();
    }
}
