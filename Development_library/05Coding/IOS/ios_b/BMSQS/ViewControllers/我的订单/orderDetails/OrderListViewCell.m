//
//  OrderListViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "OrderListViewCell.h"

@implementation OrderListViewCell

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
//        self.isSelect = NO;
        [self addSubview:self.hookImage];
        [self addSubview:self.foodNameLB];
        [self addSubview:self.numberLB];
        [self addSubview:self.priceLB];
        
    }
    return self;
}

- (UIImageView *)hookImage
{
    if (_hookImage == nil) {
        self.hookImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, self.frame.size.height / 4, self.frame.size.height / 2, self.frame.size.height / 2)];
        _hookImage.image = [UIImage imageNamed:@"radio_no.png"];
    }
    return _hookImage;
}

- (UILabel *)foodNameLB
{
    if (_foodNameLB == nil) {
        self.foodNameLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.height / 2 + 20, self.frame.size.height / 4, 150, self.frame.size.height / 2)];
//        _foodNameLB.backgroundColor = [UIColor purpleColor];
        _foodNameLB.font = [UIFont systemFontOfSize:14];
        _foodNameLB.tag = 2001;
    }
    return _foodNameLB;
}

- (UILabel *)numberLB
{
    if (_numberLB == nil) {
        self.numberLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width / 2 + 20, self.frame.size.height / 4, 20, self.frame.size.height / 2)];
//        _numberLB.backgroundColor = [UIColor redColor];
        _numberLB.font = [UIFont systemFontOfSize:14];
        _numberLB.tag = 2002;
    }
    return _numberLB;
}

- (UILabel *)priceLB
{
    if (_priceLB == nil) {
        self.priceLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 50, self.frame.size.height / 4, 40, self.frame.size.height / 2)];
        _priceLB.font = [UIFont systemFontOfSize:14];
//        _priceLB.backgroundColor = [UIColor greenColor];
        _priceLB.textAlignment = NSTextAlignmentRight;
        _priceLB.tag = 2003;
    }
    return _priceLB;
}

- (void)setCellValue:(id)object
{
    self.object = object;
    
    self.foodNameLB.text = [NSString stringWithFormat:@"%@",[object objectForKey:@"productName"]];
    self.numberLB.text = [NSString stringWithFormat:@"x%@",[object objectForKey:@"productNbr"]];
    self.priceLB.text = [NSString stringWithFormat:@"%@元",[object objectForKey:@"productPrice"]];

}

- (int)didClickCell:(BOOL)select {
    
    int money = [[self.object objectForKey:@"productPrice"] intValue];
    if (!select) {
        self.isSelect=YES;
        self.hookImage.image = [UIImage imageNamed:@"radio_yes.png"];
        return money;
        
    }else if (select) {
        self.isSelect=NO;
        self.hookImage.image = [UIImage imageNamed:@"radio_no.png"];
        return -money;
    }
    
    
    
    return 0;
    
}




@end
