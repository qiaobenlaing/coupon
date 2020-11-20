//
//  DCPaymentView.h
//  DCPayAlertDemo
//
//  Created by dawnnnnn on 15/12/9.
//  Copyright © 2015年 dawnnnnn. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol DCPaymentDelegate <NSObject>

- (void)selectBackCard;
- (void)selectCode;


@end



@interface DCPaymentView : UIView



@property (nonatomic, copy) NSString *title, *mobileNum, *cardMessage;//标题 手机号 银行卡号
@property (nonatomic, assign)float amount;

@property (nonatomic, assign)id<DCPaymentDelegate> DCdelegate;
@property (nonatomic, copy) void (^completeHandle)(NSString *inputPwd);

- (void)showInTheView:(UIView *)view isCode:(BOOL)isCode;
- (void)show;

- (void)timerFireMethod;

@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com