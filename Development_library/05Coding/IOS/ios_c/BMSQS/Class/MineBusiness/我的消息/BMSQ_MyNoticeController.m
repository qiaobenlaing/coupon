//
//  BMSQ_MyNoticeController.m
//  BMSQC
//
//  Created by djx on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyNoticeController.h"
#import "BMSQ_MyNoticeCell.h"
#import "BMSQ_MemberChartViewController.h"
#import "BMSQ_CouponDetailViewController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_ShopDetailController.h"

@interface BMSQ_MyNoticeController ()
{
    PullingRefreshTableView* m_tableView;
    NSMutableArray* m_dataSource;
    BOOL refreshing;
    int pageNumber;
    NSString* status; //0-	商家消息；1-	会员卡消息；2-	优惠券消息；3,广播信息
    UIButton* btnCupon;
    UIButton* btnCard;
    UIButton* btnShop;
    UIButton* btnGB; //广播
}

@end

@implementation BMSQ_MyNoticeController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setViewUp];
}


- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"我的消息"];
    [self setNavBackItem];
    
    pageNumber = 1;
    status = @"2";
      UIView* v_historyTop = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y+5, APP_VIEW_WIDTH, 50)];
    v_historyTop.backgroundColor = [UIColor whiteColor];
    
    
    btnCupon = [[UIButton alloc]initWithFrame:CGRectMake(50, 5, (APP_VIEW_WIDTH-100)/3, 30)];
    [btnCupon setTitle:@"优惠券" forState:UIControlStateNormal];
    [btnCupon.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftNoSelect"] forState:UIControlStateNormal];
    [btnCupon setBackgroundImage:[UIImage imageNamed:@"iv_leftSelect"] forState:UIControlStateSelected];
    [btnCupon setTitleColor:UICOLOR(0, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnCupon setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [btnCupon addTarget:self action:@selector(btnCuponClick) forControlEvents:UIControlEventTouchUpInside];
    btnCupon.selected = YES;
    [v_historyTop addSubview:btnCupon];
 
    btnShop = [[UIButton alloc]initWithFrame:CGRectMake(50+(APP_VIEW_WIDTH-100)/3, 5, (APP_VIEW_WIDTH-100)/3, 30)];
    [btnShop setTitle:@"商家沟通" forState:UIControlStateNormal];
    [btnShop.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnShop setBackgroundImage:[UIImage imageNamed:@"iv_centerNoselect"] forState:UIControlStateNormal];
    [btnShop setBackgroundImage:[UIImage imageNamed:@"iv_centerSelect"] forState:UIControlStateSelected];
    [btnShop addTarget:self action:@selector(btnShopClick) forControlEvents:UIControlEventTouchUpInside];
    [btnShop setTitleColor:UICOLOR(0, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnShop setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [v_historyTop addSubview:btnShop];
    
    btnGB = [[UIButton alloc]initWithFrame:CGRectMake(50+2*(APP_VIEW_WIDTH-100)/3, 5, (APP_VIEW_WIDTH-100)/3, 30)];
    [btnGB setTitle:@"商家广播" forState:UIControlStateNormal];
    [btnGB.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btnGB setBackgroundImage:[UIImage imageNamed:@"iv_rightNoSelect"] forState:UIControlStateNormal];
    [btnGB setBackgroundImage:[UIImage imageNamed:@"iv_rightSelect"] forState:UIControlStateSelected];
    [btnGB addTarget:self action:@selector(btnGBClick) forControlEvents:UIControlEventTouchUpInside];
    [btnGB setTitleColor:UICOLOR(0, 0, 0, 1.0) forState:UIControlStateNormal];
    [btnGB setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [v_historyTop addSubview:btnGB];
    [self.view addSubview:v_historyTop];
    
    m_tableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 50, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT - 50)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.pullingDelegate = self;
    [self.view addSubview:m_tableView];
    
    m_dataSource = [[NSMutableArray alloc]init];
    [self getMyNotice];
    
    self.m_noDataView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+50, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-50);
    
}

- (void)btnCuponClick
{
    status = @"2";
    pageNumber = 1;
    btnCupon.selected = YES;
    btnShop.selected = NO;
    btnGB.selected = NO;
    [self readMessage];
    [self getMyNotice];
}


- (void)btnShopClick
{
    pageNumber = 1;
    status = @"0";
    btnCupon.selected = NO;
    btnShop.selected = YES;
    btnGB.selected = NO;
    [self readMessage];
    [self getMsgGroup];

}

- (void)btnGBClick
{
    status = @"0";
    pageNumber = 1;
    btnCupon.selected = NO;
    btnShop.selected = NO;
    btnGB.selected = YES;
    [self readMessage];
    [self getMyNotice];
}

//阅读消息
- (void)readMessage
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:status forKey:@"type"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"readMessage" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"readMessage" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        NSNumber *num = [responseObject objectForKey:@"code"];
        
        if (num.intValue == 50000) {
            NSLog(@"成功");
        }
        

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {

    }];
}


//优惠券 商家广播 数据请求
- (void)getMyNotice
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:status forKey:@"type"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getMessageList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getMessageList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"messageList"]];
        
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

//商家沟通 数据请求
- (void)getMsgGroup
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getMsgGroup" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getMsgGroup" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"ret"]];
       
        
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
        
        if (btnShop.selected)
        {
            [self getMsgGroup];
        }
        else
        {
            [self getMyNotice];
        }
        
    });
    
}

#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return m_dataSource.count;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 80;
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
        //操作区域
    static NSString *cellIdentifier = @"BMSQ_MyNoticeCell";
    BMSQ_MyNoticeCell *cell = (BMSQ_MyNoticeCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        
        cell = [[BMSQ_MyNoticeCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    cell.backgroundColor = [UIColor whiteColor];
    cell.selectionStyle=UITableViewCellSelectionStyleNone ;
    if (btnShop.selected == YES) {
        [cell setNewsCellValue:[m_dataSource objectAtIndex:indexPath.row]];
    }
    else
    {
        [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
    }
    return cell;

    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (btnShop.selected) {
        NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
        BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
        vc.m_dic = dic;
        vc.shopID = [dic objectForKey:@"shopCode"];
        vc.myTitle = [dic objectForKey:@"shopName"];
        [self.navigationController pushViewController:vc animated:YES];

    }else if (btnCupon.selected) {
        
        NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
        BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
        couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
        [self.navigationController pushViewController:couponVC animated:YES];
        
    }else if(btnGB.selected){
//        NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
//        BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
//        shopDetailCtrl.shopCode = [dic objectForKey:@"shopCode"];
//        shopDetailCtrl.shopName = [dic objectForKey:@"shopName"];
//        shopDetailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
//        [self.navigationController pushViewController:shopDetailCtrl animated:YES];
        
         NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
        BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
        detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
        detailCtrl.userCode = [gloabFunction getUserCode];
        detailCtrl.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:detailCtrl animated:YES];
        
    }
}


@end
