//
//  HomePageGoodsViewCell.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "HomePageGoodsViewCell.h"

@implementation HomePageGoodsViewCell

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
        [self addSubview:self.iconImage1];
        [self addSubview:self.iconImage2];
        [self addSubview:self.iconImage3];
        [self addSubview:self.shopNameLB1];
        [self addSubview:self.shopNameLB2];
        [self addSubview:self.shopNameLB3];
        [self addSubview:self.shopPriceLB1];
        [self addSubview:self.shopPriceLB2];
        [self addSubview:self.shopPriceLB3];
        
    }
    return self;
}


- (UIImageView *)iconImage1
{
    if (_iconImage1 == nil) {
        self.iconImage1 = [[UIImageView alloc] initWithFrame:CGRectMake(5, 5, (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3)];
//        _iconImage1.backgroundColor = [UIColor purpleColor];
        
    }
    return _iconImage1;
}


- (UILabel *)shopNameLB1
{
    if (_shopNameLB1 == nil) {
        self.shopNameLB1 = [[UILabel alloc] initWithFrame:CGRectMake(5,_iconImage1.frame.origin.y + (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopNameLB1.font = [UIFont systemFontOfSize:12.0];
        _shopNameLB1.textColor = UICOLOR(51, 51, 51, 1.0);
        _shopNameLB1.textAlignment = NSTextAlignmentCenter;
//        _shopNameLB1.backgroundColor = [UIColor greenColor];
        
    }
    return _shopNameLB1;
}



- (UILabel *)shopPriceLB1
{
    if (_shopPriceLB1 == nil) {
        self.shopPriceLB1 = [[UILabel alloc] initWithFrame:CGRectMake(5,_shopNameLB1.frame.origin.y + 20, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopPriceLB1.font = [UIFont systemFontOfSize:12.0];
        _shopPriceLB1.textAlignment = NSTextAlignmentCenter;
        _shopPriceLB1.font = [UIFont systemFontOfSize:15.0];
        _shopPriceLB1.textColor = APP_NAVCOLOR;
//        _shopPriceLB1.backgroundColor = [UIColor redColor];
        
    }
    return _shopPriceLB1;
}

- (UIImageView *)iconImage2
{
    if (_iconImage2 == nil) {
        self.iconImage2 = [[UIImageView alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3 + 35, 5, (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3)];
//        _iconImage2.backgroundColor = [UIColor purpleColor];
        
    }
    return _iconImage2;
}

- (UILabel *)shopNameLB2
{
    if (_shopNameLB2 == nil) {
        self.shopNameLB2 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3 + 35,_iconImage2.frame.origin.y + (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopNameLB2.font = [UIFont systemFontOfSize:12.0];
        _shopNameLB2.textColor = UICOLOR(51, 51, 51, 1.0);
        _shopNameLB2.textAlignment = NSTextAlignmentCenter;
//        _shopNameLB2.backgroundColor = [UIColor greenColor];
        
    }
    return _shopNameLB2;
}


- (UILabel *)shopPriceLB2
{
    if (_shopPriceLB2 == nil) {
        self.shopPriceLB2 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3 + 35,_shopNameLB2.frame.origin.y + 20, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopPriceLB2.font = [UIFont systemFontOfSize:12.0];
        _shopPriceLB2.textAlignment = NSTextAlignmentCenter;
        _shopPriceLB2.font = [UIFont systemFontOfSize:15.0];
        _shopPriceLB2.textColor = APP_NAVCOLOR;
//        _shopPriceLB2.backgroundColor = [UIColor redColor];
        
    }
    return _shopPriceLB2;
}

- (UIImageView *)iconImage3
{
    if (_iconImage3 == nil) {
        self.iconImage3 = [[UIImageView alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3*2 + 65, 5, (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3)];
//        _iconImage3.backgroundColor = [UIColor purpleColor];
        
    }
    return _iconImage3;
}

- (UILabel *)shopNameLB3
{
    if (_shopNameLB3 == nil) {
        self.shopNameLB3 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3*2 + 65,_iconImage3.frame.origin.y + (APP_VIEW_WIDTH - 95)/3, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopNameLB3.font = [UIFont systemFontOfSize:12.0];
        _shopNameLB3.textColor = UICOLOR(51, 51, 51, 1.0);
        _shopNameLB3.textAlignment = NSTextAlignmentCenter;
//        _shopNameLB3.backgroundColor = [UIColor greenColor];
        
    }
    return _shopNameLB3;
}


- (UILabel *)shopPriceLB3
{
    if (_shopPriceLB3 == nil) {
        self.shopPriceLB3 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 95)/3*2 + 65,_shopNameLB3.frame.origin.y + 20, (APP_VIEW_WIDTH - 95)/3, 20)];
        _shopPriceLB3.font = [UIFont systemFontOfSize:12.0];
        _shopPriceLB3.textAlignment = NSTextAlignmentCenter;
        _shopPriceLB3.font = [UIFont systemFontOfSize:15.0];
        _shopPriceLB3.textColor = APP_NAVCOLOR;
//        _shopPriceLB3.backgroundColor = [UIColor redColor];
        
    }
    return _shopPriceLB3;
}

- (void)setValueHomePageAry:(NSArray *)homePageAry
{
    if (homePageAry.count == 0) {
        return ;
    }else if (homePageAry.count == 1){
//        [self.iconImage1 sd_setImageWithURL:homePageAry[0][@"url"] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        [self.iconImage1 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[0][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB1.text = [NSString stringWithFormat:@"%@", homePageAry[0][@"productName"]];
        self.shopPriceLB1.text = [NSString stringWithFormat:@"￥%@", homePageAry[0][@"finalPrice"]];
        
        [self.iconImage2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[0][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB2.text = [NSString stringWithFormat:@"%@", homePageAry[0][@"productName"]];
        self.shopPriceLB2.text = [NSString stringWithFormat:@"￥%@", homePageAry[0][@"finalPrice"]];
        
        [self.iconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[0][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB3.text = [NSString stringWithFormat:@"%@", homePageAry[0][@"productName"]];
        self.shopPriceLB3.text = [NSString stringWithFormat:@"￥%@", homePageAry[0][@"finalPrice"]];
        
    }else if (homePageAry.count == 2){
        
        [self.iconImage1 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[0][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB1.text = [NSString stringWithFormat:@"%@", homePageAry[0][@"productName"]];
        self.shopPriceLB1.text = [NSString stringWithFormat:@"￥%@", homePageAry[0][@"finalPrice"]];
        
        [self.iconImage2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[1][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB2.text = [NSString stringWithFormat:@"%@", homePageAry[1][@"productName"]];
        self.shopPriceLB2.text = [NSString stringWithFormat:@"￥%@", homePageAry[1][@"finalPrice"]];
        [self.iconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[1][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB3.text = [NSString stringWithFormat:@"%@", homePageAry[1][@"productName"]];
        self.shopPriceLB3.text = [NSString stringWithFormat:@"￥%@", homePageAry[1][@"finalPrice"]];
        
    }else{
        [self.iconImage1 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[0][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB1.text = [NSString stringWithFormat:@"%@", homePageAry[0][@"productName"]];
        self.shopPriceLB1.text = [NSString stringWithFormat:@"￥%@", homePageAry[0][@"finalPrice"]];
        
        [self.iconImage2 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[1][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB2.text = [NSString stringWithFormat:@"%@", homePageAry[1][@"productName"]];
        self.shopPriceLB2.text = [NSString stringWithFormat:@"￥%@", homePageAry[1][@"finalPrice"]];
        
        [self.iconImage3 sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,homePageAry[2][@"url"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
        self.shopNameLB3.text = [NSString stringWithFormat:@"%@", homePageAry[2][@"productName"]];
        self.shopPriceLB3.text = [NSString stringWithFormat:@"￥%@", homePageAry[2][@"finalPrice"]];
        
    }
}



@end
