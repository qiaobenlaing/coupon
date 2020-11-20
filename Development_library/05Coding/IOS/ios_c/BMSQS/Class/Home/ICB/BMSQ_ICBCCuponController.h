//
//  BMSQ_ICBCCuponController.h
//  BMSQC
//
//  Created by djx on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "PullingRefreshTableView.h"

@interface BMSQ_ICBCCuponController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate,PullingRefreshTableViewDelegate>

@property(nonatomic)int insertPage; //1平台活动，2银行活动，3商家活动

@end
