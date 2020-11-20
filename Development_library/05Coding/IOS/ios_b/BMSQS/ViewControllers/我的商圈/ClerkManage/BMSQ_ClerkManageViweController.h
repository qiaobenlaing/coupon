//
//  BMSQ_ClerkManageViweController.h
//  BMSQS
//
//  Created by gh on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "BMSQ_ClerkCell.h"

@interface BMSQ_ClerkManageViweController : UIViewControllerEx < UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate,ClerkCellDelegate,UIGestureRecognizerDelegate>

@property (nonatomic, strong)NSDictionary *shopDic;

@end
