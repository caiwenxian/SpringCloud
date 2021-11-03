var details = {};

details.init = function () {

    details.vm.getArticle();
    details.vm.listComment();

    common.checkStatus1(function (result) {
        if (result.code === 0) {
            $('.div-no-login').addClass("none");
            details.vm.user.status = 'IS_LOGIN'
        }
    });
};

details.vm = new Vue({
    el: '#vue-content',
    data: {
        article: {
            id: null
        },
        comment: {
            comment: null,
            page: 1,
            totalSize: 0,
            list: null,
            id: null,
            hasReply: false

        },
        user: {
            status: 'NOT_LOGIN'
        }


    },
    methods: {
        getArticle: function () {
            var vm = details.vm;
            var url = '/knowledge/article/' + vm.article.id;
            var data = null;
            http.get(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("获取文章失败");
                    return;
                }
                vm.article = result.data;

            });
        },
        /** 评论 */
        commitComment: function () {
            // layer.msg('暂不可评论', {offset: '200px'});
            var vm = details.vm;
            var url = '/knowledge/article/comment';
            vm.comment.articleId = vm.article.id;
            var data = {
                articleId: vm.comment.articleId,
                comment: vm.comment.comment
            };
            if ($.trim(data.comment) === '') {
                layer.msg("评论内容不能为空", {offset: '200px'});
                return;
            }
            http.post(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("评论失败", {offset: '200px'});
                    return;
                }
                layer.msg("评论成功", {offset: '200px'});
                vm.comment.comment = null;
                vm.listComment();

            });
        },
        /** 评论回复 */
        commitCommentReply: function (index) {
            // layer.msg('暂不可评论', {offset: '200px'});
            var vm = details.vm;
            var url = '/knowledge/article/comment/reply';
            vm.comment.articleId = vm.article.id;
            var data = {
                articleId: vm.comment.articleId,
                comment: $('.reply-comment').val(),
                parentId: vm.comment.id
            };
            if ($.trim(data.comment) === '') {
                layer.msg("回复内容不能为空", {offset: '200px'});
                return;
            }
            http.post(url, data, function (result) {

                if (result.code !== 0) {
                    layer.msg("评论失败", {offset: '200px'});
                    return;
                }
                layer.msg("评论成功", {offset: '200px'});
                vm.comment.comment = null;
                vm.hideComment(index, false);
                vm.listComment();

            });
        },
        /** 回复动作 */
        popReply: function (index, commentId) {
            if (this.comment.hasReply) {
                return;
            }
            this.comment.hasReply = true;
            this.comment.id = commentId;
            if (this.user.status === 'NOT_LOGIN') {
                layer.msg("请登录", {offset: '200px'});
                return;
            }
            $('.comment-bottom').addClass('none');
            var str = '<span class="hidden" onclick="details.vm.hideComment()">收起</span>\n' +
                '                        <div class="layui-input-block" style="margin-left: 80px !important;">\n' +
                '                        <textarea name="desc" placeholder="请输入内容" class="layui-textarea reply-comment" style="resize: none;"\n' +
                '                                  v-model="comment.comment"></textarea>\n' +
                '                        </div>\n' +
                '                        <button class="layui-btn" lay-submit lay-filter="formDemo" style="margin-left: 80px;"\n' +
                '                                onclick="details.vm.commitCommentReply(' + index + ')">提交评论\n' +
                '                        </button>';
            $('.comment-reply').eq(index).append(str);
        },
        /**收起回复编辑框*/
        hideComment: function (index, isDetails) {
            if (isDetails) {
                if ($('.li-comment').eq(index).find('.comment-reply-details').hasClass('none')) {
                    $('.li-comment').eq(index).find('.comment-reply-details').removeClass('none');
                    $('.comment-bottom .reply-btn').eq(index).text('收起回复');
                    return;
                }
                $('.li-comment').eq(index).find('.comment-reply-details').addClass('none');
                $('.comment-bottom .reply-btn').eq(index).text('展开回复');
                return;
            }
            $('.comment-reply').empty();
            $('.comment-bottom').removeClass('none');
            this.comment.hasReply = false;
        },
        /** 收起回复的内容 */
        /*hideReplyDetails: function (index) {
            $('.comment-reply-details').eq(index).addClass('none');
        },*/
        /** 获取评论列表 */
        listComment: function () {
            var vm = details.vm;
            var url = '/knowledge/article/comment/' + vm.comment.page;
            vm.comment.articleId = vm.article.id;
            var data = {
                articleId: vm.comment.articleId,

            };
            http.get(url, data, function (result) {
                if (result.code === 0) {
                    vm.comment.list = result.data.data;
                    vm.comment.totalSize = result.data.totalSize;
                }

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