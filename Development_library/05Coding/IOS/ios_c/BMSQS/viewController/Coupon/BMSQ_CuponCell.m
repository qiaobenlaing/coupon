//
//  BMSQ_CuponCell.m
//  BMSQC
//
//  Created by  on 15/7/30.
//  Copyright (c) 2015年 djx. All rights reserved.
//






#import "BMSQ_CuponCell.h"
#import "CouponImage.h"
@interface BMSQ_CuponCell ()

@property (nonatomic, strong)UIImageView *typeImage;

@property (nonatomic, strong)UIImageView *couponStatusImage;//已过期 已使用


@end

@implementation BMSQ_CuponCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    self.backgroundColor = [UIColor redColor];
 
    
    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20-70, 90)];
    iv_topBack.backgroundColor = [UIColor whiteColor];
    iv_topBack.userInteractionEnabled = YES;
    [self addSubview:iv_topBack];
    

    
    
    
    iv_shopLogo = [[UIImageView alloc]initWithFrame:CGRectMake(8, 10, 60, 60)];
    [iv_topBack addSubview:iv_shopLogo];
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(iv_shopLogo.frame.origin.x+iv_shopLogo.frame.size.width+5,10, iv_topBack.frame.size.width - (iv_shopLogo.frame.origin.x+iv_shopLogo.frame.size.width+5)-45, 25)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    [lb_shopName setFont:[UIFont fontWithName:APP_FONT_NAME size:14]];
    [iv_topBack addSubview:lb_shopName];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(lb_shopName.frame.origin.x, 30,lb_shopName.frame.size.width, 30)];
    lb_useDes.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_useDes.backgroundColor = [UIColor clearColor];
    lb_useDes.font = [UIFont systemFontOfSize:10];
    [iv_topBack addSubview:lb_useDes];
    
    lb_distance = [[UILabel alloc]initWithFrame:CGRectMake(iv_topBack.frame.size.width-30, 0, 30, 30)];
    [lb_distance setTextColor:UICOLOR(135, 135, 135, 1.0)];
    lb_distance.textAlignment = NSTextAlignmentCenter;
    lb_distance.backgroundColor = [UIColor clearColor];
    lb_distance.text = @"距离";
    [lb_distance setFont:[UIFont fontWithName:APP_FONT_NAME size:10]];
    [iv_topBack addSubview:lb_distance];
    
    btn_share = [[UIButton alloc]initWithFrame:CGRectMake(iv_topBack.frame.size.width - 30, 20, 30, 30)];
    [btn_share setImage:[UIImage imageNamed:@"iv_share"] forState:UIControlStateNormal];
    btn_share.backgroundColor = [UIColor clearColor];
    [btn_share addTarget:self action:@selector(btnShareClick:) forControlEvents:UIControlEventTouchUpInside];
    [iv_topBack addSubview:btn_share];
    [iv_topBack bringSubviewToFront:btn_share];
    
    lb_endTime = [[UILabel alloc]initWithFrame:CGRectMake(lb_shopName.frame.origin.x, 60, iv_topBack.frame.size.width - 70, 20)];
    lb_endTime.font = [UIFont systemFontOfSize:10];
    lb_endTime.backgroundColor = [UIColor clearColor];
    lb_endTime.textColor = UICOLOR(135, 135, 135, 1.0);
    [iv_topBack addSubview:lb_endTime];

    iv_bottomBack = [[UIImageView alloc]initWithFrame:CGRectMake(15, 77, APP_VIEW_WIDTH - 35, 70)];
    lb_couponTime = [[UILabel alloc]initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 118, 20)];
    lb_couponTime.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_couponTime.text = @"使用时间:00:00:00 - 00:00:00";
    lb_couponTime.font = [UIFont systemFontOfSize:12];
    [iv_bottomBack addSubview:lb_couponTime];
    
    lb_state = [[UILabel alloc]initWithFrame:CGRectMake(10, 25, APP_VIEW_WIDTH - 118, 20)];
    lb_state.font = [UIFont systemFontOfSize:12];
    lb_state.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_state.text = @"优惠券编码:";
    [iv_bottomBack addSubview:lb_state];
    
    lb_stateLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 45, APP_VIEW_WIDTH - 50-80, 20)];
    lb_stateLabel.font = [UIFont systemFontOfSize:12];
    lb_stateLabel.text = @"暂无说明";
    lb_stateLabel.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_stateLabel.numberOfLines = 2;
    lb_stateLabel.backgroundColor = [UIColor clearColor];

    [iv_bottomBack addSubview:lb_stateLabel];
    
     
    [iv_topBack addSubview:iv_bottomBack];
     
    
    iv_rightBack = [[UIImageView alloc]initWithFrame:CGRectMake(iv_topBack.frame.origin.x+iv_topBack.frame.size.width, 10, APP_VIEW_WIDTH-iv_topBack.frame.size.width-20, iv_topBack.frame.size.height)];
    iv_rightBack.userInteractionEnabled = YES;
    [self addSubview:iv_rightBack];
    iv_rightBack.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGest = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(click_rightView)];
    [iv_rightBack addGestureRecognizer:tapGest];
    
    
   
    
    
    self.typeImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 40, 40)];
    self.typeImage.backgroundColor = [UIColor clearColor];
    self.typeImage.center = CGPointMake(iv_rightBack.frame.size.width/2, iv_rightBack.frame.size.height/2);
    [iv_rightBack addSubview:self.typeImage];
    
    lb_price = [[UILabel alloc]initWithFrame:CGRectMake(0, 15, iv_rightBack.frame.size.width, 35)];
    [lb_price setTextColor:[UIColor whiteColor]];
    lb_price.textAlignment = NSTextAlignmentCenter;
    lb_price.backgroundColor = [UIColor clearColor];
    lb_price.text = @"张数";
    lb_price.font = [UIFont systemFontOfSize:25];
    [iv_rightBack addSubview:lb_price];
    
    lb_status = [[UILabel alloc]initWithFrame:CGRectMake(0, iv_rightBack.frame.size.height-40, iv_rightBack.frame.size.width, 20)];
    [lb_status setTextColor:[UIColor whiteColor]];
    lb_status.textAlignment = NSTextAlignmentCenter;
    lb_status.backgroundColor = [UIColor clearColor];
    [lb_status setFont:[UIFont systemFontOfSize:12]];
    [iv_rightBack addSubview:lb_status];
    
    
    btn_expansion = [[UIButton alloc]initWithFrame:CGRectMake(0, iv_topBack.frame.size.height-20, iv_rightBack.frame.size.width, 20)];
    [btn_expansion addTarget:self action:@selector(btnExpansionClick) forControlEvents:UIControlEventTouchUpInside];
    btn_expansion.backgroundColor = UICOLOR(250, 28, 0, 1);
    [btn_expansion setImage:[UIImage imageNamed:@"iv_jtDown"] forState:UIControlStateNormal];
    [iv_rightBack addSubview:btn_expansion];
    
    
    
    self.couponStatusImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, iv_rightBack.frame.size.width, iv_topBack.frame.size.height)];
    self.couponStatusImage.backgroundColor = [UIColor grayColor];
    self.couponStatusImage.userInteractionEnabled = YES;
    UITapGestureRecognizer *noTapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(noTap)];
    [self.couponStatusImage addGestureRecognizer:noTapGesture];
    [iv_rightBack addSubview:self.couponStatusImage];
    
    self.couponStatusImage.hidden = YES;
}

