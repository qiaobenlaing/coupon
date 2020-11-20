//
//  SDZX_modifyPwdViewController.m
//  SDBooking
//
//  Created by djx on 14-4-18.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "BMSQ_modifyPwdViewController.h"
#import "MD5.h"
#import "MobClick.h"
@interface BMSQ_modifyPwdViewController ()
{
    UITextField* tx_oldPwd;
    UITextField* tx_newPwd;
    UITextField* tx_newPwdSure;
}

@end

@implementation BMSQ_modifyPwdViewController

@synthesize userMobile;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"modifyPwd"];
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"modifyPwd"];
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setViewUp];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setViewUp
{
    [self setNavBackItem];
    [self setNavTitle:@"修改密码"];
    
    UIView* ivOldPwd = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 10, 300, 44)];
    [ivOldPwd.layer setBorderWidth:1.0];
    [ivOldPwd.layer setBorderColor:[UIColor colorWithRed:235.0/255.0 green:235.0/255.0 blue:235.0/255.0 alpha:1.0].CGColor];
    [ivOldPwd.layer setCornerRadius:2.0];
    [ivOldPwd setBackgroundColor:[UIColor whiteColor]];
    [self.view addSubview:ivOldPwd];
    
    tx_oldPwd = [[UITextField alloc]initWithFrame:CGRectMake(5, 5, 290, 34)];
    tx_oldPwd.placeholder = @"输入原密码";
    [tx_oldPwd setTextColor:UICOLOR(185, 185, 185, 1.0)];
    [tx_oldPwd setFont:[UIFont systemFontOfSize:16]];
    tx_oldPwd.secureTextEntry = YES;
    tx_oldPwd.backgroundColor = [UIColor clearColor];
    tx_oldPwd.returnKeyType = UIReturnKeyNext;
    tx_oldPwd.delegate = self;
    [ivOldPwd addSubview:tx_oldPwd];
    
    UIView* ivNewPwd = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 65, 300, 44)];
    [ivNewPwd.layer setBorderWidth:1.0];
    [ivNewPwd.layer setBorderColor:[UIColor colorWithRed:235.0/255.0 green:235.0/255.0 blue:235.0/255.0 alpha:1.0].CGColor];
    [ivNewPwd.layer setCornerRadius:2.0];
    [ivNewPwd setBackgroundColor:[UIColor whiteColor]];
    [self.view addSubview:ivNewPwd];
    
    tx_newPwd = [[UITextField alloc]initWithFrame:CGRectMake(5, 5, 290, 34)];
    tx_newPwd.placeholder = @"输入新密码(6-20位)";
    [tx_newPwd setTextColor:UICOLOR(185, 185, 185, 1.0)];
    [tx_newPwd setFont:[UIFont systemFontOfSize:16]];
    tx_newPwd.secureTextEntry = YES;
    tx_newPwd.returnKeyType = UIReturnKeyDone;
    tx_newPwd.delegate = self;
    [ivNewPwd addSubview:tx_newPwd];
    
    UIView* ivNewPwdSure = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y + 120, 300, 44)];
    [ivNewPwdSure.layer setBorderWidth:1.0];
    [ivNewPwdSure.layer setBorderColor:[UIColor colorWithRed:235.0/255.0 green:235.0/255.0 blue:235.0/255.0 alpha:1.0].CGColor];
    [ivNewPwdSure.layer setCornerRadius:2.0];
    [ivNewPwdSure setBackgroundColor:[UIColor whiteColor]];
    [self.view addSubview:ivNewPwdSure];
    
    tx_newPwdSure = [[UITextField alloc]initWithFrame:CGRectMake(5, 5, 290, 34)];
    tx_newPwdSure.placeholder = @"确认新密码";
    [tx_newPwdSure setTextColor:UICOLOR(185, 185, 185, 1.0)];
    [tx_newPwdSure setFont:[UIFont systemFontOfSize:16]];
    tx_newPwdSure.secureTextEntry = YES;
    tx_newPwdSure.returnKeyType = UIReturnKeyDone;
    tx_newPwdSure.delegate = self;
    [ivNewPwdSure addSubview:tx_newPwdSure];
    
    UIButton* btnSure = [[UIButton alloc]initWithFrame:CGRectMake(15, APP_VIEW_ORIGIN_Y + 184, 290, 44)];
    [btnSure setBackgroundColor:UICOLOR(244, 155, 45, 1.0)];
    [btnSure setTitle:@"确  认" forState:UIControlStateNormal];
    [btnSure addTarget:self action:@selector(btnSureClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnSure];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if (textField == tx_oldPwd)
    {
        [tx_newPwd becomeFirstResponder];
        return NO;
    }
    else if (textField == tx_oldPwd)
    {
        [tx_oldPwd resignFirstResponder];
        [tx_newPwdSure becomeFirstResponder];
        return NO;
    }
    
    [textField resignFirstResponder];
    return YES;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)btnSureClick
{
    if (tx_oldPwd.text == nil || tx_oldPwd.text.length <= 0)
    {
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"请输入原密码" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    if (tx_newPwd.text == nil || tx_newPwd.text.length <= 0)
    {
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"请输入新密码" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    if ([tx_newPwd.text isEqualToString: tx_oldPwd.text])
    {
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"新密码与原密码一致" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    if (tx_newPwd.text.length < 6  || tx_newPwd.text.length > 20)
    {
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"密码长度必须在6-20位之间" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    if (![tx_newPwdSure.text isEqualToString:tx_newPwd.text])
    {
        UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:@"2次输入的新密码不一致" delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
        [alert show];
        return;
    }
    
    
    [self initJsonPrcClient:@"0"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:userMobile forKey:@"mobileNbr"];
    [params setObject:[MD5 MD5Value:tx_oldPwd.text]  forKey:@"originalPwd"];
    [params setObject:[MD5 MD5Value:tx_newPwd.text] forKey:@"newPwd"];
    [params setObject:@"1" forKey:@"type"];
    NSString* vcode = [gloabFunction getSign:@"updatePwd" strParams:userMobile];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"updatePwd" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"修改密码成功"];
            [self.navigationController popViewControllerAnimated:YES];
            [MobClick event:@"newPwd"];// 友盟统计
        }
        else if([code isEqualToString:@"60012"])
        {
            [self showAlertView:@"原密码错误"];
        }
        else if([code isEqualToString:@"20207"])
        {
            [self showAlertView:@"用户不存在"];
            
        }
        else
        {
            [self showAlertView:@"修改失败，请重试"];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"修改失败，请重试"];
    }];
    
}

@end
