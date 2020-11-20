//
//  OpenNewClassViewController.m
//  BMSQS
//
//  Created by gh on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassViewController.h"
#import "OpenNewClassViewController2.h"
#import "UIImageView+WebCache.h"
#import "OpenClassDatePickerView.h"
#import "OpenNewClassSyllabusViewController.h"

#import "OpenClassUtil.h"
#import "OpenNewClassCell.h"

@interface OpenNewClassViewController () <UITableViewDataSource, UITableViewDelegate, OpenClassDateDelegate, OpenNewClassDelegate>

@property (nonatomic, strong)OpenClassDatePickerView *datePickerView ;
@property (nonatomic, strong)NSMutableArray *dataAry;
@property (nonatomic, strong)UITableView *tableView;


@property (nonatomic, assign)int cellTextTag;

@property (nonatomic, assign)NSString *learnStartDate;
@property (nonatomic, assign)NSString *learnEndDate;

@property (nonatomic, strong)NSMutableArray *syllabusArray;//课程表数组

@property (nonatomic, strong)UIView *cellSubView;

@end

@implementation OpenNewClassViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    
    [self setViewUp];
    
    
}

- (void)setViewUp {
    
    [self setNavBackItem];
    [self setNavTitle:@"开班"];
    [self setRightBtn];
    
    self.syllabusArray = [[NSMutableArray alloc] init];
    
    self.dataAry = [[NSMutableArray alloc] initWithObjects:@"所开班级", @"学习开始时间", @"学习结束时间", @"上课时间", @"适合年龄段", nil];
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.tableView.backgroundColor = UICOLOR(240, 238, 245, 1);
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;

    
    [self.view addSubview:self.tableView];
    
    
    self.datePickerView = [[OpenClassDatePickerView alloc] initWithFrame:CGRectMake(20, APP_VIEW_WIDTH/3, APP_VIEW_WIDTH-40, 216+40)];
    self.datePickerView.delegate = self;
    self.datePickerView.hidden = YES;
    [self.view addSubview:self.datePickerView];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(syllabusFinish:)
                                                 name:@"OpenNewClassSyllabusFinish"
                                               object:nil];
    
    self.cellSubView = [[UIView alloc] initWithFrame:CGRectMake(100, 0, APP_VIEW_WIDTH-100, 44)];
    self.cellSubView.backgroundColor = [UIColor clearColor];

    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == 3) {
        if (self.syllabusArray.count > 0) {
            return 44 * self.syllabusArray.count;
        }
        
    }
    
    return 44;
}

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
    
    if (indexPath.row == 1||indexPath.row == 2 || indexPath.row == 3) {
        cell.rightLabel.hidden = NO;
        cell.tx_content.hidden = YES;
    }else {
        cell.rightLabel.hidden = YES;
        cell.tx_content.hidden = NO;
    }
    
    if (indexPath.row == 0) {
        cell.tx_content.placeholder = @"请输入所开班级";
    }
    
    
    if (indexPath.row == 1) {
        if (self.learnStartDate.length > 0) {
            cell.rightLabel.text = self.learnStartDate;
        }else {
            cell.rightLabel.text = @"请选择开始时间";
        }
        
    }else if (indexPath.row == 2) {
        if (self.learnEndDate.length > 0) {
            cell.rightLabel.text = self.learnEndDate;
        }else {
            cell.rightLabel.text = @"请选择结束时间";
        }
        
    }else if (indexPath.row == 3) {
        cell.rightIv.hidden = NO;
        cell.rightLabel.text = @"请选择上课时间";
        if (self.syllabusArray.count > 0) {
            cell.lineView.frame = CGRectMake(0, self.syllabusArray.count*44-1, APP_VIEW_WIDTH, 1);
            if (self.cellSubView!=nil) {
                [self.cellSubView removeFromSuperview];
            }
            self.cellSubView = [[UIView alloc] initWithFrame:CGRectMake(100, 0, APP_VIEW_WIDTH-110, self.syllabusArray.count*44)];
            [cell.contentView addSubview:self.cellSubView];
            
            for (int i=0; i<self.syllabusArray.count; i++) {
                NSDictionary *dic = [self.syllabusArray objectAtIndex:i];
                NSString *weekString = [dic objectForKey:@"weekName"];
                if ([weekString isEqual:@"1"]) {
                    weekString = @"周一";
                }else if ([weekString isEqual:@"2"]) {
                    weekString = @"周二";
                }else if ([weekString isEqual:@"3"]) {
                    weekString = @"周三";
                }else if ([weekString isEqual:@"4"]) {
                    weekString = @"周四";
                }else if ([weekString isEqual:@"5"]) {
                    weekString = @"周五";
                }else if ([weekString isEqual:@"6"]) {
                    weekString = @"周六";
                }else if ([weekString isEqual:@"7"]) {
                    weekString = @"周日";
                }
                
                NSString *textString = [NSString stringWithFormat:@"%@  %@-%@", weekString, [dic objectForKey:@"startTime"], [dic objectForKey:@"endTime"]];
                
                [OpenClassUtil openClassSetLabel:CGRectMake(0, 44*i, APP_VIEW_WIDTH-110, 44) text:textString font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.cellSubView];
                
            }
            
        }else {
            cell.lineView.frame = CGRectMake(0, 44-1, APP_VIEW_WIDTH, 1);
        }
        
        
        
        
        
    }else {
        cell.tx_content.placeholder = @"请输适合年龄";
        cell.rightIv.hidden = YES;
    }
    
    
    
    
    return cell;
    
    
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 1 || indexPath.row == 2) {
        
        if (self.cellTextTag) {
            UITextField *textfield = [self.tableView viewWithTag:self.cellTextTag];
            [textfield resignFirstResponder];
            
        }
        
        [self.datePickerView showDateView:(int)indexPath.row];
        
    }
    
    
    if (indexPath.row == 3) {
        
        OpenNewClassSyllabusViewController *VC= [[OpenNewClassSyllabusViewController alloc] init];
        if (self.syllabusArray.count>0) {
            VC.dataArray = [NSMutableArray arrayWithArray:self.syllabusArray];
        }else {
            VC.dataArray = [[NSMutableArray alloc] init];
        }
        
        [self.navigationController pushViewController:VC animated:YES];
        
    }
    
}

