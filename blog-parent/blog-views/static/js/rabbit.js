


var rabbit = {};
rabbit.init = function () {
    rabbit.vm.listRabbitMessage();


};

rabbit.vm = new Vue({
    el: '.vue-content',
    data: {
        list: null,
        search: {
            id: $('#message-id').val(),
            status: $('#status').val(),
        },
        page: 1,
        totalSize: 0
    },
    created: function () {
        // this.getrabbitList();
        // this.listHotSong();
    },
    methods: {
        /**获取消息列表*/
        listRabbitMessage: function (notInitPage) {
            var index = null;
            layui.use(['layer'], function () {
                index = layui.layer.load(2);
            });


            var url = '/blog/mq/message/page';
            this.search.pageIndex = rabbit.vm.page;
            http.get(url, this.search, function (result) {
                layui.use(['layer'], function () {
                    layui.layer.close(index);
                });
                rabbit.vm.list = result.data.list;
                if (!notInitPage) {
                    rabbit.vm.totalSize = result.data.page.totalItem;
                }
            });
        },
        /* 搜索 */
        search: function () {
            var url = '/';
            http.get();
        },

    },
    computed: {
        rabbitPage: function () {
            return this.page;
        },
        rabbitTotalSize: function () {
            return this.totalSize;
        }
    },
    watch: {
        rabbitPage: function () {
            this.listRabbitMessage(true);
        },
        rabbitTotalSize: function () {
            layui.use('laypage', function() {
                layui.laypage.render({
                    elem: 'page'
                    , count: rabbit.vm.totalSize
                    , limit: 15
                    , theme: '#1E9FFF'
                    , jump: function (obj, first) {
                        if (!first) {
                            rabbit.vm.page = obj.curr;

                        }
                    }
                });
            });
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

/** 查看消息详细内容 */
rabbit.showMessageDetails = function (index) {
    var message = rabbit.vm.list[index].data;
    try {
        message = JSON.parse(message);
        message = JSON.stringify(message, null, "\t");
        message = message.replace(/\\"/g, "'");
        message = jsonShowFn(message);
    }catch (e) {

    }

    layer.open({
        type: 1,
        area: ['900px', '450px'],
        offset: '70px',
        title: '预览',
        shadeClose: true,
        content: '<div><pre>'+ message +'</pre></div>',
        success: function () {

        }
    });
};

/** 查询 */
rabbit.select = function () {
    var index = null;
    layui.use(['layer'], function () {
        index = layui.layer.load(2);
    });
    rabbit.vm.search = {
        mqId: $('#message-id').val(),
        status: $('#status').val()
    };
    var url = '/blog/mq/message/page';
    rabbit.vm.search.pageIndex = rabbit.vm.page;
    http.get(url, rabbit.vm.search, function (result) {
        layui.use(['layer'], function () {
            layui.layer.close(index);
        });
        rabbit.vm.list = result.data.list;
        rabbit.vm.totalSize = result.data.page.totalItem;
    });
}

/** 重置 */
rabbit.reset = function () {
    var index = null;
    layui.use(['layer'], function () {
        index = layui.layer.load(2);
    });
    rabbit.vm.search = {
        mqId: '',
        status: null
    };
    var url = '/blog/mq/message/page';
    rabbit.vm.page = 1;
    rabbit.vm.search.pageIndex = rabbit.vm.page;
    http.get(url, rabbit.vm.search, function (result) {
        layui.use(['layer'], function () {
            layui.layer.close(index);
        });
        rabbit.vm.list = result.data.list;
        rabbit.vm.totalSize = result.data.page.totalItem;
    });
}

/** 重新发送消息 */
rabbit.reSendMessage = function (id) {
    var url = '/rabbitmq/reSendRabbitMessage';

    http.post(url, {id: id}, function (result) {
        layer.open({
            title: '信息',
            content: result.msg
        });
        rabbit.select();
    });
}


function jsonShowFn(json){
    if (!json.match("^\{(.+:.+,*){1,}\}$")) {
        return json           //判断是否是json数据，不是直接返回
    }

    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}