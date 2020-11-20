//
//  BMSQ_LoginViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "BaseViewController.h"
#import "TPKeyboardAvoidingTableView.h"

@interface BMSQ_LoginViewController : BaseViewController
{
    __weak IBOutlet TPKeyboardAvoidingTableView *_tableView;
    __weak IBOutlet UIButton *_forgetPwdBtn;
    __weak IBOutlet UIButton *_loginBtn;
    __weak IBOutlet UIButton *_registBtn;
 
    NSMutableDictionary *_inputDic;
}
@property(nonatomic,strong)UITextField *userTextField;
@property(nonatomic,strong)UITextField *pwdTextField;

- (IBAction)loginBtnClicked:(id)sender;
- (IBAction)forgetBtnClicked:(id)sender;
- (IBAction)registBtnClicked:(id)sender;
- (IBAction)hideKeyBoard:(id)sender;

- (IBAction)gotoSettlerVc:(id)sender;






@end
