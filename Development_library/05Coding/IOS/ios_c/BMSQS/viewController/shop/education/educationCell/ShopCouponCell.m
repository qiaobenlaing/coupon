//
//  ShopCouponCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/23.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ShopCouponCell.h"
#import "CouponImage.h"
@interface ShopCouponCell()

@property (nonatomic, strong)UIImageView *topView;
@property (nonatomic, strong)UIView *bgView;

@property (nonatomic, strong)UILabel *couponTypeLabel;

@property (nonatomic, strong)UILabel *scopeLabel ;//使用范围
@property (nonatomic, strong)UILabel *expiredLabel ;//过期时间
@property (nonatomic, strong)UILabel *useLabel ;//使用须知
@property (nonatomic, strong)UILabel *scopeLabel1 ;//使用范围
@property (nonatomic, strong)UILabel *useTimeLabel ;//使用时间
@property (nonatomic, strong)UILabel *ruleLabel ;//使用规则

@property (nonatomic, strong)UILabel *remarkLabel ;//使用规则

@property (nonatomic, strong)UIView *remarkView ;//使用规则

@property (nonatomic, strong)UIImageView *receiveImage;//领取图片


@property (nonatomic, strong)UIButton *shareBtn ;//
@property (nonatomic, strong)UIButton *receiveBtn ;//


@property (nonatomic, strong)UILabel *label1 ;//元优惠券
@property (nonatomic, strong)UILabel *label2 ;//优惠券简单说明

@property (nonatomic, strong)UILabel *label3 ;//￥
@property (nonatomic, strong)UILabel *priceLabel ;//￥

//剩余张数
@property (nonatomic, strong)UIView *bgSliderView;
@property (nonatomic, strong)UIView *SliderView;
@property (nonatomic, strong)UILabel *SliderText;



@end


@implementation ShopCouponCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.backgroundColor = [UIColor clearColor];
        self.userInteractionEnabled = YES;
   
        
        self.bgView = [[UIView alloc]init];
        self.bgView.backgroundColor = [UIColor whiteColor];
        self.bgView.layer.cornerRadius = 8;
        self.bgView.layer.masksToBounds = YES;
        self.bgView.layer.borderColor = [APP_VIEW_BACKCOLOR CGColor];
        self.bgView.layer.borderWidth = 2;
        self.bgView.userInteractionEnabled = YES;

        [self addSubview:self.bgView];
        
        self.topView = [[UIImageView alloc]init];
        [self.topView setImage:[UIImage imageNamed:@"粉底"]];
        self.topView.userInteractionEnabled = YES;

        [self.bgView addSubview:self.topView];
        
        self.couponTypeLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 60, 60)];
        self.couponTypeLabel.layer.cornerRadius = 25;
        self.couponTypeLabel.layer.masksToBounds = YES;
        self.couponTypeLabel.backgroundColor = [UIColor whiteColor];
        self.couponTypeLabel.textAlignment = NSTextAlignmentCenter;
        self.couponTypeLabel.font = [UIFont systemFontOfSize:14.5];
        self.couponTypeLabel.textColor = UICOLOR(255, 81, 119, 1);
        [self.topView addSubview:self.couponTypeLabel];
        
        
        self.shareBtn = [[UIButton alloc]init];
        [self.shareBtn setImage:[UIImage imageNamed:@"icon_share"] forState:UIControlStateNormal];
        [self.shareBtn addTarget:self action:@selector(clickCouponShare) forControlEvents:UIControlEventTouchUpInside];
        [self.topView addSubview:self.shareBtn];
        
   
        
        self.label1=[[UILabel alloc]init];
        self.label1.font = [UIFont boldSystemFontOfSize:24];
        self.label1.textColor = [UIColor whiteColor];
        self.label1.text = @"元优惠券";
        [self.bgView addSubview:self.label1];
        
        self.label2=[[UILabel alloc]init];
        self.label2.font = [UIFont boldSystemFontOfSize:16];
        self.label2.textColor = [UIColor whiteColor];
        self.label2.text = @"满2000.00元减300.00元";
        self.label2.textAlignment = NSTextAlignmentRight;
        [self.bgView addSubview:self.label2];
        
        self.label3=[[UILabel alloc]init];
        self.label3.font = [UIFont boldSystemFontOfSize:22];
        self.label3.textColor = [UIColor whiteColor];
        self.label3.text = @"￥";
        self.label3.backgroundColor = [UIColor clearColor];
        self.label3.textAlignment = NSTextAlignmentRight;
        [self.bgView addSubview:self.label3];
        
        self.priceLabel=[[UILabel alloc]init];
        self.priceLabel.font = [UIFont boldSystemFontOfSize:30];
        self.priceLabel.textColor = [UIColor whiteColor];
