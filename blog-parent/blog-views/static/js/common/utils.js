let array2String = function (array) {
    if (array == null || array.length == 0) {
        return "";
    }
    let result = '';
    for (let i in array) {
        result += JSON.stringify(array[i]);
    }
    return result;
}


let getElementByAttr = function (tag, attr, value) {
    let aElements = document.getElementsByTagName(tag);
    let aEle = [];
    for (let i = 0; i < aElements.length; i++) {
        if (aElements[i].getAttribute(attr) == value)
            aEle.push(aElements[i]);
    }
    return aEle;
}

let createElement = function(sHtml){
    // 创建一个可复用的包装元素
    var recycled = document.createElement('div'),
        // 创建标签简易匹配
        reg = /^<([a-zA-Z]+)(?=\s|\/>|>)[\s\S]*>$/,
        // 某些元素HTML标签必须插入特定的父标签内，才能产生合法元素
        // 另规避：ie7-某些元素innerHTML只读
        // 创建这些需要包装的父标签hash
        hash = {
            'colgroup': 'table',
            'col': 'colgroup',
            'thead': 'table',
            'tfoot': 'table',
            'tbody': 'table',
            'tr': 'tbody',
            'th': 'tr',
            'td': 'tr',
            'optgroup': 'select',
            'option': 'optgroup',
            'legend': 'fieldset'
        };
    // 闭包重载方法（预定义变量避免重复创建，调用执行更快，成员私有化）
    createElement = function(sHtml){
        // 若不包含标签，调用内置方法创建并返回元素
        if (!reg.test(sHtml)) {
            return document.createElement(sHtml);
        }
        // hash中是否包含匹配的标签名
        var tagName = hash[RegExp.$1.toLowerCase()];
        // 若无，向包装元素innerHTML，创建/截取并返回元素
        if (!tagName) {
            recycled.innerHTML = sHtml;
            return recycled.removeChild(recycled.firstChild);
        }
        // 若匹配hash标签，迭代包装父标签，并保存迭代层次
        var deep = 0, element = recycled;
        do {
            sHtml = '<' + tagName + '>' + sHtml + '</' + tagName + '>';
            deep++;
        }
        while (tagName = hash[tagName]);
        element.innerHTML = sHtml;
        // 根据迭代层次截取被包装的子元素
        do {
            element = element.removeChild(element.firstChild);
        }
        while (--deep > -1);
        // 最终返回需要创建的元素
        return element;
    }
    // 执行方法并返回结果
    return createElement(sHtml);
}

function insertAfter(newEl, targetEl) {
    var parentEl = targetEl.parentNode;

    if(parentEl.lastChild == targetEl)
    {
        parentEl.appendChild(newEl);
    }else
    {
        parentEl.insertBefore(newEl,targetEl.nextSibling);
    }
}

function isContain(list, item) {
    for (let i in list) {
        if (list[i] == item) {
            return true;
        }
    }
    return false;
}

function isContainField(list, field, item) {
    for (let i = 0; i = list.length; i ++) {
        if (list[i][field] == item) {
            return true;
        }
    }
    return false;
}

function indexOfField(list, field, item) {
    for (let i = 0; i < list.length; i ++) {
        if (list[i][field] == item) {
            return i;
        }
    }
    return -1;
}

function isBlank(obj) {
    if (obj == null || obj == 'null' || obj == '' || obj == undefined) {
        return true;
    }
    return false;
}

function isEmpty(list) {
    if (list == null || list.length == 0) {
        return true;
    }
    return false;
}

