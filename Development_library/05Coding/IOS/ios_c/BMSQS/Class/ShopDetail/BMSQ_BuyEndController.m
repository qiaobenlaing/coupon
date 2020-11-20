//
//  BMSQ_BuyEndController.m
//  BMSQC
//
//  Created by djx on 15/8/7.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BuyEndController.h"
#import "MobClick.h"
@interface BMSQ_BuyEndController ()
{
    UITableView* m_tableView;
}

@end

@implementation BMSQ_BuyEndController
@synthesize payCount;
@synthesize payDisCount;
@synthesize payMore;
@synthesize shopName;

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"BuyEndController"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"BuyEndController"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}



- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"工银惠支付"];

    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    //[self userTotalBonusValue];
    
}

//- (void)userTotalBonusValue
//{
//    [self initJsonPrcClient:@"2"];
//    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
//    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
//    [params setObject:shopCode forKey:@"shopCode"];
//    NSString* vcode = [gloabFunction getSign:@"userTotalBonusValue" strParams:[gloabFunction getUserCode]];
//    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
//    [params setObject:vcode forKey:@"vcode"];
//    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
//    
//    [ProgressManage openProgressText:nil];
//    [self.jsonPrcClient invokeMethod:@"userTotalBonusValue" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        
//        m_dicBonus = [[NSMutableDictionary alloc]initWithDictionary:responseObject];
//        [ProgressManage closeProgress];
//        
//        tx_bmBonus.text = [NSString stringWithFormat:@"%@",[m_dicBonus objectForKey:@"platBonus"]];
//        tx_shopBonus.text = [NSString stringWithFormat:@"%@",[m_dicBonus objectForKey:@"shopBonus"]];
//        
//        [m_tableView reloadData];
//    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//        [ProgressManage closeProgress];
//        
//    }];
//}


#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 0)
    {
        return 4;
    }
    return 1;
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 3;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 44;
    }
    
    
    return 144;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //操作区域
    if (indexPath.section == 0)
    {
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        //if (cell == nil)
        {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:cellIdentifier];
        }
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        cell.textLabel.font = [UIFont systemFontOfSize:14];
        cell.detailTextLabel.font = [UIFont systemFontOfSize:14];
        cell.detailTextLabel.textColor = cell.textLabel.textColor;
        
        UIView* line = [[UIView alloc]initWithFrame:CGRectMake(10, 43, APP_VIEW_WIDTH-10, APP_CELL_LINE_HEIGHT)];
        line.backgroundColor = APP_CELL_LINE_COLOR;
        [cell addSubview:line];
        
        if (indexPath.row == 0)
        {
            cell.textLabel.text = @"商家名称";
            cell.detailTextLabel.text = [gloabFunction changeNullToBlank:shopName];
        }
        else if(indexPath.row == 1)
        {
            cell.textLabel.text = @"消费金额";
            cell.detailTextLabel.text = [gloabFunction changeNullToBlank:payCount];
        }
        else if(indexPath.row == 2)
        {
            cell.textLabel.text = @"总抵扣金额";
            cell.detailTextLabel.text = [gloabFunction changeNullToBlank:payDisCount];
        }
        else
        {
            cell.textLabel.text = @"还需支付";
            cell.detailTextLabel.text = [gloabFunction changeNullToBlank:payMore];
            line.hidden = YES;
        }
        return cell;
    }
    else
    {
        static NSString *cellIdentifier = @"Cell";
        UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        //if (cell == nil)
        {
            
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        }
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        
        UIButton* btnBuy = [[UIButton alloc]initWithFrame:CGRectMake(10, 20, APP_VIEW_WIDTH-20, 40)];
        btnBuy.backgroundColor = UICOLOR(182, 0, 12, 1.0);
        [btnBuy addTarget:self action:@selector(btnBuyClick) forControlEvents:UIControlEventTouchUpInside];
        [btnBuy setTitle:@"在线支付" forState:UIControlStateNormal];
        [btnBuy setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [cell addSubview:btnBuy];
        
        return cell;
    }
}

- (void)btnBuyClick
{
    
}

@end
