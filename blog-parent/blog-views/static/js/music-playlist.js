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
        } else {
            // 将数组进行位移
            for (let i = 0; i < playlist.vm.songs.length; i ++) {
                let offset = playlist.vm.songs.length - Number(index)
                let newIndex = (offset + i) % playlist.vm.songs.length;
                playlist.vm.currentPlaySongs[newIndex] = playlist.vm.songs[i];
                playlist.vm.currentOffset = offset;
            }
            // console.log('playlist.vm.currentPlaySongs: ' + JSON.stringify(playlist.vm.currentPlaySongs))
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
        cover: item.cover
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

});