//优惠券 过期 使用 
-(void)noTap{
    
}
- (void)setCellValue:(NSDictionary*)dicCupon row:(int)row couponStatus:(int)status
{
    if(dicCupon == nil || dicCupon.count <= 0)
    {
        return;
    }
    //status 1 有效的 2已过期 3 已使用
    if (status ==1) {
        self.couponStatusImage.hidden = YES;
    }else if(status ==2){
        self.couponStatusImage.hidden = NO;
        [self.couponStatusImage setImage:[UIImage imageNamed:@"iv_notValid"]];
    }else if(status ==3){
        self.couponStatusImage.hidden = NO;
        int status = [[NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"status"]]intValue];
        if (status == 11) {  //已退款
            [self.couponStatusImage setImage:[UIImage imageNamed:@"refunded"]];
        }else{
            [self.couponStatusImage setImage:[UIImage imageNamed:@"iv_hadUsed"]];
        }
    }else{
        self.couponStatusImage.hidden = YES;
    }
    
    
    self.currentRow = row;
    self.couponDic = dicCupon;
    self.type =[dicCupon objectForKey:@"couponType"];
    
    
    self.type = [self.type intValue]== 32 ||[self.type intValue]== 33||[self.type intValue]== 3?@"3":self.type;
    
    iv_rightBack.backgroundColor = [CouponImage couponBgColor:[self.type intValue]];
    btn_expansion.backgroundColor = [CouponImage couponBottomBgColor:[self.type intValue]];
    [self.typeImage setImage:[CouponImage couponBgImage:[self.type intValue]]];
    
    NSString *distence = [dicCupon objectForKey:@"distance"];
    NSString *userCouponNbr = [NSString stringWithFormat:@"优惠券批次号:%@",[dicCupon objectForKey:@"batchNbr"]];
    lb_state.text = userCouponNbr.length>0?userCouponNbr:@"";
    
    
    
    if (self.isGrabvotes) {
        
        NSString *countMyActiveReceived = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"countMyActiveReceived"]];
        if ([countMyActiveReceived intValue] ==0) {
            lb_price.font = [UIFont systemFontOfSize:18];
            
            if ([self.type intValue] == 7 ||[self.type intValue] == 8) {
                lb_price.text = @"购买";

            }else{
                lb_price.text = @"抢";
            }
            
        }else{
            lb_price.text =  [NSString stringWithFormat:@"X%@",countMyActiveReceived];
            lb_price.font = [UIFont systemFontOfSize:25];

        }
        
        
    }else{
        lb_price.text = [NSString stringWithFormat:@"X%@",[dicCupon objectForKey:@"userCount"]];
    }

    
    //距离
    if (distence.floatValue/1000 < 1 )
    {
        NSString *str = [NSString stringWithFormat:@"%fM",distence.floatValue];
        CGSize size= [str boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                                             options:NSStringDrawingUsesLineFragmentOrigin
                                                          attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:10.f]}
                                                             context:nil].size;
        lb_distance.frame = CGRectMake(iv_topBack.frame.size.width-size.width-5, lb_distance.frame.origin.y, size.width, size.height);
        
        lb_distance.text = str;
    }
    else
    {
        NSString *str = [NSString stringWithFormat:@"%dKm",distence.intValue/1000];
        CGSize size= [str boundingRectWithSize:CGSizeMake(MAXFLOAT, MAXFLOAT)
                                       options:NSStringDrawingUsesLineFragmentOrigin
                                    attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:10.f]}
                                       context:nil].size;
        lb_distance.frame = CGRectMake(iv_topBack.frame.size.width-size.width-5, lb_distance.frame.origin.y, size.width, size.height);
        lb_distance.text = str;
    }

    
    //说明
    if ([dicCupon objectForKey:@"remark"] == NULL || [[dicCupon objectForKey:@"remark"]  isEqual:@""]) {
        
        lb_stateLabel.text = @"暂无说明";
    }
    else
    {
        lb_stateLabel.text = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"remark"]];
    }
    
    
    if (self.type.intValue == 1)
    {
        
        NSString *insteadPrice = [NSString stringWithFormat:@"￥%@",[dicCupon objectForKey:@"insteadPrice"]];
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@%@",insteadPrice,[dicCupon objectForKey:@"function"]]];
        [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14.0] range:NSMakeRange(0,insteadPrice.length)];
        [str addAttribute:NSForegroundColorAttributeName value:UICOLOR(222, 109, 55, 1) range:NSMakeRange(0,insteadPrice.length)];
        lb_useDes.attributedText = str;
        self.couponMessage = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"function"]];
    }
    else if (self.type.intValue == 3)
    {
        NSString *insteadPrice = [NSString stringWithFormat:@"￥%@",[dicCupon objectForKey:@"insteadPrice"]];

          NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@满%@元立减%@元",insteadPrice,[dicCupon objectForKey:@"availablePrice"],[dicCupon objectForKey:@"insteadPrice"]]];
        [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14.0] range:NSMakeRange(0,insteadPrice.length)];
        [str addAttribute:NSForegroundColorAttributeName value:UICOLOR(222, 109, 55, 1) range:NSMakeRange(0,insteadPrice.length)];
        lb_useDes.attributedText = str;
        
        self.couponMessage = [NSString stringWithFormat:@"满%@元立减%@元",[dicCupon objectForKey:@"availablePrice"],[dicCupon objectForKey:@"insteadPrice"]];

    }
    else if (self.type.intValue == 4)
    {
        NSString *discountPercent = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"discountPercent"]];
        NSString *discountPStr = [NSString stringWithFormat:@"%.1f折",[discountPercent floatValue]];
        
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@满%@元打%0.1f折",discountPStr,[dicCupon objectForKey:@"availablePrice"],[[dicCupon objectForKey:@"discountPercent"]floatValue]]];
        [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14.0] range:NSMakeRange(0,discountPStr.length)];
        [str addAttribute:NSForegroundColorAttributeName value:UICOLOR(222, 109, 55, 1) range:NSMakeRange(0,discountPStr.length)];
        lb_useDes.attributedText = str;
        
        self.couponMessage = [NSString stringWithFormat:@"满%@元打%0.1f折",[dicCupon objectForKey:@"availablePrice"],[[dicCupon objectForKey:@"discountPercent"]floatValue]];


    }
    else if (self.type.intValue == 7) //兑换
    {
        NSString *discountPercent = [NSString stringWithFormat:@"￥%@",[dicCupon objectForKey:@"payPrice"]];
        
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@%@",discountPercent,[dicCupon objectForKey:@"function"]]];
        [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14.0] range:NSMakeRange(0,discountPercent.length)];
        [str addAttribute:NSForegroundColorAttributeName value:UICOLOR(222, 109, 55, 1) range:NSMakeRange(0,discountPercent.length)];
        lb_useDes.attributedText = str;
        
        
    }
    else if (self.type.intValue == 8) //代金
    {
        NSString *discountPercent = [NSString stringWithFormat:@"￥%@",[dicCupon objectForKey:@"payPrice"]];
        
        NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:[NSString stringWithFormat:@"%@代%@元",discountPercent,[dicCupon objectForKey:@"insteadPrice"]]];
        [str addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:14.0] range:NSMakeRange(0,discountPercent.length)];
        [str addAttribute:NSForegroundColorAttributeName value:UICOLOR(222, 109, 55, 1) range:NSMakeRange(0,discountPercent.length)];
        lb_useDes.attributedText = str;
        
        
    }

    else
    {
        
        lb_useDes.text = [NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"function"]];
    }
  
    NSString *dayStartUsingTime = [dicCupon objectForKey:@"dayStartUsingTime"];
    NSString *dayEndUsingTime = [dicCupon objectForKey:@"dayEndUsingTime"];
    
    if (dayStartUsingTime.length==0 && dayEndUsingTime.length ==0) {
        lb_couponTime.text = @"全天使用";
    }else{
        lb_couponTime.text = [NSString stringWithFormat:@"使用时间:%@ - %@",[dicCupon objectForKey:@"dayStartUsingTime"],[dicCupon objectForKey:@"dayEndUsingTime"]];
    }
    

    lb_status.text = [CouponImage couponTypeName:[self.type intValue]];

    lb_shopName.text = [gloabFunction changeNullToBlank:[dicCupon objectForKey:@"shopName"]];
    
    NSString *startTimeString =  [dicCupon objectForKey:@"startUsingTime"] ;
    NSString *expireTimeString = [dicCupon objectForKey:@"expireTime"] ;

    if (startTimeString.length==0 && expireTimeString.length ==0) {
        lb_endTime.text = @"不限使用";
    }else{
        lb_endTime.text = [NSString stringWithFormat:@"%@ - %@",startTimeString,expireTimeString];
    }


    [iv_shopLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicCupon objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    if ([self.couponDic objectForKey:@"isSelect"]) {
        NSString *str = [self.couponDic objectForKey:@"isSelect"];
        if ([str isEqualToString:@"YES"]) {
            [self setCellExpansion:YES];
            
        }else{
            
            [self setCellExpansion:NO];
            
        }
    }else{
        [self setCellExpansion:NO];
        
    }
    
    
}

