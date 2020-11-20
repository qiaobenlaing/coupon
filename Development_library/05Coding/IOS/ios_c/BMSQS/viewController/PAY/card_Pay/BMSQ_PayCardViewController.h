//
//  BMSQ_PayCardViewController.h
//  BMSQC
//
//  Created by liuqin on 15/9/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_PayCardViewController : UIViewControllerEx


@property (nonatomic, strong)NSString *payNewPrice; //实际支付金额



@property (nonatomic, strong)NSString *shopName;
@property(nonatomic, strong)NSString *shopCode;
@property(nonatomic, strong)NSString *consumeAmount;
@property(nonatomic, strong)NSString *batchCouponCode;
@property(nonatomic, strong)NSString *nbrCoupon;
@property(nonatomic, strong)NSString *platBonus;
@property(nonatomic, strong)NSString *shopBonus;
@property(nonatomic, strong)NSString *payType;
@property(nonatomic, strong)NSString *noDiscountPrice;//不参加优惠金额



//外卖
@property(nonatomic, assign)BOOL isTakeout;
@property(nonatomic, assign)BOOL isdining;
@property(nonatomic, strong)NSString *orderCode;
@property(nonatomic, strong)NSString *orderNbr;

@property(nonatomic, assign)BOOL type56;



@property (nonatomic, assign)int fromVC;


//订单详情
@property(nonatomic, strong)NSString *consumeCode;


//购买优惠券
@property(nonatomic, assign)BOOL isBuy;




@end
