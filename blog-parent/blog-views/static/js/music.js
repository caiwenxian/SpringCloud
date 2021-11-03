/**
 * music模块 js
 */

var music = {};

music.init = function () {
    common.changeNav(3);
    // music.getContent();
    // music.vm.getMusicList();
    music.vm.listHotSong();
    //初始化播放器
    $('audio').audioPlayer();

    // music.search();
}
music.vm = new Vue({
    el: '.vue-content',
    data: {
        list: null,
        search: null,
        hotSong: {
            page: 1,
            totalSize: null
        }
    },
    created: function () {
        // this.getMusicList();
        // this.listHotSong();
    },
    methods: {
        /**获取文件列表*/
        getMusicList: function () {
            var url = '/blog/database/music-list.json';
            http.get(url, null, function (result) {
                music.vm.list = result;
            });
        },
        /* 搜索 */
        search: function () {
            //歌手
            var url = '/';
            http.get();
        },
        /** 获取热门歌曲 */
        listHotSong: function () {
            var url = '/song/hotsong/' + this.hotSong.page;
            http.get(url, null, function (result) {
                console.log("hotsong:", result);
                if (result.code === 0) {
                    music.vm.list = result.data.data;
                }
                // music.vm.list = result;
            });
        }
    }
});

music.getContent = function () {
    var url = '/blog/database/music.json';
    var data = null;
    http.get(url, data, function (result) {
        console.log(result)
    })
}

music.search = function () {
    const keywords = '海阔天空'
    const type = 1
    const limit = 30
    const data = 's=' + keywords + '&limit=' + limit + '&type=' + type + '&offset=0'

}