//
//  BMSQ_OrderDetailController.h
//  BMSQC
//
//  Created by gh on 15/10/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_OrderDetailController : BaseViewController <UITableViewDataSource,UITableViewDelegate>

@property (nonatomic,strong)id object;


@property (nonatomic,strong)NSString* isFinish;
@property (nonatomic,strong)NSString *consumeCode;

@property (nonatomic, strong)NSDictionary *couponDic;

//1-未付款，2-付款中，3-已付款，4-已取消付款，5-付款失败
@property (nonatomic,strong)NSString* consumeStatus;


@end
