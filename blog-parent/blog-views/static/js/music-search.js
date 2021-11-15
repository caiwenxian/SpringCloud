var search = {};
search.init = function () {
    search.vm.listSearchRecord();
    search.vm.listHotSong();
};

search.vm = new Vue({
    el: '#music',
    data: {
        searchResultSongs: [],
        searchResultArtists: [],
        searchKey: null,
        showWhat: 'default',
        searchRecord: [],
        hotSongs: [],
        currentSong: null,
        currentSongIndex: 0,
    },
    methods: {
        searchSong: function (item) {
            search.vm.searchKey = item == null ? search.vm.searchKey : item;
            if (!search.vm.searchKey) {
                return;
            }
            var url = "/blog/song/search";
            http.get(url, {name: search.vm.searchKey}, function (result) {
                search.vm.showWhat = 'searchResult';
                if (result.data) {
                    search.vm.searchResultSongs = result.data.songs ? result.data.songs : [];
                    search.vm.searchResultArtists = result.data.artists ? result.data.artists : [];
                }
            });
        },
        reset: function () {
            search.vm.showWhat = 'default';
            search.vm.searchKey = null;
            this.listSearchRecord();
        },
        saveSearchRecord: function () {
            var searchRecord = window.localStorage.getItem('blog.searchRecord');
            if (searchRecord == null || searchRecord.length == 0) {
                searchRecord = [search.vm.searchKey];
                window.localStorage.setItem('blog.searchRecord', searchRecord)
                return;
            }
            searchRecord = searchRecord.split(",")
            if (searchRecord.length >= 20) {
                searchRecord.splice(-1, 1);
                searchRecord.unshift(search.vm.searchKey);
                window.localStorage.setItem('blog.searchRecord', searchRecord.toString())
                return;
            }
            searchRecord.unshift(search.vm.searchKey);
            window.localStorage.setItem('blog.searchRecord', searchRecord.toString())
        },
        listSearchRecord: function () {
            var searchRecord = window.localStorage.getItem('blog.searchRecord');
            if (searchRecord == null && searchRecord.length == 0) {
                return;
            }
            search.vm.searchRecord = searchRecord.split(",");
        },
        listHotSong: function () {
            var url = "/blog/song/hot/songs";
            http.get(url, null, function (result) {
                if (result.data) {
                    search.vm.hotSongs = result.data ? result.data : [];
                }
            });
        },
        playSong: function (song) {
            let mp3Urls = search.vm.getMp3Url([song.songId]);
            if (mp3Urls.length == 0) {
                layer.msg('暂无音源', {icon: 5});
                return;
            }
            let obj = {
                name: song.name,
                author: song.artistName,
                src: mp3Urls.mp3Url,
                cover: '/blog-views/static/images/music.png'
            }
            if (search.vm.currentSong == null) {
                parent.window.initMusic([obj]);
            } else {
                parent.window.addSong(obj, search.vm.currentSongIndex);
            }
            search.vm.currentSong = song.songId;
            search.vm.currentSongIndex ++;
        },
        playArtistSong: function (artist) {
            var url = "/blog/song/list/artist/song?artistId=" + artist.artistId;
            http.get(url, null, function (result) {
                if (result.data.length == 0) {
                    layer.msg('该歌手暂无歌曲', {icon: 5});
                    return;
                }
                let songIds = [];
                for (let i = 0; i < result.data.length; i ++) {
                    songIds.push(result.data[i].songId);
                }
                let mp3Urls = search.vm.getMp3Url(songIds);
                var songPlays = [];
                for (let i = 0; i < result.data.length; i ++) {
                    let mp3Url = null;
                    for (const [index, val] of mp3Urls.entries()) {
                        if (result.data[i].songId == val.songId) {
                            mp3Url = val.mp3Url;
                            break;
                        }
                    }
                    if (!mp3Url) {
                        continue;
                    }
                    let song = result.data[i];
                    var obj = {
                        name: song.name,
                        author: song.artistName,
                        src: mp3Url,
                        cover: '/blog-views/static/images/music.png'
                    }
                    songPlays.unshift(obj)
                }
                if (search.vm.currentSong == null) {
                    parent.window.initMusic(songPlays);
                } else {
                    parent.window.addSongList(songPlays);
                }
                search.vm.currentSong = songPlays[0].songId;
                search.vm.currentSongIndex ++;

            });
        },
        getMp3Url: function (songIds) {
            let url = "/blog/song/get/mp3";
            let data = {
                songIds: songIds
            }
            http.get(url, data, function (result) {
                return result.data
            });
        }
    }
});