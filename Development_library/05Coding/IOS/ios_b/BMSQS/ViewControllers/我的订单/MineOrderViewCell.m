//
//  MineOrderViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "MineOrderViewCell.h"

@implementation MineOrderViewCell

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
        [self addSubview:self.typeLabel];
        [self addSubview:self.numberLabel];
    }
    return self;
}

- (UILabel *)typeLabel{
    if (_typeLabel == nil) {
        self.typeLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 90, self.frame.size.height)];
//        _typeLabel.backgroundColor = [UIColor redColor];
        _typeLabel.font = [UIFont systemFontOfSize:17];
    }
    return _typeLabel;
}

- (UILabel *)numberLabel
{
    if (_numberLabel == nil) {
        self.numberLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 60, self.frame.size.height / 3, 20, 20)];
        _numberLabel.backgroundColor = [UIColor redColor];
        _numberLabel.font = [UIFont systemFontOfSize:15];
        _numberLabel.textAlignment = NSTextAlignmentCenter;
        _numberLabel.textColor = [UIColor whiteColor];
        _numberLabel.layer.cornerRadius = _numberLabel.bounds.size.width/2;
        _numberLabel.layer.masksToBounds = YES;
    }
    return _numberLabel;
}



@end
