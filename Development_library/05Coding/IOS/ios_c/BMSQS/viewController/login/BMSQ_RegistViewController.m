//
//  BMSQ_RegistViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RegistViewController.h"
#import "BMSQ_AgreementViewController.h"
#import "MD5.h"
#import "BMSQ_JoinOtherSuccessViewController.h"
#import "MobClick.h"
#import "OpenUDID.h"

@interface BMSQ_RegistViewController ()
{
    NSTimer* timer;
    UIButton* btn_VerCode;
    int timeout;
}

@end

@implementation BMSQ_RegistViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"Regist"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"Regist"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self.navigationItem setTitle:@"用户注册"];
    
}


#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    
    if ([[segue identifier] isEqualToString:@"BMSQ_Agreement"])
    {
        BMSQ_AgreementViewController *aVC = [segue destinationViewController];
        aVC.navTitle = @"注册协议";
//        aVC.url = @"http://baomi.suanzi.cn/Api/Browser/cProtocol";
        aVC.url = [NSString stringWithFormat:@"%@/Browser/cProtocol",H5_URL];
    }
}


- (void)areaBtnClicked:(id)sender
{
    [self showAreaPickerView];
}
#pragma mark 获取验证码
- (IBAction)codeBtnClicked:(id)sender
{
    [self.view endEditing:YES];
    NSString *userStr = [_phoneTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入手机号码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    [self fetchCoderegisRequest:@"r"];   //请求验证码

    
    btn_VerCode = sender;
    NSDate *scheduledTime = [NSDate dateWithTimeIntervalSinceNow:0];
    timer = [[NSTimer alloc] initWithFireDate:scheduledTime
                                     interval:1
                                       target:self
                                     selector:@selector(countdown)
                                     userInfo:nil
                                      repeats:YES];
    
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
    
}


- (void)fetchCoderegisRequest:(NSString *)type
{
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
    NSDictionary *dic = @{@"mobileNbr":[_inputDic objectForKey:@"mobileNbr"],
                          @"action":type,@"appType":@"c"};
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"验证码=%d",[[responseObject objectForKey:@"validateCode"] intValue]);
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"验证码已发送，请注意查收短信");
                [weakSelf timerFireMethod];
            }
                break;
            case 80010:
                CSAlert(@"验证码请求失败");
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
            case 60003:
                CSAlert(@"手机号码已经被使用");
                break;
            default:
            {
                NSString *message = [NSString stringWithFormat:@"获取验证码错误[%d]",resCode];
                CSAlert(message);
            }
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        CSAlert(@"error");

        [SVProgressHUD dismiss];
    }];
}

-(void)timerFireMethod{
    
    __block int timeoutS = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timers = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timers, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timers, ^{
        if (timeoutS <= 0){
            dispatch_source_cancel(timers);
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:@"再次获取" forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeoutS];
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:strTime forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = NO;
            });
            timeoutS--;
        }
    });
    dispatch_resume(timers);
}


- (void)countdown
{
    NSString *downSecondStr = [NSString stringWithFormat:@"%ds",timeout];
    btn_VerCode.enabled = NO;
    [btn_VerCode setTitle:downSecondStr forState:UIControlStateNormal];
    
    if (timeout == 0) {
        [timer invalidate];
  
        btn_VerCode.enabled =YES;
        [btn_VerCode setTitle:@"获取验证码" forState:UIControlStateNormal];
        return;
    }
    timeout--;
}

