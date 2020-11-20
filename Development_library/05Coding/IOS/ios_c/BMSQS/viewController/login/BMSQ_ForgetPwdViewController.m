//
//  BMSQ_ForgetPwdViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ForgetPwdViewController.h"
#import "PickerInputView.h"
#import "BMSQ_LoginViewController.h"
#import "MD5.h"
#import "MobClick.h"
@interface BMSQ_ForgetPwdViewController ()<PickerInputViewDelegate>
{
    NSTimer* timer;
    UIButton* btn_VerCode;
    int timeout;
}

@end

@implementation BMSQ_ForgetPwdViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ForgetPwd"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"ForgetPwd"];
}



- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"密码重置"];
    [self createModel];
    timeout = 60;
}


#pragma mark - Action
- (void)btnBackClicked:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)areaBtnClicked:(id)sender
{
    [self.view endEditing:YES];
    [self showAreaPickerView];
}

- (IBAction)codeBtnClicked:(id)sender
{
    [self.view endEditing:YES];
    

    
    NSString *userStr = [_phoneTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0){
        CSAlert(@"请输入手机号码");
        return;
    }
    else if(![gloabFunction checkTel:userStr])
    {
        
    }
    else
    {
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    btn_VerCode = sender;
    NSDate *scheduledTime = [NSDate dateWithTimeIntervalSinceNow:0];
    timer = [[NSTimer alloc] initWithFireDate:scheduledTime
                                     interval:1
                                       target:self
                                     selector:@selector(countdown)
                                     userInfo:nil
                                      repeats:YES];
    
    [[NSRunLoop currentRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
    
    [self fetchCodeRequest:@"f"];

}

- (void)countdown
{
    NSString *downSecondStr = [NSString stringWithFormat:@"%ds",timeout];
    btn_VerCode.enabled = NO;
    [btn_VerCode setTitle:downSecondStr forState:UIControlStateNormal];
    
    if (timeout == 0) {
        [timer invalidate];
        btn_VerCode.backgroundColor = [UIColor clearColor];
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
        CSAlert(@"请输入6位验证码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"validateCode"];
    }
    
    userStr = [_pwdTextField.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if(!userStr||userStr.length==0||userStr.length<6||userStr.length>20){
        CSAlert(@"请输入6-20位密码");
        return;
    }else{
        [_inputDic setObject:userStr forKey:@"password"];
    }
    [self findPwdHttpRequest];
}

- (void)showAreaPickerView
{
    UIWindow *curWindow = [UIApplication sharedApplication].keyWindow;
    CGRect rect = [curWindow bounds];

    PickerInputView *pick = [[PickerInputView alloc] initWithFrame:CGRectMake(0, rect.size.height-500, rect.size.width, 500)];
    pick.backgroundColor = [UIColor clearColor];
    pick.delegate = self;
    pick.picker.dataSource= self;
    pick.picker.delegate= self;
    pick.picker.showsSelectionIndicator = YES;
    [pick becomeFirstResponder];
    [self.view addSubview:pick];
}

- (void)createModel
{
    if (self.areaDataArray==nil) {
        self.areaDataArray = [[NSMutableArray alloc] init];
    }
    if(_inputDic==nil){
        _inputDic = [[NSMutableDictionary alloc] initWithObjectsAndKeys:@"86",@"area", nil];
        
    }
    NSString *database_path = [[NSBundle mainBundle] pathForResource:@"area" ofType:@"sqlite"];
    if (sqlite3_open([database_path UTF8String], &db) != SQLITE_OK) {
        sqlite3_close(db);
    }
    
    NSString *sqlQuery = @"select * from country_mobile_prefix";
    sqlite3_stmt * statement;
    NSLog(@"%d",sqlite3_prepare_v2(db, [sqlQuery UTF8String], -1, &statement, nil));
    if (sqlite3_prepare_v2(db, [sqlQuery UTF8String], -1, &statement, nil) == SQLITE_OK) {
        while (sqlite3_step(statement) == SQLITE_ROW) {
            
            int idCode = (int)sqlite3_column_int(statement, 0);
            NSString *bankCodeStr = [NSString stringWithFormat:@"%d",idCode];
            
            char *countryName = (char*)sqlite3_column_text(statement, 1);
            NSString *countryNameStr = [[NSString alloc]initWithUTF8String:countryName];
            
            int mobile = (int)sqlite3_column_int(statement, 2);
            NSString *mobileStr = [NSString stringWithFormat:@"%d",mobile];
            
            char *areaName = (char*)sqlite3_column_text(statement, 3);
            NSString *areaNameStr = [[NSString alloc]initWithUTF8String:areaName];

            
            NSDictionary *dic = @{@"id":bankCodeStr,@"country":countryNameStr,@"mobile":mobileStr,@"areaName":areaNameStr};
            [self.areaDataArray addObject:dic];
        }
    }
    sqlite3_close(db);
}
-(void)execSql:(NSString *)sql
{
    char *err;
    if (sqlite3_exec(db, [sql UTF8String], NULL, NULL, &err) != SQLITE_OK) {
        sqlite3_close(db);
    }
}


#pragma mark - UIPickerViewDelegate
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView;
{
    return 1.f;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component;
{
    return [self.areaDataArray count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component;
{
    NSDictionary *dic = [self.areaDataArray objectAtIndex:row];
    NSString *str = [NSString stringWithFormat:@"%@ %@",[dic objectForKey:@"country"],[dic objectForKey:@"mobile"]];
    return str;
}

-(void)picker:(PickerInputView*)picker didClickDoneButton:(id)button;
{
    [picker removeFromSuperview];
    int aa = [picker.picker selectedRowInComponent:0];
    NSDictionary *dic = [self.areaDataArray objectAtIndex:aa];
    [_inputDic setObject:[dic objectForKey:@"mobile"] forKey:@"area"];
    [_tableView reloadData];
}

#pragma mark -Delegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return 3;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 44;
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
            _areaBtn = (UIButton *)[setCell.contentView viewWithTag:100005];
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
        }else if(indexPath.row==2){
            _pwdTextField = (UITextField *)[setCell.contentView viewWithTag:102];
            if(_isSeePwd)
                _pwdTextField.secureTextEntry = NO;
            else
                _pwdTextField.secureTextEntry = YES;

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
    UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(15,27,tableView.frame.size.width-30,44)];
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

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section;
{
    return 15.f;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *v = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 15)];
    v.backgroundColor = [UIColor clearColor];
    return v;
}

/**
 *  @author lxm, 15-07-24 14:07:30
 *
 *
 *  @param type r-注册；f-找回密码
 */
- (void)fetchCodeRequest:(NSString *)type
{
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self initJsonPrcClient:@"0"];
    NSDictionary *dic = @{@"mobileNbr":[_inputDic objectForKey:@"mobileNbr"],
                          @"action":type,@"appType":@"c"};
    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"验证码=%d",[[responseObject objectForKey:@"validateCode"] intValue]);
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"验证码已发送，请注意查收短信");
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
            default:
                CSAlert(@"error");
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
    }];
}

- (void)findPwdHttpRequest
{
    [self initJsonPrcClient:@"0"];
    NSDictionary *dic = @{@"mobileNbr":[_inputDic objectForKey:@"mobileNbr"],
                          @"validateCode":[_inputDic objectForKey:@"validateCode"],
                          @"password":[MD5 MD5Value:[_inputDic objectForKey:@"password"]],
                          @"type":@"1"};  //1 顾客
    [self.jsonPrcClient invokeMethod:@"findPwd" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"找回成功");
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
            case 60010:
                CSAlert(@"请输入密码");
                break;
            case 80011:
                CSAlert(@"验证码错误");
                break;
            case 20207:
                CSAlert(@"用户不存在");
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
}


@end
