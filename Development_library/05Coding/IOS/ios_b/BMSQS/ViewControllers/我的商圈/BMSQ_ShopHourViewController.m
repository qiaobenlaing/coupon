//
//  BMSQ_ShopHourViewController.m
//  BMSQS
//
//  Created by lxm on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopHourViewController.h"

@interface BMSQ_ShopHourViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong)UIButton *startButton;
@property (nonatomic, strong)UIButton *endButton;
@property (nonatomic, assign)BOOL isStart;
@property (nonatomic, assign)BOOL isEnd;

@property (nonatomic, assign)BOOL isBig;

@property (nonatomic, strong)NSString *seleTimeStr;
@property (nonatomic, strong)UIView *seleTimePickerView;

@property (nonatomic, strong) UITableView * baseView;

@end

@implementation BMSQ_ShopHourViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"营业时间"];

    [self setViewUp];

    self.isBig = YES;
}


- (void)setViewUp
{
    
    UIButton * addBut = [UIButton buttonWithType:UIButtonTypeCustom];
    addBut.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
    [addBut addTarget:self action:@selector(addButClick:) forControlEvents:UIControlEventTouchUpInside];
    UIImageView * imageView = [[UIImageView alloc] initWithFrame:CGRectMake(addBut.frame.size.width / 2 - 20, 0, 40, addBut.frame.size.height)];
    imageView.image = [UIImage imageNamed:@"right_add"];
    [addBut addSubview:imageView];
    addBut.backgroundColor = [UIColor grayColor];
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    _baseView.rowHeight = 50;
    _baseView.tableFooterView = addBut;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    _baseView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:self.baseView];
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.shopTimeAry.count == 0) {
        return 0;
    }
    return self.shopTimeAry.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"UpdateTimeViewCell";
    UpdateTimeViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UpdateTimeViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell.startButton setTitle:self.shopTimeAry[indexPath.row][@"open"] forState:UIControlStateNormal];
    [cell.endButton setTitle:self.shopTimeAry[indexPath.row][@"close"] forState:UIControlStateNormal];
    [cell.startButton addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];
    [cell.endButton addTarget:self action:@selector(seleTime:) forControlEvents:UIControlEventTouchUpInside];
    cell.startButton.tag = 111;
    cell.endButton.tag = 222;
    
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}



- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    return YES;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary * dic = self.shopTimeAry[indexPath.row];
    
    [self.shopTimeAry removeObject:dic];
    
    [tableView  deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationRight];
}



