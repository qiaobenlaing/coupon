//
//  Coupon_ListTableViewCell.m
//  BMSQS
//
//  Created by liuqin on 15/10/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "Coupon_ListTableViewCell.h"
#import "CouponTypeModel.h"


@interface Coupon_ListTableViewCell ()
{
    NSArray *coponbackColorS;
    NSArray *coponImageS;
    NSArray *coponBottomColorS;
    NSArray *status;
}
@property (nonatomic, strong)UIImageView *backImage;
@property (nonatomic, strong)UIImageView *typeImage;
@property (nonatomic, strong)UILabel *bottomLabel;
@property (nonatomic, strong)UILabel *piciLabel;
@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)UIProgressView  *proView;
@property (nonatomic, strong)UILabel *proLabel;


@property (nonatomic, strong)UIImageView *unavaImageV;
@property (nonatomic, strong)UILabel *proBackLable;
@property (nonatomic, strong)UILabel *proBackLable1;

@property (nonatomic, strong)UILabel *insteadPriceLabel;

@end


@implementation Coupon_ListTableViewCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        
//        coponbackColorS =  @[@"",@"253,118,75",@"",@"241,101,101",@"241,154,26",@"32,198,132",@"111,201,39", @"127,206,51", @"253,118,75"];
//        coponImageS =  @[@"",@"coupon_1",@"",@"coupon_3",@"coupon_4",@"coupon_5",@"coupon_6", @"coupon_7", @"coupon_1"];
//        coponBottomColorS= @[@"",@"251,56,21",@"",@"228,40,41",@"227,93,3",@"21,152,61",@"53,155,12", @"63,168,10", @"251,56,21"];
//        status = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券",@"兑换券", @"代金券"];
        
        
        UIView *couponView = [[UIView alloc]initWithFrame:CGRectMake(8, 10, APP_VIEW_WIDTH-16, APP_VIEW_HEIGHT/6)];
        couponView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:couponView];
        

        
        self.backImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, couponView.frame.size.width/4, couponView.frame.size.height)];
        self.backImage.backgroundColor = [UIColor clearColor];
        [couponView addSubview:self.backImage];
        
        
        
        
        
        
        self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, self.backImage.frame.size.width-30, self.backImage.frame.size.height-30)];
        self.typeImage.center = CGPointMake(self.backImage.frame.size.width/2, self.backImage.frame.size.height/2-10) ;
        self.typeImage.backgroundColor = [UIColor clearColor];
        [self.backImage addSubview:self.typeImage];
 
        
        
        self.insteadPriceLabel = [[UILabel alloc] initWithFrame:self.typeImage.frame];
        self.insteadPriceLabel.textAlignment = NSTextAlignmentCenter;
        self.insteadPriceLabel.backgroundColor = [UIColor clearColor];
        self.insteadPriceLabel.font = [UIFont systemFontOfSize:15.f];
        self.insteadPriceLabel.textColor = [UIColor whiteColor];
        [self.backImage addSubview:self.insteadPriceLabel];
        
        //已过期
        CGRect frame = self.backImage.frame;
        frame.size.height = frame.size.height-20;
        self.unavaImageV = [[UIImageView alloc]initWithFrame:frame];
        [self.unavaImageV setImage:[UIImage imageNamed:@"unavailable"]];
        self.unavaImageV.hidden = YES;
        [self.backImage addSubview:self.unavaImageV];
        
        //优惠券名称
        self.bottomLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, self.backImage.frame.size.height-20, self.backImage.frame.size.width, 20)];
        self.bottomLabel.backgroundColor = [UIColor grayColor];
        self.bottomLabel.textColor = [UIColor whiteColor];
        self.bottomLabel.font = [UIFont systemFontOfSize:11.f];
        self.bottomLabel.textAlignment = NSTextAlignmentCenter;
        [self.backImage addSubview:self.bottomLabel];
        
        self.piciLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.backImage.frame.size.width+10 , 0 , couponView.frame.size.width-self.backImage.frame.size.width, couponView.frame.size.height/3-10)];
        self.piciLabel.font = [UIFont systemFontOfSize:13.f];
        self.piciLabel.backgroundColor = [UIColor clearColor];
        [couponView addSubview:self.piciLabel];

        self.nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.backImage.frame.size.width+10 , self.piciLabel.frame.size.height, couponView.frame.size.width-self.backImage.frame.size.width-110, couponView.frame.size.height/3)];
        self.nameLabel.font = [UIFont systemFontOfSize:13.f];
        self.nameLabel.backgroundColor = [UIColor clearColor];
        [couponView addSubview:self.nameLabel];
       