//        self.priceLabel.text = @"1000";
        self.priceLabel.backgroundColor = [UIColor clearColor];
        [self.bgView addSubview:self.priceLabel];

        
     
        
        self.bgSliderView = [[UIView alloc]init];
        self.bgSliderView.layer.borderColor = [UICOLOR(120, 212, 251, 1) CGColor];
        self.bgSliderView.layer.borderWidth = 1;
        self.bgSliderView.layer.cornerRadius = 8;
        self.bgSliderView.layer.masksToBounds = YES;
        [self.bgView addSubview:self.bgSliderView];
        
        self.SliderView = [[UIView alloc]init];
        self.SliderView.backgroundColor = UICOLOR(120, 212, 251, 1);
        self.SliderView.alpha = 0.613;
        [self.bgSliderView addSubview:self.SliderView];
        
        self.SliderText = [[UILabel alloc]init];
        self.SliderText.textAlignment = NSTextAlignmentCenter;
        self.SliderText.font = [UIFont systemFontOfSize:12.f];
        self.SliderText.textColor=APP_TEXTCOLOR;
        self.SliderText.backgroundColor = [UIColor clearColor];
        [self.bgSliderView addSubview:self.SliderText];
        
        
        self.scopeLabel=[[UILabel alloc]init];
        self.scopeLabel.font = [UIFont systemFontOfSize:12];
        self.scopeLabel.textColor = UICOLOR(120, 120, 120, 1);
        [self.bgView addSubview:self.scopeLabel];
        
        self.expiredLabel=[[UILabel alloc]init];
        self.expiredLabel.font = [UIFont systemFontOfSize:12];
        self.expiredLabel.textColor =  UICOLOR(120, 120, 120, 1);
        [self.bgView addSubview:self.expiredLabel];
        
        self.useLabel=[[UILabel alloc]init];
        self.useLabel.font = [UIFont systemFontOfSize:11];
        self.useLabel.textColor =  UICOLOR(164, 164, 164, 1);
        self.useLabel.text = @"使用须知:";
        [self.bgView addSubview:self.useLabel];
        
        self.scopeLabel1=[[UILabel alloc]init];
        self.scopeLabel1.font = [UIFont systemFontOfSize:11];
        self.scopeLabel1.textColor =  UICOLOR(164, 164, 164, 1);
        [self.bgView addSubview:self.scopeLabel1];
        
        self.useTimeLabel=[[UILabel alloc]init];
        self.useTimeLabel.font = [UIFont systemFontOfSize:11];
        self.useTimeLabel.textColor =  UICOLOR(164, 164, 164, 1);
        [self.bgView addSubview:self.useTimeLabel];
        
        self.ruleLabel=[[UILabel alloc]init];
        self.ruleLabel.font = [UIFont systemFontOfSize:11];
        self.ruleLabel.textColor =  UICOLOR(164, 164, 164, 1);
        self.ruleLabel.text = @"使用规则:";
        [self.bgView addSubview:self.ruleLabel];


        self.remarkLabel=[[UILabel alloc]init];
        self.remarkLabel.font = [UIFont systemFontOfSize:11];
        self.remarkLabel.textColor =  UICOLOR(164, 164, 164, 1);
        self.remarkLabel.numberOfLines = 0;
        self.remarkLabel.backgroundColor = [UIColor clearColor];
        [self.bgView addSubview:self.remarkLabel];
        
        
        self.remarkView=[[UIView alloc]init];
        [self.bgView addSubview:self.remarkView];
        
        self.receiveImage = [[UIImageView alloc]init];
        [self.receiveImage setImage:[UIImage imageNamed:@"已领取-印章"]];
        [self.bgView addSubview:self.receiveImage];
        
        
        
        self.receiveBtn = [[UIButton alloc]init];
        [self.receiveBtn setTitle:@"立即使用" forState:UIControlStateNormal];
        [self.receiveBtn setTitleColor: UICOLOR(255, 81, 119, 1)  forState:UIControlStateNormal];
        self.receiveBtn.titleLabel.font = [UIFont systemFontOfSize:18.f];
        self.receiveBtn.layer.borderColor = [UICOLOR(255, 81, 119, 1) CGColor];
        self.receiveBtn.layer.borderWidth =0.5;
        self.receiveBtn.layer.masksToBounds = YES;
        self.receiveBtn.layer.cornerRadius = 5;
        [self.receiveBtn addTarget:self action:@selector(clickReceive) forControlEvents:UIControlEventTouchUpInside];
        [self.bgView addSubview:self.receiveBtn];
    
    
    }
    return self;
}
-(void)setShopCouponCell:(NSDictionary *)couponDic heigh:(CGFloat)heigh status:(BOOL )isUserCoupon{
    self.isUserCoupon = isUserCoupon;
   
    
    int couponTypeInt = [[couponDic objectForKey:@"couponType"]intValue];
    self.couponTypeLabel.text = [CouponImage couponTypeName:couponTypeInt];
    self.label3.hidden = NO;//￥
    self.label1.hidden = NO;//元优惠券
    self.priceLabel.hidden = NO;

    self.priceLabel.font = [UIFont boldSystemFontOfSize:30];

    if(couponTypeInt == 1){
//        return @"N元购";
        [self addCouponTitiles:@[@"免预约"]];
        NSString *insteadPrice = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"insteadPrice"]];
        if ([insteadPrice floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[insteadPrice floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];
        }else{
            self.priceLabel.text = insteadPrice;

        }

        self.label2.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];

    }
    else if (couponTypeInt ==3){
//        return @"抵扣券";
        [self addCouponTitiles:@[@"免预约"]];
        
        NSString *insteadPrice = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"insteadPrice"]];
        
        if ([insteadPrice floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[insteadPrice floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];

        }else{
            self.priceLabel.text = insteadPrice;
            
        }

        
        self.label2.text = [NSString stringWithFormat:@"满%@元立减%@元",[couponDic objectForKey:@"availablePrice"],[couponDic objectForKey:@"insteadPrice"]];
        
        

    }
    else if (couponTypeInt ==4){
//        return @"折扣券";
        [self addCouponTitiles:@[@"免预约"]];
        self.label3.hidden = YES;
        self.label1.hidden = YES;
        
//        NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"discountPercent"]];

        self.priceLabel.text = [NSString stringWithFormat:@"%0.1f折",[[couponDic objectForKey:@"discountPercent"]floatValue]];

        self.label2.text = [NSString stringWithFormat:@"满%@元打%0.1f折",[couponDic objectForKey:@"availablePrice"],[[couponDic objectForKey:@"discountPercent"]floatValue]];


    }
    else if (couponTypeInt ==5){
//        return @"实物券";
        
        self.label3.hidden = YES;
        self.label1.hidden = YES;
        self.priceLabel.hidden = YES;

        [self addCouponTitiles:@[@"免预约"]];
         self.label2.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];
        NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"payPrice"]];
        
        if ([discountPercent floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[discountPercent floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];
            
        }else{
            self.priceLabel.text = discountPercent;
            
        }

    }
    else if (couponTypeInt ==6){
//        return @"体验券";
        
        self.label3.hidden = YES;
        self.label1.hidden = YES;
        self.priceLabel.hidden = YES;
        
        [self addCouponTitiles:@[@"免预约"]];
        self.label2.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];
        NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"payPrice"]];
        
        if ([discountPercent floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[discountPercent floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];
            
        }else{
            self.priceLabel.text = discountPercent;
            
        }


    }
    else if (couponTypeInt ==7){
//        return @"兑换券";

        [self addCouponTitiles:@[@"随时退",@"过期退",@"免预约"]];
        
        NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"payPrice"]];
