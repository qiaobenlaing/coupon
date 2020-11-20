$(document).ready(function(){
    $('.employee-item-imgdiv').hover(function(){
        $(this).find('.edit-img-btn').show();
    },function(){
        $(this).find('.edit-img-btn').hide();
    });

    $('#album_del').click(function(){
        var type = $(this).attr('data_type');
        if(type == 1){
            $(this).attr('data_type',2);
            $('button[action="del_album"]').show();
        }else{
            $(this).attr('data_type',1);
            $('button[action="del_album"]').hide();
        }
    });

    setTimeout(function(){
        var upload_avatar_config, upload_shop_decoration_config, upload_pc_app_config, up_pre_shop_decoration, up_license_config, up_bankcard_config, up_id_card_config, up_protocol_config;
        var avatar = $('.avatar');
        var license = $('.license');
        var bankCard = $('.bankCard');
        var idcard = $('.J_id_card');
        var protocol = $('.protocol');
        var card = $('.J_business_card');
        var pc_app = $('.pc_app');
        var old_avatar_url = avatar.attr('src');
        var old_card_url = card.attr('src');
        var old_license_url = license.attr('src');
        var old_id_card_url = idcard.attr('src');
        var old_bankcard_url = bankCard.attr('src');
        var old_protocol_url = protocol.attr('src');
        var shopCode = $("#shopCode").val();
        var shopType = $("#shopType").val();
        upload_avatar_config = {
            server: '/Admin/Shop/do_shop_upload',
            picker: '.J_avatar',
            pickerLabel: '上传',
            formData: {
                shopCode: shopCode,
                upload_type: 'logo'
            },
            old_url: old_avatar_url,
            uploadSuccess: function(file, res){
                if(res.code == 200){
                    avatar.attr('src', res.url);
                    if(res.upload_type == 'logo'){
                        $('#logoUrl').val(res.url);
                    }
                }
            }
        };
        //上传银行卡图片
        up_bankcard_config = {
                server: '/Admin/Shop/do_shop_upload',
                picker: '.J_bankcard',
                pickerLabel: '上传',
                formData: {
                    shopCode: shopCode,
                    shopType: shopType,
                    upload_type: 'bankcard'
                },
                old_url: old_bankcard_url,
                uploadSuccess: function(file, res){
                    if(res.code == 200){
                    	bankCard.attr('src', res.url);
                        if(res.upload_type == 'bankcard'){
                            $('#bankCardUrl').val(res.url);
                        }
                    }
                }
            };
        //上传营业执照图片
        up_license_config = {
                server: '/Admin/Shop/do_shop_upload',
                picker: '.J_license',
                pickerLabel: '上传',
                formData: {
                    shopCode: shopCode,
                    shopType: shopType,
                    upload_type: 'license'
                },
                old_url: old_license_url,
                uploadSuccess: function(file, res){
                    if(res.code == 200){
                    	license.attr('src', res.url);
                        if(res.upload_type == 'license'){
                            $('#licenseUrl').val(res.url);
                        }
                    }
                }
            };
        //上传惠圈协议图片
        up_protocol_config = {
                server: '/Admin/Shop/do_shop_upload',
                picker: '.J_protocol',
                pickerLabel: '上传',
                formData: {
                    shopCode: shopCode,
                    shopType: shopType,
                    upload_type: 'protocol'
                },
                old_url: old_protocol_url,
                uploadSuccess: function(file, res){
                    if(res.code == 200){
                    	protocol.attr('src', res.url);
                        if(res.upload_type == 'protocol'){
                            $('#protocolUrl').val(res.url);
                        }
                    }
                }
            };
        //上传身份证图片
        up_id_card_config = {
                server: '/Admin/Shop/do_shop_upload',
                picker: '.J_idcard',
                pickerLabel: '上传',
                formData: {
                    shopCode: shopCode,
                    shopType: shopType,
                    upload_type: 'idcard'
                },
                old_url: old_id_card_url,
                uploadSuccess: function(file, res){
                    if(res.code == 200){
                        var html = '<div class="col-md-3 shop-idcard-wrap" id="shop-idcard-wrap-{$vo.decorationCode}"><div class="del-shop-idcard"></div><img src="' + res.url + '" class="img-responsive img-rounded decoration-img"><input type="hidden" class="form-control" name="IDcardUrl[]" value="' + res.url + '" /></div>';
                        $('.del-shop-idcard').empty().hide();
                        $('.cancel-del-idcard').hide();
                        $('.ready-to-del-idcard').show();
                        $('#shopIDcard').append(html);
                    }
                }
            };
        new upload(up_id_card_config);

        // 上传PC端应用的配置
        upload_pc_app_config = {
            server: '/Admin/PcAppLog/uploadPcApp',
            picker: '.J_pcApp',
            pickerLabel: '上传',
            formData: {
            },
            old_url: old_card_url,
            allowtitle:'Application',
            allowtype: 'exe',
            allowmime: 'application/octet-strea,appmlication/x-msdownload',
            chunked: false,
            uploadProgress: function(file, res) {
                var percent = res * 100;
                $('.webuploader-pick').text('正在上传(' + percent.toFixed(1) +'%)');
            },
            uploadSuccess: function(file, res){
                if(res.status == sz.STATUS_SUCC) {
                    sz.showSuccMsg('上传成功');
                    $('.webuploader-pick').text('上传');
                    $('#updateUrl').val(res.msg);
                } else {
                    sz.showErrMsg(res.msg);
                }
            }
        };

        // 上传预采用商户的背景图片
        up_pre_shop_decoration = {
            server: '/Admin/PreShop/uploadDecoration',
            picker: '.pre-shop-decoration-picker',
            pickerLabel: '选择图片',
            formData: {},
            chunked: true,
            uploadSuccess: function(file, res) {
                if(res.status == sz.STATUS_SUCC) {
                    var url = res.data.url;
                    var html = '<div class="col-md-3 decoration-wrap"><div class="del-shop-decoration"></div><img src="' + url + '" class="img-responsive img-rounded"><input type="hidden" class="form-control" name="decorationCode[]" value="" /><input type="hidden" class="form-control" name="imgUrl[]" value="' + url + '" /><div class="form-group"><label>标题</label><input type="text" class="form-control" name="title[]" value="" placeholder="标题"/></div><div class="form-group"><label>描述</label><textarea class="form-control decoration-short-des" name="decoShortDes[]" rows="4" placeholder="描述"></textarea></div></div>';
                    $('.del-shop-decoration').empty().hide();
                    $('.cancel-del-decoration').hide();
                    $('.ready-to-del-decoration').show();
                    $('#shop-decoration-wrap').append(html);
                }
            }
        }
        new upload(up_pre_shop_decoration);

        // 上传商家背景图片的配置
        upload_shop_decoration_config = {
            server: '/Admin/Shop/do_shop_upload',
            picker: '.shop-decoration-picker',
            pickerLabel: '上传',
            formData: {
                shopCode: shopCode,
                upload_type: 'photo'
            },
            chunked: true,
            uploadSuccess: function(file, res){
                if(res.code == 200){
                    var url = res.url;
                    var html = '<div class="col-md-3 decoration-wrap"><div class="del-shop-decoration"></div><img src="' + url + '" class="img-responsive img-rounded"><input type="hidden" class="form-control" name="decorationCode[]" value="" /><input type="hidden" class="form-control" name="imgUrl[]" value="' + url + '" /><div class="form-group"><label>标题</label><input type="text" class="form-control" name="title[]" value="" placeholder="标题"/></div><div class="form-group"><label>描述</label><textarea class="form-control decoration-short-des" name="decoShortDes[]" rows="4" placeholder="描述"></textarea></div></div>';
                    $('.del-shop-decoration').empty().hide();
                    $('.cancel-del-decoration').hide();
                    $('.ready-to-del-decoration').show();
                    $('#shop-decoration-wrap').append(html);
                }
            }
        };
        new upload(upload_shop_decoration_config);

        // 上传轮播图的设置
        upload_shop_decoration_config = {
            server: '/Admin/Shop/do_shop_upload',
            picker: '.swiper-decoration-picker',
            pickerLabel: '上传',
            formData: {
                shopCode: shopCode,
                upload_type: 'photo'
            },
            chunked: true,
            uploadSuccess: function(file, res){
                if(res.code == 200){
                    var url = res.url;
                    var html = '<div class="col-md-3 decoration-wrap"><div class="del-shop-decoration"></div><img src="' + url + '" class="img-responsive img-rounded"><input type="hidden" class="form-control" name="decorationCode[]" value="" /><input type="hidden" class="form-control" name="imgUrl[]" value="' + url + '" /><div class="form-group"><label>活动名称</label><input type="text" class="form-control" name="title[]" value="" placeholder="活动名称"/></div><div class="form-group"><label>活动网址</label><textarea class="form-control decoration-short-des" name="activityWeb[]" rows="4" placeholder="活动网址"></textarea></div></div>';
                    $('.del-shop-decoration').empty().hide();
                    $('.cancel-del-decoration').hide();
                    $('.ready-to-del-decoration').show();
                    $('#shop-decoration-wrap').append(html);
                }
            }
        };
        new upload(upload_shop_decoration_config);



        new upload(upload_avatar_config);
        new upload(up_bankcard_config);
        new upload(up_license_config);
        new upload(upload_pc_app_config);
        new upload(up_protocol_config);


    }, 0);

});

