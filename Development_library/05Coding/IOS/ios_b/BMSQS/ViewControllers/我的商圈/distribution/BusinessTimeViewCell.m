//
//  BusinessTimeViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/18.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BusinessTimeViewCell.h"

@implementation BusinessTimeViewCell

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
        [self addSubview:self.titleLB];
        [self addSubview:self.leftTimeLB];
        [self addSubview:self.centerTimeLB];
        [self addSubview:self.rightTimeLB];
    }
    return self;
}

- (UILabel *)titleLB
{
    if (_titleLB == nil) {
        self.titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 100, 20)];
        _titleLB.font = [UIFont systemFontOfSize:14];
    }
    return _titleLB;
}


- (UILabel *)leftTimeLB
{
    if (_leftTimeLB == nil) {
        self.leftTimeLB = [[UILabel alloc] initWithFrame:CGRectMake(15, 35,90, 20)];
        _leftTimeLB.font = [UIFont systemFontOfSize:14];
        _leftTimeLB.textColor = UICOLOR(152, 152, 157, 1.0);
    }
    return _leftTimeLB;
}

- (UILabel *)centerTimeLB
{
    if (_centerTimeLB == nil) {
        self.centerTimeLB = [[UILabel alloc] initWithFrame:CGRectMake(120, 35, 90, 20)];
        _centerTimeLB.font = [UIFont systemFontOfSize:14];
        _centerTimeLB.textColor = UICOLOR(152, 152, 157, 1.0);
    }
    return _centerTimeLB;
}

- (UILabel *)rightTimeLB
{
    if (_rightTimeLB == nil) {
        self.rightTimeLB = [[UILabel alloc] initWithFrame:CGRectMake(225, 35, 90, 20)];
        _rightTimeLB.font = [UIFont systemFontOfSize:14];
        _rightTimeLB.textColor = UICOLOR(152, 152, 157, 1.0);
    }
    return _rightTimeLB;
}


@end