- (IBAction)seePwdBtnClicked:(id)sender
{
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.isSelected;
    _isSeePwd = btn.isSelected;
    [_tableView reloadData];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 4;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = nil;
    if(indexPath.section==0){
        if(indexPath.row==0){
            cellIdentifier = @"PhoneTableViewCell";
        }else if(indexPath.row==1){
            cellIdentifier = @"CodeTableViewCell";
        }else if(indexPath.row==2){
            cellIdentifier = @"PwdTableViewCell";
        }else if(indexPath.row==3){
            cellIdentifier = @"LicenseTableViewCell";
        }
    }
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }
    
    if(indexPath.section==0){
        setCell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if(indexPath.row==0){
            _phoneTextField = (UITextField *)[setCell.contentView viewWithTag:100];
            _phoneTextField.keyboardType = UIKeyboardTypeDecimalPad;
            [_phoneTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];
            _areaBtn = (UIButton *)[setCell.contentView viewWithTag:105];
            [_areaBtn setTitle:[NSString stringWithFormat:@"+%@",[_inputDic objectForKey:@"area"]] forState:UIControlStateNormal];
            [_areaBtn setTitle:[NSString stringWithFormat:@"+%@",[_inputDic objectForKey:@"area"]]  forState:UIControlStateHighlighted];
            [_areaBtn addTarget:self action:@selector(areaBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
            
            _codeBtn = (UIButton *)[setCell.contentView viewWithTag:104];
            _codeBtn.layer.borderColor = [UIColor colorWithHexString:@"0xB2B2B2"].CGColor;
            _codeBtn.layer.borderWidth = 0.5f;
            _codeBtn.layer.cornerRadius = 4.0f;
            
        }else if(indexPath.row==1){
            _codeTextField = (UITextField *)[setCell.contentView viewWithTag:101];
            _codeTextField.keyboardType = UIKeyboardTypeNumberPad;
            [_codeTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];

        }else if(indexPath.row==2){
            _pwdTextField = (UITextField *)[setCell.contentView viewWithTag:102];
            [_pwdTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];

            if(_isSeePwd)
                _pwdTextField.secureTextEntry = NO;
            else
                _pwdTextField.secureTextEntry = YES;
        }else if(indexPath.row==3){
            _licenseTextField = (UITextField *)[setCell.contentView viewWithTag:106];
            [_licenseTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];

        }
        
    }
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    
    
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 80)];
    v.backgroundColor = [UIColor clearColor];
    
    UIButton *btnCheck = [[UIButton alloc] initWithFrame:CGRectMake(0,0,44,44)];
    btnCheck.titleLabel.font = [UIFont systemFontOfSize:17.f];
    [btnCheck setBackgroundColor:[UIColor clearColor]];
    [btnCheck setImage:[UIImage imageNamed:@"Login_NoCheck"] forState:UIControlStateNormal];
    [btnCheck setImage:[UIImage imageNamed:@"Login_Check"] forState:UIControlStateSelected];
    btnCheck.selected = YES;
    [btnCheck addTarget:self action:@selector(checkBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
    [v addSubview:btnCheck];
    
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"遵守《惠圈用户注册协议》"];
    [str addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"0x323232"] range:NSMakeRange(0,2)];
    [str addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"0x3f975a"] range:NSMakeRange(2, str.length-2)];
    
    UIButton *btnAgree = [[UIButton alloc] initWithFrame:CGRectMake(44,0,244,44)];
    btnAgree.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [btnAgree setBackgroundColor:[UIColor clearColor]];
    [btnAgree setTitleColor:[UIColor colorWithHexString:@"0x323232"] forState:UIControlStateNormal];
    [btnAgree.titleLabel setTextAlignment:NSTextAlignmentLeft];
    [btnAgree setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
    [btnAgree setAttributedTitle:str forState:UIControlStateNormal];
    
    [btnAgree addTarget:self action:@selector(agreeeBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
    [v addSubview:btnAgree];
    
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15,52,tableView.frame.size.width-30,44)];
    btn.backgroundColor = [UIColor colorWithHexString:@"0xC5000A"];
    btn.layer.cornerRadius = 4.0f;
    btn.titleLabel.font = [UIFont systemFontOfSize:17.f];
    [btn setTitle:NSLocalizedString(@"确定", @"确定") forState:UIControlStateNormal];
    [btn setTitle:NSLocalizedString(@"确定", @"确定") forState:UIControlStateHighlighted];
    [btn addTarget:self action:@selector(setupBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    btn.showsTouchWhenHighlighted = YES;
    [v addSubview:btn];
    return v;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section;
{
    return 100.f;
}

- (void)checkBtnClicked:(id)sender
{
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.isSelected;
    _isReadAgree = btn.isSelected;
}

- (void)agreeeBtnClicked:(id)sender
{
    [self performSegueWithIdentifier:@"BMSQ_Agreement" sender:nil];
}

#pragma mark 注册
- (void)setupBtnClicked
{
 
    [self.view endEditing:YES];
    NSString *userStr = [_phoneTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入手机号码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    userStr = [_codeTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length!=6){
        CSAlert(@"请输入验证码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"validateCode"];
    }
    
    userStr = [_pwdTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length<6||userStr.length>20){
        CSAlert(@"请输入6-29位密码");
        return;
    }else{
        
        NSString *pw = [MD5 MD5Value:userStr];
        [_inputDic setObject:pw forKey:@"password"];
    }
    
    userStr = [_licenseTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        
    }else {
        [_inputDic setObject:userStr forKey:@"recomNbr"];
        
    }
    
    
    if(!_isReadAgree){
        CSAlert(@"请勾选遵守惠圈用户协议");
        return;
        
    }
    [self registHttpRequest];
}
#pragma mark 注册
- (void)registHttpRequest
{
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"2"];
      __weak typeof(self) wself = self;
    [_inputDic setObject:[OpenUDID value] forKey:@"deviceNbr"];
    [self.jsonPrcClient invokeMethod:@"register" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{

                if(self.isJoin){
                    
                    NSString *userCode = [responseObject objectForKey:@"userCode"];
                    BMSQ_JoinOtherSuccessViewController *vc = [[BMSQ_JoinOtherSuccessViewController alloc]init];
//                    http://baomi.suanzi.cn/Api/Browser/regSucc?userCode=用户编码
                    vc.requestStr = [NSString stringWithFormat:@"%@/Browser/regSucc?userCode=%@",H5_URL,userCode];
                    [wself presentViewController:vc animated:YES completion:^{
                    }];
                    
                }else{
                    
                    
                    [wself login];
                }
            
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 60000:
                CSAlert(@"请输入手机号码");
                break;
            case 60001:
                CSAlert(@"手机号码不正确");
                break;
            case 60003:
                CSAlert(@"手机号已经被使用");
                break;
            case 60004:
                CSAlert(@"推荐人手机号码错误");
                break;
            case 80012 :
                CSAlert(@"请输入验证码");
                break;
            case 80011 :
                CSAlert(@"验证码不正确");
                break;
            case 60010 :
                CSAlert(@"请输入密码");
                break;
            case 60015 :
                CSAlert(@"密码格式不正确");
                break;
            case 60020 :
                CSAlert(@"请输入推荐人手机号");
                break;
            case 50508 :
                CSAlert(@"该邀请码不存在");
                break;
   
            default:
            {
                UIAlertView *alterVier = [[UIAlertView alloc]initWithTitle:@"错误" message:[NSString stringWithFormat:@"error[%d]",resCode] delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
                [alterVier show];
            }
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}
#pragma mark 自动登录
-(void)login{
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject: [_inputDic objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];
    [params setObject:[_inputDic objectForKey:@"password"] forKey:@"password"];
    [params setObject:@"1" forKey:@"loginType"];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *regid = [userDefaults objectForKey:RegistratcionID];
    if (regid.length>0) {
        [params setObject:regid forKey:@"registrationId"];
        
    }else{
        [params setObject:@"" forKey:@"registrationId"];
        
    }

    
    [self.jsonPrcClient invokeMethod:@"login" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                
//                code = 50000;
//                expiresAt = 1442547485;
//                shopCode = "";
//                tokenCode = e57c7d97882edfda35696026f14bb5df;
//                userCode = "0814d1d0-76c4-609b-bd6b-be3c6e8cdd91";

                NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                [uD setObject:params forKey:APP_USER_AOCNUM_KEY];
                [uD synchronize];
                
                [self dismissViewControllerAnimated:YES completion:^{
                    
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
        CSAlert(error.localizedDescription);
    }];

    
    
    
}

- (void)btnBackClicked{
    if (self.isJoin) {
        [self dismissViewControllerAnimated:YES completion:nil];
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
}


-(void)changeLength:(UITextField *)textField{
    
    if (textField.tag == 100) {
        if (textField.text.length>11) {
            textField.text = [textField.text substringToIndex:11];
        }
    }else if (textField.tag == 101){
        if (textField.text.length>8) {
            textField.text = [textField.text substringToIndex:8];
        }
        
    }else if (textField.tag == 102){
        if (textField.text.length>24) {
            textField.text = [textField.text substringToIndex:24];
        }
        
    }else if (textField.tag == 106){
        if (textField.text.length>10) {
            textField.text = [textField.text substringToIndex:10];
        }
        
    }


}
@end
