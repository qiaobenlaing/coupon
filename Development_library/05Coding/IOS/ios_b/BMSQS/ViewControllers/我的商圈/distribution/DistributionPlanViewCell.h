//
//  DistributionPlanViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface DistributionPlanViewCell : UITableViewCell

@property (nonatomic, strong) UILabel * leftRange;// 配送范围
@property (nonatomic, strong) UITextField * rangeField;
@property (nonatomic, strong) UILabel * rightRange;//
@property (nonatomic, strong) UILabel * leftRise;//  起送价
@property (nonatomic, strong) UITextField * riseField;
@property (nonatomic, strong) UILabel * rightRise;//
@property (nonatomic, strong) UILabel * leftDelivery;// 配送费
@property (nonatomic, strong) UITextField * deliveryField;
@property (nonatomic, strong) UILabel * rightDelivery;//
@property (nonatomic, strong) UILabel * headerLB;



@end
