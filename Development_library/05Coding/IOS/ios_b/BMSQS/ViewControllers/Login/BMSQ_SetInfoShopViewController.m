//
//  BMSQ_SetInfoShopViewController.m
//  BMSQS
//
//  Created by liuqin on 15/11/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_SetInfoShopViewController.h"

#import "BMSQ_applicationResultViewController.h"

#import "SVProgressHUD.h"

@interface BMSQ_SetInfoShopViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)NSArray *leftS;


@property (nonatomic, strong)UIView *seleTimePickerView;


@property (nonatomic, strong)NSString *startTime;
@property (nonatomic, strong)NSString *endTime;

@property (nonatomic, assign)BOOL isStart;
@property (nonatomic, assign)BOOL isEnd;

@property (nonatomic, strong)NSString *seleTimeStr;

@property (nonatomic, strong)UITableView *infoTableView;

@end

@implementation BMSQ_SetInfoShopViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"填写门店信息"];
    self.leftS = @[@"店铺简称",@"联系电话",@"营业时间",@"店铺地址",@"手机号码"];
    self.startTime = @"08:00";
    self.endTime = @"18:00";
    
    
    self.infoTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45*5+80)];
    self.infoTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.infoTableView.backgroundColor = [UIColor clearColor];
    self.infoTableView.delegate =self;
    self.infoTableView.dataSource =self;
    self.infoTableView.scrollEnabled = NO;
    [self.view addSubview:self.infoTableView];
    
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(15, self.infoTableView.frame.origin.y+self.infoTableView.frame.size.height,APP_VIEW_WIDTH-30, 40)];
    button.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [button setTitle:@"提交申请" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    button.layer.cornerRadius = 5;
    button.layer.masksToBounds = YES;
    [self.view addSubview:button];
    
    [button addTarget:self action:@selector(clickSubmit) forControlEvents:UIControlEventTouchUpInside];
    
    
}
-(void)clickSubmit{

    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    UITableViewCell *cell = [self.infoTableView cellForRowAtIndexPath:indexPath];
    UITextField *shopName = (UITextField *)[cell viewWithTag:200];
    
    indexPath=[NSIndexPath indexPathForRow:1 inSection:0];
    cell = [self.infoTableView cellForRowAtIndexPath:indexPath];
    UITextField *tel = (UITextField *)[cell viewWithTag:200];
    
    indexPath=[NSIndexPath indexPathForRow:3 inSection:0];
    cell = [self.infoTableView cellForRowAtIndexPath:indexPath];
    UITextField *Street = (UITextField *)[cell viewWithTag:200];
    
    indexPath=[NSIndexPath indexPathForRow:4 inSection:0];
    cell = [self.infoTableView cellForRowAtIndexPath:indexPath];
    UITextField *mobileNbr = (UITextField *)[cell viewWithTag:200];
    
    
    NSString *shopCode =[gloabFunction getShopCode];
    if ([shopCode isEqualToString:@"(null)"]) {
        shopCode = @"";
    }
    
    if ([shopName.text isEqualToString:@""] || [tel.text isEqualToString:@""] || [Street.text isEqualToString:@""] || [mobileNbr.text isEqualToString:@""]) {
        
        CSAlert(@"请将门店信息填写完整");
        
    }else{
    
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:shopCode forKey:@"shopCode"];
    [params setObject:shopName.text forKey:@"shopName"];
    [params setObject:tel.text forKey:@"tel"];
    [params setObject:self.startTime forKey:@"startTime"];
    [params setObject:self.endTime forKey:@"endTime"];
    [params setObject:Street.text forKey:@"street"];
    [params setObject:mobileNbr.text forKey:@"mobileNbr"];

    [self initJsonPrcClient:@"1"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"applyEntry" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        
        if ([code intValue]== 50000) {
            BMSQ_applicationResultViewController *vc = [[BMSQ_applicationResultViewController alloc]init];
            vc.isLogin = weakSelf.isLogin;
            [weakSelf.navigationController pushViewController:vc animated:YES];

        }else{
            [SVProgressHUD showErrorWithStatus:@"error"];
        }
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        NSLog(@"asdfasdfa");
        
        
    }];

    }
    
    
    
  }

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.leftS.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *indentifier = @"shopSetInfoCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:indentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:indentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 44)];
        bgView.backgroundColor = [UIColor whiteColor];
        [cell addSubview:bgView];
        
        UILabel *leftlabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
        leftlabel.textAlignment = NSTextAlignmentCenter;
        leftlabel.tag = 100;
        leftlabel.textColor = UICOLOR(73, 73, 73, 1);
        leftlabel.font = [UIFont systemFontOfSize:14];
        [bgView addSubview:leftlabel];
        
        
        UITextField *rigintText = [[UITextField alloc]initWithFrame:CGRectMake(80, 0, APP_VIEW_WIDTH-80-10, 44)];
        rigintText.textAlignment = NSTextAlignmentRight;
        rigintText.tag = 200;
        rigintText.textColor = UICOLOR(73, 73, 73, 1);
        rigintText.font = [UIFont systemFontOfSize:14];
        [bgView addSubview:rigintText];
        rigintText.hidden = YES;
        
        
        UIButton *endBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 0, 60, 44)];
        [endBtn setTitle:@"18:00" forState:UIControlStateNormal];
        endBtn.tag = 222;
        endBtn.titleLabel.font =[UIFont systemFontOfSize:14];
        [endBtn setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        [bgView addSubview:endBtn];
        endBtn.hidden = YES;
        
        UIButton *startBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-140, 0, 60, 44)];
        [startBtn setTitle:@"08:00" forState:UIControlStateNormal];
        startBtn.tag = 111;
        startBtn.titleLabel.font =[UIFont systemFontOfSize:14];
        [startBtn setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        [bgView addSubview:startBtn];
        startBtn.hidden = YES;
        
        
        [startBtn addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];
        [endBtn addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];

        
        UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(startBtn.frame.origin.x+60+10,22, 5, 1)];
        lineView.backgroundColor =UICOLOR(182, 0, 12, 1.0);
        [bgView addSubview:lineView];
        lineView.hidden = YES;
        lineView.tag = 333;

    }

    UILabel *leftlabel = (UILabel *)[cell viewWithTag:100];
    leftlabel.text = [self.leftS objectAtIndex:indexPath.row];
    
    UITextField *rightText = (UITextField *)[cell viewWithTag:200];
    UIButton *endButton = (UIButton *)[cell viewWithTag:222];
    UIButton *startButton = (UIButton *)[cell viewWithTag:111];
    UIView *linView = (UIView *)[cell viewWithTag:333];


    endButton.hidden = YES;
    startButton.hidden = YES;
    linView.hidden = YES;


    ;
    
    if (indexPath.row ==0) {
        rightText.placeholder = @"请输入店铺简称";
        rightText.text = [gloabFunction getShopName];
        rightText.hidden = NO;

    }else if (indexPath.row == 1){
        rightText.placeholder = @"请输入联系电话";
        rightText.keyboardType = UIKeyboardTypeNumberPad;
        rightText.text = [gloabFunction getTel];
        rightText.hidden = NO;

    }else if (indexPath.row == 3){
        rightText.placeholder = @"请输入店铺地址";
        rightText.text = [gloabFunction getStreet];
        rightText.hidden = NO;

    }else if (indexPath.row == 4){
        rightText.placeholder = @"请输入手机号码";
        rightText.keyboardType = UIKeyboardTypeNumberPad;
        rightText.hidden = NO;

        
    }else if (indexPath.row == 2){
        rightText.hidden = YES;
        endButton.hidden = NO;
        startButton.hidden = NO;
        linView.hidden = NO;
        [startButton setTitle:self.startTime forState:UIControlStateNormal];
        [endButton setTitle:self.endTime forState:UIControlStateNormal];


    }
    
    
    
    return cell;
}


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 45;
}


