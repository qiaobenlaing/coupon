
//
//  BMSQ_LoginViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_LoginViewController.h"
#import "MD5.h"
#import "AppDelegate.h"
#import "MobClick.h"
@interface BMSQ_LoginViewController ()

@end

@implementation BMSQ_LoginViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self createModel];
}


- (void)viewWillDisappear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarHidden:NO];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"Login"];
}

- (void)viewWillAppear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"Login"];//
}

#pragma mark - CreateModel
- (void)createModel
{
//    [self setNavHidden:YES];
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    _loginBtn.layer.borderColor = [[UIColor colorWithRed:215.0 / 255.0 green:215.0 / 255.0 blue:215.0 / 255.0 alpha:1] CGColor];
    _loginBtn.layer.borderWidth = 0.f;
    _loginBtn.layer.cornerRadius = 4.f;
    [_loginBtn.layer setMasksToBounds:YES];

    _registBtn.layer.borderColor = [[UIColor colorWithRed:196.0/ 255.0 green:8.0 / 255.0 blue:24.0 / 255.0 alpha:1] CGColor];
    _registBtn.layer.borderWidth = 0.7f;
    _registBtn.layer.cornerRadius = 8.f;
    [_registBtn.layer setMasksToBounds:YES];
    
    if(_inputDic==nil)
        _inputDic = [[NSMutableDictionary alloc] init];
}
#pragma mark - Action
- (IBAction)hideKeyBoard:(id)sender
{
    [self.view endEditing:YES];
}
- (IBAction)loginBtnClicked:(id)sender;
{
    [self.view endEditing:YES];
    NSString *userStr = [self.userTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入用户名");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    NSString *pwdStr = [self.pwdTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!pwdStr||pwdStr.length==0){
        CSAlert(@"请输入密码");
        return;
    }else{
        [_inputDic setObject:[MD5 MD5Value:pwdStr] forKey:@"password"];
    }

    [_inputDic setObject:@"1" forKey:@"loginType"];
   
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *regid = [userDefaults objectForKey:RegistratcionID];
    if (regid.length>0) {
        [_inputDic setObject:regid forKey:@"registrationId"];

    }else{
        [_inputDic setObject:@"" forKey:@"registrationId"];

    }

    [self loginRequest];
}

- (IBAction)forgetBtnClicked:(id)sender;
{
    
}

- (IBAction)registBtnClicked:(id)sender;
{
    [self performSegueWithIdentifier:@"BMSQ_Regist" sender:nil];
}

- (IBAction)CloseView:(id)sender
{
    [[NSNotificationCenter defaultCenter] postNotificationName:@"REQUESTDATA" object:nil];
    UITabBarController *tabBarController = (UITabBarController *)appDelegate.window.rootViewController;
    tabBarController.selectedIndex = 0;
    [self dismissViewControllerAnimated:YES completion:nil];
    
    
}

#pragma mark -Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.row==0){
        cellIdentifier = @"UserTableViewCell";
    }
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }
    
    if(indexPath.row==0){
        self.userTextField = (UITextField *)[setCell.contentView viewWithTag:100];
        self.userTextField.placeholder = @"手机号";
        self.userTextField.keyboardType = UIKeyboardTypeDecimalPad;
        self.userTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
        self.userTextField.tag = 100;
        [self.userTextField addTarget:self action:@selector(changeText:) forControlEvents:UIControlEventEditingChanged];
        setCell.imageView.image = [UIImage imageNamed:@"Login_User"];
    }else if(indexPath.row==1){
        self.pwdTextField = (UITextField *)[setCell.contentView viewWithTag:100];
        self.pwdTextField.placeholder = @"密码";
        self.pwdTextField.secureTextEntry = YES;
        self.pwdTextField.tag = 200;
        [self.pwdTextField addTarget:self action:@selector(changeText:) forControlEvents:UIControlEventEditingChanged];

        setCell.imageView.image = [UIImage imageNamed:@"Login_Pwd"];
    }
    setCell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 0.01f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 0.01f;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 9)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([cell respondsToSelector:@selector(setSeparatorInset:)]) {
        [cell setSeparatorInset:UIEdgeInsetsMake(0, 8, 0, 0)];
    }
    
    if ([cell respondsToSelector:@selector(setLayoutMargins:)]) {
        [cell setLayoutMargins:UIEdgeInsetsMake(0, 8, 0, 0)];
    }
}

#pragma mark - HttpRequest
- (void)loginRequest
{
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
    [self.jsonPrcClient invokeMethod:@"login" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                [uD setObject:_inputDic forKey:APP_USER_AOCNUM_KEY];
                [uD synchronize];
                [self dismissViewControllerAnimated:YES completion:^{
                    [[NSNotificationCenter defaultCenter]postNotificationName:@"loginSuccess" object:nil];
                    [[NSNotificationCenter defaultCenter]postNotificationName:@"freshEff" object:nil];

                }];
            }
                break;
            case 20207:
                CSAlert(@"账号不存在");
                break;
            case 20208:
                CSAlert(@"账号已被禁用");
                break;
            case 60000:
                CSAlert(@"请输入手机号码");
                break;
            case 60001:
                CSAlert(@"手机号码不正确");
                break;
            case 60010:
                CSAlert(@"请输入密码");
                break;
            case 60011 :
                CSAlert(@"用户名密码不匹配");
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(@"连接服务器失败");
    }];
}

-(void)changeText:(UITextField *)textField{
    
    if (textField.tag == 100) {
        if (textField.text.length > 11) {
            textField.text = [textField.text substringToIndex:11];
        }
        
    }else if (textField.tag == 200){
        if (textField.text.length > 24) {
            textField.text = [textField.text substringToIndex:24];
        }
    }
    
}
@end
