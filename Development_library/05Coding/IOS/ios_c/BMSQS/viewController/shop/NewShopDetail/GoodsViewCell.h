//
//  GoodsViewCell.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GoodsViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * shopIconImage;// 商品图标
@property (nonatomic, strong) UILabel     * shopNameLB;// 商品名称
@property (nonatomic, strong) UILabel     * goodsConentLB;//商品介绍
@property (nonatomic, strong) UILabel     * shopPriceLB;// 商品原价格
@property (nonatomic, strong) UILabel     * shopNewPriceLB;//商品现价格
@property (nonatomic, strong) UIView      * lineView;
- (void)setValueGoodsDic:(NSDictionary *)goodsDic;

@end
