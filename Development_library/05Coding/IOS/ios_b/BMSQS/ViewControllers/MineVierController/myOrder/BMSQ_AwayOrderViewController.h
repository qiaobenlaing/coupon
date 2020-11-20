//
//  BMSQ_AwayOrderViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/1.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "OrderDetailsViewCell.h"
#import "OrderDetailsModel.h"
#import "DetailViewController.h"
@interface BMSQ_AwayOrderViewController : UIViewControllerEx

@property (nonatomic, strong) NSNumber * orderTypeValue;  //门面订单  外卖订单



@property (nonatomic, strong)NSString *searchStr;

@end
