//
//  DistributionViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "DistributionViewCell.h"

@implementation DistributionViewCell

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
        [self addSubview:self.leftLable];
        [self addSubview:self.centerLable];
        [self addSubview:self.rightLable];
    }
    return self;
}

- (UILabel *)leftLable
{
    if (_leftLable == nil) {
        self.leftLable = [[UILabel alloc] initWithFrame:CGRectMake(20, 0, (self.frame.size.width - 40) / 3, self.frame.size.height)];
        _leftLable.font = [UIFont systemFontOfSize:12];
        _leftLable.textAlignment = NSTextAlignmentLeft;
    }
    return _leftLable;
}

- (UILabel *)centerLable
{
    if (_centerLable == nil) {
        self.centerLable = [[UILabel alloc] initWithFrame:CGRectMake((self.frame.size.width - 40) / 3 + 20, 0, (self.frame.size.width - 40) / 3, self.frame.size.height)];
        _centerLable.font = [UIFont systemFontOfSize:12];
        _centerLable.textAlignment = NSTextAlignmentLeft;
    }
    return _centerLable;
}

- (UILabel *)rightLable
{
    if (_rightLable == nil) {
        self.rightLable = [[UILabel alloc] initWithFrame:CGRectMake((self.frame.size.width - 40) / 3 * 2 + 20, 0, (self.frame.size.width - 40) / 3, self.frame.size.height)];
        _rightLable.font = [UIFont systemFontOfSize:12];
        _rightLable.textAlignment = NSTextAlignmentLeft;
    }
    return _rightLable;
}





@end
