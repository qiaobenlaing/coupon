//
//  BMSQ_shopCouponTableViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_shopCouponTableViewCell.h"
#import "UIButtonEx.h"


@interface BMSQ_shopCouponTableViewCell ()

@property (nonatomic, strong)UIImageView* iv_topBack;
@property (nonatomic, strong)UIImageView* iv_rightBack;

@property (nonatomic, strong)UILabel *titleLabel;
@property (nonatomic, strong)UILabel *timeDateLabel;

@property (nonatomic, strong)UIButton *useButton;

@property (nonatomic, strong)UIButton * btn_expansion;


@property (nonatomic, strong)UIView *secView;
@property (nonatomic, strong)UILabel *userTime;
@property (nonatomic, strong)UILabel *couponCode;
@property (nonatomic, strong)UILabel *message;
@property (nonatomic, strong)UIImageView *typeImage;


@property (nonatomic, strong)NSArray *coponbackColorS;
@property (nonatomic, strong)NSArray *coponImageS;
@property (nonatomic, strong)NSArray *coponBottomColorS;

@end


@implementation BMSQ_shopCouponTableViewCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.coponbackColorS = @[@"",@"250,96,59",@"",@"235,77,83",@"235,136,22",@"32,198,132",@"111,201,39"];
        self.coponImageS = @[@"",@"copon_1",@"",@"copon_3",@"copon_4",@"copon_5",@"copon_6"];
        self.coponBottomColorS= @[@"",@"246,30,19",@"",@"218,15,31",@"219,71,10",@"21,152,61",@"53,155,12"];

        
        self.backgroundColor = [UIColor colorWithRed:235/255.0 green:233/255.0 blue:241/255.0 alpha:1];
        
        self.iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20-80, 77)];
        self.iv_topBack.backgroundColor = [UIColor whiteColor];
        self.iv_topBack.userInteractionEnabled = YES;
        [self addSubview:self.iv_topBack];
        
        self.titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 0, 250, 40)];
        self.titleLabel.backgroundColor = [UIColor whiteColor];
        self.titleLabel.textColor = [UIColor colorWithRed:219/255.0 green:71/255.0 blue:8/255.0 alpha:1];
        [self.iv_topBack addSubview:self.titleLabel];
        
        self.timeDateLabel =[[UILabel alloc]initWithFrame:CGRectMake(15, 40, 250, 20)];
        self.timeDateLabel.font = [UIFont systemFontOfSize:12.f];
        self.timeDateLabel.textColor = [UIColor colorWithRed:133/255.0 green:133/255.0 blue:133/255.0 alpha:1];
        [self.iv_topBack addSubview:self.timeDateLabel];
        
        
        
        self.secView = [[UIView alloc]initWithFrame:CGRectMake(10, 80,  APP_VIEW_WIDTH-40-90, 0)];
        self.secView.backgroundColor = [UIColor whiteColor];
        [self.iv_topBack addSubview:self.secView];

        
        
        self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40, 40)];
        self.typeImage.backgroundColor = [UIColor whiteColor];
        [self.typeImage setImage:[UIImage imageNamed:@"copon_1"]];
        self.typeImage.center = CGPointMake(self.iv_rightBack.frame.size.width/2, self.iv_rightBack.frame.size.height/2);
        [self.iv_rightBack addSubview:self.typeImage];
        
        self.userTime = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, self.secView.frame.size.width, 20)];
        self.userTime.backgroundColor = [UIColor clearColor];
        self.userTime.font = [UIFont systemFontOfSize:12.f];
        self.userTime.textColor = [UIColor colorWithRed:133/255.0 green:133/255.0 blue:133/255.0 alpha:1];
        [self.secView addSubview:self.userTime];
        
        self.couponCode =[[UILabel alloc]initWithFrame:CGRectMake(0, 20, self.secView.frame.size.width, 20)];
        self.couponCode.backgroundColor = [UIColor clearColor];
        self.couponCode.font = [UIFont systemFontOfSize:12.f];
        self.couponCode.textColor = [UIColor colorWithRed:133/255.0 green:133/255.0 blue:133/255.0 alpha:1];
        [self.secView addSubview:self.couponCode];
        
        self.message = [[UILabel alloc]initWithFrame:CGRectMake(0, 40, self.secView.frame.size.width, 20)];
        self.message.backgroundColor = [UIColor clearColor];
        self.message.font = [UIFont systemFontOfSize:12.f];
        self.message.numberOfLines = 0;
        self.message.textColor = [UIColor colorWithRed:133/255.0 green:133/255.0 blue:133/255.0 alpha:1];
        [self.secView addSubview:self.message];
        
        
        
        
        self.iv_rightBack = [[UIImageView alloc]initWithFrame:CGRectMake(self.iv_topBack.frame.origin.x+self.iv_topBack.frame.size.width, 10, 80, self.iv_topBack.frame.size.height)];
        self.iv_rightBack.userInteractionEnabled = YES;
        [self addSubview:self.iv_rightBack];
        
        self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40, 40)];
        self.typeImage.backgroundColor = [UIColor clearColor];
        [self.typeImage setImage:[UIImage imageNamed:@"copon_1"]];
        self.typeImage.center = CGPointMake(self.iv_rightBack.frame.size.width/2, self.iv_rightBack.frame.size.height/2);
        [self.iv_rightBack addSubview:self.typeImage];
        
        self.useButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 60)];
        self.useButton.backgroundColor = [UIColor clearColor];
        [self.useButton setTitle:@"使用" forState:UIControlStateNormal];
        [self.useButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        self.useButton.titleLabel.font = [UIFont boldSystemFontOfSize:16.f];
        [self.iv_rightBack addSubview:self.useButton];
        [self.useButton addTarget:self action:@selector(clickUse) forControlEvents:UIControlEventTouchUpInside];

        
        
        self.btn_expansion = [[UIButton alloc]initWithFrame:CGRectMake(0, self.iv_topBack.frame.size.height-20, 80, 20)];
        [self.btn_expansion addTarget:self action:@selector(btnExpansionClick) forControlEvents:UIControlEventTouchUpInside];
//        self.btn_expansion.backgroundColor = UICOLOR(250, 28, 0, 1);
        [self.btn_expansion setImage:[UIImage imageNamed:@"iv_jtDown"] forState:UIControlStateNormal];
        [self.iv_rightBack addSubview:self.btn_expansion];
    
    }
    
    return self;
}

