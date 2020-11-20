//
//  GatheringListViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "GatheringListViewCell.h"
#import "UIImageView+WebCache.h"

@implementation GatheringListViewCell

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
        [self addSubview:self.consumeLB]; // 消费
        [self addSubview:self.indicateLB]; // 标示码
        [self addSubview:self.redPacketLB];// 红包
        [self addSubview:self.couponCountLB]; // 优惠券数量
        [self addSubview:self.integral]; // 积分
        [self addSubview:self.couponLB]; //优惠券抵扣
        [self addSubview:self.payLB];  // 支付
        [self addSubview:self.titleLB]; // 时间
        [self addSubBottomView];
    }
    return self;
}

- (UIImageView *)iconImage
{
    if (_iconImage == nil) {
        self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(5, 25, 50, 50)];
        _iconImage.backgroundColor = [UIColor clearColor];
    }
    return _iconImage;
}

- (UILabel *)consumeLB
{
    if (_consumeLB == nil) {
        self.consumeLB = [[UILabel alloc] initWithFrame:CGRectMake(65, 5, (APP_VIEW_WIDTH - 65)/2, 20)];
        _consumeLB.font = [UIFont systemFontOfSize:14];

        _consumeLB.backgroundColor = [UIColor clearColor];
    }
    return _consumeLB;
}

- (UILabel *)indicateLB
{
    if (_indicateLB == nil) {
        self.indicateLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2  , 5, (APP_VIEW_WIDTH - 65)/2 + 20, 20)];
        _indicateLB.font = [UIFont systemFontOfSize:14];

        _indicateLB.backgroundColor = [UIColor clearColor];
    }
    return _indicateLB;
}

- (UILabel *)redPacketLB
{
    if (_redPacketLB == nil) {
        self.redPacketLB = [[UILabel alloc] initWithFrame:CGRectMake(65, 30, (APP_VIEW_WIDTH - 65)/2, 20)];
        _redPacketLB.font = [UIFont systemFontOfSize:14];
        _redPacketLB.backgroundColor = [UIColor clearColor];
    }
    return _redPacketLB;
}

- (UILabel *)couponCountLB
{
    if (_couponCountLB == nil) {
        self.couponCountLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH /2 , 30, APP_VIEW_WIDTH /2, 20)];
        _couponCountLB.font = [UIFont systemFontOfSize:14];
        _couponCountLB.backgroundColor = [UIColor clearColor];
    }
    return _couponCountLB;
}

- (UILabel *)integral
{
    if (_integral == nil) {
        self.integral = [[UILabel alloc] initWithFrame:CGRectMake(65, 55, (APP_VIEW_WIDTH - 65)/2, 20)];
        _integral.font = [UIFont systemFontOfSize:14];
        _integral.backgroundColor = [UIColor clearColor];
    }
    return _integral;

}

- (UILabel *)couponLB
{
    if (_couponLB == nil) {
        self.couponLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2 , 55, APP_VIEW_WIDTH/2, 20)];
        _couponLB.font = [UIFont systemFontOfSize:14];
        _couponLB.backgroundColor = [UIColor clearColor];
    }
    return _couponLB;
}

- (UILabel *)payLB
{
    if (_payLB == nil) {
        self.payLB = [[UILabel alloc] initWithFrame:CGRectMake(65, 80, (APP_VIEW_WIDTH - 65)/2, 20)];
        _payLB.font = [UIFont systemFontOfSize:14];
        _payLB.backgroundColor = [UIColor clearColor];
    }
    return _payLB;
}

- (UILabel *)titleLB
{
    if (_titleLB == nil) {
        self.titleLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2 , 80, APP_VIEW_WIDTH/2, 20)];
        _titleLB.font = [UIFont systemFontOfSize:14];
        _titleLB.backgroundColor = [UIColor clearColor];
    }
    return _titleLB;
}

- (void)addSubBottomView {
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 104, APP_VIEW_WIDTH, 1)];
    view.backgroundColor = [UIColor clearColor];
    [self addSubview:view];
    
}



//placeholderImage:
- (void)setCellValue:(NSDictionary *)dic {
    NSDictionary *subdic = [dic objectForKey:@"extras"];
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,subdic[@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"] ];
    self.consumeLB.text = [NSString stringWithFormat:@"消费:%@元",subdic[@"totalPay"]];
    self.indicateLB.text = [NSString stringWithFormat:@"标示码:%@",subdic[@"identityCode"]];
    self.redPacketLB.text = [NSString stringWithFormat:@"红包抵扣:%@元",subdic[@"bonusPay"]];
    self.couponCountLB.text = [NSString stringWithFormat:@"优惠券使用:%@张",subdic[@"couponUsed"]];
    self.integral.text = [NSString stringWithFormat:@"积分:%@分",subdic[@"point"]];
    self.couponLB.text = [NSString stringWithFormat:@"优惠券抵扣:%@元",subdic[@"couponPay"]];
    self.payLB.text = [NSString stringWithFormat:@"支付:%@元",subdic[@"realPay"]];
    self.titleLB.text = [NSString stringWithFormat:@"%@",subdic[@"consumeTime"]];// 日期
    
}


@end
