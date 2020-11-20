//
//  BMSQ_CouponDetailCell.m
//  BMSQC
//
//  Created by gh on 15/10/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponDetailCell.h"

@implementation BMSQ_CouponDetailCell

- (void)awakeFromNib {
    // Initialization code
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    return self;
}

- (void)setViewUp {
    
    _lb_1 = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 20)];
    _lb_1.backgroundColor = [UIColor clearColor];
    _lb_1.textColor = UICOLOR(135, 135, 135, 1.0);
    _lb_1.font = [UIFont systemFontOfSize:12];
    [self addSubview:_lb_1];
    
    _lb_2 = [[UILabel alloc] initWithFrame:CGRectMake(10, 20, APP_VIEW_WIDTH, 20)];
    _lb_2.backgroundColor = [UIColor clearColor];
    _lb_2.textColor = UICOLOR(135, 135, 135, 1.0);
    _lb_2.font = [UIFont systemFontOfSize:12];
    [self addSubview:_lb_2];
}


@end
