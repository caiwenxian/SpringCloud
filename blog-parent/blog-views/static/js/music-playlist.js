var playlist = {};

playlist.init = function () {
    console.log("music playlist init")
    // 初始化播放列表
    let songs = window.localStorage.getItem('blog.playlist');
    if (!isBlank(songs)) {
        playlist.vm.songs.push.apply(playlist.vm.songs, JSON.parse(songs));
    }
    // 初始化当前播放歌曲
    playlist.vm.currentSongId = window.localStorage.getItem('blog.currentSongId');
    // 初始化播放器
    if (playlist.vm.currentSongId && playlist.vm.songs.length > 0) {
        let index = 0;
        for (let i in playlist.vm.songs) {
            if (playlist.vm.currentSongId == playlist.vm.songs[i].id) {
                playlist.vm.currentSong = playlist.vm.songs[i];
                index = i;
                break;
            }
        }
        if (playlist.vm.currentSong == null) {
            playlist.vm.currentPlaySongs = playlist.vm.songs;
            playlist.vm.currentSong =  playlist.vm.songs[0];
        } else {
            // 将数组进行位移
            for (let i = 0; i < playlist.vm.songs.length; i ++) {
                let offset = playlist.vm.songs.length - Number(index)
                let newIndex = (offset + i) % playlist.vm.songs.length;
                playlist.vm.currentPlaySongs[newIndex] = playlist.vm.songs[i];
                playlist.vm.currentOffset = offset;
            }
            // console.log('playlist.vm.currentPlaySongs: ' + JSON.stringify(playlist.vm.currentPlaySongs))
            let currentIndex = indexOfField(playlist.vm.songs, 'id', playlist.vm.currentSongId);
            playlist.vm.currentSong =  playlist.vm.songs[currentIndex];
        }
        // parent.window.initMusic(playlist.vm.currentPlaySongs, false);
        // parent.player.currentIndex = index;
        // playlist.vm.currentPlaySongs = playlist.vm.songs;
        parent.ap.init(playlist.vm.currentPlaySongs);
        playlist.vm.cover = playlist.vm.currentSong.cover;
    }

}

/**
 * 添加歌曲到列表
 * @param item
 */
playlist.pushSong = function (item) {
    if (isContainField(playlist.vm.songs, 'id', item.id)) {
        return;
    }
    let song = {
        name: item.name,
        author: item.artist,
        url: item.url,
        id: item.id,
        cover: item.cover,
        lrc: item.lrc
    }
    let index = playlist.vm.songs.length - playlist.vm.currentOffset + 1;
    playlist.vm.songs.splice(index, 0, song);
    playlist.vm.currentPlaySongs.splice(1, 0, song);
    // 记录播放列表
    let oldSongs = window.localStorage.getItem('blog.playlist');
    if (isBlank(oldSongs)) {
        window.localStorage.setItem('blog.playlist', JSON.stringify([song]));
    } else {
        let newSongs = JSON.parse(oldSongs);
        newSongs.splice(index, 0, song);
        window.localStorage.setItem('blog.playlist', JSON.stringify(newSongs));
    }
    playlist.vm.currentSongId = song.id;

}

/**
 * 切换歌曲
 * @param song
 */
playlist.switchSong = function(song) {
    console.log('inter switchSong')
    playlist.vm.currentSong = song;
    playlist.vm.currentSongId = song.id;
    playlist.vm.cover = song.cover;
    // 记录当前播放歌曲
    window.localStorage.setItem('blog.currentSongId', song.id);
    // 切换歌词
    playlist.vm.lyric.lineNo = 1;
    $('#lyric').animate({
        scrollTop: 0
    });
    let lyrics = playlist.vm.currentSong.lrc.replaceAll("\"", "").split("\n");
    for (let i in lyrics) {
        let t = lyrics[i].substring(lyrics[i].indexOf("[") + 1, lyrics[i].indexOf("]"));
        playlist.vm.lyric.lyricArray.push({
            t: (t.split(":")[0] * 60 + parseFloat(t.split(":")[1])).toFixed(3),
            c: lyrics[i].substring(lyrics[i].indexOf("]") + 1, lyrics[i].length)
        })
    }
}

