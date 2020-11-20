var merge = require('webpack-merge')
var prodEnv = require('./prod.env')
var chalk = require('chalk')

const target = process.env.npm_lifecycle_event;

// 设置默认参数，即npm run dev时启用的参数
var obj = {
	NODE_ENV: '"development"', // 调试模式
	APP_ROUTER_BASE: '"/"', // 路由基本路径，调试模式为localhost没有二级文件夹所以为'/'
	APP_TYPE: 1, // 单机版-0，sass化版-1。用在项目内存不存储相关值的判断
    BASE_URL: '"https://cloud.hfq.huift.com.cn"', // 基本域名
	API_ROOT_DICT:'""', // 请求后端接口的前缀url，由于本地测试环境跨域，因此该值在代理中设置，替代locahost。或者注释掉代理，必须配置该值，因为本地没有服务
	IMG_SRC: '"https://gzyh.hfqimg.huift.com.cn"' // 图片前缀url。如果图片就在域名下，则可不填
}

if (target == 'sass') {
    // sass版本调试模式，只有
	obj.APP_TYPE = 1;
    obj.BASE_URL = '"https://cloud.hfq.huift.com.cn"',
	obj.API_ROOT_DICT = '"https://cloud.hfq.huift.com.cn"';
	obj.IMG_SRC = '"https://oss.cloud.hfq.huift.com.cn"';
} else if(target == 'dev2'){
	// 单机版调试模式
	obj.APP_TYPE = 0;
    obj.BASE_URL = '"https://hq.hkctsbus.com"',
	obj.API_ROOT_DICT = '"https://hq.hkctsbus.com"';
	obj.IMG_SRC = '"https://hq.hkctsbus.com"';
} else {
    // 默认模式npm run dev
    
}
console.log(chalk.yellow(
'当前配置为:\n' + 
'版本：' + obj.APP_TYPE + '\n' +
'路由前缀：' + obj.APP_ROUTER_BASE + '\n' +
'域名url' + obj.BASE_URL + '\n' +
'接口url' + obj.API_ROOT_DICT + '\n' +
'图片路径前缀' + obj.IMG_SRC + '\n'
))
module.exports = merge(prodEnv, obj)

// module.exports = merge(prodEnv, {
//   NODE_ENV: '"development"',
//   APP_ROUTER_BASE: '""'
// })
