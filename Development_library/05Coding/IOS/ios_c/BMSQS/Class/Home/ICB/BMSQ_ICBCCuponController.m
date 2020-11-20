//
//  BMSQ_ICBCCuponController.m
//  BMSQC
//
//  Created by djx on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ICBCCuponController.h"
#import "BMSQ_ICBCCuponCell.h"
#import "RRC_webViewController.h"

@interface BMSQ_ICBCCuponController ()
{
    PullingRefreshTableView* m_tableView;
    NSMutableArray* m_dataSource;
    int pageNumber; //当前页码
    BOOL refreshing;
}

@end

@implementation BMSQ_ICBCCuponController

@synthesize insertPage;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
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
    if (insertPage == 1)
    {
        [self setNavTitle:@"平台主题"];
    }
    else if (insertPage == 2)
    {
        [self setNavTitle:@"工银特惠"];
    }
    else
    {
        [self setNavTitle:@"商家活动"];
    }
    
    
    m_tableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) pullingDelegate:self];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    pageNumber = 1;
    m_dataSource = [[NSMutableArray alloc]init];
    
    [self getactivitylist];
}

- (void)getactivitylist
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:insertPage] forKey:@"type"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *currentCityName = [userDefults objectForKey:SELECITY];
    
    
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:currentCityName forKey:@"city"];
    NSString* vcode = [gloabFunction getSign:@"getActivityList" strParams:[NSString stringWithFormat:@"%d",insertPage]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getActivityList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"activityList"]];
        
        if (m_dataSource.count <= 0)
        {
            [self showNoDataView];
        }
        else
        {
            [self hiddenNoDataView];
        }
        
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
    }];
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
        
        [self getactivitylist];
    });
    
}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 128;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    //操作区域
    static NSString *cellIdentifier = @"Cell";
    BMSQ_ICBCCuponCell *cell = (BMSQ_ICBCCuponCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        
        cell = [[BMSQ_ICBCCuponCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;
    [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary* dicData = [m_dataSource objectAtIndex:indexPath.row];
    RRC_webViewController* webCtrl = [[RRC_webViewController alloc]init];
    webCtrl.navtitle = @"活动详情";
    webCtrl.hidesBottomBarWhenPushed = YES;
    webCtrl.requestUrl = [NSString stringWithFormat:@"%@/Browser/cGetActInfo?actCode=%@",H5_URL,[dicData objectForKey:@"activityCode"]];
    [self.navigationController pushViewController:webCtrl animated:YES];
}

@end
