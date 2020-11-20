//
//  DetailsViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "DetailsViewCell.h"

@implementation DetailsViewCell

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
        [self addSubview:self.timeImage];
        [self addSubview:self.addressImage];
        [self addSubview:self.businessTimeLB1];
        [self addSubview:self.businessTimeLB2];
        [self addSubview:self.businessTimeLB3];
        [self addSubview:self.addressLB];
        [self addSubview:self.popularityLB];
        [self addSubview:self.line];
    }
    return self;
}


- (UIView *)line
{
    if (_line == nil) {
        self.line = [[UIView alloc] initWithFrame:CGRectMake(0, 84, APP_VIEW_WIDTH, 1)];
        self.line.backgroundColor = APP_VIEWCOLOR;
    }
    return _line;
    
}

- (UIImageView *)timeImage
{
    if (_timeImage == nil) {
        self.timeImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 20, 20)];
//        _timeImage.backgroundColor = [UIColor purpleColor];
        _timeImage.image = [UIImage imageNamed:@"time"];
    }
    return _timeImage;
}
- (UIImageView *)addressImage
{
    if (_addressImage == nil) {
        self.addressImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 45, 20, 20)];
//        _addressImage.backgroundColor = [UIColor purpleColor];
        _addressImage.image = [UIImage imageNamed:@"address"];
    }
    return _addressImage;
}
- (UILabel *)businessTimeLB1
{
    if (_businessTimeLB1 == nil) {
        self.businessTimeLB1 = [[UILabel alloc] initWithFrame:CGRectMake(30, 10, 70, 25)];
        _businessTimeLB1.textAlignment = NSTextAlignmentCenter;
        _businessTimeLB1.font = [UIFont systemFontOfSize:11.0];
//        _businessTimeLB1.backgroundColor = [UIColor redColor];
    }
    return _businessTimeLB1;
}
- (UILabel *)businessTimeLB2
{
    if (_businessTimeLB2 == nil) {
        self.businessTimeLB2 = [[UILabel alloc] initWithFrame:CGRectMake(103, 10, 70, 25)];
        _businessTimeLB2.textAlignment = NSTextAlignmentCenter;
        _businessTimeLB2.font = [UIFont systemFontOfSize:11.0];
//        _businessTimeLB2.backgroundColor = [UIColor redColor];
    }
    return _businessTimeLB2;
}
- (UILabel *)businessTimeLB3
{
    if (_businessTimeLB3 == nil) {
        self.businessTimeLB3 = [[UILabel alloc] initWithFrame:CGRectMake(176, 10, 70, 25)];
        _businessTimeLB3.textAlignment = NSTextAlignmentCenter;
        _businessTimeLB3.font = [UIFont systemFontOfSize:11.0];
//        _businessTimeLB3.backgroundColor = [UIColor redColor];
    }
    return _businessTimeLB3;
}
- (UILabel *)addressLB
{
    if (_addressLB == nil) {
        self.addressLB = [[UILabel alloc] initWithFrame:CGRectMake(35, 45, APP_VIEW_WIDTH - 45, 35)];
        _addressLB.font = [UIFont systemFontOfSize:13.0];
        _addressLB.numberOfLines = 0;
    }
    return _addressLB;
}
- (UILabel *)popularityLB
{
    if (_popularityLB == nil) {
        self.popularityLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 60, 10, 60, 25)];
        _popularityLB.font = [UIFont systemFontOfSize:10.0];
//        _popularityLB.backgroundColor = [UIColor purpleColor];
    }
    return _popularityLB;
}

- (void)setDetailsValueDic:(NSDictionary *)valueDic
{

    if (valueDic[@"businessHours"] != nil && ![valueDic[@"businessHours"] isKindOfClass:[NSNull class]] && [valueDic[@"businessHours"] count] != 0){
        
        NSArray * businessHours = [NSArray arrayWithArray:valueDic[@"businessHours"]];
        if (businessHours.count == 1) {
            self.businessTimeLB1.text = [NSString stringWithFormat:@"%@-%@", businessHours[0][@"open"], businessHours[0][@"close"]];
        }else if (businessHours.count == 2){
            self.businessTimeLB1.text = [NSString stringWithFormat:@"%@-%@", businessHours[0][@"open"], businessHours[0][@"close"]];
            self.businessTimeLB2.text = [NSString stringWithFormat:@"%@-%@", businessHours[1][@"open"], businessHours[1][@"close"]];
        }else if (businessHours.count == 3){
            self.businessTimeLB1.text = [NSString stringWithFormat:@"%@-%@", businessHours[0][@"open"], businessHours[0][@"close"]];
            self.businessTimeLB2.text = [NSString stringWithFormat:@"%@-%@", businessHours[1][@"open"], businessHours[1][@"close"]];
            self.businessTimeLB3.text = [NSString stringWithFormat:@"%@-%@", businessHours[2][@"open"], businessHours[2][@"close"]];
        }
        
    }
        
    
    self.popularityLB.text = [NSString stringWithFormat:@"人气:%@", valueDic[@"popularity"]];
//    self.addressLB.text = [NSString stringWithFormat:@"%@%@", valueDic[@"district"],valueDic[@"street"]];
    self.addressLB.text = [NSString stringWithFormat:@"%@%@%@", valueDic[@"city"],valueDic[@"district"],valueDic[@"street"]];
    
}


@end
