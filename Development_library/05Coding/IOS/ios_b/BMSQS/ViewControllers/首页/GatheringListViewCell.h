//
//  GatheringListViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GatheringListViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * iconImage;
@property (nonatomic, strong) UILabel * consumeLB; // 消费
@property (nonatomic, strong) UILabel * indicateLB;// 标示码
@property (nonatomic, strong) UILabel * redPacketLB;// 红包
@property (nonatomic, strong) UILabel * couponCountLB;// 优惠券数量
@property (nonatomic, strong) UILabel * integral; // 积分
@property (nonatomic, strong) UILabel * couponLB;  //优惠券抵扣
@property (nonatomic, strong) UILabel * payLB;  // 支付
@property (nonatomic, strong) UILabel * titleLB; // 时间

- (void)setCellValue:(NSDictionary *)dic;

@end
