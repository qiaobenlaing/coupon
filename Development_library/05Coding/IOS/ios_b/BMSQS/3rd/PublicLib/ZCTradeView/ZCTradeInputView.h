//
//  ZCTradeInputView.h
//  直销银行
//
//  Created by 塔利班 on 15/4/30.
//  Copyright (c) 2015年 联创智融. All rights reserved.
//  交易输入视图

#import <Foundation/Foundation.h>

static NSString *ZCTradeInputViewCancleButtonClick = @"ZCTradeInputViewCancleButtonClick";
static NSString *ZCTradeInputViewOkButtonClick = @"ZCTradeInputViewOkButtonClick";
static NSString *ZCTradeInputViewPwdKey = @"ZCTradeInputViewPwdKey";

#import <UIKit/UIKit.h>
#import "UIView+Extension.h"

@class ZCTradeInputView;

@protocol ZCTradeInputViewDelegate <NSObject>

@optional
/** 确定按钮点击 */
- (void)tradeInputView:(ZCTradeInputView *)tradeInputView okBtnClick:(UIButton *)okBtn;
/** 取消按钮点击 */
- (void)tradeInputView:(ZCTradeInputView *)tradeInputView cancleBtnClick:(UIButton *)cancleBtn;

- (void)tradeInputViewallNum:(ZCTradeInputView *)tradeInputView;

@end

@interface ZCTradeInputView : UIView


@property (nonatomic,assign)BOOL isModify;

@property (nonatomic,strong)NSString *title;
/** 选择银行卡 */  //
@property (nonatomic, weak) UIButton *okBtn;
/** 取消按钮 */  
@property (nonatomic, weak) UIButton *cancleBtn;
/** 线 */
@property (nonatomic, weak)UIView *line;

/** 申请人手机号 */
@property (nonatomic, strong)NSString *mobileNum;

/** 银行卡信息 */
@property (nonatomic, strong)NSString *cardMessagte;

/** 消费金额 */
@property (nonatomic, strong)NSString *priceStr;

/** 数字数组 */
@property (nonatomic, strong) NSMutableArray *nums;

@property (nonatomic, weak) id<ZCTradeInputViewDelegate> delegate;


-(void)deleteAllNum;


@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com