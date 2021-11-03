var article = {};

article.init = function () {

    article.vm.listArticle();
};

article.vm = new Vue({
    el: '#vue-content',
    data: {
        article: {
            list: null,
            page: 1,
            totalSize: 0,
            current: null,
            searchKeyWord: null

        }

    },
    methods: {
        listArticle: function () {
            var vm = article.vm;
            var url = '/knowledge/articles/' + vm.article.page;
            var data = {
                title: vm.article.searchKeyWord
            };
            http.get(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("获取文章列表失败");
                    return;
                }
                vm.article.list = result.data.data;
                vm.article.totalSize = result.data.totalSize;

            });
        },
        goDetails: function (id) {
            var data = {
                id: id
            }
            var url = '/knowledge/article/browseAmount/' + id;
            http.put(url, data, null);
            window.location.href = '/knowledge/article/details/' + id;
        }
    },
    computed: {
        articlePage: function () {
            return this.article.page;
        }
    },
    watch: {
        articlePage: function () {
            this.listArticle();
        }
    },
    filters: {
        date: function (value, fmt) {
            if (null === value) {
                return null;
            }
            return new Date(value).Format(fmt);
        }
    }
});