#pragma mark ---- 按钮
-(void)seleTime:(UIButton *)button{
    
    NSLog(@"%@", self.shopTimeAry);
    
    UITableViewCell * cell = (UITableViewCell *)[button superview];
    NSIndexPath * indexPath = [self.baseView indexPathForCell:cell];
    
    if (button.tag == 111) {
        self.isStart = YES;
        self.isEnd = NO;
        
    }else {
        self.isStart = NO;
        self.isEnd = YES;
    }
    
        self.seleTimePickerView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-140,90, 280, 200)];
        self.seleTimePickerView.backgroundColor = [UIColor whiteColor];
        self.seleTimePickerView.layer.borderWidth = 0.5;
        self.seleTimePickerView.layer.borderColor = [[UIColor grayColor]CGColor];
        self.seleTimePickerView.layer.cornerRadius = 0.3;
        self.seleTimePickerView.layer.masksToBounds = YES;
        
        [self.view addSubview:self.seleTimePickerView];
        
        shopTimeButton * shopTimeBut = [[shopTimeButton alloc]initWithFrame:CGRectMake(240,0 ,40 ,40)];
        shopTimeBut.backgroundColor = [UIColor clearColor];
        [shopTimeBut setTitle:@"关闭" forState:UIControlStateNormal];
        shopTimeBut.titleLabel.font = [UIFont systemFontOfSize:13.f];
        [shopTimeBut setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
        shopTimeBut.indexPath = indexPath;
        [self.seleTimePickerView addSubview:shopTimeBut];
        [shopTimeBut addTarget:self action:@selector(closeTimePicker:) forControlEvents:UIControlEventTouchUpInside];
    
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
-(void)closeTimePicker:(shopTimeButton *)sender{
    
    self.seleTimePickerView.hidden = YES;
    NSIndexPath * index = sender.indexPath;
    UpdateTimeViewCell * newCell = [self.baseView cellForRowAtIndexPath:index];
    
    
    if (self.isStart){
        self.startTime = self.seleTimeStr;
        [self compareTimeSuecc:index];
        if (!self.isBig) {
            self.startTime = nil;
            self.isBig = YES;
            CSAlert(@"开始营业时间不能比结束营业时间晚");
            
            
        }else{
            NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithDictionary:self.shopTimeAry[index.row]];
            [self.shopTimeAry removeObjectAtIndex:index.row];
            
            if (self.startTime == nil) {
                [dic setObject:@"" forKey:@"open"];
            }else{
                [dic setObject:self.startTime forKey:@"open"];
        
            }
            
            [self.shopTimeAry insertObject:dic atIndex:index.row];
            [newCell.startButton setTitle:self.startTime forState:UIControlStateNormal];
            self.startTime = nil;
        }
        
    }else{
        
        if ([self.seleTimeStr isEqualToString:@"00:00"]) {
            
            CSAlert(@"最晚时间设定为23:59");
            
        }else{
            self.endTime  = self.seleTimeStr;
            [self compareTimeSuecc:index];
            
            if (!self.isBig) {
                self.isBig = YES;
                self.endTime = nil;
                CSAlert(@"开始营业时间不能比结束营业时间晚");
                
            }else{
                
                NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithDictionary:self.shopTimeAry[index.row]];
                [self.shopTimeAry removeObjectAtIndex:index.row];
                if (self.endTime == nil) {
                    [dic setObject:@"" forKey:@"close"];
                }else{
                    [dic setObject:self.endTime forKey:@"close"];
                }
                [self.shopTimeAry insertObject:dic atIndex:index.row];
                [newCell.endButton setTitle:self.endTime forState:UIControlStateNormal];
                self.endTime = nil;
                
            }
        }
    }
    
    
}
-(void)dateTimePickerValueChanged:(UIDatePicker *)picker{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"HH:mm"];
    NSString *timestamp_str = [outputFormatter stringFromDate:picker.date];
    self.seleTimeStr = timestamp_str;
}

-(void)compareTimeSuecc:(NSIndexPath *)index{
    
    int i = 0;
    int j = 0;
    int a = 0;
    int b = 0;
    BOOL isEndNull = NO;
    BOOL isStartNull = NO;

    
    if (self.startTime == nil) {
        NSMutableString *str1 = [[NSMutableString alloc]initWithString:self.shopTimeAry[index.row][@"open"]];
        if ([str1 isEqualToString:@""]) {
            isStartNull = YES;
        }else{
            
            NSString * hourStart = [str1 substringWithRange:NSMakeRange(0, 2)];
            NSString * minuteStart = [str1 substringWithRange:NSMakeRange(3, 2)];
            i = [hourStart intValue];
            j = [minuteStart intValue];
        }
       
        
    }else{
        NSMutableString *str1 = [[NSMutableString alloc]initWithString:self.startTime];
        NSString * hourStart = [str1 substringWithRange:NSMakeRange(0, 2)];
        NSString * minuteStart = [str1 substringWithRange:NSMakeRange(3, 2)];
        i = [hourStart intValue];
        j = [minuteStart intValue];
    }
    
    
    
    if (self.endTime == nil) {
        NSMutableString *str2 = [[NSMutableString alloc]initWithString:self.shopTimeAry[index.row][@"close"]];
        if ([str2 isEqualToString:@""]) {
            isEndNull = YES;
        }else{
            
            NSString * hourEnd = [str2 substringWithRange:NSMakeRange(0, 2)];
            NSString * minuteEnd = [str2 substringWithRange:NSMakeRange(3, 2)];
            a = [hourEnd intValue];
            b = [minuteEnd intValue];
        }
        
    }else{
        NSMutableString *str2 = [[NSMutableString alloc]initWithString:self.endTime];
        NSString * hourEnd = [str2 substringWithRange:NSMakeRange(0, 2)];
        NSString * minuteEnd = [str2 substringWithRange:NSMakeRange(3, 2)];
        a = [hourEnd intValue];
        b = [minuteEnd intValue];
    }
        
    
    if (!isStartNull && !isEndNull) {
        
        if (i > a) {
            
            self.isBig = NO;
            
        }else if (i == a && j > b){
            
            self.isBig = NO;
        }

    }
    
    
}




