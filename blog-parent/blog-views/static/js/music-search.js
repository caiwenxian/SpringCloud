var search = {};
search.init = function () {


};

search.vm = new Vue({
    el: '#music',
    data: {
        searchResultSongs: [],
        searchResultArtists: [],
        searchKey: null,
        showWhat: 'default',
    },
    methods: {
        searchSong: function () {
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
        }
    }
});