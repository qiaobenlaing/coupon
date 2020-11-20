//
//  BMSQ_MemberCardChooseLevelViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_MemberCardChooseLevelViewController : BaseViewController
{
    __weak IBOutlet UITableView *_tableView;
    
    __strong UITextField *_levelTextField;
}
@property(nonatomic,strong)NSArray *cardDataArray;
@end
