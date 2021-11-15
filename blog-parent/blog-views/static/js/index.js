var cu = {
    cube: document.querySelector('.cube'),
    timer: null,
    x: 0,
    y: 0,
};
cu.init = function() {
    cu.cube = document.querySelector('.cube');
    clearInterval(cu.timer);
    cu.timer = setInterval(() => {
        rotate();
    }, 1000);
    //考虑兼容性
    var hiddenProperty = 'hidden' in document ? 'hidden' :
        'webkitHidden' in document ? 'webkitHidden' :
            'mozHidden' in document ? 'mozHidden' :
                null;
    var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
    var onVisibilityChange = function () {
        if (!document[hiddenProperty]) {
            cu.timer = setInterval(() => {
                rotate();
            }, 1000);
            console.log(hiddenProperty);
        } else {
            clearInterval(cu.timer)
        }
    }
    //不考虑兼容性
    document.addEventListener(visibilityChangeEvent, onVisibilityChange);
    rotate();
}
function rotate() {
    cu.cube.style.transform = 'rotateX(' + cu.x + 'deg)' + '' + 'rotateY(' + cu.y + 'deg)';
    cu.x += 30
    cu.y += 45
}
function stop() {
    clearInterval(cu.timer);
}

