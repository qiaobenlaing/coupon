//
//  BMSQ_BenefitCardDetailController.h
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_BenefitCardDetailController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate>

@property(nonatomic,strong)NSString* userCardCode;
@property(nonatomic)BOOL isAttention;
@end