//        self.priceLabel.text = discountPercent;
        
        if ([discountPercent floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[discountPercent floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];

        }else{
            self.priceLabel.text = discountPercent;
            
        }

        self.label2.text =  [NSString stringWithFormat:@"%@%@",discountPercent,[couponDic objectForKey:@"function"]];

        
    }
    else if (couponTypeInt ==8){
//        return @"代金券";
        [self addCouponTitiles:@[@"随时退",@"过期退",@"免预约"]];
        
        NSString *discountPercent = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"payPrice"]];
//        self.priceLabel.text = discountPercent;
        
        if ([discountPercent floatValue]>=10000) {
            self.priceLabel.text = [NSString stringWithFormat:@"%.2f万",[discountPercent floatValue]/10000];
            self.priceLabel.font = [UIFont boldSystemFontOfSize:25.f];

        }else{
            self.priceLabel.text = discountPercent;
            
        }

         self.label2.text = [NSString stringWithFormat:@"%@代%@元",discountPercent,[couponDic objectForKey:@"insteadPrice"]];
       

        
    } else{
//        return @"优惠券";
        
        self.label2.text = [NSString stringWithFormat:@"%@",[couponDic objectForKey:@"function"]];

    }
    
 
    
    
    self.bgView.frame = CGRectMake(10, 8, APP_VIEW_WIDTH-20, heigh-10);
    self.topView.frame = CGRectMake(0, 0, self.bgView.frame.size.width, 85);
    self.couponTypeLabel.frame = CGRectMake(10, 15, 50, 50);
    self.shareBtn.frame = CGRectMake(self.topView.frame.size.width-40, self.topView.frame.size.height-45, 30, 30) ;
    self.label1.frame =CGRectMake(self.topView.frame.size.width-140, self.topView.frame.size.height-45, 100, 30) ;
    self.label2.frame =CGRectMake(70, 5, self.topView.frame.size.width-80, 30) ;
     self.label3.frame =CGRectMake(50,50,30, 30) ;
    self.priceLabel.frame =CGRectMake(80,30,self.topView.frame.size.width-80-140, 50) ;

 
    
    
    self.scopeLabel.text = @"使用范围: 全场通用 单笔金额不限制";
    self.expiredLabel.text = [NSString stringWithFormat:@"使用日期:%@-%@",[couponDic objectForKey:@"startUsingTime"],[couponDic objectForKey:@"expireTime"]];
    self.scopeLabel1.text =[NSString stringWithFormat:@"使用时间:%@-%@",[couponDic objectForKey:@"dayStartUsingTime"],[couponDic objectForKey:@"dayEndUsingTime"]];
    
    self.receiveBtn.frame = CGRectMake(self.bgView.frame.size.width-100, self.topView.frame.size.height+30, 80, 30);
    
    //剩余张数
    if (self.isUserCoupon) {
        self.bgSliderView.hidden  = YES;
        self.SliderView.hidden  = YES;
        self.SliderText.hidden  = YES;

        
    }else{
        self.bgSliderView.hidden  = NO;
        self.SliderView.hidden  = NO;
        self.SliderText.hidden  = NO;
        
        int totalVolume = [[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"totalVolume"]] intValue];
        int remaining = [[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"remaining"]] intValue];
