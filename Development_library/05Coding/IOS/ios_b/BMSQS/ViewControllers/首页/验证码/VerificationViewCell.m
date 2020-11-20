//
//  VerificationViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "VerificationViewCell.h"

@implementation VerificationViewCell

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
        [self addSubview:self.verifyCodeLB];
    }
    return self;
}


- (UILabel *)verifyCodeLB
{
    if (_verifyCodeLB == nil) {
        self.verifyCodeLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH - 20, 30)];
        _verifyCodeLB.font = [UIFont systemFontOfSize:14];
//        _verifyCodeLB.backgroundColor = [UIColor redColor];
    }
    return _verifyCodeLB;
}




@end
