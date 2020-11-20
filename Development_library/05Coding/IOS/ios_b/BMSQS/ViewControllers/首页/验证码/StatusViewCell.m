//
//  StatusViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "StatusViewCell.h"

@implementation StatusViewCell

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
        [self addSubview:self.statusLB];
    }
    return self;
}



- (UILabel *)statusLB
{
    if (_statusLB == nil) {
        self.statusLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 20, 30)];
        _statusLB.font = [UIFont systemFontOfSize:14];
//        _statusLB.backgroundColor = [UIColor redColor];
    }
    return _statusLB;
}


@end
