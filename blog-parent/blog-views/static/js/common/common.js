var common = {};

/**改变导航栏样式*/
common.changeNav = function (index) {
    $('.nav-ul li').eq(index).addClass('layui-this').siblings().removeClass('layui-this');
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(function () {
    //.ajaxError事件定位到document对象，文档内所有元素发生ajax请求异常，都将冒泡到document对象的ajaxError事件执行处理
    $(document).ajaxError(
        //所有ajax请求异常的统一处理函数，处理
        function (event, xhr, options, exc) {
            if (xhr.status == 'undefined') {
                return;
            }
            switch (xhr.status) {
                case 403:
                    // 未授权异常
                    alert("系统拒绝：您没有访问权限。");
                    break;

                case 404:
                    alert("您访问的资源不存在。");
                    break;
            }
        }
    );
});

/*common.checkStatus = function () {
    var url = '/account/checkStatus';
    http.get(url, null, function (result) {
        $('.img-photo').css('margin-top', '0px');
        if (result.code === 401) {  //未登录
            $('.li-login').removeClass('none');
            $('.li-register').removeClass('none');
            $('.li-logout').addClass('none');
            return;
        }
        $('.li-logout').removeClass('none');
    })
}*/

common.checkStatus1 = function (callback) {
    var url = '/account/checkStatus';
    http.get(url, null, function (result) {
        if (typeof callback === 'function') {
            return callback(result);
        }
    })
}