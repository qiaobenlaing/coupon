//
//  BMSQ_BuyOrderController.h
//  BMSQC
//
//  Created by djx on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "BMSQ_BuyOrderCuponController.h"

@interface BMSQ_BuyOrderController : UIViewControllerEx<UITextFieldDelegate,BuyOrderCuponDelegate>

@property(nonatomic,strong)NSString* shopCode;
@property(nonatomic,strong)NSString* shopName;
@end
