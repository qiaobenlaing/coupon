//
//  ReservationlistViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ReservationlistViewCell.h"
#import "UIImageView+WebCache.h"
@implementation ReservationlistViewCell

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
        [self addSubview:self.iconImage];
        [self addSubview:self.userLB];
        [self addSubview:self.nameLB];
//        [self addSubview:self.numLB];
    }
    return self;
}

- (UIImageView *)iconImage
{
    if (_iconImage == nil) {
        self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
    }
    return _iconImage;
}

- (UILabel *)userLB
{
    if (_userLB == nil) {
        self.userLB = [[UILabel alloc] initWithFrame:CGRectMake(self.iconImage.frame.size.width + 20, 10, 120, self.iconImage.frame.size.height / 2)];
        _userLB.font = [UIFont systemFontOfSize:14];
    }
    return _userLB;
}

- (UILabel *)nameLB
{
    if (_nameLB == nil) {
        self.nameLB = [[UILabel alloc] initWithFrame:CGRectMake(self.iconImage.frame.size.width + 20, self.iconImage.frame.size.height / 2 + 10, 200, self.iconImage.frame.size.height / 2)];
         _nameLB.font = [UIFont systemFontOfSize:14];
        _nameLB.textColor = UICOLOR(83, 83, 83, 1.0);
    }
    return _nameLB;
}


- (UILabel *)numLB
{
    if (_numLB == nil) {
        self.numLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 80, 10, 30, 20)];
         _numLB.font = [UIFont systemFontOfSize:14];
    }
    return _numLB;
}

- (void)addSubBottomView {
    
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, self.feeScaleAry.count * 30 + 85, APP_VIEW_WIDTH, 5)];
    view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self addSubview:view];
    
}


- (void)listViewCell
{
    for (int i = 0; i < self.feeScaleAry.count; i++) {
        
    self.listView = [[UIView alloc] initWithFrame:CGRectMake(0, 80 + i * 30, APP_VIEW_WIDTH, 30)];
    _listView.backgroundColor = [UIColor whiteColor];
    _listView.tag = 1000 + i;
    [self addSubview:self.listView];
        
    self.ticketTypeLB = [[UILabel alloc] initWithFrame:CGRectMake(20, 5, 50, 20)];
    _ticketTypeLB.font = [UIFont systemFontOfSize:14];
    _ticketTypeLB.text = [NSString stringWithFormat:@"%@", self.feeScaleAry[i][@"des"]];
    [self.listView addSubview:self.ticketTypeLB];
    
    self.numberLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width / 2 - 15, 5, 30, 20)];
    _numberLB.font = [UIFont systemFontOfSize:14];
    _numberLB.textColor = UICOLOR(83, 83, 83, 1.0);
    _numberLB.text = [NSString stringWithFormat:@"x%@", self.feeScaleAry[i][@"count"]];
    [self.listView addSubview:self.numberLB];
    
    self.priceLB = [[UILabel alloc] initWithFrame:CGRectMake(self.frame.size.width - 100, 5, 90, 20)];
    _priceLB.font = [UIFont systemFontOfSize:14];
    _priceLB.textAlignment = NSTextAlignmentCenter;
    _priceLB.textColor = UICOLOR(206, 87, 21, 1.0);
    _priceLB.text = [NSString stringWithFormat:@"￥%@", self.feeScaleAry[i][@"price"]];
    [self.listView addSubview:self.priceLB];
        
    }
    
}



- (void)setCellReservationWithDic:(NSDictionary *)newDic
{
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,newDic[@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    
    self.userLB.text = [NSString stringWithFormat:@"%@", newDic[@"nickName"]];
    self.nameLB.text = [NSString stringWithFormat:@"%@ %@", newDic[@"realName"], newDic[@"mobileNbr"]];
    NSString * num = [NSString stringWithFormat:@"%@", newDic[@"totalNbr"]];
    if (num.intValue == 0) {
        
        self.numLB.text = [NSString stringWithFormat:@"%@", @""];
    }else{
        
        self.numLB.text = [NSString stringWithFormat:@"x%@", newDic[@"totalNbr"]];
    }

    self.feeScaleAry = [NSMutableArray arrayWithArray:newDic[@"feeScale"]];
    [self listViewCell];
    [self addSubBottomView];
    
}


@end
