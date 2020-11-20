#这里是惠圈项目的前端代码

#初次运行时请下载依赖
npm install
#本地测试环境运行
npm run dev[默认，目前为单机版]
npm run sass[sass化版本]

#注意：主要开发的是src目录

#其它命令
##打包
npm run build
##编译并生成项目js依赖占比报告
npm run build --report
##打包并上传到测试服务器
npm run buildT:up

##打包并上传到正式服务器（谨慎，稳定无任何报错时再上传）
npm run buildD:up

npm run buildD2:up

npm run buildS:up

npm run buildS2:up