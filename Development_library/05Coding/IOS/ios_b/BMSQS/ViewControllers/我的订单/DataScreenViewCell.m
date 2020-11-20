//
//  DataScreenViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "DataScreenViewCell.h"

@implementation DataScreenViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self addSubview:self.dayBut];
        [self addSubview:self.weekBut];
        [self addSubview:self.monthBut];
    }
    return self;
}

- (UIButton *)dayBut{
    if (_dayBut == nil) {
        self.dayBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _dayBut.frame = CGRectMake(10, self.frame.size.height / 4, (self.frame.size.width / 2 - 40) / 3 , self.frame.size.height / 2);
        [_dayBut setTitle:@"日" forState:UIControlStateNormal];
        [_dayBut setTitleColor:UICOLOR(88, 88, 88, 1.0) forState:UIControlStateNormal];
        _dayBut.titleLabel.font = [UIFont systemFontOfSize:14];
        _dayBut.layer.borderColor = [UICOLOR(232, 232, 232, 1.0) CGColor];
        _dayBut.layer.borderWidth = 1.5;
        _dayBut.layer.cornerRadius = 4.5;
    }
    return _dayBut;
}

- (UIButton *)weekBut{
    if (_weekBut == nil) {
        self.weekBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _weekBut.frame = CGRectMake((self.frame.size.width / 2 - 40) / 3 + 20, self.frame.size.height / 4, (self.frame.size.width / 2 - 40) / 3, self.frame.size.height / 2);
        [_weekBut setTitle:@"周" forState:UIControlStateNormal];
        [_weekBut setTitleColor:UICOLOR(88, 88, 88, 1.0) forState:UIControlStateNormal];
        _weekBut.titleLabel.font = [UIFont systemFontOfSize:14];
        _weekBut.layer.borderColor = [UICOLOR(232, 232, 232, 1.0) CGColor];
        _weekBut.layer.borderWidth = 1.5;
        _weekBut.layer.cornerRadius = 4.5;
    }
    return _weekBut;
}

- (UIButton *)monthBut{
    if (_monthBut == nil) {
        self.monthBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _monthBut.frame = CGRectMake((self.frame.size.width / 2 - 40) / 3 * 2 + 30, self.frame.size.height / 4, (self.frame.size.width / 2 - 40) / 3, self.frame.size.height / 2);
        [_monthBut setTitle:@"月" forState:UIControlStateNormal];
        [_monthBut setTitleColor:UICOLOR(88, 88, 88, 1.0) forState:UIControlStateNormal];
        _monthBut.titleLabel.font = [UIFont systemFontOfSize:14];
        _monthBut.layer.borderColor = [UICOLOR(232, 232, 232, 1.0) CGColor];
        _monthBut.layer.borderWidth = 1.5;
        _monthBut.layer.cornerRadius = 4.5;
    }
    return _monthBut;
}



@end
