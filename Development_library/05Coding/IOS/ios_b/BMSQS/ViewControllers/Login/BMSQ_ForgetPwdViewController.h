//
//  BMSQ_ForgetPwdViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import <sqlite3.h>
#import "TPKeyboardAvoidingTableView.h"
@interface BMSQ_ForgetPwdViewController : UIViewControllerEx<UIPickerViewDataSource,UIPickerViewDelegate>
{
    IBOutlet __weak TPKeyboardAvoidingTableView *_tableView;
    __strong UITextField *_phoneTextField;
    __strong UITextField *_pwdTextField;
    __strong UITextField *_codeTextField;

    __strong UIButton *_areaBtn;
    __strong UIButton *_codeBtn;
    sqlite3 *db;
    BOOL _isSeePwd;
    NSMutableDictionary *_inputDic;
//    NSTimer *_timer;            //计时器
    NSDate *_startTime;         //计时开始时间
}

@property (nonatomic)BOOL isShowNav;
@property(nonatomic,strong)NSMutableArray *areaDataArray;
- (void)showAreaPickerView;
- (void)fetchCodeRequest:(NSString *)type;



@end
