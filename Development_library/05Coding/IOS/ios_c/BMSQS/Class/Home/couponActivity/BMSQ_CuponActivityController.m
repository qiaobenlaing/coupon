//
//  BMSQ_CuponController.m
//  BMSQC
//
//  Created by djx on 15/7/27.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CuponActivityController.h"
#import "BMSQ_CuponCell.h"
#import "BMSQ_ShopDetailController.h"


@interface BMSQ_CuponActivityController ()
{
    PullingRefreshTableView* m_tableView;
    NSMutableArray* m_tablSelectIndexPath;
    NSMutableArray* m_dataSource;
    int pageNumber; //当前页码
    BOOL refreshing;
    UISearchBar* tx_search;
    UIImageView* iv_topTitle;
}

@end

@implementation BMSQ_CuponActivityController

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


- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    
    
    iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake(30, APP_STATUSBAR_HEIGHT + 6, 60, 30)];
    iv_topTitle.userInteractionEnabled = YES;
    //[iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    
    tx_search = [[UISearchBar alloc]initWithFrame:CGRectMake(5, 0, APP_VIEW_WIDTH - 70, 30)];
    tx_search.delegate = self;
    [tx_search setPlaceholder:@"搜索商户名、地点"];
    
    UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
    [topView setBarStyle:UIBarStyleBlackTranslucent];
    
    UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
    
    UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
    btn.frame = CGRectMake(2, 5, 50, 25);
    [btn setTitle:@"隐藏" forState:UIControlStateNormal];
    [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
    [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
    UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
    NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
    [topView setItems:buttonsArray];
    [tx_search setInputAccessoryView:topView];
    
    //设置frmae
    //    searchBar.width=300;
    //    searchBar.height=30;
    
    //设置背景图片
    float version = [[[ UIDevice currentDevice ] systemVersion ] floatValue ];
    
    if ([ tx_search respondsToSelector : @selector (barTintColor)]) {
        
        float  iosversion7_1 = 7.1 ;
        
        if (version >= iosversion7_1)
            
        {
            
            //iOS7.1
            
            [[[[ tx_search . subviews objectAtIndex : 0 ] subviews ] objectAtIndex : 0 ] removeFromSuperview ];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
        else
            
        {
            
            //iOS7.0
            
            [ tx_search setBarTintColor :[ UIColor clearColor ]];
            
            [ tx_search setBackgroundColor :[ UIColor clearColor ]];
            
        }
        
    }
    
    else
        
    {
        
        //iOS7.0 以下
        
        [[ tx_search . subviews objectAtIndex : 0 ] removeFromSuperview ];
        
        [ tx_search setBackgroundColor :[ UIColor clearColor ]];
        
    }
    
    [iv_topTitle addSubview:tx_search];
    [self setNavCustomerView:iv_topTitle];
    
    
    m_tableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) pullingDelegate:self];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    pageNumber = 1;
    m_dataSource = [[NSMutableArray alloc]init];
    m_tablSelectIndexPath = [[NSMutableArray alloc]init];
    [self getlistCoupon];
}

#pragma  mark -  uisearchbar delegate
- (void)searchBarSearchButtonClicked:(UISearchBar *)searchbar
{
    [tx_search resignFirstResponder];
    [m_dataSource removeAllObjects];
    [m_tableView reloadData];
    pageNumber = 1;
    [self getlistCoupon];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *) searchbar
{
    [tx_search resignFirstResponder];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [tx_search resignFirstResponder];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    //    [tx_search resignFirstResponder];
    [m_dataSource removeAllObjects];
    [m_tableView reloadData];
    pageNumber = 1;
    [self getlistCoupon];
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
        
        [self getlistCoupon];
    });
    
}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    for (NSDictionary*  dicPath in m_tablSelectIndexPath)
    {
        if ([[m_dataSource objectAtIndex:indexPath.row] isEqual:dicPath])
        {
            return 150;
        }
        
    }
    
    return 80;
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    //操作区域
    static NSString *cellIdentifier = @"Cell";
    BMSQ_CuponActivityCell *cell = (BMSQ_CuponActivityCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        
        cell = [[BMSQ_CuponActivityCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    cell.delegate = self;
    cell.backgroundColor = APP_VIEW_BACKCOLOR;
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;
    [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row] indexPath:indexPath];
    BOOL isSelect = NO;
    for (NSDictionary*  dicPath in m_tablSelectIndexPath)
    {
        if ([[m_dataSource objectAtIndex:indexPath.row] isEqual:dicPath])
        {
            
            isSelect = YES;
            break;
        }
        
    }
    
    [cell.btn_grab addTarget:self action:@selector(btnGrabClick:) forControlEvents:UIControlEventTouchUpInside];
    
    [cell setCellExpansion:isSelect];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary* dicData = [m_dataSource objectAtIndex:indexPath.row];
    
    BMSQ_ShopDetailController* detailCtrl = [[BMSQ_ShopDetailController alloc]init];
    detailCtrl.shopCode = [dicData objectForKey:@"shopCode"];
    detailCtrl.couponDic = dicData;
    [self.navigationController pushViewController:detailCtrl animated:YES];
    
    
}


-(void)setCellSelect:(BOOL)isSelect indexPath:(int)row dic:(NSDictionary *)dicupon{
    
    if (isSelect)
    {
        [m_tablSelectIndexPath addObject:dicupon];
    }
    else
    {
        [m_tablSelectIndexPath removeObject:dicupon];
    }
    
    [m_tableView reloadData];
    
}

#pragma mark - http request
- (void)getlistCoupon
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:@"" forKey:@"shopName"];
    //    [params setObject:[NSNumber numberWithInt:insertPage] forKey:@"couponType"];
    [params setObject:@"" forKey:@"couponType"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    [params setObject:@"" forKey:@"searchWord"];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *currentCityName = [userDefults objectForKey:SELECITY];
    NSString *userCodeStr = [gloabFunction getUserCode];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    if ([userCodeStr  isEqual: @"(null)"]) {
        userCodeStr = @"";
    }
    [params setObject:userCodeStr forKey:@"userCode"];
    [params setObject:currentCityName forKey:@"city"];
    NSString* vcode = [gloabFunction getSign:@"listCoupon" strParams:[NSString stringWithFormat:@"%d",insertPage]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"listCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"couponList"]];
        
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




- (void)btnGrabClick:(UIButtonEx*)sender
{
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[sender.object objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[sender.object objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券成功"];
            
            sender.userInteractionEnabled = NO;
            [sender setTitle:@"已领" forState:UIControlStateNormal];
            [self getlistCoupon];
        }
        else
        {
            
            int i = [code intValue];
            
            switch (i) {
                case 80218:
                    [self showAlertView:@"优惠券开始使用日期不正确"];
                    break;
                case 80219:
                    [self showAlertView:@"优惠券已经被领取"];
                    break;
                case 80220:
                    [self showAlertView:@"优惠券已过期"];
                    break;
                case 80221:
                    [self showAlertView:@"优惠券已领走"];
                    break;
                case 80222:
                    [self showAlertView:@"您领用的数量已经达上限"];
                    break;
                case 80223:
                    [self showAlertView:@"优惠券不存在"];
                    break;
                    
                default:
                    [self showAlertView:@"优惠券状态不对，过段时间再领吧"];
                    
                    break;
            }
            
        }
        
        //[m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"抢券失败"];
    }];
}

- (void)dismissKeyB {
    [tx_search resignFirstResponder];
    
    
}



@end
