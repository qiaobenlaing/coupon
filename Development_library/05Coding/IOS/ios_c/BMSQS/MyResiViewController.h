//
//  MyResiViewController.h
//  BMSQC
//
//  Created by liuqin on 15/9/18.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyResiViewController : UIViewController
{
    __strong UITextField *_phoneTextField;
    __strong UITextField *_pwdTextField;
    __strong UITextField *_codeTextField;
    
    __strong UIButton *_areaBtn;
    __strong UIButton *_codeBtn;
    BOOL _isSeePwd;
    NSMutableDictionary *_inputDic;

    __strong UITextField *_licenseTextField;
    BOOL _isReadAgree;


}
@property (nonatomic, strong)UITableView *_tableView;
@property(nonatomic,strong)AFJSONRPCClient* jsonPrcClient;
@end
