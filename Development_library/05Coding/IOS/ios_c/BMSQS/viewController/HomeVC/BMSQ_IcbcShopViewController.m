//
//  abcd_ViewController.m
//  BMSQC
//
//  Created by dongzhonghui on 15/12/1.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_IcbcShopViewController.h"
#import "BMSQ_ShopDetailController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "MobClick.h"
@interface BMSQ_IcbcShopViewController (){
    PullingRefreshTableView *mTableView;
    NSMutableArray *mDataSource;
    int pageNumber;
    BOOL refreshing;//刷新
}
@end

@implementation BMSQ_IcbcShopViewController

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"lcbcShopView"];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"lcbcShopView"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"工银专享"];
    [self setContentView];
    [mTableView launchRefreshing];
}

- (void) setContentView{
    pageNumber = 1;
    mDataSource = [[NSMutableArray alloc]init];
    refreshing = YES;
    
    mTableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    mTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    mTableView.dataSource = self;
    mTableView.delegate = self;
    mTableView.pullingDelegate = self;
    mTableView.backgroundColor = UICOLOR(221, 221, 221, 1);
    [self.view addSubview:mTableView];
}

- (void) getDataForPage:(int)page{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *longitude = [userDefaults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefaults objectForKey:LATITUDE];
    NSString *city = [userDefaults objectForKey:SELECITY];
    NSString *userCode = [gloabFunction getUserCode];
    if ([userCode isEqual:@"(null)"] || userCode  == nil) {
        userCode = @"";
    }
    
    [params setObject:@"" forKey:@"searchWord"];//关键字
    [params setObject:@"0" forKey:@"type"];//类型 0为所有
    [params setObject:longitude forKey:@"longitude"];//
    [params setObject:latitude forKey:@"latitude"];//
    [params setObject:city forKey:@"city"];//
    [params setObject:[NSString stringWithFormat:@"%d",page] forKey:@"page"];
    [params setObject:@"0" forKey:@"moduleValue"];//模板号
    [params setObject:@"" forKey:@"content"];//与模板号对应的内容，模板号为0此值为空
    [params setObject:@"0" forKey:@"order"];//排序
    [params setObject:@"3" forKey:@"filter"];//筛选 3为工行折扣
    [params setObject:userCode forKey:@"userCode"];//筛选 3为工行折扣
    
    [self initJsonPrcClient:@"2"];
    [self.jsonPrcClient invokeMethod:@"searchShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"%@",responseObject);
        if (pageNumber == 1) {
            [mDataSource removeAllObjects];
        }
        NSArray *resultArray = [responseObject objectForKey:@"shopList"];
        if (resultArray.count <= 0) {
            pageNumber--;
        }
        [mDataSource addObjectsFromArray:resultArray];
        [mTableView reloadData];
        [mTableView tableViewDidFinishedLoading];
        refreshing = NO;
    }failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"%@",error);
        [mTableView tableViewDidFinishedLoading];
        refreshing = NO;
    }];
}

#pragma mark UITableView Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return mDataSource.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return [BMSQ_IcbcShopCell cellHeight:[mDataSource objectAtIndex:indexPath.row]];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = @"BMSQ_ShopCellcell";
    BMSQ_IcbcShopCell *cell = (BMSQ_IcbcShopCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        cell = [[BMSQ_IcbcShopCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone ;
    }
    [cell setCellValue:[mDataSource objectAtIndex:indexPath.row]];
    return cell;
}

- (void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if(![gloabFunction isLogin]){
        [self getLogin];
    }else{
//        NSDictionary* dicData = [mDataSource objectAtIndex:indexPath.row];
//        BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
//        detailCtrl.shopCode = [dicData objectForKey:@"shopCode"];
//        detailCtrl.shopName = [dicData objectForKey:@"shopName"];
//        detailCtrl.shopImage = [dicData objectForKey:@"logoUrl"];
//        detailCtrl.couponDic = dicData;
//        [self.navigationController pushViewController:detailCtrl animated:YES];
        
        BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
       NSDictionary* dicData = [mDataSource objectAtIndex:indexPath.row];
        detailCtrl.shopCode = [dicData objectForKey:@"shopCode"];
        detailCtrl.userCode = [gloabFunction getUserCode];
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
    }
}

#pragma mark PullingRefreshTableView Delegate
//开始数据刷新
- (void)pullingTableViewDidStartRefreshing:(PullingRefreshTableView *)tableView{
    refreshing = YES;
    pageNumber = 1;
    [self performSelector:@selector(loadData) withObject:nil afterDelay:1.f];
}
//下拉刷新时间
- (NSDate *) pullingTableViewRefreshingFinishedDate{
    return [NSDate date];
}

//上拉加载更多
- (void) pullingTableViewDidStartLoading:(PullingRefreshTableView *)tableView{
    refreshing = YES;
    pageNumber ++;
    [self performSelector:@selector(loadData) withObject:nil afterDelay:1.f];
}
//上拉加载更多时间
- (NSDate *) pullingTableViewLoadingFinishedDate{
    return [NSDate date];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    if (scrollView == mTableView){
        [mTableView tableViewDidScroll:scrollView];
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    if (scrollView == mTableView){
        [mTableView tableViewDidEndDragging:scrollView];
    }
}

- (void)loadData{
    if (refreshing) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            [self getDataForPage:pageNumber];
        });
    }
}

@end
