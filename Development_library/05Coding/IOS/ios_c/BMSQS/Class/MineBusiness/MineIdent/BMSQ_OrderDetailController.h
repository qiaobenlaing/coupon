//
//  BMSQ_OrderDetailController.h
//  BMSQC
//
//  Created by gh on 15/10/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_OrderDetailController : UIViewControllerEx <UITableViewDataSource,UITableViewDelegate>

@property (nonatomic,strong)id object;

@property (nonatomic,strong)NSString *consumeCode;
@property (nonatomic, strong)NSDictionary *couponDic;

@property (nonatomic, assign)BOOL isFinsh;

@end
