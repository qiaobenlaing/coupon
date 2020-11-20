//
//  BMSQ_RedEnvelopeController.m
//  BMSQC
//
//  Created by djx on 15/8/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RedEnvelopeController.h"
#import "BMSQ_RedEnvelopeCell.h"

@interface BMSQ_RedEnvelopeController ()
{
    PullingRefreshTableView* m_tableView;
    UIImageView *iv_noData;
    NSMutableArray* m_dataSource;
    BOOL refreshing;
    int pageNumber;
}

@end

@implementation BMSQ_RedEnvelopeController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"我的红包"];
    [self setNavBackItem];
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    pageNumber = 1;
    
    m_tableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    m_tableView.dataSource = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.delegate = self;
    m_tableView.pullingDelegate = self;
    m_tableView.headerOnly = YES;
    m_tableView.footOnly = YES;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:m_tableView];
    
    m_dataSource = [[NSMutableArray alloc]init];
    
    //[m_dataSource addObject:@"10"];
    [self getMyBonus];
}


- (void)getMyBonus
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getMyBonus" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getMyBonus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        
        

        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        [m_dataSource addObjectsFromArray:responseObject];

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
        
        [self getMyBonus];
        
    });
    
}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 90;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //操作区域
    static NSString *cellIdentifier = @"Cell";
    BMSQ_RedEnvelopeCell *cell = (BMSQ_RedEnvelopeCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        
        cell = [[BMSQ_RedEnvelopeCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    cell.backgroundColor = APP_VIEW_BACKCOLOR;
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;
    [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
    return cell;
    
    
}


@end
