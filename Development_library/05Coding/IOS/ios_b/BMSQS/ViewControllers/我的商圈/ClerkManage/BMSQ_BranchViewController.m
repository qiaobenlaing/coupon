//
//  BMSQ_BranchViewController.m
//  BMSQS
//
//  Created by gh on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_BranchViewController.h"

#import "BMSQ_ClerkManageViweController.h"

@interface BMSQ_BranchViewController(){
    
    UITableView *m_tableView;
    
    NSMutableArray *m_shopArray;
    
    
    
}

@end

@implementation BMSQ_BranchViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setViewUp];
}

- (void)setViewUp {
    [self.view setBackgroundColor:UICOLOR(255, 255, 255, 1)];
    
    [self setNavTitle:@"分店"];
    [self setNavBackItem];
    
    
    
    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-64)];
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor=APP_VIEW_BACKCOLOR;
    [self.view addSubview:m_tableView];
    
    [self getStaffShopList];
    
}

#pragma mark - UITableView Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return m_shopArray.count;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSString *cellIdentify = @"cellId";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentify];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
        
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.backgroundColor = UICOLOR(255, 255, 255, 1);
    cell.textLabel.font = [UIFont systemFontOfSize:13];
    NSDictionary *dic = [m_shopArray objectAtIndex:indexPath.row];
    cell.textLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"shopName"]];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_CELL_LINE_COLOR;
    [cell addSubview:line];
    
    return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    //NSLog(@"选择店铺");
    NSDictionary *dic = [m_shopArray objectAtIndex:indexPath.row];
    BMSQ_ClerkManageViweController *VC = [[BMSQ_ClerkManageViweController alloc] init];
    VC.shopDic = dic;

    [self.navigationController pushViewController:VC animated:YES];
    
    
}


-(void)getStaffShopList {
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
   
    NSUserDefaults *UD = [NSUserDefaults standardUserDefaults];
    NSString *staffCode = [UD objectForKey:@"staffCode"];
    [params setObject:staffCode forKey:@"staffCode"];
    [params setObject:@"1" forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"getStaffShopList" strParams:staffCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStaffShopList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        NSLog(@"%@",responseObject);
        m_shopArray = [responseObject objectForKey:@"shopList"];
        
        [m_tableView reloadData];
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
    
    
    
}


@end
