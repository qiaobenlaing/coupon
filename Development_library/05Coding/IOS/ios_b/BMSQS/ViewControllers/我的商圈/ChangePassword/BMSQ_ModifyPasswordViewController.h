//
//  BMSQ_ModifyPasswordViewController.h
//  BMSQS
//
//  Created by lxm on 15/9/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_ModifyPasswordViewController : UIViewControllerEx
{
    __strong UITextField *_pwdTextField;
    
    BOOL _isSeePwd;
    
    __weak UITableView *_tableView;
    NSMutableDictionary *_inputDic;
}
- (void)httpModifyRequest;
@end
