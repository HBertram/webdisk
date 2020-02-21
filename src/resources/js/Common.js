
String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2)
};

/*
 @ 使用AJAX发送请求，并处理返回结果
 @ para[in] strUrl URL
 @ para[in] oJsonInput Post 的JSON数据
 @ para[in] failedCallback 失败的回调，如果返回true则表示处理成功，否则将会继续执行默认处理
 @ para[in] callBack 回调函数，该函数接受一个返回json结构数据参数
 */
function PostDataAsync(strUrl, oJsonInput, callBack, failedCallback) {
    PostData(strUrl, oJsonInput, callBack, failedCallback, false)
}

function PostData(strUrl, oJsonInput, callBack, failedCallback, isAsync) {
    if (typeof oJsonInput !== "string") {
        try {
            oJsonInput = JSON.stringify(oJsonInput)
        } catch (e) {
            console.error(r)
        }
    }

    let ajaxObject = {
        type: "POST", //访问WebService使用Post方式请求
        contentType: "application/json;charset=utf-8", //WebService 会返回Json类型
        url: strUrl, //调用WebService
        data: oJsonInput,
        dataType: 'json',
        beforeSend: function (x) {
            x.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        },
        error: function (xhr, errorText, errorType) {
            if (typeof(failedCallback) === "function") {
                failedCallback({})
            } else {
                console.log(JSON.stringify(xhr));
                console.log(xhr.status + " : " + xhr.statusText);
                console.log(errorType + " : " + errorText);
            }
        },
        success: function (result) { //回调函数，result，返回值
            if (result === "") {
                callBack(result);
                return;
            } else if (result.d === "") {
                callBack(result.d);
                return;
            }
            let jsonOutput = null;
            try {
                if (result.d !== undefined) jsonOutput = $.parseJSON(result.d);
                else if (typeof(result) === "string") jsonOutput = $.parseJSON(result);
                else if (typeof(result) === "object") jsonOutput = result;
            } catch (ex) {
            }
            if (jsonOutput == null) {
                if (result.d === undefined) callBack(result);
                else callBack(result.d);
                return;
            }
            //以下为反序列化成功的数据回调处理
            if (!jsonOutput.m_strCodeNum) {
                callBack(jsonOutput);
            } else if("00000" === jsonOutput.m_strCodeNum) {
                if (undefined != jsonOutput.m_obj) callBack(jsonOutput.m_obj);
                else callBack(jsonOutput);
            } else {
                if (typeof (failedCallback) === "function") {
                    if (false !== failedCallback(jsonOutput)) {
                        return;
                    }
                }
                let strErrorHint = `操作失败 : ${jsonOutput.m_strErrorinfo || ""}    错误码 : ${jsonOutput.m_strCodeNum}`;
                if (!!jsonOutput.m_strErrorinfo) {
                    strErrorHint = `操作失败 : ${jsonOutput.m_strErrorinfo || ""}`;
                }
                try {
                    alert(strErrorHint, "操作失败");
                } catch (e) {
                    alert(strErrorHint);
                }
            }
        }
    };
    if (isAsync === false) {
        ajaxObject.async = false
    }
    $.ajax(ajaxObject);
}

function Ajax(url, data, successed, async) {
    $.ajax({
        type: 'POST',
        contentType: "application/json;charset=utf-8",
        url: url,
        async: !!async,
        data: data,
        dataType: 'json',
        beforeSend: function (x) {
            x.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        },
        error: function (x, e) {
            console.log(x);
            console.log(e);
        },
        success: successed
    });
}

/**
 * 从URL获取参数
 * @param name 参数名
 * @param defaultValue 默认值
 * @param location 可选参数为window.location对象，如果不传入则使用当前页面
 * @returns 返回URL中的参数或者无参数时返回默认值
 */
function getUrlParam(name, defaultValue, location) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    let r = (location || window.location).search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return defaultValue;
}

/**
 * 替换URL中的某个参数
 * @param url 原始URL
 * @param arg 被替换参数
 * @param val 替换值
 * @returns {*}
 */
function changeUrlArg(url, arg, val) {
    let pattern = arg + '=([^&]*)';
    let replaceText = arg + '=' + val;
    return url.match(pattern) ? url.replace(eval('/(' + arg + '=)([^&]*)/gi'), replaceText) : (url.match('[\?]') ? url + '&' + replaceText : url + '?' + replaceText);
}

/**
 * 类的深拷贝
 * */
let objDeepCopy = function (source) {
    let sourceCopy = source instanceof Array ? [] : {};
    for (let item in source) {
        sourceCopy[item] = typeof(source[item]) === 'object' ? objDeepCopy(source[item]) : source[item];
    }
    return sourceCopy;
};
