//
//  BMSQ_MyBankCardController.h
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_MyBankCardController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate>


@property (nonatomic, assign)BOOL ispayCard;

@property (nonatomic, assign)BOOL isPayQR;//是不是我的付款码页面  yes 返回发送通知 rootVC 重新请求

@property (nonatomic, assign)BOOL isCanDele; //NO 能 YES 不能


@end
