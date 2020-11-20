//
//  MoneyViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "MoneyViewCell.h"

@implementation MoneyViewCell

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
        [self addSubview:self.consumptionLB];
        [self addSubview:self.moneyLB];
    }
    return self;
}

- (UILabel *)consumptionLB
{
    if (_consumptionLB == nil) {
        self.consumptionLB = [[UILabel alloc] initWithFrame:CGRectMake(10, self.frame.size.height / 4, 60, self.frame.size.height / 2)];
        _consumptionLB.font = [UIFont systemFontOfSize:14];
        _consumptionLB.text = @"消费金额";
    }
    return _consumptionLB;
}

- (UILabel *)moneyLB
{
    if (_moneyLB == nil) {
        self.moneyLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 60, self.frame.size.height / 4, 50, self.frame.size.height / 2)];
        _moneyLB.font = [UIFont systemFontOfSize:14];
//        _moneyLB.backgroundColor = [UIColor redColor];
        _moneyLB.textAlignment = NSTextAlignmentRight;
    }
    return _moneyLB;
}



@end
