//
//  BMSQ_MyBankDetailController.m
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyBankDetailController.h"
#import "BMSQ_MyBankDetailCell.h"

@interface BMSQ_MyBankDetailController ()
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
    NSDictionary* m_dicBankInfo;
}

@end

@implementation BMSQ_MyBankDetailController

@synthesize bankAccountCode;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}


- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的银行卡"];
    
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT - 10)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];

     m_dicBankInfo = [[NSDictionary alloc]init];
    [self getBankAccountInfo];
   
}

- (void)getBankAccountInfo
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:bankAccountCode forKey:@"bankAccountCode"];
    NSString* vcode = [gloabFunction getSign:@"getBankAccountInfo" strParams:bankAccountCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getBankAccountInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        m_dicBankInfo = [[NSDictionary alloc]initWithDictionary:responseObject];
        [ProgressManage closeProgress];

        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 1)
    {
        return 3;
    }
    return 3;
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.section == 0)
    {
        static NSString *cellIdentifier = @"Cell";
        BMSQ_MyBankDetailCell *cell = (BMSQ_MyBankDetailCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[BMSQ_MyBankDetailCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            //cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        if (indexPath.row == 0)
        {
            
            [cell setCellValue:@"iv_bankInfo" title:@"银行卡末四位" content:[gloabFunction changeNullToBlank:[m_dicBankInfo objectForKey:@"accountNbrLast4"]]];
        }
        else if(indexPath.row == 1)
        {
            [cell setCellValue:@"iv_bankPoint" title:@"积分" content:[gloabFunction changeNullToBlank:[m_dicBankInfo objectForKey:@"points"]]];
        }
        else
        {
            [cell setCellValue:@"iv_limit" title:@"额度" content:@""];
        }
        return cell;
    }
    else
    {
        static NSString *cellIdentifier = @"Cell";
        BMSQ_MyBankDetailCell *cell = (BMSQ_MyBankDetailCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[BMSQ_MyBankDetailCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            //cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        
        if (indexPath.row == 0)
        {
            
            [cell setCellValue:@"iv_payMoney" title:@"本期还款金额" content:@""];
        }
        else if(indexPath.row == 1)
        {
            [cell setCellValue:@"" title:@"最低还款金额" content:@""];
        }
        else
        {
            [cell setCellValue:@"" title:@"还款日期" content:@""];
        }
        
        return cell;
    }


}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
//    if (indexPath.section == 0)
//    {
//        BMSQ_MyBankDetailController* detailCtrl = [[BMSQ_MyBankDetailController alloc]init];
//        detailCtrl.bankAccountCode = [[m_dataSource objectAtIndex:indexPath.row] objectForKey:@"bankAccountCode"];
//        [self.navigationController pushViewController:detailCtrl animated:YES];
//    }
}

@end