//        self.proView = [[UIProgressView alloc] initWithFrame:CGRectMake(self.backImage.frame.size.width+10, self.nameLabel.frame.origin.y+self.nameLabel.frame.size.height, self.nameLabel.frame.size.width, 20)];
//        [self.proView setProgressViewStyle:UIProgressViewStyleDefault]; //设置进度条类型
//        [couponView addSubview:self.proView];
        
        
        self.proBackLable = [[UILabel alloc]initWithFrame:CGRectMake(self.backImage.frame.size.width+10, self.nameLabel.frame.origin.y+self.nameLabel.frame.size.height, self.nameLabel.frame.size.width, 10)];
        self.proBackLable.layer.masksToBounds = YES;
        self.proBackLable.layer.cornerRadius = 8;
        self.proBackLable.layer.borderWidth = 0.5;
        self.proBackLable.layer.borderColor = [[UIColor grayColor]CGColor];
        self.proBackLable.hidden = YES;
        [couponView addSubview:self.proBackLable];
        
        self.proBackLable1 = [[UILabel alloc]init];
        self.proBackLable1.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        self.proBackLable1.layer.masksToBounds = YES;
        self.proBackLable1.layer.cornerRadius = 6;
        self.proBackLable1.hidden = YES;
        [self.proBackLable addSubview:self.proBackLable1];

//
       self.proLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.backImage.frame.size.width+10 , couponView.frame.size.height-20 , couponView.frame.size.width-self.backImage.frame.size.width,15)];
        self.proLabel.font = [UIFont systemFontOfSize:12.f];
        self.proLabel.backgroundColor = [UIColor clearColor];
        [couponView addSubview:self.proLabel];
//
//
        
        
        
        
        
        
        UIButton *shareBtn = [[UIButton alloc]initWithFrame:CGRectMake(self.nameLabel.frame.origin.x+self.nameLabel.frame.size.width+3, self.nameLabel.frame.origin.y, 30, 30)];
        shareBtn.tag = 1000;
        shareBtn.backgroundColor = [UIColor clearColor];
        [shareBtn setImage:[UIImage imageNamed:@"iv_share"] forState:UIControlStateNormal];
        [shareBtn addTarget:self action:@selector(shareClick) forControlEvents:UIControlEventTouchUpInside];
        [couponView addSubview:shareBtn];
        
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(shareBtn.frame.origin.x+shareBtn.frame.size.width+3, self.nameLabel.frame.origin.y, couponView.frame.size.width-shareBtn.frame.origin.x -40, 30)];
        button.tag = 1001;
        button.backgroundColor = [UIColor whiteColor];
        button.layer.borderWidth = 0.8;
        button.layer.cornerRadius = 5;
        button.layer.masksToBounds = YES;
        button.layer.borderColor = [[UIColor redColor]CGColor];
        [button setTitle:@"领取人员" forState:UIControlStateNormal];
        [button setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
        button.titleLabel.font = [UIFont systemFontOfSize:12.f];
        [button addTarget:self action:@selector(ReceiveClick) forControlEvents:UIControlEventTouchUpInside];
        [couponView addSubview:button];

        
        
        
        
        
    }
    return self;
}