- (void)setCellValue:(NSDictionary*)dicCupon row:(int)row{
    
    if(dicCupon == nil || dicCupon.count <= 0)
    {
        return;
    }
    
    
    self.shopCouponDic = dicCupon;
    self.row = row;
    self.type =[dicCupon objectForKey:@"couponType"];
    self.type = [self.type intValue]== 32 ||[self.type intValue]== 33||[self.type intValue]== 3?@"3":self.type;
 
    NSString *backC = [self.coponbackColorS objectAtIndex:[self.type intValue]];
    NSArray *colorArr = [backC componentsSeparatedByString:@","];
    self.iv_rightBack.backgroundColor = UICOLOR([[colorArr objectAtIndex:0]floatValue], [[colorArr objectAtIndex:1] floatValue], [[colorArr objectAtIndex:2] floatValue], 1);
    
    
    backC = [self.coponBottomColorS objectAtIndex:[self.type intValue]];
    colorArr = [backC componentsSeparatedByString:@","];
    self.btn_expansion.backgroundColor = UICOLOR([[colorArr objectAtIndex:0]floatValue], [[colorArr objectAtIndex:1] floatValue], [[colorArr objectAtIndex:2] floatValue], 1);
    
    
    backC = [NSString stringWithFormat:@"copon_%@",self.type];
    [self.typeImage setImage:[UIImage imageNamed:backC]];
    
    
    

    int i = (int)[[dicCupon objectForKey:@"couponType"]integerValue];
    NSString *str;

    if ([self.type integerValue] ==3 ) {
        str=[NSString stringWithFormat:@"满%@元立减%@元",[dicCupon objectForKey:@"availablePrice"],[dicCupon objectForKey:@"insteadPrice"]];
        
    }else if (i == 4){
        str=[NSString stringWithFormat:@"满%@元打%0.1f折",[dicCupon objectForKey:@"availablePrice"],(float) [[dicCupon objectForKey:@"discountPercent"]floatValue]];
    }
      self.titleLabel.text =str;
    
    NSString *startUsingTime = [dicCupon objectForKey:@"startUsingTime"];
    NSString *expireTime = [dicCupon objectForKey:@"expireTime"];

    self.height = 80;
   
    
    self.timeDateLabel.text = [NSString stringWithFormat:@"有效期：%@-%@",startUsingTime,expireTime];
    
    self.userTime.frame = CGRectMake(0, 0, self.secView.frame.size.width, 20);

    self.userTime.text = [NSString stringWithFormat:@"使用时间：%@-%@",[dicCupon objectForKey:@"dayStartUsingTime"],[dicCupon objectForKey:@"dayEndUsingTime"]];
    NSString *userCouponNbr = [dicCupon objectForKey:@"userCouponNbr"];
    CGRect rect = self.couponCode.frame;
    if (userCouponNbr.length>0) {
        self.couponCode.text = [NSString stringWithFormat:@"优惠券编码：%@",[dicCupon objectForKey:@"userCouponNbr"]];
        rect = CGRectMake(0, 20, self.secView.frame.size.width, 20);
    }else{
        self.couponCode.text = @"";
        rect = CGRectMake(0, 20, self.secView.frame.size.width, 20);
        rect.size.height = 0;
        self.couponCode.frame = rect;
    }
    
    
    
    self.message.text = [NSString stringWithFormat:@"优惠券说明：%@",[dicCupon objectForKey:@"remark"]];
    CGSize size= [ self.message.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30-90, MAXFLOAT)
                                        options:NSStringDrawingUsesLineFragmentOrigin
                                     attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                        context:nil].size;
    self.message.frame = CGRectMake(0, self.couponCode.frame.origin.y+self.couponCode.frame.size.height+3, size.width, size.height);
    
    self.message.frame = CGRectMake(0, self.couponCode.frame.origin.y+self.couponCode.frame.size.height, size.width, size.height);
    self.height1 = self.message.frame.origin.y+self.message.frame.size.height;
    
    rect = self.secView.frame;
    rect.origin.y = 80;
    rect.size.height = self.height1;
    self.secView.frame = rect;
    
    
    [self.useButton setTitle:self.buttonStr forState:UIControlStateNormal];
    
    if ([dicCupon objectForKey:@"isSelect"]) {
        
        NSString *str = [dicCupon objectForKey:@"isSelect"];
        if ([str isEqualToString:@"YES"]) {
            [self setCellExpansion:YES];
            
        }else{
            [self setCellExpansion:NO];
            
        }
        
    }else{
        [self setCellExpansion:NO];
        
    }
    
    

}
- (void)setCellExpansion:(BOOL)isExpansion
{
    
    if (isExpansion)
    {
        CGRect rect = self.iv_topBack.frame;
        rect.size.height = self.height+self.height1+5;
        self.iv_topBack.frame = rect;
        self.secView.hidden = NO;
        
        rect = self.iv_rightBack.frame;
        rect.size.height = self.height+self.height1+5;
        self.iv_rightBack.frame = rect;
   
        rect = self.secView.frame;
        rect.size.height = self.height1;
        self.secView.frame = rect;
        
        self.typeImage.center = CGPointMake(self.iv_rightBack.frame.size.width/2, self.iv_rightBack.frame.size.height/2);

        
        self.btn_expansion.frame = CGRectMake(0, self.height1+self.height+5-20, 80, 20);
        self.btn_expansion.selected =isExpansion;

    }
    else
    {
        CGRect rect = self.iv_topBack.frame;
        rect.size.height = self.height;
        self.iv_topBack.frame = rect;
        self.secView.hidden = YES;
        
        
        rect = self.iv_rightBack.frame;
        rect.size.height = self.height;
        self.iv_rightBack.frame = rect;
    
        rect = self.secView.frame;
        rect.size.height = self.height1;
        self.secView.frame = rect;
        
        
        self.typeImage.center = CGPointMake(self.iv_rightBack.frame.size.width/2, self.iv_rightBack.frame.size.height/2);

        
        self.btn_expansion.frame = CGRectMake(0, self.height-20, 80, 20);
        self.btn_expansion.selected =isExpansion;

        
    }
    

}

