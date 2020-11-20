//
//  BMSQ_ShopOrderCell.m
//  BMSQS
//
//  Created by gh on 15/10/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopOrderCell.h"
#import "UIImageView+WebCache.h"


@interface BMSQ_ShopOrderCell() {
    UIImageView* iv_Back;
    UILabel *lb_orderNbr;
    UIImageView* iv_shopLogo;
    UILabel* lb_shopName;
    UILabel* lb_realPay;
    UILabel* lb_orderTime;
    UILabel* lb_orderNumber;
    UILabel* lb_payment;
    
}

@end

@implementation BMSQ_ShopOrderCell

- (void)awakeFromNib {
    [self setViewUp];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setViewUp];
        
    }
    
    return self;
}


- (void)setViewUp {
    CGFloat x = 10;
    CGFloat y = 10;
    
    iv_Back = [[UIImageView alloc]initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH, 140-y)];
    iv_Back.backgroundColor = [UIColor whiteColor];
    iv_Back.userInteractionEnabled = YES;
    [self addSubview:iv_Back];
    
    CGFloat height1 = 30;
    lb_orderNbr = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-(x*2), height1)];
    lb_orderNbr.backgroundColor = [UIColor clearColor];
    lb_orderNbr.font = [UIFont systemFontOfSize:12];
    lb_orderNbr.text = @"订单号";
    [iv_Back addSubview:lb_orderNbr];
    
    lb_payment = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, APP_VIEW_WIDTH-(x*2), height1)];
    lb_payment.backgroundColor = [UIColor clearColor];
    lb_payment.textColor = [UIColor darkGrayColor];
    lb_payment.font = [UIFont systemFontOfSize:12];
    lb_payment.textAlignment = NSTextAlignmentRight;
    lb_payment.text = @"支付状态";
    [iv_Back addSubview:lb_payment];
    
    
    UIView *line1 = [[UIView alloc] initWithFrame:CGRectMake(0, height1-0.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line1.backgroundColor = APP_CELL_LINE_COLOR;
    [iv_Back addSubview:line1];
    
    CGFloat logosize = 50;
    
    iv_shopLogo = [[UIImageView alloc] initWithFrame:CGRectMake(x, height1+10, logosize, logosize)];
    iv_shopLogo.layer.borderWidth = 0.5;
    iv_shopLogo.layer.cornerRadius = 5;
    iv_shopLogo.layer.borderColor = [[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1]CGColor];
    iv_shopLogo.layer.masksToBounds = YES;
    iv_shopLogo.layer.backgroundColor = [[UIColor colorWithRed:231.0/255.0 green:231.0/255.0 blue:231.0/255.0 alpha:1]CGColor];
    [iv_Back addSubview:iv_shopLogo];
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(x+logosize+5, iv_shopLogo.frame.origin.y, APP_VIEW_WIDTH/2, logosize/2)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    lb_shopName.text = @"用户昵称";
    [lb_shopName setFont:[UIFont systemFontOfSize:12]];
    [iv_Back addSubview:lb_shopName];
    
    lb_orderTime = [[UILabel alloc] initWithFrame:CGRectMake(x+logosize+5, iv_shopLogo.frame.origin.y+logosize/2, APP_VIEW_WIDTH-(65*2), logosize/2)];
    lb_orderTime.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_orderTime.text = @"时间";
    lb_orderTime.font = [UIFont systemFontOfSize:12];
    [iv_Back addSubview:lb_orderTime];
    
    y = height1+70;
    
    UIView *line2 = [[UIView alloc] initWithFrame:CGRectMake(0, y-0.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line2.backgroundColor = APP_CELL_LINE_COLOR;
    [iv_Back addSubview:line2];
    
    
    UILabel *lb_orderMoney = [[UILabel alloc] initWithFrame:CGRectMake(x, y, APP_VIEW_WIDTH-(2*x), height1)];
    lb_orderMoney.text = @"订单金额";
    lb_orderMoney.font = [UIFont systemFontOfSize:13];
    [iv_Back addSubview:lb_orderMoney];
    
    
    lb_realPay = [[UILabel alloc]initWithFrame:CGRectMake(x, y, APP_VIEW_WIDTH-(2*x), height1)];
    lb_realPay.text = @"";
    lb_realPay.textColor = [UIColor redColor];
    lb_realPay.font = [UIFont systemFontOfSize:18];
    [iv_Back addSubview:lb_realPay];
}


- (void)setCellValue:(NSDictionary*)dicCupon {
    if(dicCupon == nil || dicCupon.count <= 0)
    {
        return;
    }
//    avatarUrl = "/Public/Uploads/20151013/561c6aac5e0b4.jpg";
//    bankCardDeduction = "0.00";
//    cardDeduction = "0.00";
//    consumeCode = "4f4e67f8-ac71-9779-ded3-48c5f021739a";
//    couponDeduction = "0.00";
//    couponUsed = 0;
//    deduction = "0.00";
//    isCard = 0;
//    logoUrl = "/Public/Uploads/20150929/560a3876d40dc.png";
//    nickName = "\U5c0f\U9e21";
//    orderAmount = "0.00";
//    orderNbr = 0001;
//    orderTime = "2015-09-25 14:55:52";
//    payType = 1;
//    platBonus = "0.00";
//    realPay = "0.00";
//    shopBonus = "0.00";
//    shopName = "\U7269\U4f18\U8d85\U5e02";
//    usedCardCode = "<null>";
//    usedUserCouponCode = "<null>";
//    userConsumeStatus = 3;
//    userMobileNbr = 15868179748;
    
//    self.couponDic = dicCupon;
    
    [iv_shopLogo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dicCupon objectForKey:@"avatarUrl"]]]];
    
    lb_orderNbr.text = [NSString stringWithFormat:@"订单号:%@",[dicCupon objectForKey:@"orderNbr"]];
    
    lb_shopName.text = [dicCupon objectForKey:@"nickName"];
    
    lb_realPay.text = [NSString stringWithFormat:@"               %@元",[dicCupon objectForKey:@"realPay"]];
    
    lb_orderTime.text = [NSString stringWithFormat:@"下单时间:%@",[dicCupon objectForKey:@"orderTime"]];
    
    NSLog(@"%@", [dicCupon objectForKey:@"userConsumeStatus"]);//订单类型
    NSString *s = [dicCupon objectForKey:@"userConsumeStatus"];
    
    if ([s  isEqual: @"3"]) {
        lb_payment.text = @"已付款";
        
    } else if ([s  isEqual: @"1"]){
        lb_payment.text = @"未付款";
        
    }else if ([s  isEqual: @"2"]){
        lb_payment.text = @"付款中";
        
    }else if ([s  isEqual: @"4"]){
        lb_payment.text = @"已取消付款";
        
    }else if ([s  isEqual: @"5"]){
        lb_payment.text = @"付款失败";
        
    }

}

@end
