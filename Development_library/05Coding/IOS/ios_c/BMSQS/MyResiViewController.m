//
//  MyResiViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/18.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "MyResiViewController.h"
#import "SVProgressHUD.h"
#import "UIColor+Tools.h"

#import "BMSQ_JoinOtherSuccessViewController.h"
#import "MD5.h"
#import "AFJSONRPCClient.h"

@interface MyResiViewController ()<UITableViewDataSource,UITableViewDelegate>
{
    NSTimer* timer;
    UIButton* btn_VerCode;
    int timeout;
}
@property (nonatomic, strong)NSString * iphone;

@end

@implementation MyResiViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.view.backgroundColor = [UIColor whiteColor];
    UIView *navView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y)];
    navView.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [self.view addSubview:navView];
    UILabel *titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_ORIGIN_Y-20)];
    titleLabel.backgroundColor = [UIColor clearColor];
    titleLabel.text = @"新人注册";
    titleLabel.textAlignment = NSTextAlignmentCenter;
    titleLabel.font = [UIFont boldSystemFontOfSize:18];
    titleLabel.textColor = [UIColor whiteColor];
    [navView addSubview:titleLabel];
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y-APP_NAV_LEFT_ITEM_HEIGHT, 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(goBackclick) forControlEvents:UIControlEventTouchUpInside];
    [navView addSubview:btnback];
    
    _inputDic = [[NSMutableDictionary alloc]init];
    
    self._tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self._tableView.delegate = self;
    self._tableView.dataSource = self;
    [self.view addSubview:self._tableView];
    


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

- (void)seePwdBtnClicked:(id)sender
{
    UIButton *btn = (UIButton *)sender;
    btn.selected = !btn.isSelected;
    _isSeePwd = btn.isSelected;
    [self._tableView reloadData];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 44;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 4;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{

    if (indexPath.row ==0) {
        static NSString *identifier = @"myPhoneTableViewCell";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
            [button setTitle:@"+86" forState:UIControlStateNormal];
            button.backgroundColor = [UIColor clearColor];
            [button setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            [cell.contentView addSubview:button];
            button.titleLabel.font = [UIFont systemFontOfSize:15.f];
            UILabel *lab = [[UILabel alloc]initWithFrame:CGRectMake(70, 5, 0.4, 44-10)];
            lab.backgroundColor = [UIColor grayColor];
            [cell.contentView addSubview:lab];
            
            UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(90, 0, APP_VIEW_WIDTH-200, 44)];
            textField.backgroundColor = [UIColor clearColor];
            textField.placeholder = @"输入手机号";
            textField.font = [UIFont systemFontOfSize:13.f];
            textField.keyboardType = UIKeyboardTypeDecimalPad;
            textField.tag = 200;
            [textField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];
            [cell.contentView addSubview:textField];
            
            UIButton *yzmbutton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 10, 80, 25)];
            yzmbutton.layer.borderWidth = 0.6;
            yzmbutton.layer.borderColor = [[UIColor grayColor]CGColor];
            yzmbutton.layer.cornerRadius = 4;
            yzmbutton.layer.masksToBounds = YES;
            yzmbutton.backgroundColor = [UIColor clearColor];
            [yzmbutton setTitle:@"获取验证码" forState:UIControlStateNormal];
            [yzmbutton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
            yzmbutton.titleLabel.font = [UIFont systemFontOfSize:13.f];
            yzmbutton.tag = 100;
            [yzmbutton addTarget:self action:@selector(getYZMclick:) forControlEvents:UIControlEventTouchUpInside];
            [cell.contentView addSubview:yzmbutton];
            
            
        }
        return cell;
    }else{
        static NSString *identifier = @"myCodeTableViewCell";
        
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(15, 0, 80, 44)];
           label.text = @"验证码";
            label.backgroundColor = [UIColor clearColor];
            label.tag = 100;
            label.font = [UIFont systemFontOfSize:13.f];
            [cell.contentView addSubview:label];
      
            
            UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(90, 0, APP_VIEW_WIDTH-60, 44)];
            textField.backgroundColor = [UIColor clearColor];
            textField.placeholder = @"请输入验证码";
            textField.tag =101;
            textField.font = [UIFont systemFontOfSize:13.f];
            [textField addTarget:self action:@selector(changeLength:) forControlEvents:UIControlEventEditingChanged];

            [cell.contentView addSubview:textField];
            
        }
        
        UILabel *label = (UILabel *)[cell.contentView viewWithTag:100];
        UITextField *textfield = (UITextField *)[cell.contentView viewWithTag:101];
        textfield.tag = 200+indexPath.row;

        if (indexPath.row ==1) {
            label.text = @"验证码";
            textfield.placeholder = @"请输入验证码";

        }else if(indexPath.row == 2){
            label.text = @"密码";
            textfield.placeholder = @"输入新密码";

        }else if(indexPath.row == 3){
            label.text = @"推荐码";
            textfield.placeholder = @"输入推荐码(可不填)";
        }
        
        return cell;
        
    }
    
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
#pragma mark 注册协议
- (void)agreeeBtnClicked:(id)sender
{
    
    [self setupBtnClicked];
}

