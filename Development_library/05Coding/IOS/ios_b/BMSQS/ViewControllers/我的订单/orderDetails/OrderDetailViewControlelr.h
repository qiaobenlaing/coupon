//
//  OrderDetailViewControlelr.h
//  BMSQS
//
//  Created by gh on 15/12/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "SettlementViewController.h"
@interface OrderDetailViewControlelr : UIViewControllerEx<UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong)NSString *orderCode;
@property (nonatomic, assign)int       statusValue;

@property (nonatomic, strong)NSNumber * eatPayType;

@property (nonatomic, strong) NSNumber * orderTypeValue;//订单类型

@end
