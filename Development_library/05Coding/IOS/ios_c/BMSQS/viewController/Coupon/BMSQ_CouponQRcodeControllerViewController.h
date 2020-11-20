//
//  BMSQ_CouponQRcodeControllerViewController.h
//  BMSQC
//
//  Created by liuqin on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_CouponQRcodeControllerViewController : UIViewControllerEx
{
    UIImageView *_qrcodeImageView;
    
}
@property(nonatomic, strong)NSString *myTitle;
@property(nonatomic, strong)NSString *userCouponCode;
@property(nonatomic, strong)NSString *shopCode;
@property(nonatomic, strong)NSString *logoUrl;
@property(nonatomic, strong)NSString *consumeCode;

@end
