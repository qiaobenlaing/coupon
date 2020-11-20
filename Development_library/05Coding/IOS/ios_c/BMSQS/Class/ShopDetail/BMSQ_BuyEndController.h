//
//  BMSQ_BuyEndController.h
//  BMSQC
//
//  Created by djx on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_BuyEndController : UIViewControllerEx

@property(nonatomic,strong)NSString* shopName;
@property(nonatomic,strong)NSString* payCount; //消费金额
@property(nonatomic,strong)NSString* payDisCount; //抵扣金额
@property(nonatomic,strong)NSString* payMore; //还需支付

@end
