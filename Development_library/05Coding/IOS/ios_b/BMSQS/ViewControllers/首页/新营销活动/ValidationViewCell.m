//
//  ValidationViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ValidationViewCell.h"

@implementation ValidationViewCell

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
        [self addSubview:self.verificationCodeLB];
        [self addSubview:self.phoneNumberLB];
        [self addSubview:self.stateLB];
    }
    return self;
}

- (UILabel *)verificationCodeLB
{
    if (_verificationCodeLB == nil) {
        self.verificationCodeLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 150, 30)];
        _verificationCodeLB.font = [UIFont systemFontOfSize:16];
        _verificationCodeLB.backgroundColor = [UIColor yellowColor];
    }
    return _verificationCodeLB;
}

- (UILabel *)phoneNumberLB
{
    if (_phoneNumberLB == nil) {
        self.phoneNumberLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 40, 180, 30)];
        _phoneNumberLB.font = [UIFont systemFontOfSize:16];
        _phoneNumberLB.backgroundColor = [UIColor purpleColor];
    }
    return _phoneNumberLB;
}

- (UILabel *)stateLB
{
    if (_stateLB == nil) {
        self.stateLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 70, 5, 60, 30)];
        _stateLB.font = [UIFont systemFontOfSize:16];
        _stateLB.backgroundColor = [UIColor greenColor];
    }
    return _stateLB;
}

- (void)setCellValidationWithDic:(NSDictionary *)newDic
{
    
}


@end
