
//
//  BMSQ_LoginViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_LoginViewController.h"
#import "MD5.h"
#import "BMSQ_SettlerViewController.h"
#import "APService.h"
@interface BMSQ_LoginViewController ()

@property (nonatomic, strong) NSUserDefaults * registrationIdStr;

@end

@implementation BMSQ_LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getShopOrderType) name:@"getShopOrderType" object:nil];
    
    
    if ([APService registrationID]) {
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        [userDefault setObject:[APService registrationID] forKey:RegistratcionID];
        [userDefault synchronize];
    }
    
    [self createModel];
}


- (void)viewWillDisappear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [super viewWillDisappear:animated];
}

- (void)viewWillAppear:(BOOL)animated
{
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    [super viewWillAppear:animated];
}



#pragma mark - CreateModel
- (void)createModel
{
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

- (IBAction)gotoSettlerVc:(id)sender {
    
    BMSQ_SettlerViewController *vc = [[BMSQ_SettlerViewController alloc]init];
    vc.isLogin = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
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

    [_inputDic setObject:@"0" forKey:@"loginType"];

    [self loginRequest];
}

- (IBAction)forgetBtnClicked:(id)sender;
{
    
}

- (IBAction)registBtnClicked:(id)sender;
{
    [self performSegueWithIdentifier:@"BMSQ_Regist" sender:nil];
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
        self.userTextField.keyboardType = UIKeyboardTypePhonePad;
        self.userTextField.clearButtonMode = UITextFieldViewModeWhileEditing;
        setCell.imageView.image = [UIImage imageNamed:@"Login_User"];
    }else if(indexPath.row==1){
        self.pwdTextField = (UITextField *)[setCell.contentView viewWithTag:100];
        self.pwdTextField.placeholder = @"密码";
        self.pwdTextField.secureTextEntry = YES;
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
//    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
     NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
     NSString *registrationId = [userDefault objectForKey:RegistratcionID];
    
    // 031d2d9891b   0017d0a6587
    NSLog(@"registrationId---> %@", registrationId);
    
    if (registrationId == nil) {
        [_inputDic setObject:@"" forKey:@"registrationId"];
    }else{
        [_inputDic setObject:registrationId forKey:@"registrationId"];  /////////////test

    }
    
    [self.jsonPrcClient invokeMethod:@"login" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        NSString *str = [NSString stringWithFormat:@"%d", resCode];
        switch (resCode) {
                
            case 50000:{
                NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                [uD setObject:[NSString stringWithFormat:@"%@",[_inputDic objectForKey:@"mobileNbr"]] forKey:@"mobileNbr"];
                [uD setObject:[NSString stringWithFormat:@"%@",[_inputDic objectForKey:@"password"]]  forKey:@"password"];
                [uD setObject:[responseObject objectForKey:@"staffCode"] forKey:@"staffCode"];
                [uD setObject:[responseObject objectForKey:@"userLvl"] forKey:@"userLvl"];
                
                [uD synchronize];
                
                
                
                NSString* vcode = [gloabFunction getSign:@"sGetShopBasicInfo" strParams:[gloabFunction getShopCode]];
                
                if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
                    return;
                NSDictionary *dic = @{@"shopCode":[gloabFunction getShopCode],
                                      @"tokenCode":[gloabFunction getUserToken],
                                      @"vcode":vcode,
                                      @"reqtime":[gloabFunction getTimestamp]};
                [self initJsonPrcClient:@"1"];
                [self.jsonPrcClient invokeMethod:@"sGetShopBasicInfo" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
                    if(IsNOTNullOrEmptyOfDictionary(responseObject)){
                        [uD setObject:responseObject[@"shopName"] forKey:@"shopName"];
                        [uD setObject:responseObject[@"logoUrl"] forKey:@"logoUrl"];
                        [uD setObject:responseObject[@"tel"] forKey:@"tel"];
                        [uD setObject:responseObject[@"street"] forKey:@"street"];
                        [uD setObject:responseObject[@"province"] forKey:@"province"];
                        [uD setObject:responseObject[@"city"] forKey:@"city"];
                        [uD setObject:responseObject[@"isCatering"] forKey:@"isCatering"];
                        
                        [uD synchronize];
                        [[NSNotificationCenter defaultCenter]postNotificationName:@"setMyTitle" object:nil];
                        [[NSNotificationCenter defaultCenter]postNotificationName:@"refrshCoupon" object:nil];
                        
                        
                    }
                    
                } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                    CSAlert(error.localizedDescription);
                }];
                
                
                [[NSNotificationCenter defaultCenter]postNotificationName:@"getShopOrderType" object:nil];
                
                [self dismissViewControllerAnimated:YES completion:nil];
                
                //                UIApplication *app = [UIApplication sharedApplication];
                //                UIWindow *window = app.keyWindow;
                //                UITabBarController *tabbarVC = (UITabBarController *)[window rootViewController];
                //                tabbarVC.selectedIndex = 3;
                
                
                
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
            case 80047:
                CSAlert(@"未激活");
                break;
                
            default:
                CSAlert(str);
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}

- (void)getShopOrderType{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopOrderType" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getShopOrderType" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSDictionary *dic = responseObject;
        NSString *isCatering = [NSString stringWithFormat:@"%@",[dic objectForKey:@"isCatering"]];
        NSString *isOuttake = [NSString stringWithFormat:@"%@",[dic objectForKey:@"isOuttake"]];
        NSString *shopTypeFood,*shopTypeNoFood,*shopTypeTakeOut;
        shopTypeFood=[isCatering isEqualToString:@"1"]?@"1":@"0";
        shopTypeTakeOut=[isOuttake isEqualToString:@"1"]?@"1":@"0";
        shopTypeNoFood=[isCatering isEqualToString:@"0"]?@"1":@"0";
      
        NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
        [uD setObject:shopTypeFood forKey:SHOPTYPE_FOOD];
        [uD setObject:shopTypeTakeOut forKey:SHOPTYPE_FOOD_TAKEOUT];
        [uD setObject:shopTypeNoFood forKey:SHOPTYPE_NO_FOOD];
        [uD synchronize];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        
    }];
    
}

@end
