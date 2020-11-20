//
//  VisitorViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "VisitorViewCell.h"

@implementation VisitorViewCell

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
        [self addSubview:self.iconImage];
        [self addSubview:self.shopName];
    }
    return self;
}

- (UIImageView *)iconImage
{
    if (_iconImage == nil) {
        self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, 40, 40)];
//        _iconImage.backgroundColor = [UIColor purpleColor];
    }
    return _iconImage;
}

- (UILabel *)shopName
{
    if (_shopName == nil) {
        self.shopName = [[UILabel alloc] initWithFrame:CGRectMake(90, 20, APP_VIEW_WIDTH - 55, 20)];
        _shopName.font = [UIFont systemFontOfSize:14.0];
//        _shopName.backgroundColor = [UIColor grayColor];
    }
    return _shopName;
}





@end
