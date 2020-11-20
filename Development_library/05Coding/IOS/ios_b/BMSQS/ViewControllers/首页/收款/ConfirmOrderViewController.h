//
//  ConfirmOrderViewController.h
//  BMSQS
//
//  Created by gh on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface ConfirmOrderViewController : UIViewControllerEx

@property (nonatomic, strong)NSString *bankAccountCode;
@property (nonatomic,strong)NSString *orderAmount;
@property (nonatomic, strong)NSString *noDiscountPrice;

@property (nonatomic, strong)NSDictionary *orderData;


@end
