//
//  SortingViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "SortingViewCell.h"

@implementation SortingViewCell

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
        [self addSubview:self.titleLabel];
        [self addSubview:self.hookImage];
    }
    return self;
}

- (UILabel *)titleLabel{
    if (_titleLabel == nil) {
        self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, 0, self.frame.size.width / 2, self.frame.size.height)];
        _titleLabel.font = [UIFont systemFontOfSize:14];
        _titleLabel.textColor = UICOLOR(50, 50, 50, 1.0);
    }
    return _titleLabel;
}
- (UIImageView *)hookImage{
    if (_hookImage == nil) {
        self.hookImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.frame.size.width + 20, self.frame.size.height / 3, 10, self.frame.size.height / 3)];
        _hookImage.image = [UIImage imageNamed:@"对号.png"];
    }
    return _hookImage;
}


@end
