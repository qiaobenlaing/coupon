//
//  BMSQ_OrderDetailEXViewController.h
//  BMSQS
//
//  Created by liuqin on 15/10/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_OrderDetailEXViewController : UIViewControllerEx

//@property (nonatomic,strong)id object;


@property (nonatomic,assign)BOOL coupon78;
@property (nonatomic,strong)NSString *isFinish;
@property (nonatomic,strong)NSString *consumeCode;


//1-未付款，2-付款中，3-已付款，4-已取消付款，5-付款失败
//@property (nonatomic,strong)NSString* consumeStatus;


@end
