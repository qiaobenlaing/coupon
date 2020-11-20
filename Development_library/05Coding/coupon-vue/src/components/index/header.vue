<template>
	<div id="header">
		<div class="header">
		<router-link class="header-item" to="/location">{{computedlocalCity}}&nbsp;<span class="icon-ctrl"></span></router-link>
		<div class="header-item">
			<router-link to="/couponSearch">
			<div class="search"><label for="search" class="logo-search icon-search"></label><span class="text-search">搜索</span></div></router-link><!-- <a style="color:#fff" href="recharge.html?openid=oboBC0ZSJDOlnSNOdq56B-cNDbzU&zoneId=2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;充值</a> -->
		</div>
		</div>
	</div>
</template>
<script>
import { mapState } from 'vuex';
export default{
    computed: {
        ...mapState(['localCity']),
        computedlocalCity: function() {
            return this.methodGetByteLen(this.localCity, 6);
        }
    },
    methods: {
        methodGetByteLen: function(str, len) { // 控制字符长度，超过则削减添加省略号
            if (str.replace(/[^\x00-\xff]/g, '01').length <= len) {
                return str;
            }
            for (let i = Math.floor(len / 2); i < str.length; i++) {
                if (str.substr(0, i).replace(/[^\x00-\xff]/g, '01').length >= len) {
                    return str.substr(0, Math.floor(i / 2) * 2) + '...';
                }
            }
        }
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
#header
	.header
		z-index: 1;
		position:fixed
		top:0
		left:0
		height:1.24rem
		width:10.8rem
		line-height:1.24rem
		background:#f64f48
		// background:linear-gradient(#f64f48,transparent);
		.header-item
			height:1.24rem
			line-height:1.24rem
			font-size:0.4rem
			text-align:center
		&>:nth-child(1)
			position:absolute
			top:0
			left:0
			width:2.2rem
			color:#fff
			.icon-ctrl
				display:inline
				position:absolute
				top:0.49rem
				left:1.7rem
				width:0.54rem
				height:0.27rem
				transform:rotate(180deg)
				font-size:0.54rem
				color:#fff
		&>:nth-child(2)
			position:absolute
			top:0
			left:2.6rem
			width:8.25rem
			.search
				position:absolute
				top:0.24rem
				left:0
				height:0.8rem
				width:6.5rem
				background:#fff
				text-align:center
				border-radius:1rem
				.logo-search
					position:absolute
					height:0.68rem
					line-height:0.8rem
					width:1rem
					top:0.05rem
					left:1rem
					padding-right:1rem
					font-size:0.6rem
					color:#525252
					&:before
						color:#f64f48
						font-size:1em
				.text-search
					display:block
					margin: 0 auto
					height:0.68rem
					line-height:0.88rem
					width:1.3rem
					font-size:0.4rem
					color:#808080
			&>:nth-child(2)
				position:absolute
				top:0
				right:0.5rem
</style>