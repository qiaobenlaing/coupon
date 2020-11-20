//
//  BMSQ_ShopOrderViewController.m
//  BMSQS
//
//  Created by gh on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopOrderViewController.h"
#import "BMSQ_ShopOrderCell.h"
#import "BMSQ_OrderDetailController.h"

@interface BMSQ_ShopOrderViewController () {
    
    BOOL refreshing;
    
    UIButton* btn_leftItem;
    UIButton* btn_rightItem;
    UIImageView* iv_topTitle;
    
    PullingRefreshTableView *m_tableView;
    
    NSMutableArray *m_dataSource;
    
    int pageNumber;
    NSString* isFinish;
    
}

@property (nonatomic,strong)NSDictionary *m_dic;



@end

@implementation BMSQ_ShopOrderViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setViewUp];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setViewUp {
    
    [self.navigationController setNavigationBarHidden:NO];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self.navigationItem setTitle:@"订单管理"];
    
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 50)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    NSArray *segmentedArray = [[NSArray alloc]initWithObjects:@"已完成",@"未完成",nil];
    //初始化UISegmentedControl
    UISegmentedControl *segmentedTemp = [[UISegmentedControl alloc]initWithItems:segmentedArray];
    segmentedTemp.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, 30.0);
    segmentedTemp.tintColor= [UIColor colorWithRed:182/255.0 green:0/255.0 blue:12/255.0 alpha:1];
    segmentedTemp.center = CGPointMake(APP_VIEW_WIDTH/2, 25);
    
    segmentedTemp.selectedSegmentIndex = 0;
    [segmentedTemp addTarget:self action:@selector(didClicksegmentedControlAction:) forControlEvents:UIControlEventValueChanged];
    [topView addSubview:segmentedTemp];
    
  
    m_tableView = [[PullingRefreshTableView alloc] initWithFrame:CGRectMake(0, 50, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.pullingDelegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    m_dataSource = [[NSMutableArray alloc] init];
    isFinish = @"1";
    pageNumber = 1;
    
    [self getShopOrderList];
}


- (void)getShopOrderList {
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:isFinish forKey:@"isFinish"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getShopOrderList" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
//    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"getShopOrderList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [ProgressManage closeProgress];
//        [SVProgressHUD dismiss];
        [m_tableView tableViewDidFinishedLoading];
        
        if (pageNumber == 1) {
            [m_dataSource removeAllObjects];
        }
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"orderList"]];
        

        [m_tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [m_tableView tableViewDidFinishedLoading];
        [ProgressManage closeProgress];
//        [SVProgressHUD dismiss];
    }];
    
}

-(void)didClicksegmentedControlAction:(id)sender{
    
    UISegmentedControl *control = (UISegmentedControl *)sender;
    int tag = (int)control.selectedSegmentIndex;
    switch (tag) {
        case 0:   //已完成
        {
            isFinish = @"1";
            pageNumber = 1;
            [m_dataSource removeAllObjects];
            [m_tableView reloadData];
            
            [self getShopOrderList];

            
        }
            break;
        case 1:  //未完成
        {
            isFinish = @"0";
            pageNumber = 1;
            [m_dataSource removeAllObjects];
            [m_tableView reloadData];
            
            [self getShopOrderList];
        }
            break;
            
        default:
            break;
    }
    
    
}


- (void)btnLeftItemClick
{
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    btn_leftItem.selected = YES;
    btn_rightItem.selected = NO;
    
    
}

- (void)btnRightItemClick
{
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_redPackage"]];
    btn_rightItem.selected = YES;
    btn_leftItem.selected = NO;
    
    
}




#pragma mark - UITabelView Delegate 

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 140;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if ([m_dataSource count]>0) {
        return m_dataSource.count;
    }
    else
        return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if ([m_dataSource count]>0) {
        NSString *identify = @"shopOrderCell";
        BMSQ_ShopOrderCell *cell = (BMSQ_ShopOrderCell *)[tableView  dequeueReusableCellWithIdentifier:identify];
        if (cell == nil) {
            cell =  [[BMSQ_ShopOrderCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identify];
        }
        cell.backgroundColor = APP_VIEW_BACKCOLOR;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
        return cell;
    }
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary *dic = m_dataSource[indexPath.row];
    BMSQ_OrderDetailController *orderDetailVC = [[BMSQ_OrderDetailController alloc] init];
    orderDetailVC.consumeCode = [dic objectForKey:@"consumeCode"];
    orderDetailVC.couponDic = dic;
    orderDetailVC.isFinish = isFinish;
    orderDetailVC.consumeStatus = [dic objectForKey:@"userConsumeStatus"];
    [self.navigationController pushViewController:orderDetailVC animated:YES];
    
}

#pragma mark - PullingRefreshTableViewDelegate

- (void)pullingTableViewDidStartRefreshing:(PullingRefreshTableView *)tableView
{
    refreshing = YES;
    [self performSelector:@selector(loadData) withObject:nil afterDelay:1.f];
}

- (NSDate *)pullingTableViewRefreshingFinishedDate
{
    NSDate *date = [NSDate date];
    
    return date;
}

- (void)pullingTableViewDidStartLoading:(PullingRefreshTableView *)tableView
{
    [self performSelector:@selector(loadData) withObject:nil afterDelay:1.f];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    
    if (scrollView == m_tableView)
    {
        [m_tableView tableViewDidScroll:scrollView];
    }
    
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate
{
    //
    if (scrollView == m_tableView)
    {
        [m_tableView tableViewDidEndDragging:scrollView];
    }
    
}

- (void)loadData{
    
    
    if (refreshing)
    {
        pageNumber = 1;
        refreshing = NO;
    }
    else
    {
        pageNumber++;
    }
    
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        [self getShopOrderList];
        
    });
    
}


@end
