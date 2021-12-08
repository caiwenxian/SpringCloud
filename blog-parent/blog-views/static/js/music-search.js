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
        searchResultSongTotal: 0,
        searchKey: null,
        searchResultPage: 1,
        searchOldKey: null,
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
            if (search.vm.searchOldKey != null) {
                search.vm.searchResultPage = search.vm.searchOldKey == item ? search.vm.searchResultPage : 1;
            }
            let params = {
                searchKey: search.vm.searchKey,
                pageIndex: search.vm.searchResultPage
            }
            var url = "/blog/song/search";
            http.get(url, params, function (result) {
                search.vm.showWhat = 'searchResult';
                if (result.data) {
                    if (search.vm.searchKey != search.vm.searchOldKey) {
                        search.vm.searchResultSongs = [];
                    }
                    search.vm.searchResultSongs.push.apply(search.vm.searchResultSongs, result.data.songs.list);
                    search.vm.searchResultArtists = result.data.artists ? result.data.artists : [];
                    search.vm.searchResultSongTotal = result.data.songs.page.totalItem;
                }
            });
            search.vm.searchOldKey = item;
        },
        searchMore: function () {
            search.vm.searchResultPage ++;
            this.searchSong(search.vm.searchKey);
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
            search.vm.getMp3Url([song.songId], function (mp3Urls) {
                if (mp3Urls.length == 0) {
                    layer.msg('暂无音源', {icon: 5});
                    return;
                }
                /*let obj = {
                    name: song.name,
                    author: song.artistName,
                    src: mp3Urls[0].mp3Url,
                    cover: '/blog-views/static/images/music.png',
                    id: song.songId
                }*/
                let obj = {
                    name: song.name,
                    artist: song.artistName,
                    url: mp3Urls[0].mp3Url,
                    cover: isBlank(song.cover) ? '/blog-views/static/images/music.png' : song.cover + '?param=430y430',
                    id: song.songId
                }
                /*if (parent.player == null) {
                    parent.window.initMusic([obj]);
                } else {
                    parent.window.addSong(obj);
                }*/
                if (!parent.ap.isStart) {
                    parent.window.ap.init([obj]);
                } else {
                    parent.window.ap.addOne(obj);
                }
                search.vm.currentSong = song.songId;
                search.vm.currentSongIndex ++;
                // parent.window.document.getElementById("music-playlist").contentWindow.playlist.pushSong(obj);
            });


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
                search.vm.getMp3Url(songIds, function (mp3Urls) {
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
            });
        },
        getMp3Url: function (songIds, callback) {
            let url = "/blog/song/get/mp3";
            let data = {
                songIds: songIds
            }
            http.post(url, JSON.stringify(data), function (result) {
                callback(result.data);
            }, http.CONTENT_TYPE_ONE);
        }
    },
    computed: {
    }
});