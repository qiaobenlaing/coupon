//
//  BMSQ_DineSetUpViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/15.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_DineSetUpViewController.h"

@interface BMSQ_DineSetUpViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;

@end

@implementation BMSQ_DineSetUpViewController


- (void)viewDidAppear:(BOOL)animated
{
    [self listShopDelivery];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.deliveryList = [@[] mutableCopy];
    
    [self setNavBackItem];
    [self setNavTitle:@"餐厅设置"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    [self setViewUp];
    
    
    
    
    
    
}

- (void)setViewUp
{
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT) style:UITableViewStyleGrouped];
    _baseView.rowHeight = 40;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    _baseView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:self.baseView];
    
    [self listShopDelivery];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0){
        
        return 2;
    }
    
    return self.deliveryList.count + 2;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1 && indexPath.row > 1) {
        static NSString * cell_id = @"DistributionViewCell";
        DistributionViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[DistributionViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        NSNumber * leftStr = self.deliveryList[indexPath.row - 2][@"deliveryDistance"];
        int leftNum = [leftStr intValue] / 1000;
        NSString * centerStr = self.deliveryList[indexPath.row - 2][@"requireMoney"];
        NSString * rightStr = self.deliveryList[indexPath.row - 2][@"deliveryFee"];
        cell.leftLable.text = [NSString stringWithFormat:@"配送范围 %dkm",leftNum];
        cell.centerLable.text = [NSString stringWithFormat:@"起送价 %@元",centerStr];
        cell.rightLable.text = [NSString stringWithFormat:@"配送费 %@元",rightStr];
        
        return cell;
    }
    
    
    static NSString * cell_id = @"switchCell";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:cell_id];
        
    }
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 0, 120, 50)];
    [button setImage:[UIImage imageNamed:@"closeStatus"] forState:UIControlStateNormal];
    [button setImage:[UIImage imageNamed:@"openStatus"] forState:UIControlStateSelected];
    [cell addSubview:button];
    [button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
    button.hidden = YES;
    
    if (indexPath.section == 0) {
        button.tag = 100+indexPath.row;
    }else if (indexPath.section == 1){
        button.tag = 102+indexPath.row;
    }
    
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.textLabel.font = [UIFont systemFontOfSize:14.f];
    cell.detailTextLabel.font = [UIFont systemFontOfSize:14.f];
    
    
    if (indexPath.section == 0) {
        
        UIButton *button = (UIButton *)[cell viewWithTag:100+indexPath.row];
        button.hidden = NO;
        if (indexPath.row == 0) {
            cell.textLabel.text = @"堂食点餐";
            
            button.selected = [self.isOpenEat boolValue];

            
        }
        if (indexPath.row == 1) {
            cell.textLabel.text = @"桌号管理";
            
            button.selected = [self.tableNbrSwitch boolValue];

        }
        
        
        
    }
    else if (indexPath.section == 1){
        UIButton *button = (UIButton *)[cell viewWithTag:102+indexPath.row];
        
        if (indexPath.row == 0) {
            button.hidden = NO;
            cell.textLabel.text = @"外卖点餐";
            button.selected = [self.isOpenTakeout boolValue];
            
        }
        
        if (indexPath.row == 1) {
            cell.textLabel.text = @"配送方案";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
                
    }
    
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    
    if (section == 0) {
        return 1;
    }
    return 13;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if (section == 0) {
        return 1;
    }
    return 1;
}
#pragma mark ---- 
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 1 && indexPath.row == 1) {
        DistributionPlanViewController * distributionVC = [[DistributionPlanViewController alloc] init];
        distributionVC.deliveryList = [NSMutableArray arrayWithArray:self.deliveryList];
        [self.navigationController pushViewController:distributionVC animated:YES];
    }
}

#pragma mark ---- 获得配送方案
// 获得配送方案
- (void)listShopDelivery
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"listShopDelivery" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"listShopDelivery" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        self.deliveryList = responseObject;
        NSLog(@"%@", responseObject);
        [self.baseView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];


}



-(void)clickButton:(UIButton *)button{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"updateShop" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    NSString *updateKey;
    NSString *updateValue;
    if (button.tag == 100) {
        BOOL btnSelect = !button.selected;
        NSString *btnstr;
        if (btnSelect) {
            btnstr = @"1";
        }else {
            btnstr = @"0";
        }
        updateKey = [NSString stringWithFormat:@"isOpenEat"];
        updateValue = [NSString stringWithFormat:@"%@",btnstr];
        
    }else if (button.tag == 101) {
        updateKey = [NSString stringWithFormat:@"tableNbrSwitch"];
        BOOL btnSelect = !button.selected;
        NSString *btnstr;
        if (btnSelect) {
            btnstr = @"1";
        }else {
            btnstr = @"0";
        }
        updateValue = [NSString stringWithFormat:@"%@",btnstr];
        
    } else if (button.tag == 102) {
        updateKey = [NSString stringWithFormat:@"isOpenTakeout"];
        BOOL btnSelect = !button.selected;
        NSString *btnstr;
        if (btnSelect) {
            btnstr = @"1";
        }else {
            btnstr = @"0";
        }
        updateValue = [NSString stringWithFormat:@"%@",btnstr];
        
    }
    
    
    [params setObject:updateKey forKey:@"updateKey"];
    [params setObject:updateValue forKey:@"updateValue"];
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"updateShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        NSNumber * code = [responseObject objectForKey:@"code"];
        
        if (code.intValue == 50000) {
            button.selected = !button.selected;
            
        }else {
            CSAlert(@"修改失败");
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        CSAlert(@"修改失败");
    }];
}






#pragma mark ------

#pragma mark ----
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