;function del_decoration(){
    $("button[action='del_decoration']").unbind('click').click(function(){
        var decorationCode = $(this).attr('decoration_code');
        $.post(
            '/Admin/Shop/delDecoration',
            {
                decorationCode:decorationCode,
                isAjax:true },
            function(result){
                var results=JSON.parse(result);
                if(results.response.result==true){
                    $("#div-"+decorationCode).remove();
                }
            }
        )
    });
}
//删除身份证图片
;function del_id_card(){
    $("button[action='del_id_card']").unbind('click').click(function(){
        var idCode = $(this).attr('id_code');
        var shopCode = $(this).attr('shop_code');
        var shopType = $(this).attr('shop_type');
        $.post(
                '/Admin/Shop/delIdCard',
                {	
                	idCode:idCode,
                	shopCode:shopCode,
                	shopType:shopType,
                    isAjax:true 
                },
                function(result){
                    var results=JSON.parse(result);
                    if(results.response.result==true){
                    	console.log(results.response.urlMessage);
                    	$("#IDcardUrl").empty();
                    	$("#IDcardUrl").val(results.response.urlMessage);
                        $("#div-"+idCode).remove();
                    }
                }
            )
    });
}

;function del_album(){
    $("button[action='del_album']").unbind('click').click(function(){
        var code = $(this).attr('code');
        $.post(
            '/Admin/Shop/delAlbum',
            {
                code:code,
                isAjax:true },

            function(result){
                var results=JSON.parse(result);
                if(results.response.result==true){
                    $("#album_"+code).remove();
                }
            }
        )
    });
}