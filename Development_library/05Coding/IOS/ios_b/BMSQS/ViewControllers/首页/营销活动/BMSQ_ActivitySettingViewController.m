//
//  BMSQ_ActivitySettingViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivitySettingViewController.h"
#import "BMSQ_ActivitySettingViewController1.h"
#import "DateInputTableViewCell.h"
#import "StringInputTableViewCell.h"


@interface BMSQ_ActivitySettingViewController ()<UITableViewDataSource,UITableViewDelegate,DateInputTableViewCellDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIButton *nextButton;
@property (weak, nonatomic) UITextField *activityNameTextField;
@property (weak, nonatomic) UITextField *activityAddTextField;
@property (nonatomic ,strong) NSDate *startDate;                 // 开始时间
@property (nonatomic ,strong) NSDate *endDate;                   // 结束时间

@end

@implementation BMSQ_ActivitySettingViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"营销活动设置"];
    [self setNavBackItem];
    
    NSDate *date = [NSDate date];
    
    self.startDate = date;
    self.endDate = date;

    
    _nextButton.layer.cornerRadius = 3.0;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 4;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 1 ||indexPath.row == 2) {
        static NSString *cellID0 = @"UITableViewCell0";
        DateInputTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID0];
        
        if (!cell) {
            cell = [[DateInputTableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID0];
        }
        cell.delegate = self;
        cell.tag = indexPath.row;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (indexPath.row == 1) {
            cell.textLabel.text = @"开始时间";
        }else{
            cell.textLabel.text = @"结束时间";
        }
        
        return cell;
        
    }
    if (indexPath.row == 0 ||indexPath.row == 3) {
        static NSString *cellID = @"reuseIdentifier";
        StringInputTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID];
        if (!cell) {
            cell = [[StringInputTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellID];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.tag = indexPath.row;
        
        if (indexPath.row == 0) {
            cell.textLabel.text = @"活动主题";
            cell.textField.placeholder = @"请输入活动主题";
            _activityNameTextField = cell.textField;
        }else{
            cell.textLabel.text = @"活动地点";
            _activityAddTextField = cell.textField;
            cell.textField.placeholder = @"请输入活动地点";

        }
        
        return cell;
    }
    
    static NSString *cellID1 = @"reuseIdentifier1";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellID1];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellID1];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;

    
}


- (void)tableViewCell:(DateInputTableViewCell *)cell didEndEditingWithDate:(NSDate *)value{
    
    if (cell.tag == 1) {
        _startDate = value;
    }else{
        _endDate   = value;
    }
}


- (void)tableViewCell:(StringInputTableViewCell *)cell didEndEditingWithString:(NSString *)value{
    
    
}


- (IBAction)didClickNextButton:(id)sender {


    if (!_activityNameTextField.text || _activityAddTextField.text.length == 0) {
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:nil message:@"活动主题,地点不能为空" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        [alterView show];
        
        return;
    }
    if (!_activityAddTextField.text || _activityAddTextField.text.length == 0) {
        return;
    }
    
    if ([_startDate compare:_endDate] == -1) {
        NSLog(@"YES");
    }else{
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:nil message:@"开始时间>=结束时间" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"OK", nil];
        [alterView show];
        return;
    }
    
    NSDateFormatter *dataFormatter = [[NSDateFormatter alloc] init];
    dataFormatter.dateFormat = @"YYYY-MM-DD hh:mm:ss";
    
    
    //1 活动主题     activityName 活动名称
    //2 活动开始时间 startTime
    //3 活动结束时间 endTime
    //4 活动地点    activityLocation

    NSDictionary *dataDic = @{@"activityName":_activityNameTextField.text,
                              @"activityLocation":_activityAddTextField.text,
                              @"startTime":[dataFormatter stringFromDate:_startDate],
                              @"endTime":[dataFormatter stringFromDate:_endDate]};
    
    BMSQ_ActivitySettingViewController1 *pushVC = [[BMSQ_ActivitySettingViewController1 alloc] initWithNibName:@"BMSQ_ActivitySettingViewController1" bundle:nil];
    pushVC.dataDic = dataDic;
    [self.navigationController pushViewController:pushVC animated:YES];

}

@end
