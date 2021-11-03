var publish = {};

publish.init = function () {

};

publish.vm = new Vue({
    el: '#vue-content',
    data: {
        articleContent: '',
        articleType: 'TECHNOLOGY',
        articleTitle: null,
        articleContentText: null
    },
    methods: {
        publish: function () {
            publish.vm.articleContent = editor.txt.html();
            publish.vm.articleContentText = editor.txt.text();
            publish.vm.articleContent = publish.vm.articleContent.replace(/<pre><code>/g, "<pre class=layui-code>");
            publish.vm.articleContent = publish.vm.articleContent.replace(/<\/code><\/pre>/g, "</pre>");
            var url = '/knowledge/publish';
            var data = {
                content: publish.vm.articleContent,
                type: publish.vm.articleType,
                title: publish.vm.articleTitle,
                contentText: publish.vm.articleContentText
            };
            if ($.trim(data.content) === '' || $.trim(data.type) === '' || $.trim(data.title) === '') {
                layer.msg('请完善文章信息');
                return;
            }
            http.post(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("发布失败");
                    return;
                }
                layer.msg("发布成功");
                window.location.href = '/knowledge';
                // music.vm.list = result;
            });
        },
        saveDraft: function () {
            publish.vm.articleContent = editor.txt.html();
            publish.vm.articleContentText = editor.txt.text();
            publish.vm.articleContent = publish.vm.articleContent.replace(/<pre><code>/g, "<pre class=layui-code>");
            publish.vm.articleContent = publish.vm.articleContent.replace(/<\/code><\/pre>/g, "</pre>");

            var url = '/knowledge/draft';
            var data = {
                content: publish.vm.articleContent,
                type: publish.vm.articleType,
                title: publish.vm.articleTitle,
                contentText: publish.vm.articleContentText
            };
            if ($.trim(data.content) === '' || $.trim(data.type) === '' || $.trim(data.title) === '') {
                layer.msg('请完善文章信息');
                return;
            }
            http.post(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("保存失败");
                    return;
                }
                layer.msg("保存成功");
                // music.vm.list = result;
            });
        },
        preview: function () {

            publish.vm.articleContent = editor.txt.html();
            publish.vm.articleContent = publish.vm.articleContent.replace(/<pre><code>/g, "<pre class=layui-code>");
            publish.vm.articleContent = publish.vm.articleContent.replace(/<\/code><\/pre>/g, "</pre>");
            console.log('publish.vm.articleContent:' + publish.vm.articleContent)
            // publish.vm.articleContent.replace("pre/>", "<pre class='layui-code'");
            layer.open({
                type: 1,
                area: ['1200px', '750px'],
                offset: '70px',
                title: '预览',
                shadeClose: true,
                content: $('#preview'),
                success: function () {

                    layui.use('code', function () {

                        layui.code(); //
                    });
                }
            });
        }
    },
    filters: {
        date: function (value, fmt) {
            if (null === value) {
                return null;
            }
            return new Date(value, fmt);
        }
    }
});