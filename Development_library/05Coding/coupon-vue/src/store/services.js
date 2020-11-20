// 其它封装的函数
class Services {
    constructor () {
        this.ls = window.localStorage;
    }
    // 设置cookie
    setCookie (name, value, day) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object') {
            for (var i in setting) {
                oDate = new Date();
                oDate.setDate(oDate.getDate() + day);
                document.cookie = i + 'HFT=' + escape(setting[i]) + ';expires=' + oDate;
            }
        } else {
            var oDate = new Date();
            oDate.setDate(oDate.getDate() + day);
            document.cookie = name + 'HFT=' + escape(value) + ';expires=' + oDate;
        }
    };
    // 获取cookie
    getCookie (name) {
        var arr = document.cookie.split('; ');
        name = name + 'HFT';
        for (var i = 0; i < arr.length; i++) {
            var arr2 = arr[i].split('=');
            if (arr2[0] === name) {
                return unescape(arr2[1]);
            }
        };
        return '';
    };
    // 删除cookie
    removeCookie (name) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Array') {
            setting.forEach((item) => {
                this.setCookie(item, 1, -1);
            });
        } else {
            this.setCookie(name, 1, -1);
        }
    };
    // 设置localStorage*/
    setLocal(key, val) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object') {
            for (var i in setting) {
                this.ls.setItem(i + 'HFT', JSON.stringify(setting[i]));
            }
        } else {
            this.ls.setItem(key + 'HFT', JSON.stringify(val));
        }
    };
    // 获取localStorage*/
    getLocal(key) {
        key = key + 'HFT';
        if (key) return JSON.parse(this.ls.getItem(key));
        return null;
    };
    // 移除localStorage*/
    removeLocal(key) {
        key = key + 'HFT';
        this.ls.removeItem(key);
    };
    // 移除所有localStorage*/
    clearLocal() {
        this.ls.clear();
    };
    // 序列化方法
    stringify(Object) {
        var string = '';
        for (let key in Object) {
            string += key + '=' + Object[key] + '&';
        }
        return string;
    }
}

export default new Services();
