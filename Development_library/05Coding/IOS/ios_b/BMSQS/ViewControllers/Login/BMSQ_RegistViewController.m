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

@interface BMSQ_RegistViewController ()

@end

@implementation BMSQ_RegistViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationItem setTitle:@"账户激活"];

    CGFloat height = APP_VIEW_WIDTH*(229/750.0);
    UIImageView *bottomView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT/3*2, APP_VIEW_WIDTH, height)];
    bottomView.image = [UIImage imageNamed:@"login_footer"];
    [self.view addSubview:bottomView];
    [self.view bringSubviewToFront:bottomView];
    
}

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
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
    NSDictionary *dic = @{@"mobileNbr":[_inputDic objectForKey:@"mobileNbr"],
                          @"action":@"r",
                          @"appType":@"s"};
     __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        if (resCode == SUCCESSRESULT) {
            CSAlert(@"验证码已发送，请注意查收短信");
            [weakSelf timerFireMethod];
        }else{
            [self codeShowValue:resCode];

        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
    
}
-(void)timerFireMethod{
    __block int timeout = 60;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_source_t timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(timer, dispatch_walltime(NULL, 0), 1.0 * NSEC_PER_SEC, 0); //每秒执行
    dispatch_source_set_event_handler(timer, ^{
        if (timeout <= 0){
            dispatch_source_cancel(timer);
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:@"再次获取" forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = YES;
            });
        } else {
            NSString *strTime = [NSString stringWithFormat:@"%ds", timeout];
            dispatch_async(dispatch_get_main_queue(), ^{
                [_codeBtn setTitle:strTime forState:UIControlStateNormal];
                [_codeBtn setTitleColor:[UIColor colorWithHexString:@"0xB2B2B2"] forState:UIControlStateNormal];
                _codeBtn.userInteractionEnabled = NO;
            });
            timeout--;
        }
    });
    dispatch_resume(timer);
}
- (IBAction)seePwdBtnClicked:(id)sender
{
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.isSelected;
    _isSeePwd = btn.isSelected;
    [_tableView reloadData];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
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
            _phoneTextField.keyboardType = UIKeyboardTypePhonePad;
            [_phoneTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];
            
            _areaBtn = (UIButton *)[setCell.contentView viewWithTag:105];
            [_areaBtn setTitle:[NSString stringWithFormat:@"+%@",[_inputDic objectForKey:@"area"]] forState:UIControlStateNormal];
            [_areaBtn setTitle:[NSString stringWithFormat:@"+%@",[_inputDic objectForKey:@"area"]]  forState:UIControlStateHighlighted];
//            [_areaBtn addTarget:self action:@selector(areaBtnClicked:) forControlEvents:UIControlEventTouchUpInside];

            [_areaBtn addTarget:self action:@selector(areaBtnClick:) forControlEvents:UIControlEventTouchUpInside];
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
            if(_isSeePwd)
                _pwdTextField.secureTextEntry = NO;
            else
                _pwdTextField.secureTextEntry = YES;
            [_pwdTextField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];
        }else if(indexPath.row==3){
            _licenseTextField = (UITextField *)[setCell.contentView viewWithTag:106];
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
    
//    UIButton *btnCheck = [[UIButton alloc] initWithFrame:CGRectMake(0,0,44,44)];
//    btnCheck.titleLabel.font = [UIFont systemFontOfSize:17.f];
//    [btnCheck setBackgroundColor:[UIColor clearColor]];
//    [btnCheck setImage:[UIImage imageNamed:@"Login_NoCheck"] forState:UIControlStateNormal];
//    [btnCheck setImage:[UIImage imageNamed:@"Login_Check"] forState:UIControlStateSelected];
//    [btnCheck addTarget:self action:@selector(checkBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
//    [v addSubview:btnCheck];
//    
//    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"遵守《惠圈商户注册协议》"];
//    [str addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"0x323232"] range:NSMakeRange(0,2)];
//    [str addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithHexString:@"0x3f975a"] range:NSMakeRange(2, str.length-2)];
//    
//    UIButton *btnAgree = [[UIButton alloc] initWithFrame:CGRectMake(44,0,244,44)];
//    btnAgree.titleLabel.font = [UIFont systemFontOfSize:14.f];
//    [btnAgree setBackgroundColor:[UIColor clearColor]];
//    [btnAgree setTitleColor:[UIColor colorWithHexString:@"0x323232"] forState:UIControlStateNormal];
//    [btnAgree.titleLabel setTextAlignment:NSTextAlignmentLeft];
//    [btnAgree setContentHorizontalAlignment:UIControlContentHorizontalAlignmentLeft];
//    [btnAgree setAttributedTitle:str forState:UIControlStateNormal];
//    
//    [btnAgree addTarget:self action:@selector(agreeeBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
//    [v addSubview:btnAgree];
    
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15,15,tableView.frame.size.width-30,44)];
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
        [_inputDic setObject:[MD5 MD5Value:userStr] forKey:@"password"];
    }
    
//    userStr = [_licenseTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
//    if(!userStr||userStr.length==0){
//        CSAlert(@"请输入营业执照");
//        return;
//    }else{
//        [_inputDic setObject:userStr forKey:@"licenseNbr"];
//    }
//    if(!_isReadAgree){
//        CSAlert(@"请勾选遵守惠圈商户协议");
//        return;
//        
//    }
    [self registHttpRequest];
}
#pragma mark --商户激活--
- (void)registHttpRequest
{
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"activate" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"您已经成功注册");
                [self.navigationController popViewControllerAnimated:YES];
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
                CSAlert(@"请输入营业执照编号");
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}
-(void)changeLength:(UITextField *)textField{
    
    
    if (textField.tag == 100) {
        if (textField.text.length>11) {
            textField.text = [textField.text substringToIndex:11];
        }
    }else if (textField.tag ==101){
        if (textField.text.length>8) {
            textField.text = [textField.text substringToIndex:8];
        }
    }else if (textField.tag == 102){
        if (textField.text.length>24) {
            textField.text = [textField.text substringToIndex:24];
        }
    }
    
}

- (void)codeShowValue:(int)code{
  
    switch (code) {
        case 80010:
            CSAlert(@"验证码请求失败") ;
            break;
            
        case 60000:
            CSAlert(@"请输入手机号码") ;
            break;
        case 60001:
            CSAlert(@"手机号码不正确") ;
            break;
        case 60003:
            CSAlert(@"手机号码已经注册") ;
            break;
        case 60200:
            CSAlert(@"手机号未提交审核") ;
            break;
        case 60201:
            CSAlert(@"手机号审核还未通过，请耐心等待") ;
            break;
        case 60202:
            CSAlert(@"手机号审核已经通过，请直接登录") ;
            break;
        case 60203:
            CSAlert(@"商家已经审核过，不能重复审核") ;
            break;
        case 80013:
            CSAlert(@"动作不正确") ;
            break;
        case 80014:
            CSAlert(@"app类型不正确") ;
            break;
        default:
            break;
    }
    
    
}



- (void)areaBtnClick:(id)sender {
    
    NSLog(@"+86");
    
}


@end
