const target = process.env.npm_lifecycle_event;
var chalk = require('chalk')

// 默认模式 npm run build
    var obj = {
        NODE_ENV: '"production"',
        APP_ROUTER_BASE: '"/huiquan/"', // 路由前缀，由于在服务器端存在二级目录，因此路由需配置初始url
        APP_TYPE: 0, // 单机版-0，sass化版本-1
        BASE_URL: '""', // 基本域名
        API_ROOT_DICT:'""', // 请求后端接口的前缀url，默认为域名因此不填
        IMG_SRC: '""' // 图片前缀url。如果图片就在域名下，则可不填
    }
    
if (target == 'buildT:up') {
    // 单机版测试
    obj.APP_TYPE = 0;
    obj.BASE_URL = '"https://test.hfq.huift.com.cn"';
    obj.API_ROOT_DICT = '""';
    obj.IMG_SRC = '""';
}else if (target == 'buildD:up') {
    // 单机版正式
    obj.APP_TYPE = 0;
    obj.BASE_URL = '"https://hfq.huift.com.cn"';
    obj.API_ROOT_DICT = '""';
    obj.IMG_SRC = '""';
} else if (target == 'buildD2:up') {
    // 单机版正式2
    obj.APP_TYPE = 0;
    obj.BASE_URL = '"https://hq.hkctsbus.com"';
    obj.API_ROOT_DICT = '""';
    obj.IMG_SRC = '""';
}else if (target == 'buildS:up') {
    // sass版
    obj.APP_TYPE = 1;
    obj.BASE_URL = '"https://gzyh.hfq.huift.com.cn"';
    obj.API_ROOT_DICT = '""';
    obj.IMG_SRC = '"https://oss.cloud.hfq.huift.com.cn"';
}else if (target == 'buildS2:up') {
    // sass版
    obj.APP_TYPE = 1;
    obj.BASE_URL = '"https://cloud.hfq.huift.com.cn"';
    obj.API_ROOT_DICT = '""';
    //obj.IMG_SRC = '"https://gzyh.hfq.huift.com.cn"';
    obj.IMG_SRC = '"https://gzyh.hfqimg.huift.com.cn"';
} else {
    // 默认模式 npm run build
}

console.log(chalk.yellow(
'当前配置为:\n' + 
'版本：' + obj.APP_TYPE + '\n' +
'路由前缀：' + obj.APP_ROUTER_BASE + '\n' +
'域名url' + obj.BASE_URL + '\n' +
'接口url' + obj.API_ROOT_DICT + '\n' +
'图片路径前缀' + obj.IMG_SRC + '\n'
))

module.exports = obj;

// module.exports = {
//   NODE_ENV: '"production"'
// }