- (void)btnExpansionClick
{
    btn_expansion.selected = !btn_expansion.selected;
    if (self.coupondelegate != nil)
    {
        [self.coupondelegate setCellSelect:btn_expansion.selected indexPath:self.currentRow dic:self.couponDic];
    }
}

- (void)setCellExpansion:(BOOL)isExpansion
{
    btn_expansion.selected = isExpansion;
    if (btn_expansion.selected)
    {
        CGRect rect = iv_topBack.frame;
        rect.size.height = 160;
        iv_topBack.frame = rect;
        iv_bottomBack.hidden = NO;
        rect = iv_rightBack.frame;
        rect.size.height = 160;
        iv_rightBack.frame = rect;
        
        iv_rightBack.backgroundColor = [CouponImage couponBgColor:[self.type intValue]];

        CGRect rectExpance = btn_expansion.frame;
         iv_jtDown.image = [UIImage imageNamed:@"iv_jtUp"];
        rectExpance.origin.y = 160-rectExpance.size.height;
        btn_expansion.frame = rectExpance;
        btn_expansion.transform = CGAffineTransformMakeRotation(0);
        
        self.typeImage.center = CGPointMake(iv_rightBack.frame.size.width/2, iv_rightBack.frame.size.height/2);
        
     

    }
    else
    {
        CGRect rect = iv_topBack.frame;
        rect.size.height = 90;
        iv_topBack.frame = rect;
        iv_bottomBack.hidden = YES;
        rect = iv_rightBack.frame;
        rect.size.height = 90;
        iv_rightBack.frame = rect;
        
        iv_rightBack.backgroundColor = [CouponImage couponBgColor:[self.type intValue]];


        
        
        CGRect rectExpance = btn_expansion.frame;
        rectExpance.origin.y = 90-rectExpance.size.height;
        btn_expansion.frame = rectExpance;
        btn_expansion.transform = CGAffineTransformMakeRotation(0);
        
        self.typeImage.center = CGPointMake(iv_rightBack.frame.size.width/2, iv_rightBack.frame.size.height/2);
        
   
    }
}

- (void)btnShareClick:(UIButtonEx*)sender
{
    if (self.coupondelegate != nil)
    {
        [self.coupondelegate btnShareClick:self.couponDic];
    }
}
-(void)click_rightView{
    
    if(self.isGrabvotes){
        
        
        NSString *couponType =[NSString stringWithFormat:@"%@",[self.couponDic objectForKey:@"couponType"]];
        if ([couponType intValue]==7 ||[couponType intValue]==8) {
            [self.coupondelegate grabBuyCoupon:self.couponDic];
        }else{
            [self.coupondelegate grabCupon:self.couponDic currenRow:self.currentRow];

        }
        
    }else{
        
        if([self.coupondelegate respondsToSelector:@selector(clickCell:currenRow:)]){
            [self.coupondelegate clickCell:self.couponDic currenRow:self.currentRow];
        }
    }
    
}

@end
