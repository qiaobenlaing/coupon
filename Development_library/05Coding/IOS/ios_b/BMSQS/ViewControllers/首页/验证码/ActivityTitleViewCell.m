//
//  ActivityTitleViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ActivityTitleViewCell.h"

@implementation ActivityTitleViewCell

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
    }
    return self;
}

- (UILabel *)titleLB
{
    if (_titleLB == nil) {
        self.titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 20, 30)];
        _titleLB.font = [UIFont systemFontOfSize:14];
//        _titleLB.backgroundColor = [UIColor redColor];
    }
    return _titleLB;
}




@end
