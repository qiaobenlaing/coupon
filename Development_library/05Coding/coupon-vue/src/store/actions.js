import Services from './services';
import axios from 'axios';
const jsonp = require('jsonp');
// 百度地图接口数据
var ipurl = 'https://api.map.baidu.com/location/ip';
var h5url = 'https://api.map.baidu.com/geocoder/v2/';
const ak = 'gWgw4P7VsdvIduiXqujiRW3ObT3lkmhD';
const localCity = '武汉市';
const latitude = '30.878131';
const longitude = '120.137198';
export default{
    // 刷新页面的免确认登录
    initLogin({state, commit}, query) {
        return new Promise((resolve, reject) => {
            // 无论登录与否，也会通过上一次的浏览痕迹存储以下信息
            if (Services.getCookie('localCity') !== '' && Services.getCookie('localCity') !== undefined) {
                commit('setState', {
                    'localCity': Services.getCookie('localCity'),
                    'longitude': Services.getCookie('longitude'),
                    'latitude': Services.getCookie('latitude')
                });
                // commit('setStateItem', {name: 'zoneId', data: Services.getCookie('zoneId')});
            } else {
                commit('setState', {
                    'localCity': localCity,
                    'longitude': longitude,
                    'latitude': latitude
                });
            }
            if (!state.isLogin) {
                if (Services.getCookie('userCode') === '' || Services.getCookie('userCode') === undefined) {
                    commit('setStateItem', {name: 'isLogin', data: false});
                    reject();
                } else {
                    commit('setState', {
                        'mobileNbr': Services.getCookie('mobileNbr'),
                        'userCode': Services.getCookie('userCode')
                    });
                    if (state.appType === 1) {
                        commit('setState', {
                            'zoneId': Services.getCookie('zoneId')
                        });
                    }
                    commit('setStateItem', {name: 'isLogin', data: true});
                    resolve();
                }
            }
        });
    },
    // 用户名密码登录（非静默登录）
    loginUser({state, commit}, user) {
        return new Promise((resolve, reject) => {
            axios({
                url: state.global.hftcomShop,
                method: 'POST',
                data: '{"id": 67,"jsonrpc": "2.0","method": "mobileLogin","params": {"mobileNbr": "' + user.mobileNbr + '","Vcode":"' + user.Vcode + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((data) => {
                if (data.data.result.Code === 50000) {
                    Services.setCookie({
                        'mobileNbr': data.data.result.mobileNbr,
                        'userCode': data.data.result.userCode
                    }, null, 1);
                    commit('setState', {
                        'mobileNbr': data.data.result.mobileNbr,
                        'userCode': data.data.result.userCode
                    });
                    commit('setStateItem', {name: 'isLogin', data: true});
                    resolve(true);
                } else {
                    reject(data.data.result.error);
                }
            });
        });
    },
    // 用户名密码注册（非静默）
    registerUser({state, commit}, user) {
        return new Promise((resolve, reject) => {
            axios({
                url: state.global.hftcomShop,
                method: 'POST',
                data: '{"id": 67,"jsonrpc": "2.0","method": "registerMobile","params": {"mobileNbr": "' + user.mobileNbr + '","Vcode":"' + user.Vcode + '"}}',
                contentType: 'application/json'
            }).then((data) => {
                if (data.data.result.code === 50000) {
                    commit('setState', {
                        'mobileNbr': user.mobileNbr,
                        'userCode': data.data.result.userCode
                    });
                    Services.setCookie({
                        'mobileNbr': state.mobileNbr,
                        'userCode': state.userCode
                    }, null, 1);
                    commit('setStateItem', {name: 'isLogin', data: true});
                    resolve(true);
                } else {
                    reject(data.data.result.error);
                }
            });
        });
    },
    // 分配各种来源跳转过来的登录
    loginApp({state, dispatch, commit}, query) {
        return new Promise((resolve, reject) => {
            // 判断是否是微信浏览器
            dispatch('isWeiXin').then(() => {
                switch (query.to.from) {
                    // 使用优惠券时跳转过来
                case 'weixin' :
                    commit('setStateItem', {name: 'from', data: query.to.from});
                    console.log(state.from);
                    dispatch('loginWeiXin_other', query);
                    break;
                    // 微信浏览器默认登录方式
                default :
                    if (query.to.openid === '' || query.to.openid === undefined) {
                        // console.error('没有openId，无法登录');
                    } else {
                        dispatch('loginWeiXin', query);
                    }
                }
                resolve();
            }).catch(() => {
                // 不是来自微信浏览器
                if (JSON.stringify(query.to) === '{}') {
                    for (var item in query.to) {
                        query.to[item] = '';
                    }
                    reject();
                } else {
                    if (query.to.IcbcApp !== '' && query.to.IcbcApp !== undefined) {
                        dispatch('loginICBC', query);
                    }
                    resolve();
                }
            });
            // 运营版无论来源于哪里，都必须有商圈id，携带，则刷新zoneId
            if (state.appType === 1) {
                if (query.to.zoneId !== '' && query.to.zoneId !== undefined) {
                    commit('setStateItem', {name: 'zoneId', data: query.to.zoneId});
                    Services.setCookie('zoneId', state.zoneId, 1);
                } else {
                    if (state.zoneId === '') {
                        console.error('必须携带zoneId');
                    }
                };
            }
        });
    },
    loginICBC({state, commit}, query) {
        return new Promise((resolve, reject) => {
            // 关于IcbcApp与WeiApp
            axios({
                url: state.global.hftcomShop,
                method: 'POST',
                data: '{"id":19,"jsonrpc":"2.0","method":"BankLogin","params":{"IcbcApp":"' + query.to.IcbcApp + '","WeiApp":""}}',
                contentType: 'application/json'
            }).then((data) => {
                Services.setCookie({
                    'userCode': data.data.result.userCode,
                    'mobileNbr': data.data.result.mobileNbr
                }, null, 1);
                commit('setState', {
                    'userCode': data.data.result.userCode,
                    'mobileNbr': data.data.result.mobileNbr
                });
                commit('setStateItem', {name: 'isLogin', data: true});
                resolve();
            });
        });
    },
    loginWeiXin({state, commit}, query) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url: state.global.getAT,
                data: '',
                dataType: 'json'
            }).then((result) => {
                axios({
                    url: state.global.hftcomShop,
                    method: 'POST',
                    data: '{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"openId":"' + query.to.openid + '","access_token":"' + result.data.access_token + '","zoneId":"' + query.to.zoneId + '"}}',
                    contentType: 'application/json'
                }).then((data) => {
                    Services.setCookie('userCode', data.data.result.userCode, 1);
                    commit('setStateItem', {name: 'userCode', data: data.data.result.userCode});
                    if (state.userCode === undefined) {
                        console.log('登录未成功');
                    } else {
                        commit('setStateItem', {name: 'isLogin', data: true});
                    }
                });
            });
        });
    },
    loginWeiXin_other({state, commit}, query) {
        if (query.to.from === 'weixin') {
            state.isChooseCity = true;
            return new Promise((resolve, reject) => {
                axios({
                    method: 'get',
                    url: state.global.getAT,
                    data: '',
                    dataType: 'json'
                }).then((result) => {
                    axios({
                        url: state.global.hftcomShop,
                        method: 'POST',
                        data: '{"id":19,"jsonrpc":"2.0","method":"getUserInfo","params":{"openId":"' + query.to.openid_other + '","access_token":"' + result.data.access_token + '","zoneId":"' + query.to.zoneId + '"}}',
                        contentType: 'application/json'
                    }).then((data) => {
                        Services.setCookie('userCode', data.data.result.userCode, 1);
                        commit('setStateItem', {name: 'userCode', data: data.data.result.userCode});
                        if (state.userCode === undefined) {
                            console.log('登录未成功');
                        } else {
                            commit('setStateItem', {name: 'isLogin', data: true});
                        }
                    });
                });
            });
        }
    },
    // 退出登录清除所有用户数据
    clearCookie({state, commit}) {
        Services.removeCookie(['mobileNbr', 'userCode', 'longitude', 'latitude', 'localCity']);
        Services.setCookie({'localCity': localCity, 'latitude': '30.878131', 'longitude': '120.137198'}, null, 1);
        commit('setState', {
            'mobileNbr': '',
            'userCode': '',
            'localCity': localCity, // 默认城市
            'latitude': latitude,
            'longitude': longitude,
            'isLogin': false
        });
    },
    // h5获取定位
    getLocation({state, dispatch}) {
        return new Promise((resolve, reject) => {
            var options = {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            };
            // 成功时
            function onSuccess(position) {
                  // 返回用户位置
                  // 经度
                var longitude = position.coords.longitude;
                  // 纬度
                var latitude = position.coords.latitude;
                // loca为传到百度逆解析拼接的字符串，location为函数更新经纬度的对象
                var loca = latitude + ',' + longitude;
                var location = {};
                location.hftsource = 'navigator';
                location.longitude = longitude;
                location.latitude = latitude;
                h5url += '?' + 'callback=renderReverse&location=' + loca + '&output=json&pois=1&ak=' + ak;
                // 更新经纬度
                dispatch('setLocation', location);
                jsonp(h5url, null, (err, data) => {
                    if (err) {
                        reject(err);
                    } else {
                        console.log('h5位置');
                        // 只以字符串传回城市
                        resolve(data.result.addressComponent.city);
                    }
                });
            };
              // 失败时
            function onError(error) {
                switch (error.code) {
                case error.PERMISSION_DENIED:
                    reject('用户拒绝对获取地理位置的请求');
                    break;
                case error.POSITION_UNAVAILABLE:
                    reject('位置信息是不可用的');
                    break;
                case error.TIMEOUT:
                    reject('请求用户地理位置超时');
                    break;
                case error.UNKNOWN_ERROR:
                    reject('未知错误');
                    break;
                }
            };
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(onSuccess, onError, options);
            } else {
                reject('该浏览器不支持获取地理位置');
            };
        });
    },
    // 百度ip获取地理位置
    getLocationBaidu({ state, dispatch }) {
        return new Promise((resolve, reject) => {
            ipurl += '?' + 'ak=' + ak + '&ip=' + '' + '&coor=' + '';
            jsonp(ipurl, null, (err, data) => {
                if (err) {
                    reject(err);
                } else {
                    data.hftsource = 'baiduip';
                    dispatch('setLocation', data);
                    resolve(data);
                }
            });
        });
    },
    getOpenCity({state, dispatch}) {
        return new Promise((resolve, reject) => {
            if (state.appType === 0) {
                if (state.parentId === Number || state.parentId === undefined) {
                    axios({
                        method: 'post',
                        url: state.global.hftcomClient,
                        data: '{"id":19,"jsonrpc":"2.0","method":"listOpenCity","params":{"parentId":25}}'
                    }).then((result) => {
                        resolve(result.data);
                    });
                } else {
                    axios({
                        method: 'post',
                        url: state.global.hftcomClient,
                        data: '{"id":19,"jsonrpc":"2.0","method":"listOpenCityH5","params":{"parentId":' + state.parentId + '}}'
                    }).then((result) => {
                        if (result.data.result.length !== 0) {
                            state.isChooseCity = true;
                            dispatch('setLocalCity', result.data.result[0].name);
                        }
                        resolve(result.data);
                    });
                }
            } else {
                if (state.zoneId === Number || state.zoneId === undefined) {
                    console.log('没有选择商圈');
                } else {
                    axios({
                        method: 'post',
                        url: state.global.hftcomClient,
                        data: '{"id":19,"jsonrpc":"2.0","method":"zonelistOpenCity","params":{"zoneId":' + state.zoneId + '}}'
                    }).then((result) => {
                        if (result.data.result.length !== 0) {
                            state.isChooseCity = true;
                        }
                        resolve(result.data);
                    });
                }
            }
        });
    },
    setLocalCity({state}, data) {
        return new Promise((resolve, reject) => {
            if (data !== '') {
                state.localCity = data;
                console.log('设置cookie' + state.localCity);
                Services.setCookie('localCity', state.localCity, 1);
                resolve();
            }
        });
    },
    setLocation({state}, data) {
        return new Promise((resolve, reject) => {
            if (data.hftsource === 'navigator') {
                // 浏览器原生获取的经纬度
                state.longitude = data.longitude;
                Services.setCookie('longitude', state.longitude, 1);
                state.latitude = data.latitude;
                Services.setCookie('latitude', state.latitude, 1);
                return;
            } else if (data.hftsource === 'baiduip') {
                // 百度ip获取的经纬度
                state.longitude = data.content.point.x;
                state.latitude = data.content.point.y;
                Services.setCookie('longitude', state.longitude, 1);
                Services.setCookie('latitude', state.latitude, 1);
                return;
            } else {
                // 直接点击设置城市的中心经纬度
                h5url += '?address=' + data + '&output=json&ak=' + ak;
                jsonp(h5url, null, (err, data) => {
                    if (err) {
                    } else {
                        state.longitude = data.result.location.lng;
                        Services.setCookie('longitude', state.longitude, 1);
                        state.latitude = data.result.location.lat;
                        Services.setCookie('latitude', state.latitude, 1);
                    }
                });
            }
        });
    },
    getCollectInfo({state, commit}) {  // 获取收藏与领取的优惠券信息，专门用于优惠券的领取按钮，收藏按钮的显示
        return new Promise((resolve, reject) => {
            axios({
                url: state.global.hftcomShop,
                method: 'POST',
                data: '{"id":19,"jsonrpc":"2.0","method":"isCollectGet","params":{"userCode":"' + state.userCode + '","zoneId": "' + state.zoneId + '"}}'
            }).then((data) => {
                commit('setStateItem', {name: 'CouponListInfo', data: data.data.result});
                resolve(state.CouponListInfo);
            });
        });
    },
    getCollectShop({state, commit}) { // 获取收藏的商户列表，也用于收藏按钮的显示与否
        return new Promise((resolve, reject) => {
            axios({
                method: 'post',
                url: state.global.hftcomShop,
                data: '{"id":19,"jsonrpc":"2.0","method":"collectShop","params":{"userCode":"' + state.userCode + '","latitude":' + state.latitude + ',"longitude":' + state.longitude + ',"zoneId":"' + state.zoneId + '"}}',
                contentType: 'application/json',
                dataType: 'json'
            }).then((result) => {
                if (result.data.result === 'null' || result.data.result === null) {
                    commit('setState', {
                        'collectShopList': []
                    });
                } else {
                    commit('setState', {
                        'collectShopList': result.data.result[0]
                    });
                }
                resolve();
            }).catch((error) => {
                reject(error);
            });
        });
    },
    fetchUserCouponList({state, commit}, parameter) { // 拿取用户优惠券列表
        return new Promise((resolve, reject) => {
            axios({
                url: state.global.hftcomClient,
                method: 'POST',
                data: '{"id": 67,"jsonrpc": "2.0","method": "getMyAvailableCoupon","params": {"shopCode": "","userCode": "' + state.userCode + '","status": ' + parameter.status + ',"page": ' + parameter.page + ',"longitude": ' + state.longitude + ',"latitude":' + state.latitude + ',"city":"' + state.localCity + '","zoneId":"' + state.zoneId + '"}}',
                contentType: 'application/json'
            }).then((data) => {
                if (parameter.page === 1) {
                    commit('setStateItem', {name: 'userCouponList', data: data.data.result.userCouponList});
                    resolve(data.data.result.totalCount);
                } else {
                    state.userCouponList = [...state.userCouponList, ...data.data.result.userCouponList];
                    resolve(data.data.result.totalCount);
                }
            });
        });
    },
    bindMobile({state}, user) { // 绑定手机号
        return new Promise((resolve, reject) => {
            axios({
                url: state.global.hftcomShop,
                method: 'POST',
                data: '{"id":19,"jsonrpc":"2.0","method":"bindMobile","params":{"userCode":"' + state.userCode + '","mobileNbr":"' + user.mobileNbr + '","Vcode":"' + user.Vcode + '"}}',
                contentType: 'application/json'
            }).then((data) => {
                if (data.data.result.code === 50000) {
                    Services.setCookie('mobileNbr', user.mobileNbr, 1);
                    state.mobileNbr = user.mobileNbr;
                    resolve(data);
                } else {
                    reject(data);
                }
            });
        });
    },
    isWeiXin() { // 判断是否微信登陆 是不是微信浏览器
        return new Promise((resolve, reject) => {
            let ua = window.navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) !== null) {
                resolve();
            } else {
                reject();
            }
        });
    }
};
