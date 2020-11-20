//
//  FilterViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "FilterViewCell.h"

@implementation FilterViewCell

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
        [self addSubview:self.iconImage];
        [self addSubview:self.titleLabel];
        [self addSubview:self.hookeImage];
    }
    return self;
}
- (UIImageView *)iconImage{
    if (_iconImage == nil) {
        self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, self.frame.size.height / 3, self.frame.size.height / 3, self.frame.size.height / 3)];
    }
    return _iconImage;
}
- (UILabel *)titleLabel{
    if (_titleLabel == nil) {
        self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.iconImage.frame.size.width + 30, 0, self.frame.size.width / 2, self.frame.size.height)];
        _titleLabel.font = [UIFont systemFontOfSize:14];
        _titleLabel.textColor = UICOLOR(49, 49, 49, 1.0);
    }
    return _titleLabel;
}
- (UIImageView *)hookeImage{
    if (_hookeImage == nil) {
        self.hookeImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.frame.size.width + 20, self.frame.size.height / 3, 10, self.frame.size.height / 3)];
        _hookeImage.image = [UIImage imageNamed:@"对号.png"];
    }
    return _hookeImage;
}


@end