- (void)didClickRightButton:(UIButton *)sender{
    
    
    [self initJsonPrcClient:@"1"];
    
    NSLog(@"%@", self.shopTimeAry);
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];

    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    if (self.shopTimeAry.count == 0) {
        CSAlert(@"营业时间不能为空");
        return;
    }
    NSString * updataNum = @"";
    
    for (int i = 0; i<self.shopTimeAry.count; i++) {
        NSDictionary *dic = [self.shopTimeAry objectAtIndex:i];
        
        if ([dic[@"open"] length]==0 || [dic[@"close"] length]==0) {
            CSAlert(@"请将信息填写完整");
            return;
        }
        
        if (i > 0) {
            updataNum = [NSString stringWithFormat:@"%@;",updataNum];
        }
        updataNum = [NSString stringWithFormat:@"%@%@,%@", updataNum, dic[@"open"], dic[@"close"]];
        
    }
    
    
    
//    else if (self.shopTimeAry.count == 1) {
//        NSDictionary *dic = [self.shopTimeAry objectAtIndex:0];
//        NSString * updataNum = [NSString stringWithFormat:@"%@,%@", dic[@"open"], dic[@"close"]];
//        [params setObject:updataNum forKey:@"updateValue"];
//        
//    }else if (self.shopTimeAry.count == 2){
//        NSString * updataNum = [NSString stringWithFormat:@"%@,%@;%@,%@", self.shopTimeAry[0][@"open"],self.shopTimeAry[0][@"close"],self.shopTimeAry[1][@"open"],self.shopTimeAry[1][@"close"]];
//        [params setObject:updataNum forKey:@"updateValue"];
//        
//    }else if (self.shopTimeAry.count == 3){
//        NSString * updataNum = [NSString stringWithFormat:@"%@,%@;%@,%@;%@,%@", self.shopTimeAry[0][@"open"],self.shopTimeAry[0][@"close"],self.shopTimeAry[1][@"open"],self.shopTimeAry[1][@"close"],self.shopTimeAry[2][@"open"],self.shopTimeAry[2][@"close"]];
//
//    }
    [params setObject:updataNum forKey:@"updateValue"];
    
    [params setObject:@"businessHours" forKey:@"updateKey"];
    
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];

        if (code.intValue == 50000) {

            [self.navigationController popViewControllerAnimated:YES];
            [[NSNotificationCenter defaultCenter] postNotificationName:@"shopHourFinish" object:nil];
            
        }else {
            CSAlert(@"修改失败");
            
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];
    
    
}


#pragma mark ---- 添加
- (void)addButClick:(UIButton *)sender
{
    NSLog(@"添加");
    if (self.shopTimeAry.count < 3) {
        
        NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithCapacity:0];
        [dic setObject:@"" forKey:@"open"];
        [dic setObject:@"" forKey:@"close"];
        [self.shopTimeAry addObject:dic];
        [self.baseView reloadData];
        
    }else{
        UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"营业时间段不能超过3个" delegate:nil cancelButtonTitle:nil otherButtonTitles:nil, nil];
        [alertView show];
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [alertView dismissWithClickedButtonIndex:0 animated:YES];
        });
    }
   
    
    
}


@end
