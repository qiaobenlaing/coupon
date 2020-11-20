//
//  UpdateTimeViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/18.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UpdateTimeViewCell.h"

@implementation UpdateTimeViewCell

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
        [self addSubview:self.startLB];
        [self addSubview:self.startButton];
        [self addSubview:self.endButton];
        [self addSubview:self.endLB];
    }
    return self;
}


- (UILabel *)startLB
{
    if (_startLB == nil) {
        self.startLB = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 30, 50)];
        _startLB.text = @"从";
        _startLB.textColor = APP_TEXTCOLOR;
        _startLB.backgroundColor = [UIColor whiteColor];
        _startLB.textAlignment = NSTextAlignmentCenter;

    }
    return _startLB;
}

- (UILabel *)endLB
{
    if (_endLB == nil) {
        self.endLB = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 30, 50)];
        _endLB.text = @"到";
        _endLB.textColor = APP_TEXTCOLOR;
        _endLB.backgroundColor = [UIColor whiteColor];
        _endLB.textAlignment = NSTextAlignmentCenter;
        _endLB.center = CGPointMake(APP_VIEW_WIDTH/2, 25);
    }
    return _endLB;
}

- (UIButton *)startButton
{
    if (_startButton == nil) {
        self.startButton = [[UIButton alloc]initWithFrame:CGRectMake(40, 0, APP_VIEW_WIDTH/2-60, 50)];
        _startButton.backgroundColor = [UIColor clearColor];
        [_startButton setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        _startButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
    }
    return _startButton;
}

- (UIButton *)endButton
{
    if (_endButton == nil) {
        self.endButton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2+30, 0, APP_VIEW_WIDTH/2-60, 50)];
        _endButton.backgroundColor = [UIColor clearColor];
        [_endButton setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        _endButton.titleLabel.font = [UIFont systemFontOfSize:14.f];
    }
    return _endButton;
}

@end