//        NSLog(@" totalVolume, remaining -> %d %d ",totalVolume, remaining);
        
        self.SliderText.text =[NSString stringWithFormat:@"剩余%@张",[couponDic objectForKey:@"remaining"]] ;
        
        self.bgSliderView.frame = CGRectMake(self.bgView.frame.size.width-110, self.receiveBtn.frame.origin.y+40, 100, 15);
        self.SliderView.frame = CGRectMake(0, 0, (remaining*100)/totalVolume, 15);
        self.SliderText.frame = self.bgSliderView.bounds;
    }
    

       
    
    
    self.scopeLabel.frame = CGRectMake(10, self.topView.frame.size.height, self.bgView.frame.size.width-20, 20);
     self.expiredLabel.frame = CGRectMake(10, self.topView.frame.size.height+20, self.bgView.frame.size.width-20, 20);
    
    
    self.remarkView.frame =CGRectMake(10, self.expiredLabel.frame.origin.y+20, self.bgView.frame.size.width-20, 15);
    
  
    self.useLabel.frame = CGRectMake(10, self.topView.frame.size.height+70, 100, 15);
    self.scopeLabel1.frame = CGRectMake(10, self.useLabel.frame.origin.y+15, self.bgView.frame.size.width-20, 15);
     self.useTimeLabel.frame = CGRectMake(10, self.scopeLabel1.frame.origin.y+15, self.bgView.frame.size.width-20, 15);
    self.ruleLabel.frame = CGRectMake(10, self.scopeLabel1.frame.origin.y+15, self.bgView.frame.size.width-20, 15);

    
    NSString *remark = [couponDic objectForKey:@"remark"];
    if (remark.length>0) {
        self.remarkLabel.text = remark;
        
    }else{
        self.remarkLabel.text = @"●  暂无说明 \n   " ;
        
    }
    CGSize size = [self.remarkLabel.text boundingRectWithSize:CGSizeMake(self.bgView.frame.size.width-20,MAXFLOAT)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:self.remarkLabel.font}
                                                      context:nil].size;
    
    self.remarkLabel.frame = CGRectMake(10, self.ruleLabel.frame.origin.y+15, self.bgView.frame.size.width-20, size.height<=15?15:size.height);
    
    
    
    if (isUserCoupon) {
        [self.receiveBtn setTitle:@"立即使用" forState:UIControlStateNormal];
        self.receiveImage.hidden = NO;
        self.receiveImage.frame = CGRectMake(self.bgView.frame.size.width-80, self.bgView.frame.size.height-80, 80, 80);
    }else{
        self.receiveImage.hidden = YES;
        
        [self.receiveBtn setTitle:@"立即领取" forState:UIControlStateNormal];
        self.receiveImage.frame = CGRectMake(self.bgView.frame.size.width-80, self.bgView.frame.size.height-80, 0, 0);
        
    }

    

}