-(void)seleTime:(UIButton *)button{
    
    if (button.tag == 111) {
        self.isStart = YES;
        self.isEnd = NO;
        
    }else {
        self.isStart = NO;
        self.isEnd = YES;
    }
    
    if (self.seleTimePickerView == nil) {
        self.seleTimePickerView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-100, APP_VIEW_HEIGHT/2-90, 280, 200)];
        self.seleTimePickerView.backgroundColor = [UIColor whiteColor];
        self.seleTimePickerView.layer.borderWidth = 0.5;
        self.seleTimePickerView.layer.borderColor = [[UIColor grayColor]CGColor];
        self.seleTimePickerView.layer.cornerRadius = 0.3;
        self.seleTimePickerView.layer.masksToBounds = YES;
        self.seleTimePickerView.center = self.view.center;
        [self.view addSubview:self.seleTimePickerView];
        
        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(240,0 ,40 ,40)];
        button.backgroundColor = [UIColor clearColor];
        [button setTitle:@"关闭" forState:UIControlStateNormal];
        button.titleLabel.font = [UIFont systemFontOfSize:13.f];
        [button setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        [self.seleTimePickerView addSubview:button];
        [button addTarget:self action:@selector(closeTimePicker:) forControlEvents:UIControlEventTouchUpInside];
        
        
    }else{
        self.seleTimePickerView.hidden = NO;
    }
    UIDatePicker *datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 50, 280, 150)];
    datePicker.backgroundColor = [UIColor whiteColor];
    datePicker.layer.borderWidth = 0.5;
    datePicker.layer.borderColor = [[UIColor grayColor]CGColor];
    datePicker.layer.cornerRadius = 0.3;
    datePicker.layer.masksToBounds = YES;    // 设置时区
    // 设置UIDatePicker的显示模式
    [datePicker setDatePickerMode:UIDatePickerModeTime];
    // 当值发生改变的时候调用的方法
    [datePicker addTarget:self action:@selector(dateTimePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.seleTimePickerView addSubview:datePicker];
    
}
-(void)closeTimePicker:(UIButton *)sender{
    self.seleTimePickerView.hidden = YES;
    
    if (self.isStart){
        self.startTime = self.seleTimeStr;
    }
    else{
        self.endTime  = self.seleTimeStr;
    }

    [self.infoTableView reloadData];
    
    
}

-(void)dateTimePickerValueChanged:(UIDatePicker *)picker{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"HH:mm"];
    NSString *timestamp_str = [outputFormatter stringFromDate:picker.date];
    self.seleTimeStr = timestamp_str;
}




@end
