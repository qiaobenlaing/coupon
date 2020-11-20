//
//  BMSQ_ShopHourViewController.h
//  BMSQS
//
//  Created by lxm on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "BMSQ_ShopInfoEditViewController.h"
#import "UpdateTimeViewCell.h"
#import "shopTimeButton.h"

@interface BMSQ_ShopHourViewController : BMSQ_ShopInfoEditViewController

@property (nonatomic, strong)NSString *startTime;
@property (nonatomic, strong)NSString *endTime;

@property (nonatomic, strong) NSMutableArray * shopTimeAry;


@end
