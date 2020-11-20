const gulp = require('gulp');
const ftp = require('gulp-sftp');
var config = {
    devTest: { //部署到测试服务器上test.hfq.huift.com.cn
        remotePath: '/usr/local/nginx/html/test.hfq.huift.com.cn/huiquan/', //部署到服务器的路径
        host: '39.107.254.229', //ip地址
        user: 'root', //帐号
        pass: "Whhft123", //密码
        port: 22 //端口
    },
    devDist: { //部署正式单机版服务器上hfq.huift.com.cn
        remotePath: '/usr/local/nginx/html/hfq.huift.com.cn/huiquan/', //部署到服务器的路径
        host: '39.107.254.229', //ip地址
        user: 'root', //帐号
        pass: 'Whhft123', //密码
        port: 22 //端口
    },
	devDist2: { //部署正式2单机版服务器上hq.hkctsbus.com
	    remotePath: '/data/wwwroot/hq.hkctsbus.com/huiquan/', //部署到服务器的路径
	    host: '120.78.11.152', //ip地址
	    user: 'root', //帐号
	    pass: 'HQ@huift.com.cn', //密码
	    port: 22 //端口
	},
    devNewDist: { //部署sass化的正式服务器上cloud.hfq.huift.com.cn
        remotePath: '/usr/local/nginx/html/hfq.huift.com.cn/huiquan/', //部署到服务器的路径
        host: '47.104.88.253', //ip地址
        user: 'root', //帐号
        pass: 'Whhft123456', //密码
        port: 22 //端口
    },
    devNewDist2: { //部署sass2化的正式服务器上
        remotePath: '/usr/local/nginx/html/hfq.huift.com.cn/huiquan/', //部署到服务器的路径
        host: '47.104.88.253', //ip地址
        user: 'root', //帐号
        pass: 'Whhft123456', //密码
        port: 22 //端口
    },
    publicPath: '/dist/' //程序编译好路径
};
gulp.task('uploadTest', function (callback) {
    console.log('## 正在部署到惠圈单机版测试服务器上')
    var dev = config.devTest
    gulp.src('.' + config.publicPath + '**')
        .pipe(ftp(Object.assign(dev, {callback})))
});
gulp.task('uploadD', function (callback) {
    console.log('## 正在部署到惠圈单机版正式服务器上')
    var dev = config.devDist
    gulp.src('.' + config.publicPath + '**')
        .pipe(ftp(Object.assign(dev, {callback})))
});
gulp.task('uploadD2', function (callback) {
    console.log('## 正在部署到惠圈单机版正式2服务器上')
    var dev = config.devDist2
    gulp.src('.' + config.publicPath + '**')
        .pipe(ftp(Object.assign(dev, {callback})))
});
gulp.task('uploadSASS', function (callback) {
    console.log('## 正在部署到惠圈saas化版本正式服务器上')
    var dev = config.devNewDist
    gulp.src('.' + config.publicPath + '**')
        .pipe(ftp(Object.assign(dev, {callback})))
});
gulp.task('uploadSASS2', function (callback) {
    console.log('## 正在部署到惠圈saas化版本正式服务器上')
    var dev = config.devNewDist2
    gulp.src('.' + config.publicPath + '**')
        .pipe(ftp(Object.assign(dev, {callback})))
});