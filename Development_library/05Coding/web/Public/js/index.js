$(function(){
	var i=0;
	var num=0;
	var num2=0;
	$('.leftB').click(function(){
		i--;
		if(i<0){
			i=$('.imgB1 img').size()-1;
		}
		$('.imgB1 img').eq(i).stop().show().siblings('.imgB1 img').hide();
	})
	$('.rightB').click(function(){
		i++;
		if(i>$('.imgB1 img').size()-1){
			i=0;
		}
		$('.imgB1 img').eq(i).stop().show().siblings('.imgB1 img').hide();
	})
	$('.leftC').click(function(){
		num--;
		if(num<0){
			num=$('.imgB2 img').size()-1;
		}
		$('.imgB2 img').eq(num).stop().show().siblings('.imgB2 img').hide();
	})
	$('.rightC').click(function(){
		num++;
		if(num>$('.imgB2 img').size()-1){
			num=0;
		}
		$('.imgB2 img').eq(num).stop().show().siblings('.imgB2 img').hide();
	})
	$('.leftD').click(function(){
		num2--;
		if(num2<0){
			num2=$('.imgd img').size()-1;
		}
		$('.imgd img').eq(num2).stop().show().siblings('.imgd img').hide();
		$('.dcontentT li').eq(num2).addClass('active').siblings().removeClass('active');
	})
	$('.rightD').click(function(){
		num2++;
		if(num2>$('.imgd img').size()-1){
			num2=0;
		}
		$('.imgd img').eq(num2).stop().show().siblings('.imgd img').hide();
		$('.dcontentT li').eq(num2).addClass('active').siblings().removeClass('active');
	})
	$('.dcontentT li').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('.imgd img').eq($(this).index()).stop().show().siblings('.imgd img').hide();
	})
})