-(void)addCouponTitiles:(NSArray *)array{
    
    for (UIView *coupView in self.remarkView.subviews) {
        if ([coupView isKindOfClass:[couponRemark class]]) {
            [coupView removeFromSuperview];
        }
    }
    for (int i=0; i<array.count; i++) {
        couponRemark *remark = [[couponRemark alloc]initWithFrame:CGRectMake(i*60, 0, 60, 15) remark:[array objectAtIndex:i]];
        [self.remarkView addSubview:remark];
    }

}
-(void)clickCouponShare{
    NSLog(@"分享");
    if ([self.couponDelegate respondsToSelector:@selector(shareCoupon:)]) {
        [self.couponDelegate shareCoupon:self.couponDic];
    }
    
}
-(void)clickReceive{
    
    
    if (self.isUserCoupon) { //使用
        NSLog(@"使用 ");
        if ([self.couponDelegate respondsToSelector:@selector(useCoupon:)]) {
            [self.couponDelegate useCoupon:self.couponDic];
        }

    }else{  //领取
        NSLog(@" 领用");
        if ([self.couponDelegate respondsToSelector:@selector(receivceCoupon:)]) {
            [self.couponDelegate receivceCoupon:self.couponDic];
        }

    }
}

@end




@implementation couponRemark


-(id)initWithFrame:(CGRect)frame remark:(NSString *)str{
    self = [super initWithFrame:frame];
    if (self) {
        UIImageView *bgimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, frame.size.height, frame.size.height)];
        [bgimageView setImage:[UIImage imageNamed:@"粉底对号"]];
        [self addSubview:bgimageView];
        
        UILabel *bgLabel = [[UILabel alloc]initWithFrame:CGRectMake(bgimageView.frame.size.width+3, 0, 50, frame.size.height)];
        bgLabel.textColor =  UICOLOR(255, 81, 119, 1);
        bgLabel.font = [UIFont systemFontOfSize:11];
        bgLabel.text = str;
        [self addSubview:bgLabel];
    }
    return self;
}
@end
