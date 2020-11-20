//
//  BMSQ_addCourseViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/29.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_addCourseViewController.h"


#import "BMSQ_seleDateView.h"
#import "BMSQ_seleTimeView.h"

@interface BMSQ_addCourseViewController ()<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,BMSQ_seleTimeViewDelegate,BMSQ_seleDateViewDelegate>

@property (nonatomic, strong)UITableView *classTable;

@property (nonatomic, strong)BMSQ_seleDateView *seleDateView;
@property (nonatomic, strong)BMSQ_seleTimeView *seleTimeView;

@property (nonatomic, strong)NSArray *iconArray;

@property (nonatomic, strong)NSString *startDate;
@property (nonatomic, strong)NSString *endDate;



@property (nonatomic, strong)NSMutableArray *TimeStrS;
@property (nonatomic, strong)NSMutableArray *learnTimeArray;


@end



@implementation BMSQ_addCourseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"添加/编辑课程详情"];
    self.iconArray =@[@"course_name",@"course_add",@"course_clock",@"course_address",@"course_set",@"course_spx",@"course_startTime",@"course_endTime"];
    self.TimeStrS = [[NSMutableArray alloc]initWithObjects:@"", nil];
    self.learnTimeArray = [[NSMutableArray alloc]init];
    
    UIButton *addBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 20, 50, 40)];
    addBtn.backgroundColor = [UIColor clearColor];
    [addBtn setTitle:@"保存" forState:UIControlStateNormal];
    [addBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    addBtn.titleLabel.font = [UIFont boldSystemFontOfSize:13];
    [addBtn addTarget:self action:@selector(editUserClassInfo) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:addBtn];
    
    self.classTable = [[UITableView alloc]initWithFrame:CGRectMake(0,APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.classTable.backgroundColor = [UIColor whiteColor];
    self.classTable.delegate = self;
    self.classTable.dataSource = self;
    self.classTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.classTable];
    
    self.seleDateView = [[BMSQ_seleDateView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.seleDateView.delegate = self;
    self.seleDateView.hidden = YES;
    [self.view addSubview:self.seleDateView];
    
    
    self.seleTimeView = [[BMSQ_seleTimeView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    self.seleTimeView.delegate = self;
    self.seleTimeView.hidden = YES;
    [self.view addSubview:self.seleTimeView];
  

}

-(NSString *)getWeekId:(NSString *)weekStr{
    if ([weekStr isEqualToString:@"周一"]) {
        return @"1";
    }else if ([weekStr isEqualToString:@"周二"]){
        return @"2";
    }else if ([weekStr isEqualToString:@"周三"]){
        return @"3";
    }else if ([weekStr isEqualToString:@"周四"]){
        return @"4";
    }else if ([weekStr isEqualToString:@"周五"]){
        return @"5";
    }else if ([weekStr isEqualToString:@"周六"]){
        return @"6";
    }else if ([weekStr isEqualToString:@"周七"]){
        return @"7";
    }
    
    
    return @"1";
}

-(void)seleWeek:(NSString *)weekStr startTime:(NSString *)startTime endTime:(NSString *)endTime isSubmit:(BOOL)isSubmit{
  
    
    if (isSubmit) {
        
        
        NSMutableDictionary *dic = [[NSMutableDictionary alloc]init];
        [dic setObject:[self getWeekId:weekStr] forKey:@"weekDay"];
        [dic setObject:startTime forKey:@"startTime"];
        [dic setObject:endTime forKey:@"endTime"];
        [self.learnTimeArray addObject:dic];
        
        
        NSString *str = [NSString stringWithFormat:@"%@  %@-%@",weekStr,startTime,endTime];
        [self.TimeStrS insertObject:str atIndex:self.TimeStrS.count-1];
        
        
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:1];
        [self.classTable reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        
        
    }
    
    
    
    self.seleTimeView.hidden = YES;
    
}


-(void)seleDate:(NSString *)dateStr isSubmit:(BOOL)isSubmit status:(BOOL)status{
    if (isSubmit) {
        if (status) {
            self.startDate = dateStr;
        }else{
            self.endDate = dateStr;
        }
        NSIndexSet *indexSet = [[NSIndexSet alloc]initWithIndex:2];
        [self.classTable reloadSections:indexSet withRowAnimation:UITableViewRowAnimationNone];
        

    }
  
    
    self.seleDateView.hidden = YES;

}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 3;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section ==0) {
        return 1;
    }else if (section ==1){
        return self.TimeStrS.count;
    }
    return 6;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 48;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"classCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 47, APP_VIEW_WIDTH, 1)];
            lineView.backgroundColor = APP_VIEW_BACKCOLOR;
            [cell addSubview:lineView];
            
            UIImageView *iconImage = [[UIImageView alloc]initWithFrame:CGRectMake(15, 10, 21, 20)];
            iconImage.tag = 200;
            [cell addSubview:iconImage];
            
            UITextField *textField = [[UITextField alloc]initWithFrame:CGRectMake(50, 0, APP_VIEW_WIDTH-60, 46)];
            textField.backgroundColor = [UIColor whiteColor];
            textField.tag = 100;
            textField.delegate = self;
            textField.returnKeyType =UIReturnKeyDone;
            textField.font = [UIFont systemFontOfSize:13.f];
            [cell addSubview:textField];
            
            UISwitch *switchView = [[UISwitch alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-70, 10,10, 10)];
            switchView.onTintColor = APP_NAVCOLOR;
            switchView.transform = CGAffineTransformMakeScale(0.75, 0.75);
            switchView.hidden = YES;
            switchView.tag = 300;
            [cell addSubview:switchView];
            
            
            UIButton *bgButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-70, 46)];
            bgButton.backgroundColor = [UIColor clearColor];
            [cell addSubview:bgButton];
            bgButton.hidden = YES;
            bgButton.tag = 400;
            [bgButton addTarget:self action:@selector(clickCourseButton:) forControlEvents:UIControlEventTouchUpInside];
            
            
    }
    
    UIImageView *iconImage = [cell viewWithTag:200];
    
    UITextField *textField = [cell viewWithTag:100];
    textField.enabled = YES;
    
    
    UISwitch *switchView = [cell viewWithTag:300];
    switchView.hidden = YES;
    
    
    UIButton *bgButton = [cell viewWithTag:400];
    bgButton.hidden = YES;
//    bgButton.tag = indexPath.row;
    
    
    if (indexPath.section ==0) {
        
        
        
        [iconImage setImage:[UIImage imageNamed:[self.iconArray objectAtIndex:indexPath.row]]];
            textField.placeholder = @"点击输入课程名称";
        
        
        
        
    }
    else if (indexPath.section ==1){
        
        
        
            [iconImage setImage:[UIImage imageNamed:[self.iconArray objectAtIndex:1]]];

        
        
        bgButton.tag = [[NSString stringWithFormat:@"%d",(int)indexPath.row+100]intValue];
        bgButton.hidden = NO;
        textField.enabled = NO;
        
        textField.placeholder = @"点击添加更多上课时间";
        textField.text = [self.TimeStrS objectAtIndex:indexPath.row];
    }
    else {
        [iconImage setImage:[UIImage imageNamed:[self.iconArray objectAtIndex:indexPath.row+2]]];
        
        if (indexPath.row ==0){
            textField.text = @"课前提醒已开启";
            textField.enabled = NO;
            switchView.hidden = NO;
            
        }else if (indexPath.row ==1){
            textField.placeholder = @"点击输入地点,备注";
            textField.text =@"";

            
            
        }else if (indexPath.row ==2){
            textField.text = @"进阶设置";
            textField.enabled = NO;
            switchView.hidden = NO;
            
            
        }else if (indexPath.row ==3){
            textField.text = @"每隔一周";
            bgButton.hidden = NO;
            
        }else if (indexPath.row ==4){
            textField.placeholder = @"点击设置开始";
            textField.text = self.startDate;
            textField.enabled = NO;
            bgButton.hidden = NO;
            bgButton.tag = indexPath.row;
            
            
        }else if (indexPath.row ==5){
            textField.placeholder = @"点击设置结束时间";
            textField.text = self.endDate;
            textField.enabled = NO;
            bgButton.hidden = NO;
            bgButton.tag =indexPath.row;
            
        }
    }
    
    
    return cell;
    
}
#pragma mark 添加/修改课程
-(void)editUserClassInfo{
    [self initJsonPrcClient:@"2"];
    
    
    NSMutableDictionary* updateData = [[NSMutableDictionary alloc]init];
    [updateData setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:0 inSection:0];
    UITableViewCell *cell=[self.classTable cellForRowAtIndexPath:indexPath];
    UITextField *textField = (UITextField *)[cell.contentView viewWithTag:100];
    [updateData setObject:@"英语" forKey:@"courseName"];
    
  indexPath=[NSIndexPath indexPathForRow:1 inSection:2];
    cell=[self.classTable cellForRowAtIndexPath:indexPath];
   textField = (UITextField *)[cell.contentView viewWithTag:100];
    [updateData setObject:@"杭州" forKey:@"courseAddr"];
    
    
    
    [updateData setObject:self.startDate forKey:@"courseStartDate"];
    [updateData setObject:self.endDate forKey:@"courseEndDate"];
    
    [updateData setObject:@"1" forKey:@"isRemindClass"];
    [updateData setObject:@"1" forKey:@"weekStep"];

  
    [updateData setObject:self.learnTimeArray forKey:@"learnTime"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[self jsonStringWithDictionary:updateData] forKey:@"updateData"];
    [params setObject:@"" forKey:@"tableCode"];

    
    NSString* vcode = [gloabFunction getSign:@"editUserClassInfo" strParams:@""];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:@""];
    
    [self.jsonPrcClient invokeMethod:@"editUserClassInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSDictionary *dic = responseObject;
        
        int code =[[dic objectForKey:@"code"]intValue];
        
        if (code == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"课程添加成功"];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"loadCourse" object:nil];
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
        else if (code == 20000){
            [SVProgressHUD showErrorWithStatus:@"课程添加失败"];
        }
        else {
            [SVProgressHUD showErrorWithStatus:@"课程添加失败"];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        [weakSelf.navigationController popViewControllerAnimated:YES];
    }];
}


-(void)clickCourseButton:(UIButton *)button{
    
    NSLog(@"%d",(int)button.tag);
    if (button.tag >=100) {   //上课时间
        self.seleTimeView.hidden = NO;
    }else if (button.tag == 4){  //开始日期
        self.seleDateView.remakLabel.text = @"设置开始时间: ";
        self.seleDateView.hidden = NO;
        self.seleDateView.status = YES;  //yes 开始时间

    }else if (button.tag == 5 ){  //结束日期
        self.seleDateView.remakLabel.text = @"设置结束时间: ";
        self.seleDateView.hidden = NO;
        self.seleDateView.status = NO;  //no 结束时间

     }
    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (NSString *)jsonStringWithDictionary:(NSMutableDictionary *)dic {
    NSString *jsonString ;
    if (dic.count > 0) {
        NSError *err;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic
                                                           options:NSJSONWritingPrettyPrinted
                                                             error:&err];
        if (err) {
            return nil;
        }
        
        jsonString = [[NSString alloc] initWithData:jsonData
                                           encoding:NSUTF8StringEncoding];
        
    }else {
        jsonString = @"";
    }
    
    return jsonString;
}
@end
