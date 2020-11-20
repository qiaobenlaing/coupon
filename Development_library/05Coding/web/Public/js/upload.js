$(function(){
    var clear = function(modal_avatar, old_url){
        modal_avatar.attr('src', old_url);
        try{
            uploader.remove();
        }catch(e){}
    }
    var LOOP = function(){};

    var create_upload = function(_cfg){
        var cfg = {};
        cfg.picker = _cfg.picker || '';
        cfg.pickerLabel = _cfg.pickerLabel || '';
        cfg.formData = _cfg.formData || {};
        cfg.server = _cfg.server || '';
        cfg.old_url = _cfg.old_url || '';
        cfg.uploadSuccess = _cfg.uploadSuccess || LOOP;
        cfg.fileQueued = _cfg.fileQueued || LOOP;
        cfg.startUpload = _cfg.startUpload || LOOP;
        cfg.uploadProgress = _cfg.uploadProgress || LOOP;
        cfg.uploadError = _cfg.uploadError || LOOP;
        cfg.uploadComplete = _cfg.uploadComplete || LOOP;
        cfg.uploadBeforeSend = _cfg.uploadBeforeSend || LOOP;
        cfg.extension = _cfg.allowtype || 'gif,jpg,jpeg,bmp,png';
        cfg.title = _cfg.allowtitle || 'Images';
        cfg.mime = _cfg.allowmime || 'image/*';
        cfg.chunked = _cfg.chunked  || false;
        var self = this;
        this.cfg = cfg;
        if(!cfg.picker) {
            return;
        }
        var uploader = WebUploader.create({
            // 选完文件后，是否自动上传。
            auto: true,
            // swf文件路径
            swf:  '',
            formData: cfg.formData,
            //    sendAsBinary : true,
            fileVal: 'userfile',

            // 文件接收服务端。
            server: cfg.server,
            chunked: cfg.chunked,
            duplicate: true,

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: {
                id: cfg.picker,
                label: cfg.pickerLabel
            },

            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false,
            // 只允许选择图片文件
            accept: {
                title: cfg.title,
                extensions: cfg.extension,
                mimeTypes: cfg.mime
            }

        });
        uploader.on('uploadBeforeSend', function(obj, data, header){
            var arg = [].slice.call(arguments);
           self.cfg.uploadBeforeSend.apply(uploader, arg);
        });
        uploader.on( 'fileQueued', function( file ) {
            self.cfg.fileQueued.call(uploader, file);
        });
// 文件上传过程中创建进度条实时显示。
        uploader.on( 'uploadProgress', function( file, percentage ) {
//            console.log(file, percentage);
            self.cfg.uploadProgress.call(uploader, file, percentage);
        });

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on( 'uploadSuccess', function( file, res ) {
            self.cfg.uploadSuccess.call(uploader, file, res);
        });
// 文件上传失败，显示上传出错。
        uploader.on( 'uploadError', function( file , reason) {
            alert('上传失败');
            self.cfg.uploadError.call(uploader, file);
        });

// 完成上传完了，成功或者失败，先删除进度条。
        uploader.on( 'uploadComplete', function( file ) {
            self.cfg.uploadComplete.call(uploader, file);
        });

    }

    var init = function(cfg){
        new create_upload(cfg);
    }
    window.upload = init;
});