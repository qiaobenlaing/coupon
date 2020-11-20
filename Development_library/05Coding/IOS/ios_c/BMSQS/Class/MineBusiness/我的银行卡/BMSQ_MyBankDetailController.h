//
//  BMSQ_MyBankDetailController.h
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_MyBankDetailController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate>

@property(nonatomic,strong)NSString* bankAccountCode;
@end
