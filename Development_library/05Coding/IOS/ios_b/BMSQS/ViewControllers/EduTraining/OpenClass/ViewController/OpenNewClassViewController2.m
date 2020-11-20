//
//  OpenNewClassViewController2.m
//  BMSQS
//
//  Created by gh on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassViewController2.h"
#import "OpenNewClassViewController3.h"
#import "OpenClassUtil.h"
#import "OpenNewClassCell.h"
#import "OpenClassDatePickerView.h"
#import "OpenNewClassTeacherViewController.h"

@interface OpenNewClassViewController2 ()<UITableViewDataSource, UITableViewDelegate, OpenClassDateDelegate, OpenNewClassDelegate>

@property (nonatomic, strong)NSMutableArray *dataAry;
@property (nonatomic, strong)UITableView *tableView;
@property (nonatomic,strong)OpenClassDatePickerView *datePickerView;
@property (nonatomic, assign)int cellTextTag;



@property (nonatomic, strong)NSString *teacherName;
@property (nonatomic, strong)NSString *teacherCode;
@property (nonatomic, strong)NSString *signStartDate;
@property (nonatomic, strong)NSString *signEndDate;

@end

@implementation OpenNewClassViewController2

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    self.teacherName = @"";
    self.teacherCode = @"";
    self.signStartDate = @"";
    self.signEndDate = @"";
    
    [self setNavBackItem];
    [self setNavTitle:@"开班"];
    [self setRightBtn];
    
    self.dataAry = [[NSMutableArray alloc] initWithObjects:@"报名费用", @"所学课时", @"任课老师", @"报名开始时间", @"报名结束时间", nil];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.backgroundColor = UICOLOR(240, 238, 245, 1);
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    [self.view addSubview:self.tableView];
    
    self.datePickerView = [[OpenClassDatePickerView alloc] initWithFrame:CGRectMake(20, APP_VIEW_WIDTH/3, APP_VIEW_WIDTH-40, APP_VIEW_WIDTH/2+40)];
    self.datePickerView.delegate = self;
    self.datePickerView.hidden = YES;
    [self.view addSubview:self.datePickerView];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(selectTeacher:)
                                                 name:@"selectTeacher"
                                               object:nil];
    
    
}

#pragma mark - UITableView delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 5;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *identifier = @"OpenNewClassCell";
    OpenNewClassCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[OpenNewClassCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.delegate = self;
    [cell setCellValue:[self.dataAry objectAtIndex:indexPath.row]];
    
    
    cell.tx_content.tag = 2000+indexPath.row;
    
    if (indexPath.row == 2) {
        cell.rightLabel.text = self.teacherName;
    }
    
    
    if (indexPath.row == 0||indexPath.row == 1) {
        cell.rightLabel.hidden = YES;
        cell.tx_content.hidden = NO;
    }else {
        cell.rightLabel.hidden = NO;
        cell.tx_content.hidden = YES;
        
    }
    
    if (indexPath.row == 3) {
        cell.rightLabel.text = self.signStartDate;
    }else if (indexPath.row == 4) {
        cell.rightLabel.text = self.signEndDate;
    }
    
    if (indexPath.row == 2 ||indexPath.row == 3||indexPath.row == 4) {
        cell.rightIv.hidden = NO;
    }else {
        cell.rightIv.hidden = YES;
    }
    
    
    return cell;
    
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 3 || indexPath.row == 4) {
        
        if (self.cellTextTag) {
            UITextField *textfield = [self.tableView viewWithTag:self.cellTextTag];
            [textfield resignFirstResponder];
            
        }
        
        [self.datePickerView showDateView:(int)indexPath.row];
        
    }
    
    
    if (indexPath.row == 2) {
        OpenNewClassTeacherViewController *vc = [[OpenNewClassTeacherViewController alloc] init];
        [self.navigationController pushViewController:vc animated:YES];
        
        
    }
    
}


#pragma mark - button 点击事件
- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 1000) { //下一步
        UITextField *learnFeeTF = [self.view viewWithTag:2000];
        UITextField *learnNumTF = [self.view viewWithTag:2001];
        
        NSString *learnFee = learnFeeTF.text;
        NSString *learnNum = learnNumTF.text;
        
        if (learnFee.length == 0) {
            CSAlert(@"请填写报名费用");
            return;
        }
        if (learnNum.length == 0) {
            CSAlert(@"请填写所学课时");
            return;
        }
        if (self.signStartDate.length == 0) {
            CSAlert(@"请选择开始时间");
            return;
        }
        if (self.signEndDate.length == 0) {
            CSAlert(@"请选择结束时间");
            return;
        }
        if (self.teacherCode.length == 0) {
            CSAlert(@"请选择任课老师");
            return;
        }
        
        OpenNewClassViewController3 *vc = [[OpenNewClassViewController3 alloc] init];
        vc.requestDic = [NSMutableDictionary dictionaryWithDictionary:self.requestDic];
        
        [vc.requestDic setObject:learnFee forKey:@"learnFee"];///报名费用
        [vc.requestDic setObject:learnNum forKey:@"learnNum"];//所学课时
        [vc.requestDic setObject:self.signStartDate forKey:@"signStartDate"]; //报名开始日期
        [vc.requestDic setObject:self.signEndDate forKey:@"signEndDate"]; //报名结束日期
        [vc.requestDic setObject:self.teacherCode forKey:@"teacherCode"]; // 老师编码
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }
    
    
}

//  下一步
- (void)setRightBtn {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH-74, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + APP_STATUSBAR_HEIGHT, 64, APP_NAV_LEFT_ITEM_HEIGHT);
    button.tag = 1000;
    button.backgroundColor = [UIColor clearColor];
    [button setTitle:@"下一步" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [button.titleLabel setTextAlignment:NSTextAlignmentRight];
    [self setRight:button];
    
    
}

//打开输入框 隐藏时间选择器
- (void)OpenNewCellTag:(UITextField *)textField {
    
    self.cellTextTag = (int)textField.tag;
    
    [self.datePickerView disMiss];
    
}

//选择时间
- (void)getSelectDate:(NSString *)date row:(int)row {
    
    if (row == 3){
        self.signStartDate = date;
        
    }else if ( row == 4) {
        self.signEndDate = date;
    }
    
    NSIndexPath *indexPath=[NSIndexPath indexPathForRow:row inSection:0];
    [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];
    
}

//选择老师
- (void)selectTeacher:(NSNotification *)notification {
//    teacherData
    NSDictionary *dic = [notification.userInfo objectForKey:@"teacherData"];
    
    self.teacherCode =  [NSString stringWithFormat:@"%@",[dic objectForKey:@"teacherCode"]];
    self.teacherName = [dic objectForKey:@"teacherName"];
    
    [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:[NSIndexPath indexPathForRow:2 inSection:0], nil] withRowAnimation:UITableViewRowAnimationNone];
    
    
}


@end
