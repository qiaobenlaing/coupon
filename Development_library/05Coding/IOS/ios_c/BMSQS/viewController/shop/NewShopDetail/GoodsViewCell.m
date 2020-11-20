//
//  GoodsViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "GoodsViewCell.h"

@implementation GoodsViewCell

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
        [self addSubview:self.shopIconImage];
        [self addSubview:self.shopNameLB];
        [self addSubview:self.shopPriceLB];
        [self addSubview:self.shopNewPriceLB];
        [self addSubview:self.goodsConentLB];
        [self addSubview:self.lineView];
    }
    return self;
}

- (UIImageView *)shopIconImage
{
    if (_shopIconImage == nil) {
        self.shopIconImage = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 60, 60)];
//        _shopIconImage.backgroundColor = [UIColor purpleColor];
        
    }
    return _shopIconImage;
}

- (UILabel *)shopNameLB
{
    if (_shopNameLB == nil) {
        self.shopNameLB = [[UILabel alloc] initWithFrame:CGRectMake(80, 10, APP_VIEW_WIDTH - 90, 20)];
        _shopNameLB.font = [UIFont systemFontOfSize:14.0];
//        _shopNameLB.backgroundColor = [UIColor greenColor];
    }
    return _shopNameLB;
}

- (UILabel *)goodsConentLB
{
    if (_goodsConentLB == nil) {
        self.goodsConentLB = [[UILabel alloc] initWithFrame:CGRectMake(80, _shopNameLB.frame.origin.y + 20, APP_VIEW_WIDTH - 90, 20)];
        _goodsConentLB.font = [UIFont systemFontOfSize:12.0];
//        _goodsConentLB.backgroundColor = [UIColor purpleColor];
    }
    return _goodsConentLB;
}

- (UIView *)lineView
{
    if (_lineView == nil) {
        self.lineView = [[UIView alloc] initWithFrame:CGRectMake(170, self.shopIconImage.frame.size.height, 80, 1)];
        self.lineView.backgroundColor = UICOLOR(128, 128, 128, 1.0);
    }
    return _lineView;
}

- (UILabel *)shopPriceLB
{
    if (_shopPriceLB == nil) {
        self.shopPriceLB = [[UILabel alloc] initWithFrame:CGRectMake(170, self.shopIconImage.frame.size.height - 10, 90, 20)];
        _shopPriceLB.font = [UIFont systemFontOfSize:13.0];
        _shopPriceLB.textColor = UICOLOR(128, 128, 128, 1.0);
//        _shopPriceLB.backgroundColor = [UIColor redColor];
    }
    return _shopPriceLB;
}

- (UILabel *)shopNewPriceLB
{
    if (_shopNewPriceLB == nil) {
        self.shopNewPriceLB = [[UILabel alloc] initWithFrame:CGRectMake(80, self.shopIconImage.frame.size.height - 10, 80, 20)];
        _shopNewPriceLB.font = [UIFont systemFontOfSize:17.0];
        _shopNewPriceLB.textColor = UICOLOR(183, 34, 26, 1.0);
//        _shopNewPriceLB.backgroundColor = [UIColor purpleColor];
    }
    return _shopNewPriceLB;
}

- (void)setValueGoodsDic:(NSDictionary *)goodsDic
{
    
    [self.shopIconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,goodsDic[@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
    self.shopNameLB.text = [NSString stringWithFormat:@"%@", goodsDic[@"productName"]];
    NSString * priceStr = goodsDic[@"originalPrice"];
    NSString * newPriceStr = goodsDic[@"finalPrice"];
    if (priceStr.intValue > newPriceStr.intValue) {
        
        self.shopPriceLB.text = [NSString stringWithFormat:@"原价:￥%@", goodsDic[@"originalPrice"]];//originalPrice
        self.shopNewPriceLB.text = [NSString stringWithFormat:@"￥%@", goodsDic[@"finalPrice"]];
        self.lineView.hidden = NO;
    }else{
        self.shopNewPriceLB.text = [NSString stringWithFormat:@"￥%@", goodsDic[@"finalPrice"]];
        self.lineView.hidden = YES;
    }
    
    self.goodsConentLB.text = [NSString stringWithFormat:@"%@", goodsDic[@"des"]];
    
}


@end