#pragma mark --clickEvent--
-(void)clickUse{
    
    [self.sdelegate useCouponRow:self.row section:self.section];
    
    
    
}

- (void)btnExpansionClick
{
    self.btn_expansion.selected = !self.btn_expansion.selected;
    
    [self.sdelegate setCellSelect:self.btn_expansion.selected section:self.section indexPath:self.row dic:self.shopCouponDic];
    
}

+(CGFloat )noromlH:(NSDictionary *)dic{
 
    return 80+10;
    
}

+(CGFloat )seleH:(NSDictionary *)dic{

    
    int h1 = 80;
  
    int h2 = 20;
    NSString *userCouponNbr = [dic objectForKey:@"userCouponNbr"];
    if (userCouponNbr.length>0) {
        
        h2 = h2+20;
        
    }else{
        
    }
    NSString *function = [NSString stringWithFormat:@"%@",[dic objectForKey:@"remark"]];
    if (function.length >0) {
        function = [NSString stringWithFormat:@"优惠券说明：%@",function];
        
    }else{
        function = [NSString stringWithFormat:@"优惠券说明：%@",@"暂无说明"];
    }
   CGSize size= [function boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-30-90, MAXFLOAT)
                                 options:NSStringDrawingUsesLineFragmentOrigin
                              attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                 context:nil].size;
    
    h2 = h2+size.height;
    
    
    
    return h1+h2+10;
}

@end
