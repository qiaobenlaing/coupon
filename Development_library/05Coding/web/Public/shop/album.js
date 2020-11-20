$(document).ready(function(){
    $('.employee-item-imgdiv').hover(function(){
        $(this).find('.edit-img-btn').show();
    },function(){
        $(this).find('.edit-img-btn').hide();
    });

    setTimeout(function(){
        var upload_card_config;
        var card = $('.J_business_card');
        var old_card_url = card.attr('src');
        var code = $("#sub_album_code").val();

        upload_card_config = {
            server: '/Admin/Shop/do_shop_upload',
            picker: '.J_card',
            pickerLabel: '上传',
            formData: {
                code: code,
                upload_type: 'sub_album'
            },
            old_url: old_card_url,
            uploadSuccess: function(file, res){
                if(res.code == 200){
                    $('#product').prepend('<div class="col-xs-4" id="div-'+res.photoCode+'" style="position: relative;"><img src="'+res.url+'" class="img-responsive img-rounded" style="height: 150px;width: auto;"><a href="#" action="del_product" code="'+res.photoCode+'" style="position: absolute;right: 35px;top: 0;"><span class="glyphicon glyphicon-remove-circle" style="color: #ff0000;font-size: 20px;"></span></a><div class="row row-top" style="margin: 10px 0;"><div class="input-group"><span class="input-group-addon frm-span">标题</span><input type="text" class="form-control" name="title['+res.photoCode+']" /></div></div><div class="row row-top" style="margin: 10px 0;"><div class="input-group"><span class="input-group-addon frm-span">价格</span><input type="text" class="form-control" name="price['+res.photoCode+']" /></div></div><div class="row row-top" style="margin: 10px 0;"><div class="input-group"><span class="input-group-addon frm-span">描述</span><input type="text" class="form-control" name="des['+res.photoCode+']" /></div></div></div>');
                    del_product();
                }
            }
        };

        new upload(upload_card_config);
    }, 0);

});

;function del_product(){
    $("a[action='del_product']").unbind('click').click(function(){
        var decorationCode = $(this).attr('code');
        $.post(
            '/Admin/Shop/delProduct',
            {
                code:decorationCode,
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