- (void)setupBtnClicked
{

    
    //手机号
     NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    UITableViewCell *cell=[self._tableView cellForRowAtIndexPath:indexPath];
    UITextField *phone = (UITextField *)[cell.contentView viewWithTag:200];
    //验证码
    indexPath=[NSIndexPath indexPathForRow:1 inSection:0];
    cell=[self._tableView cellForRowAtIndexPath:indexPath];
    UITextField *codeTextField = (UITextField *)[cell.contentView viewWithTag:201];
    //密码
    indexPath=[NSIndexPath indexPathForRow:2 inSection:0];
    cell=[self._tableView cellForRowAtIndexPath:indexPath];
    UITextField *pwdTextField = (UITextField *)[cell.contentView viewWithTag:202];
    //推荐人
    indexPath=[NSIndexPath indexPathForRow:3 inSection:0];
    cell=[self._tableView cellForRowAtIndexPath:indexPath];
    UITextField *otherTextField = (UITextField *)[cell.contentView viewWithTag:203];
  
    
    NSString *userStr = [phone.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入手机号码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    userStr = [codeTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length!=6){
        CSAlert(@"请输入正确的验证码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"validateCode"];
    }
    
    userStr = [pwdTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length<6||userStr.length>20){
        CSAlert(@"请输入6-29位密码");
        return;
    }else{
        
        NSString *pw = [MD5 MD5Value:userStr];
        [_inputDic setObject:pw forKey:@"password"];
    }
    
    userStr = [otherTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length!=0){
        
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
    AFJSONRPCClient  *jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    __weak typeof(self) wself = self;
    [jsonPrcClient invokeMethod:@"register" withParameters:_inputDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
       
        switch (resCode) {
            case 50000:
            {
                NSString *userCode = [responseObject objectForKey:@"userCode"];
                BMSQ_JoinOtherSuccessViewController *vc = [[BMSQ_JoinOtherSuccessViewController alloc]init];
                vc.requestStr = [NSString stringWithFormat:@"%@/Browser/regSucc?userCode=%@",H5_URL,userCode];
                
                [wself loginRequestabcd];
                [wself.navigationController pushViewController:vc animated:YES];
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


- (void)loginRequestabcd
{
    [SVProgressHUD showWithStatus:ProgressHudStr];
    AFJSONRPCClient  *jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
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
        
    }    [jsonPrcClient invokeMethod:@"login" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                [uD setObject:_inputDic forKey:APP_USER_AOCNUM_KEY];
                [uD synchronize];
            }
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}

-(void)getYZMclick:(UIButton *)button{
    
    id v=[button superview];
    while (![v isKindOfClass:[UITableViewCell class]]) {
        v=[v superview];
    }
    UITableViewCell *cell=(UITableViewCell*)v;
    UITextField *textField = (UITextField *)[cell.contentView viewWithTag:200];
    self.iphone = textField.text;
    if (self.iphone.length ==0) {
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"错误" message:@"请输入手机号码" delegate:nil cancelButtonTitle:nil
    otherButtonTitles:@"ok", nil];
        [alterView show];
        return;
    }
    
    
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    
    AFJSONRPCClient *jsonPrcClient = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
    NSDictionary *dic = @{@"mobileNbr":textField.text.length>0?textField.text:@"",
                          @"action":@"r",@"appType":@"c"};
    __weak typeof(self) weakSelf = self;
    [jsonPrcClient invokeMethod:@"getValidateCode" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"验证码已发送，请注意查收短信");
                [weakSelf timerFireMethod];
            }
                break;
            case 60003:
                CSAlert(@"手机号码已经被使用");
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
            default:{
                NSString *str = [NSString stringWithFormat:@"错误:CODE[%d]",resCode];
                CSAlert(str);
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

-(void)changeLength:(UITextField *)textField{
    
    NSLog(@"%d",(int)textField.tag);
    
    if (textField.tag == 200) {
        if (textField.text.length>11) {
            textField.text = [textField.text substringToIndex:11];
        }
    }else if (textField.tag ==201){
        if (textField.text.length>8) {
            textField.text = [textField.text substringToIndex:8];
        }
    }else if (textField.tag == 202){
        if (textField.text.length>24) {
            textField.text = [textField.text substringToIndex:24];
        }
    }else if (textField.tag ==203){
        if (textField.text.length>8) {
            textField.text = [textField.text substringToIndex:8];
        }
    }
    
}

-(void)goBackclick{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
