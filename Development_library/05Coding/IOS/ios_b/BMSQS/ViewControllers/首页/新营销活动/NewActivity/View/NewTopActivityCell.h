//
//  NewTopActivityCell.h
//  BMSQS
//
//  Created by gh on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol TopActivityDelegate <NSObject>


- (void)topCellTag:(UITextField *)textField;
- (void)topCellTextFieldChange:(UITextField *)textField;
- (void)gotoActivityType;

- (void)clickBeginTime;
- (void)clickOverTime;

@end


@interface NewTopActivityCell : UITableViewCell

//@property (nonatomic, strong)UIImageView *topImageView;
@property (nonatomic, strong)UITextField *textField1;
@property (nonatomic, strong)UITextField *textField2;
@property (nonatomic, strong)UITextField *textField6;
@property (nonatomic, strong)UITextField *textField7;
@property (nonatomic, strong)UILabel *label3;
@property (nonatomic, strong)UILabel *label4;

@property (nonatomic, strong)UIButton *button5;

@property (nonatomic, strong)id<TopActivityDelegate>topActivityDelegate;


@end
