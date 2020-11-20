//
//  BMSQ_RegistViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "BMSQ_ForgetPwdViewController.h"

@interface BMSQ_RegistViewController : BMSQ_ForgetPwdViewController
{
    __strong UITextField *_licenseTextField;
    
    BOOL _isReadAgree;
}

@property (nonatomic, assign)BOOL isJoin;

@end
