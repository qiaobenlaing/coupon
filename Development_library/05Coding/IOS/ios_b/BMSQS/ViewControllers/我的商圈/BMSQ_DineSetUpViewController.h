//
//  BMSQ_DineSetUpViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/15.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "SVProgressHUD.h"
#import "DistributionViewCell.h"
#import "DistributionPlanViewController.h"
@interface BMSQ_DineSetUpViewController : UIViewControllerEx

@property (nonatomic, strong) NSArray * deliveryList;//配送方案
@property (nonatomic, strong) NSNumber * isOpenEat;//是否开启堂食
@property (nonatomic, strong) NSNumber * isOpenTakeout;//是否开启外卖
@property (nonatomic, strong) NSNumber * tableNbrSwitch;//是否开启桌号管理

@end
