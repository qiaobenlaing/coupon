//
//  HomePageGoodsViewCell.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomePageGoodsViewCell : UITableViewCell


@property (nonatomic, strong) UIImageView * iconImage1;// 商品图标1
@property (nonatomic, strong) UIImageView * iconImage2;// 商品图标2
@property (nonatomic, strong) UIImageView * iconImage3;// 商品图标3
@property (nonatomic, strong) UILabel     * shopNameLB1;// 商品名称1
@property (nonatomic, strong) UILabel     * shopNameLB2;// 商品名称2
@property (nonatomic, strong) UILabel     * shopNameLB3;// 商品名称3
@property (nonatomic, strong) UILabel     * shopPriceLB1;// 商品价格1
@property (nonatomic, strong) UILabel     * shopPriceLB2;// 商品价格2
@property (nonatomic, strong) UILabel     * shopPriceLB3;// 商品价格3

- (void)setValueHomePageAry:(NSArray *)homePageAry;


@end
