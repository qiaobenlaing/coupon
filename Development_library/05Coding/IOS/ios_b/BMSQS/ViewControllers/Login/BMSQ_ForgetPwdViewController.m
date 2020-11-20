//
//  BMSQ_ForgetPwdViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ForgetPwdViewController.h"
#import "PickerInputView.h"
#import "MD5.h"
@interface BMSQ_ForgetPwdViewController ()

@end

@implementation BMSQ_ForgetPwdViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"密码重置"];
    [self createModel];
    
    
    if (self.isShowNav) {
        [self setNavBackItem];
        [self setNavTitle:@"密码重置"];
        CGRect frame = _tableView.frame;
        frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, frame.size.width, frame.size.height-64);
        _tableView.frame = frame;
    } else {
        [self setNavHidden:YES];
        
        self.view.backgroundColor = UICOLOR(239, 239, 244, 1);
        
        
        [self customBackBtn];
    }
    
    
}

/*自定义后退按钮*/
- (void)customBackBtn
{
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - APP_STATUSBAR_HEIGHT), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(-16, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(btnBackClicked) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
    self.navigationItem.leftBarButtonItem = backButtonItem;
}

- (void)btnBackClicked
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


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
    }else{
        [_inputDic setObject:userStr forKey:@"mobileNbr"];
    }
    
    [self fetchCodeRequest:@"f"];

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
    long aa = [picker.picker selectedRowInComponent:0];
    NSDictionary *dic = [self.areaDataArray objectAtIndex:aa];
    [_inputDic setObject:[dic objectForKey:@"mobile"] forKey:@"area"];
    [_tableView reloadData];
}

- (void)timerLogic
{
    NSDate *moment = [NSDate date];
    NSTimeInterval difference = [_startTime timeIntervalSinceDate:moment];
    
    if (difference > -60) {
        [_codeBtn setTitle:[NSString stringWithFormat:@"%d秒",60 + (int)difference] forState:UIControlStateNormal];
    }
    else{
        [_codeBtn setTitle:@"获取验证码" forState:UIControlStateNormal];
        _codeBtn.userInteractionEnabled = YES;
//        [_timer invalidate];
    }
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
            _phoneTextField.keyboardType = UIKeyboardTypePhonePad;
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
                          @"action":type,
                          @"appType":@"s"};
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getValidateCode" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"验证码已发送，请注意查收短信");
                [weakSelf timerFireMethod];
                _codeBtn.userInteractionEnabled = NO;
                //添加按钮倒计时功能
                _startTime = [NSDate date];
//                _timer = [NSTimer scheduledTimerWithTimeInterval:1/60.0 target:self selector:@selector(timerLogic) userInfo:nil repeats:YES];
//                [[NSRunLoop currentRunLoop] addTimer:_timer forMode:NSRunLoopCommonModes];
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
                CSAlert(@"手机号码已经注册");
                break;
            case 80013:
                CSAlert(@"动作不正确");
                break;
            case 80014:
                CSAlert(@"app类型不正确");
                break;
            case 60200:
                CSAlert(@"手机号未提交审核");
                break;
            case 60201:
                CSAlert(@"手机号审核还未通过，请耐心等待");
                break;
            case 60202:
                CSAlert(@"手机号审核已经通过，请直接登录");
                break;
            default:
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
                          @"type":@"0"};
    [self.jsonPrcClient invokeMethod:@"findPwd" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"找回成功");
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
