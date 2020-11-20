//
//  DistributionPlanViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "DistributionPlanViewCell.h"

@implementation DistributionPlanViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self addSubview:self.leftRange];
        [self addSubview:self.rightRange];
        [self addSubview:self.leftDelivery];
        [self addSubview:self.rightDelivery];
        [self addSubview:self.leftRise];
        [self addSubview:self.rightRise];
        [self addSubview:self.rangeField];
        [self addSubview:self.riseField];
        [self addSubview:self.deliveryField];
        [self addSubview:self.headerLB];
        
    }
    return self;
}

- (UIView *)headerLB
{
    if (_headerLB == nil) {
        self.headerLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 100, 30)];
        _headerLB.backgroundColor = [UIColor whiteColor];
        _headerLB.textColor = APP_NAVCOLOR;
    }
    return _headerLB;
}




- (UILabel *)leftRange
{
    if (_leftRange == nil) {
        self.leftRange = [[UILabel alloc] initWithFrame:CGRectMake(10, 30, 100, 20)];
        _leftRange.text = @"配送范围:";
        
    }
    return _leftRange;
}

- (UITextField *)rangeField
{
    if (_rangeField == nil) {
        self.rangeField = [[UITextField alloc] initWithFrame:CGRectMake(self.frame.size.width - 70, 30, 30, 20)];
        _rangeField.font = [UIFont systemFontOfSize:14];
        _rangeField.keyboardType = UIKeyboardTypeDecimalPad;
        
        UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
        [topView setBarStyle:UIBarStyleBlackTranslucent];
        
        UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(2, 5, 50, 25);
        [btn setTitle:@"隐藏" forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
        [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
        UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
        NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
        [topView setItems:buttonsArray];
        [_rangeField setInputAccessoryView:topView];

        
    }
    return _rangeField;
}

- (UILabel *)rightRange
{
    if (_rightRange == nil) {
        self.rightRange = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 40, 30, 30, 20)];
        _rightRange.text = @"km";
        _rightRange.textAlignment = NSTextAlignmentLeft;
        _rightRange.font = [UIFont systemFontOfSize:14];
    }
    return _rightRange;
}

- (UILabel *)leftRise
{
    if (_leftRise == nil) {
        self.leftRise = [[UILabel alloc] initWithFrame:CGRectMake(10, 50, 100, 20)];
        _leftRise.text = @"起送价:";
    }
    return _leftRise;
}

- (UITextField *)riseField
{
    if (_riseField == nil) {
        self.riseField = [[UITextField alloc] initWithFrame:CGRectMake(self.frame.size.width - 70, 50, 30, 20)];
        _riseField.font = [UIFont systemFontOfSize:14];
        _riseField.keyboardType = UIKeyboardTypeDecimalPad;
        
        UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
        [topView setBarStyle:UIBarStyleBlackTranslucent];
        
        UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(2, 5, 50, 25);
        [btn setTitle:@"隐藏" forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
        [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
        UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
        NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
        [topView setItems:buttonsArray];
        [_riseField setInputAccessoryView:topView];

    }
    return _riseField;
}

- (UILabel *)rightRise
{
    if (_rightRise == nil) {
        self.rightRise = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 40, 50, 30, 20)];
        _rightRise.text = @"元";
        _rightRise.textAlignment = NSTextAlignmentLeft;
        _rightRise.font = [UIFont systemFontOfSize:14];
    }
    return _rightRise;
}

- (UILabel *)leftDelivery
{
    if (_leftDelivery == nil) {
        self.leftDelivery = [[UILabel alloc] initWithFrame:CGRectMake(10, 70, 100, 20)];
        _leftDelivery.text = @"配送费:";
    }
    return _leftDelivery;
}

- (UITextField *)deliveryField
{
    if (_deliveryField == nil) {
        self.deliveryField = [[UITextField alloc] initWithFrame:CGRectMake(self.frame.size.width - 70, 70, 30, 20)];
        _deliveryField.font = [UIFont systemFontOfSize:14];
        _deliveryField.keyboardType = UIKeyboardTypeDecimalPad;
        
        
        UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
        [topView setBarStyle:UIBarStyleBlackTranslucent];
        
        UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(2, 5, 50, 25);
        [btn setTitle:@"隐藏" forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
        [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
        UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
        NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
        [topView setItems:buttonsArray];
        [_deliveryField setInputAccessoryView:topView];
        
        
        
    }
    return _deliveryField;
}


- (UILabel *)rightDelivery
{
    if (_rightDelivery == nil) {
        self.rightDelivery = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 40, 70, 30, 20)];
        _rightDelivery.text = @"元";
        _rightDelivery.textAlignment = NSTextAlignmentLeft;
        _rightDelivery.font = [UIFont systemFontOfSize:14];
    }
    return _rightDelivery;
}

- (void)dismissKeyB {
    
    
    [self.rangeField resignFirstResponder];
    [self.riseField resignFirstResponder];
    [self.deliveryField resignFirstResponder];
        
}




@end
