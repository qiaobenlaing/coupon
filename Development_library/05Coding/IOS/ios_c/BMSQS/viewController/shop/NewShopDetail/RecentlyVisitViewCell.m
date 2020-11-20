//
//  RecentlyVisitViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RecentlyVisitViewCell.h"

@implementation RecentlyVisitViewCell

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
        [self addSubview:self.recentlyVisitLB];
        [self addSubview:self.userIconImage1];
        [self addSubview:self.userIconImage2];
        [self addSubview:self.userIconImage3];
    }
    return self;
}

- (UILabel *)recentlyVisitLB
{
    if (_recentlyVisitLB == nil) {
        self.recentlyVisitLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 100, 30)];
        _recentlyVisitLB.font = [UIFont systemFontOfSize:15.0];
        _recentlyVisitLB.text = @"最近访问";
//        _recentlyVisitLB.backgroundColor = [UIColor purpleColor];
    }
    return _recentlyVisitLB;
}

- (UIImageView *)userIconImage1
{
    if (_userIconImage1 == nil) {
        self.userIconImage1 = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 125, 5, 25, 25)];
//        _userIconImage1.backgroundColor = [UIColor purpleColor];
    }
    return _userIconImage1;
}

- (UIImageView *)userIconImage2
{
    if (_userIconImage2 == nil) {
        self.userIconImage2 = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 90, 5, 25, 25)];
//        _userIconImage2.backgroundColor = [UIColor purpleColor];
    }
    return _userIconImage2;
}

- (UIImageView *)userIconImage3
{
    if (_userIconImage3 == nil) {
        self.userIconImage3 = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 55, 5, 25, 25)];
//        _userIconImage3.backgroundColor = [UIColor purpleColor];
    }
    return _userIconImage3;
}

- (void)setRecentlyValueAry:(NSArray *)valueAry
{
    
    if (valueAry != nil && ![valueAry isKindOfClass:[NSNull class]] && valueAry != 0 && valueAry.count != 0){
        
        if (valueAry.count == 1) {

            [self.userIconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[0][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        }else if (valueAry.count == 2){
            [self.userIconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[0][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
            [self.userIconImage2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[1][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        }else{
            [self.userIconImage1 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[0][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
            [self.userIconImage2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[1][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
            [self.userIconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,valueAry[2][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        }
        
        
    }
    
    
}


@end
