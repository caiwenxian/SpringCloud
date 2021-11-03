/**
 * life模块 js
 */

var life = {};

life.init = function () {
    common.changeNav(2);
    life.initCarousel();

}
life.vm = new Vue({
    el: '.vue-content',
    data: {
        list: null
    },

    methods: {}
});

life.initCarousel = function () {
    if (carousel == undefined) {
        setTimeout(function () {
            carousel.render({
                elem: '#carousel'
                , arrow: 'always'
                , width: '100%'
                , height: '280px'
            });
        }, 300);
    } else {
        carousel.render({
            elem: '#carousel'
            , arrow: 'always'
            , width: '100%'
            , height: '280px'
        });
    }
}