/**
 * 展示歌词
 * @param song
 * @param audio
 */
playlist.showLyric = function(song, audio) {
    let lineNo = playlist.vm.lyric.lineNo;
    let lyricArray = playlist.vm.lyric.lyricArray;
    if (isBlank(song.lrc)) {
        return;
    }
    if (!isBlank(lyricArray[lineNo]) && lineNo == lyricArray.length - 1 && audio.currentTime.toFixed(3) >= parseFloat(lyricArray[lineNo].t)) {
        lineHeight(lineNo);
    }
    if (!isBlank(lyricArray[lineNo + 1]) && parseFloat(lyricArray[lineNo].t) <= audio.currentTime.toFixed(3) &&
        audio.currentTime.toFixed(3) <= parseFloat(lyricArray[lineNo + 1].t)) {
        lineHeight(lineNo);
        playlist.vm.lyric.lineNo ++;
    }
}


playlist.vm = new Vue({
    el: '#playlist',
    data: {
        // 页面播放列表
        songs: [],
        currentSongId: null,
        currentSong: null,
        // 播放器的播放列表
        currentPlaySongs: [],
        // 播放器的播放列表的偏移量
        currentOffset: 0,
        hasPlay: false,
        cover: null,
        lyric: {
            lineNo: 1,
            lyricArray: [],
        }

    },
    created: function () {
        
    },
    methods: {
        play: function (song, index) {
            if (song.id == this.currentSongId && this.hasPlay) {
                return;
            }
            this.currentSongId = song.id;
            this.currentSong = song;
            // 记录当前播放歌曲
            window.localStorage.setItem('blog.currentSongId',song.id);
            // 调用播放器
            parent.window.ap.switch(song);
            this.cover = song.cover;
            this.hasPlay = true;
        }
    },
    computed: {
        lyrics: function () {
            if (this.currentSong != null) {
                let arr = this.currentSong.lrc.replaceAll("\"", "").split("\n");
                let lyricsArr = [];
                for (let i in arr) {
                    lyricsArr.push(arr[i].substring(arr[i].indexOf("]") + 1, arr[i].length))
                }
                return lyricsArr;
            }
            return [];
        }
    }

});




let fraction = 0.5;
let topNum = 0;
function lineHeight(lineno){
    var ul = $(".aplayer-lrc-contents");
    var $ul = document.getElementById('lyric');
    // 令正在唱的那一行高亮显示
    if (lineno > 0) {
        $(ul.find("li").get(topNum + lineno - 1)).removeClass("line-height");
    }
    var nowline = ul.find("li").get(topNum + lineno);
    $(nowline).addClass("line-height");

    // 实现文字滚动
    var _scrollTop;
    // $ul.scrollTop = 0;
    if ($ul.clientHeight * fraction > nowline.offsetTop) {
        _scrollTop = 0;
    } else if (nowline.offsetTop > ($ul.scrollHeight - $ul.clientHeight * (1 - fraction))) {
        _scrollTop = $ul.scrollHeight - $ul.clientHeight;
    } else {
        _scrollTop = nowline.offsetTop - $ul.clientHeight * fraction;
    }

    //以下声明歌词高亮行固定的基准线位置成为 “A”
    let top = 0;
    if ((nowline.offsetTop - $ul.scrollTop) >= $ul.clientHeight * fraction) {
        top = $ul.scrollTop + Math.ceil(nowline.offsetTop - $ul.scrollTop - $ul.clientHeight * fraction);
    } else if ((nowline.offsetTop - $ul.scrollTop) < $ul.clientHeight * fraction && _scrollTop != 0) {
        top = $ul.scrollTop - Math.ceil($ul.clientHeight * fraction - (nowline.offsetTop - $ul.scrollTop));
    } else if (_scrollTop == 0) {
        top = 0;
    } else {
        top = $(ul.find('li').get(0)).height();
    }
    ul.animate({
        scrollTop: top
    });
}