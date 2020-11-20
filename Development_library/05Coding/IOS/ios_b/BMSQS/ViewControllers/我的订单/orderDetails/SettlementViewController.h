//
//  SettlementViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "OrderListViewCell.h"
#import "AllChooseViewCell.h"
#import "MoneyViewCell.h"
#import "InputViewCell.h"
#import "ZBarSDK.h"

@interface SettlementViewController : UIViewControllerEx

@property (nonatomic, strong)NSString *orderCode; //订单编码
@property (nonatomic, strong)NSNumber * actualOrderAmount;// 结算金额
@property (nonatomic, strong)NSMutableArray * orderProductList; //订单产品列表
@property (nonatomic, strong)NSArray * productList;//

@property (nonatomic, strong)NSNumber * payTypeNum;


@end
