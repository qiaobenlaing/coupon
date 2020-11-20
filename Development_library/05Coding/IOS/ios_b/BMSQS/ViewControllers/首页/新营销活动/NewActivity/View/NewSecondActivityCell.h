//
//  NewActivityCell.h
//  BMSQS
//
//  Created by gh on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ActivityDelegate <NSObject>

- (void)gotoChargeVC;
- (void)showRefundList;
- (void)secondTextField:(UITextField *)textField;


@end



@interface NewSecondActivityCell : UITableViewCell


@property (nonatomic, strong)UILabel *label01;
@property (nonatomic, strong)UILabel *label02;
//@property (nonatomic, strong)UILabel *label03;
@property (nonatomic, strong)UIButton *btn3;
@property (nonatomic, strong)UITextField *textField4;
@property (nonatomic, strong)UITextField *textField5;
@property (nonatomic, strong)UITextField *textField6;


@property (nonatomic, strong)id<ActivityDelegate>activityDelegate;


@end