- (void)getSelectDate:(NSString *)date row:(int)row {
    
    if (row == 1){
        self.learnStartDate = date;
        
    }else if ( row == 2) {
        self.learnEndDate = date;
    }
    
     NSIndexPath *indexPath=[NSIndexPath indexPathForRow:row inSection:0];
    [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:indexPath,nil] withRowAnimation:UITableViewRowAnimationNone];

}



#pragma mark - button 点击事件
- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 1000) { //下一步
        
        UITextField *classNameTF = [self.view viewWithTag:2000];
        UITextField *learnMemoTF = [self.view viewWithTag:2004];
        
        NSString *className = classNameTF.text;
        NSString *learnMemo = learnMemoTF.text;
        if (className.length == 0) {
            CSAlert(@"请输入班级名称");
            return;
        }
        if (learnMemo.length == 0) {
            CSAlert(@"请输入适合年龄段");
            return;
        }
        if (self.learnStartDate.length == 0) {
            CSAlert(@"请输入学习开始时间");
            return;
        }
        if (self.learnEndDate.length == 0) {
            CSAlert(@"请输入学习结束时间");
            return;
        }
        if (self.syllabusArray.count == 0) {
            CSAlert(@"请选择上课时间");
            return;
        }
        
        OpenNewClassViewController2 *vc = [[OpenNewClassViewController2 alloc] init];
        
        vc.requestDic = [[NSMutableDictionary alloc] init];
        [vc.requestDic setObject:className forKey:@"className"]; //所开班级
        [vc.requestDic setObject:learnMemo forKey:@"learnMemo"]; //适合描述
        [vc.requestDic setObject:self.learnStartDate forKey:@"learnStartDate"]; //学习结束时间
        [vc.requestDic setObject:self.learnEndDate forKey:@"learnEndDate"]; // 学习开始时间
        
        
        
        [vc.requestDic setObject:[OpenClassUtil jsonStringWithArray:self.syllabusArray] forKey:@"classWeekInfo"]; // 上课时间
        
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if (button.tag == 1002) { //选择时间
        
        
        
    }else if (button.tag == 1005) { //确定按钮
        //        [self btnDatePicker];
//        [self.datePickerView reloadView];
        
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


- (void)OpenNewCellTag:(UITextField *)textField {
    
    self.cellTextTag = (int)textField.tag;
    
    [self.datePickerView disMiss];
    
}


- (void)syllabusFinish:(NSNotification *)notification {
    NSArray *array = [notification.userInfo objectForKey:@"syllabusData"];
    
    self.syllabusArray = [NSMutableArray arrayWithArray:array];

    
    [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:[NSIndexPath indexPathForRow:3 inSection:0], nil] withRowAnimation:UITableViewRowAnimationNone];
    
    
}





@end
