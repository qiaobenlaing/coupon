//
//  BMSQ_BuyOrderCuponController.h
//  BMSQC
//
//  Created by djx on 15/8/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@protocol BuyOrderCuponDelegate <NSObject>

- (void)getCuponData:(NSDictionary*)dicCupon;

@end

@interface BMSQ_BuyOrderCuponController : UIViewControllerEx

@property(nonatomic,strong)NSString* shopCode;
@property(nonatomic,strong)NSString* consumeAmount;

@property(nonatomic,assign)NSObject<BuyOrderCuponDelegate>* delegate;
@end
