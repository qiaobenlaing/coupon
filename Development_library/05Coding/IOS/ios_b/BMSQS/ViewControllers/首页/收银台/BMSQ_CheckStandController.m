//
//  BMSQ_CheckStandController.m
//  BMSQS
//
//  Created by djx on 15/7/16.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CheckStandController.h"

@interface BMSQ_CheckStandController ()
{
    UITableView* m_tableView;
    UITextField* tx_money;
}

@end

@implementation BMSQ_CheckStandController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"收银台"];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT+APP_TABBAR_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    tx_money = [[UITextField alloc]initWithFrame:CGRectMake(0, 15, APP_VIEW_WIDTH, 40)];
    tx_money.leftViewMode = UITextFieldViewModeAlways;
    tx_money.rightViewMode = UITextFieldViewModeAlways;
    tx_money.backgroundColor = [UIColor whiteColor];
    UILabel* lb_msg = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 40)];
    [lb_msg setText:@"消费金额"];
    lb_msg.textAlignment = NSTextAlignmentCenter;
    [lb_msg setFont:[UIFont systemFontOfSize:16]];
    [lb_msg setTextColor:UICOLOR(51, 51, 51, 1.0)];
    tx_money.leftView = lb_msg;
    
    UILabel* lb_unit = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 50, 40)];
    [lb_unit setText:@"元"];
    lb_unit.textAlignment = NSTextAlignmentCenter;
    [lb_unit setFont:[UIFont systemFontOfSize:16]];
    [lb_unit setTextColor:UICOLOR(51, 51, 51, 1.0)];
    tx_money.rightView = lb_unit;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 125;
    }
    else
    {
        return 450;
    }
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(indexPath.section == 0)
    {
        static NSString* cellIdentify = @"cellIdentify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.backgroundColor= UICOLOR(236, 235, 243, 1.0);
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            UIButton* btnSure = [[UIButton alloc]initWithFrame:CGRectMake(15, 70, APP_VIEW_WIDTH-30, 40)];
            [btnSure setBackgroundColor:UICOLOR(182, 0, 12, 1.0)];
            [btnSure setTitle:@"确定" forState:UIControlStateNormal];
            [btnSure.layer setCornerRadius:4];
            //[btnSure.layer setBorderWidth:1.0];
            [btnSure addTarget:self action:@selector(btnSureClick) forControlEvents:UIControlEventTouchUpInside];
            [cell addSubview:btnSure];
        }
        
        [cell addSubview:tx_money];
        return cell;
    }
    else
    {
        static NSString* cellIdentify = @"cellIdentify";
        UITableViewCell* cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        }
        return cell;
    }
}

- (void)btnSureClick
{
    
}

@end
