
var ins = {};
ins.init = function () {
};

ins.vm = new Vue({
    el: '.vue-content',
    data: {
        list: [],
        search: {
            link: $('#link').val()
        }
    },
    created: function () {
    },
    methods: {

    },
    computed: {
    },
    watch: {

    },
    filters: {

    }
});

/* 搜索 */
ins.search = function () {
    layer.open({
        title: '信息',
        content: '正在搜索'
    });
    ins.vm.search = {
        link: $('#link').val()
    };
    var url = '/ins/link/picture/url';
    http.get(url, ins.vm.search, function (result) {
        ins.vm.list = result.data;
    });
}
