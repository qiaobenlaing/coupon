	var takeoutRequirePrice = $("#takeoutRequirePrice").val();
	var order_type = $("#order_type").val();
	function frameChange(categoryId, productId, productName, productPrice, type) {
		if(type == 'add'){
			var index = $("#frame_"+productId).find(".number").text();
			index = Number(index) + 1;
			$("#index_" + productId + "").find('.number').text(index);
			$("#frame_"+productId).find(".number").text(index);
			$("#frame_"+productId).parent().find("#index_value_" + productId).val(index);
			var category_total_index = categoryChange(categoryId, 'add');
			$("#category-" + categoryId).text(category_total_index);
			var total_index = totalIndexChange('add');
			$(".order .order_number").text(total_index);
			totalPriceAdd(productPrice);
		}else {
			var index = $("#frame_"+productId).find(".number").text();
			var total_price = $(".total_price").text();
			index = Number(index) - 1;
			if(index == 0){
				$(".index_" + productId + "").find(
				".number").addClass(
				'display_none');
				$(".index_" + productId + "").find(
						".minus").addClass(
						'display_none');
				$("#frame_" + productId).remove();
				$("#input_value_" + productId).remove();
			}
			$("#frame_"+productId).find(".number").text(index);
			$("#frame_"+productId).parent().find("#index_value_" + productId).val(index);
			$(".index_" + productId + "").find('.number').text(index);
			var category_total_index = categoryChange(categoryId, 'minus');
			$("#category-" + categoryId).text(category_total_index);
			total_price = Number(total_price) - Number(productPrice);
			if (takeoutRequirePrice>0){
				if(total_price<takeoutRequirePrice){
					$(".link").attr("disabled", true);
					$(".link").addClass("inoperable");
					var difference = takeoutRequirePrice - total_price;
					if(total_price == 0) {
						$(".link").html("&#65509;"+takeoutRequirePrice+"起送");
					}else{
						$(".link").text("还差"+difference+"元起送");
					}
				}
			}else if (total_price == 0){
				$(".link").addClass("inoperable");
			}
			$(".total_price").text(total_price);
			var total_index = totalIndexChange('minus');
			$(".order .order_number").text(total_index);
		}	
	}
		$(".food_inner2 .add").click(function() {
			var categoryId = $(this).data('categoryId');
			var productId = $(this).data('productId');
			var productName = $(this).data('productName');
			var productPrice = $(this).data('productPrice');
			var index = $(this).parent().find(".number").text();
			if (index == '') {
				index = 0;
			}
			index = Number(index) + 1;
			$(".food_inner2 .index_"+productId+" .number").text(index);
			if (index > 0) {
				$(".food_inner2 .index_"+productId+" .minus").removeClass('display_none');
				$(".food_inner2 .index_"+productId+" .number").removeClass("display_none");
				$("#category-div-" + categoryId).removeClass('display_none');
				$(".order .order_number_div").removeClass('display_none');
			}			
			var category_total_index = categoryChange(categoryId, 'add');
			$("#category-" + categoryId).text(category_total_index);
			addFrame(categoryId, productId, productName, productPrice, index);
			var total_index = totalIndexChange('add');
			$(".order .order_number").text(total_index);
			totalPriceAdd(productPrice, total_index);		
		});
		$(".food_inner2 .minus").click(function() {
			var categoryId = $(this).data('categoryId');
			var productId = $(this).data('productId');
			var index = $(this).parent().find(".number").text();
			var price = $(this).parent().parent().find(".food_price").text();
			var total_price = $(".total_price").text();
			index = Number(index) - 1;
			$(".food_inner2 .index_"+productId+" .number").text(index);
			$("#frame_" + productId + " .number").text(index);
			$("#frame_"+productId).parent().find("#index_value_" + productId).val(index);
			if (index == 0) {				
				$(".food_inner2 .index_"+productId+" .number").addClass('display_none');
				$(".food_inner2 .index_"+productId+" .minus").addClass('display_none');
				$("#frame_" + productId).remove();	
				$("#input_value_" + productId).remove();
			}
			var category_total_index = categoryChange(categoryId, 'minus');
			$("#category-" + categoryId).text(category_total_index);
			total_price = Number(total_price) - Number(price);
			if (takeoutRequirePrice>0){
				if(total_price<takeoutRequirePrice){
					$(".link").attr("disabled", true);
					$(".link").addClass("inoperable");
					var difference = takeoutRequirePrice - total_price;
					if(total_price == 0) {
						$(".link").html("&#65509;"+takeoutRequirePrice+"起送");
					}else{
						$(".link").text("还差"+difference+"元起送");
					}
				}
			}else if (total_price == 0){
				$(".link").addClass("inoperable");
			}
			$(".total_price").text(total_price);
			var total_index = totalIndexChange('minus');
			$(".order .order_number").text(total_index);			
		});
		function addFrame(categoryId, productId, productName, productPrice,
				index) {
			var type = $("#type_" + productId).val();
			var add_list = "<li id='frame_"+productId+"'><div class='left'>"
			+ "<div class='food_name'>"
			+ productName
			+ "</div>"
			+ "</div>"
			+ "<div class='right'>"
			+ "<div class='food_number'>"
			+ "<button class='number_button minus' type='button' " +
				"data-category-id="+categoryId+" data-product-id="+productId+" " +
				"data-product-name="+productName+" data-product-price="+productPrice+" " +
				"onclick='frameChange("+categoryId+","+productId+",\""+productName+"\","+productPrice+", \"minus\");'>"
			+ "<img src='/Public/img/icon/order_minus.png' /></button>"
			+ "<span class='number'>"
			+ index
			+ "</span>"
			+ "<button class='number_button add' type='button' " +
				"data-category-id="+categoryId+" data-product-id="+productId+" " +
				"data-product-name="+productName+" data-product-price="+productPrice+" " +
				"onclick='frameChange("+categoryId+","+productId+",\""+productName+"\","+productPrice+", \"add\");'>"
			+ "<img src='/Public/img/icon/order_add.png' /></button></div>"
			+ "<div class='cart_univalent'><span class='price_symbol'>&#65509;</span><span class='food_price'>"
			+ productPrice
			+ "</span>"
			+ "<span class='food_company'></span></div></div></li>"
			+ "<div id='input_value_"+productId+"'><input type='hidden' name='productId[]'value='"+productId+"'/>"
			+ "<input type='hidden' name='productUnitPrice[]' value='"+productPrice+"'/>"
			+ "<input type='hidden' name='productNbr[]' id='index_value_"+productId+"' value='1'/></div>";
			
			if ($("#frame_" + productId).length > 0) {
				if(type == 1){
					if($("#frame_" + productId).length == 1){
						$("#frame_" + productId + " .number").text(index);
						$("#frame_" + productId).parent().find("#index_value_" + productId).val(index);
					}else{
					$(".picker_model ul ").prepend(add_list);
					}
				}else{
					$("#frame_" + productId + " .number").text(index);
					$("#frame_" + productId).parent().find("#index_value_" + productId).val(index);
				}
			} else {			
				$(".picker_model ul ").prepend(add_list);								
			}
		}
		function categoryChange(categoryId, type) {
			var category_total_index = $("#category-" + categoryId).text();
			if (type == 'add') {
				category_total_index = Number(category_total_index) + 1;
			} else if (type == 'minus') {
				category_total_index = Number(category_total_index) - 1;
				if (category_total_index == 0) {
					$("#category-div-" + categoryId).addClass('display_none');
					$("#category-" + categoryId).text('');
				}
			}
			return category_total_index;
		}
		function totalIndexChange(type) {
			var total_index = $(".order .order_number").text();
			if (total_index == '') {
				total_index = 0;
			}
			if (type == 'add') {
				total_index = Number(total_index) + 1;	
				if (total_index > 0) {
					$(".order").removeAttr("disabled");
				}
			} else if (type == 'minus') {
				total_index = Number(total_index) - 1;
				if (total_index == 0) {
					if(order_type == 1){
						$(".link").html("&#65509;"+takeoutRequirePrice+"起送");
					}
					$(".link").addClass("inoperable");
					$(".total_price").text(total_index);
					$(".order").attr("disabled", true);
					$(".link").attr("disabled", true);
					$(".picker_model").removeClass("in");
					$(".modal_overlay").removeClass("in");
				}
				if (total_index == '') {
					$(".order .order_number").text('');
					$(".order .order_number_div").addClass('display_none');
				}
			}
			return total_index;				
		}
		function totalPriceAdd(productPrice, total_index) {
			var total_price = $(".total_price").text();
			if (total_price == '') {
				total_price = 0;
			}
			total_price = Number(total_price) + Number(productPrice);	
			
			if (takeoutRequirePrice>0){
				if(total_price>=takeoutRequirePrice){
					$(".link").removeAttr("disabled");
					$(".link").removeClass("inoperable");
					$(".link").text("选好了");
				}else{
					var difference = takeoutRequirePrice - total_price;
					$(".link").text("还差"+difference+"元起送");
				}
			}else if (total_price > 0) {
				$(".order").removeAttr("disabled");
				$(".link").removeAttr("disabled");
				$(".link").removeClass("inoperable");
			}
			$(".total_price").text(total_price);
		}
		$(".col-25").find('a:eq(0)').addClass('active');
		$(".order").on('click', function() {
			$(".picker_model").toggleClass("in");
			$(".modal_overlay").toggleClass("in");
		});
		$(".modal_overlay").on('click', function() {
			$(".modal_overlay").removeClass("in");
			$(".picker_model").removeClass("in");
		});
		$(".category").on('click', function() {
			$(".category").removeClass('active');
			$(this).addClass('active');			
		});