-(void)setListCoupon:(NSDictionary *)dic{
    
    if (self.hiddenBtn == YES) {
        CGRect frame = self.nameLabel.frame;
        frame.size.width =  frame.size.width+110;
        self.nameLabel.frame = frame;
        UIButton *shareBtn = [self viewWithTag:1000];
        UIButton *button = [self viewWithTag:1001];
        shareBtn.hidden = YES;
        button.hidden = YES;
        
    } else {
        if ([[dic objectForKey:@"isSend"] intValue]==1 ) {//满就送隐藏分享按钮
            UIButton *shareBtn = [self viewWithTag:1000];
            shareBtn.hidden = YES;
            
        } else {
            UIButton *shareBtn = [self viewWithTag:1000];
            shareBtn.hidden = NO;
        }
        
    }
    

    
    self.couponDic = dic;
    
    NSString *type = [NSString stringWithFormat:@"%@",[dic objectForKey:@"couponType"]];
    //人民币￥
    
    if (type.intValue < 9) {
        NSDictionary *couponDic = [CouponTypeModel createCoupon:[type intValue]];
        
        
        NSString *imageName = [couponDic objectForKey:@"image"];
        NSString *backColor = [couponDic objectForKey:@"backColor"];
        NSString *bottomColor = [couponDic objectForKey:@"bottomColor"];
        NSString *couponTypeStr = [couponDic objectForKey:@"status"];
        
        
        
        
        NSArray *backColors = [backColor componentsSeparatedByString:@","];
        self.backImage.backgroundColor =UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
        [self.typeImage setImage:[UIImage imageNamed:imageName]];
        
        
        backColors = [bottomColor componentsSeparatedByString:@","];
        self.bottomLabel.backgroundColor =UICOLOR([[backColors objectAtIndex:0]floatValue], [[backColors objectAtIndex:1] floatValue], [[backColors objectAtIndex:2] floatValue], 1);
        self.bottomLabel.text = couponTypeStr;
        
        self.piciLabel.text = [NSString stringWithFormat:@"批次:%@",[dic objectForKey:@"batchNbr"]];

        float w;
        if ([[dic objectForKey:@"totalVolume"]floatValue] != 0) {
            w = [[dic objectForKey:@"takenCount"]floatValue] / [[dic objectForKey:@"totalVolume"]floatValue] * (self.proBackLable.frame.size.width-10);
        }else {
            w = 0;
        }
        self.proBackLable1.frame = CGRectMake(5, 2, w, 6);
        
        
        self.insteadPriceLabel.text = [NSString stringWithFormat:@"Ұ %@",[dic objectForKey:@"insteadPrice"]];
        
        if (type.intValue == 3) {
            
            
            float availablePrice = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"availablePrice"]]floatValue];
            float insteadPrice = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"insteadPrice"]]floatValue];

            self.nameLabel.text = [NSString stringWithFormat:@"满%0.2f元立减%0.2f元",availablePrice,insteadPrice];
                                   
                                   
        }else if(type.intValue == 4) {
            
            
            float availablePrice = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"availablePrice"]]floatValue];
            float discountPercent = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"discountPercent"]]floatValue];


            self.insteadPriceLabel.text = [NSString stringWithFormat:@"%0.1f折",discountPercent];
            self.nameLabel.text = [NSString stringWithFormat:@"满%0.2f元打%0.1f折",availablePrice,discountPercent];
            
        }else if (type.intValue == 8) {
            self.nameLabel.text = [NSString stringWithFormat:@"%@元代%@元", [dic objectForKey:@"payPrice"], [dic objectForKey:@"insteadPrice"]];
            
        }else{
            self.nameLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"function"]];
        
        }
        
        NSString *totalVolume;
        if ([[dic objectForKey:@"totalVolume"] isEqual:@"-1"]) {
            self.proBackLable1.hidden = YES;
            self.proBackLable.hidden = YES;
            
            NSString *proLabelText = @"";
            int isAvailable  = [[dic objectForKey:@"isAvailable"] intValue];
            if (isAvailable) { //是否停用
                proLabelText = [NSString stringWithFormat:@"领取张数: %@",[dic objectForKey:@"takenCount"]];
            }else {
                proLabelText = [NSString stringWithFormat:@"已停用: %@",[dic objectForKey:@"takenCount"]];
            }
            self.proLabel.text = proLabelText;
            CGRect proFrame = self.proLabel.frame;
            proFrame.origin.y = APP_VIEW_HEIGHT/6/3*2;
            self.proLabel.frame = proFrame;
            
            
        }else {
            totalVolume = [dic objectForKey:@"totalVolume"];
            self.proBackLable1.hidden = NO;
            self.proBackLable.hidden = NO;

            NSString *proLabelText = @"";
            int isAvailable  = [[dic objectForKey:@"isAvailable"] intValue];
            if (isAvailable) { //是否停用
                proLabelText = [NSString stringWithFormat:@" 已领取(%@/%@)",[dic objectForKey:@"takenCount"],totalVolume];
            }else {
                proLabelText = [NSString stringWithFormat:@" 已停用(%@/%@)",[dic objectForKey:@"takenCount"],totalVolume];
            }
            
            self.proLabel.text = proLabelText;
            CGRect proFrame = self.proLabel.frame;
            proFrame.origin.y = APP_VIEW_HEIGHT/6/4*3;
            self.proLabel.frame = proFrame;
            
        }
        
        
        
        if ([dic objectForKey:@"isExpire"]) {
            if([[dic objectForKey:@"isExpire"]intValue] ==0){
                self.unavaImageV.hidden = NO;
            }else{
                self.unavaImageV.hidden = YES;
                
            }
        }
        
        
        
    }
    
    


    
}
-(void)shareClick{
    [self.listDelegate shareCoupon:self.couponDic];
}
-(void)ReceiveClick{
    [self.listDelegate receiveCoupon:self.couponDic];
    
}
@end
