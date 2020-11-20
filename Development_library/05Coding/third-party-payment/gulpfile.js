// gulpfile.js
var gulp = require('gulp');
var http = require('http');
var connect = require('gulp-connect');
var livereload = require('gulp-livereload')

var serverConfig = {
        root: "./",
        port: 2323,
        livereload: true,
        host: '::'
}
gulp.task('html', function(){
    console.log('html改变，刷新');
    gulp.src('*.html')
    .pipe(connect.reload());
});

gulp.task('server',function(){
connect.server(serverConfig);
});

gulp.task('watch', function () {
    livereload.listen();
  gulp.watch(['*.html'],function(event){
    livereload.changed(event.path);
  })
});
gulp.task('default',['server','watch']);