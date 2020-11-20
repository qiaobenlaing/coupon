//
//  NewMarketingViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "NewMarketingViewCell.h"
#import "UIImageView+WebCache.h"
@implementation NewMarketingViewCell

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
        [self.contentView addSubview:self.topImageView];
        [self.contentView addSubview:self.signUpLB];
        [self.contentView addSubview:self.timeLB];
        [self.contentView addSubview:self.priceLB];
    }
    return self;
}

- (UIImageView *)topImageView
{
    if (_topImageView == nil) {
        self.topImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH*32.0/75)];
    }
    return _topImageView;
}



// 活动或报名
- (UILabel *)signUpLB
{
    if (_signUpLB == nil) {
        self.signUpLB = [[UILabel alloc] initWithFrame:CGRectMake(100, APP_VIEW_WIDTH*32.0/75+10, 100, 20)];
        _signUpLB.font = [UIFont systemFontOfSize:12];
        _signUpLB.textAlignment = NSTextAlignmentCenter;
        _signUpLB.layer.cornerRadius = 5;
        _signUpLB.clipsToBounds = YES;
        _signUpLB.layer.borderColor = [[UIColor grayColor]CGColor];
        _signUpLB.layer.borderWidth = 0.5f;
        _signUpLB.layer.masksToBounds = YES;
        
    }
    return _signUpLB;
}

- (UIView *)background
{
    if (_background == nil) {
        self.background = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_WIDTH*32.0/75-50, APP_VIEW_WIDTH, 50)];
        _background.backgroundColor = [UIColor blackColor];
        _background.alpha = 0.3;
        [self.topImageView addSubview:self.background];

    }
    return _background;
}


- (UILabel *)countLB
{
    if (_countLB == nil) {
        self.countLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH - 20, 50)];
        _countLB.font = [UIFont systemFontOfSize:14];
        _countLB.numberOfLines = 0;
        _countLB.textColor = [UIColor whiteColor];
        [self.background addSubview:self.countLB];
    }
    return _countLB;
}

- (UILabel *)timeLB
{
    if (_timeLB == nil) {
        self.timeLB = [[UILabel alloc] initWithFrame:CGRectMake(10, APP_VIEW_WIDTH*32.0/75+10, 80, 20)];
        _timeLB.font = [UIFont systemFontOfSize:12];
        _timeLB.textAlignment = NSTextAlignmentCenter;
        _timeLB.layer.cornerRadius = 5;
        _timeLB.clipsToBounds = YES;
         _timeLB.layer.borderColor = [[UIColor grayColor]CGColor];
          _timeLB.layer.borderWidth = 0.5f;
         _timeLB.layer.masksToBounds = YES;
    }
    return _timeLB;
}

- (UILabel *)priceLB
{
    if (_priceLB == nil) {
        self.priceLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 110, APP_VIEW_WIDTH*32.0/75+10, 110, 20)];
        _priceLB.font = [UIFont systemFontOfSize:12];
        _priceLB.textAlignment = NSTextAlignmentCenter;
        _priceLB.layer.cornerRadius = 7;
        _priceLB.clipsToBounds = YES;
        _priceLB.layer.borderColor = [[UIColor grayColor]CGColor];
        _priceLB.layer.borderWidth = 0.5f;
        _priceLB.layer.masksToBounds = YES;
    }
    return _priceLB;
}


- (void)setCellWithDic:(NSDictionary *)newDic;
{
    [self.topImageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,newDic[@"activityImg"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    
    self.countLB.text = [NSString stringWithFormat:@"%@", newDic[@"activityName"]];
    
    NSString * num = [NSString stringWithFormat:@"%@",newDic[@"status"]];
 
    if (num.intValue == 0) {
        
        self.signUpLB.text = @"未发布";
        
    }else if (num.intValue == 1){
        
        if ([newDic[@"limitedParticipators"] isEqualToString:@"0"]) {
            self.signUpLB.text = @"活动已发布";
            
        }else{
            
            NSString * participators = newDic[@"participators"];
            if ([participators isEqual:[NSNull null]]){
                self.signUpLB.text = [NSString stringWithFormat:@"已报名:%@/%@", @"0", newDic[@"limitedParticipators"]];
            }else{
                self.signUpLB.text = [NSString stringWithFormat:@"已报名:%@/%@", newDic[@"participators"], newDic[@"limitedParticipators"]];
            }
            
        }
        
    }else if (num.intValue == 2){
        
        self.signUpLB.text = @"停止报名";
        
    }else if (num.intValue == 3){
        
        self.signUpLB.text = @"活动已取消";
        
    }else if (num.intValue == 4){
        
        self.signUpLB.text = @"活动已结束";
        
    }else if (num.intValue == 5){
        
        self.signUpLB.text = @"活动已满员";
        
    }
    
    NSString * startTime = newDic[@"startTime"];
    NSString * endTime = newDic[@"endTime"];
    if ([startTime isEqualToString:endTime]) {
        NSString * monthStr = [startTime substringWithRange:NSMakeRange(5, 2)];
        NSString * dayStr = [startTime substringWithRange:NSMakeRange(8, 2)];
        NSString * huorStr = [startTime substringWithRange:NSMakeRange(11, 5)];
        self.timeLB.text = [NSString stringWithFormat:@"%@.%@  %@",monthStr,dayStr, huorStr];
    }else{
        NSString * startMonthStr = [startTime substringWithRange:NSMakeRange(5, 2)];
        NSString * startDayStr = [startTime substringWithRange:NSMakeRange(8, 2)];
        NSString * endMonthStr = [endTime substringWithRange:NSMakeRange(5, 2)];
        NSString * endDayStr = [endTime substringWithRange:NSMakeRange(8, 2)];
        self.timeLB.text = [NSString stringWithFormat:@"%@.%@--%@.%@",startMonthStr,startDayStr, endMonthStr,endDayStr];
    }
    
    NSNumber * maxPay = (NSNumber *)newDic[@"totalPayment"];
    NSNumber * minPay = (NSNumber *)newDic[@"minPrice"];
    if ([maxPay isEqual:@"0"]) {
        self.priceLB.text = @"免费";
        
        
    }else{
        
        if ([minPay isEqualToNumber:maxPay]){
            
            self.priceLB.text = [NSString stringWithFormat:@"￥%.2f", [maxPay floatValue]];
            
        }else{
            
            self.priceLB.text = [NSString stringWithFormat:@"￥%.2f-￥%.2f", [minPay floatValue],[maxPay floatValue]];
            
        }
        
    }
    
    
    
    
    
}




@end
