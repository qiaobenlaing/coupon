//
//  BMSQ_OrderViewController.h
//  BMSQC
//
//  Created by liuqin on 15/9/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_OrderViewController : UIViewControllerEx


@property (nonatomic, strong)NSString *shopName;
@property (nonatomic, strong)NSString *userCode;   //用户编码
@property (nonatomic, strong)NSString *shopCode;   //商家编码
@property (nonatomic, strong)NSString *consumeAmount;  //消费金额
@property (nonatomic, strong)NSString *userCouponCode;  //用户优惠券编码
@property (nonatomic, strong)NSString *platbouns;    //红包金额
@property (nonatomic, strong)NSString *shopbouns;    //商家红包

@property (nonatomic,strong)NSString *price;

@property (nonatomic, assign)int fromVC;

@end
