//
//  BMSQ_selePayCardViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_selePayCardViewController.h"
#import "BMSQ_MyBankCell.h"
#import "PullingRefreshTableView.h"
#import "BMSQ_AddBankCardController.h"
@interface BMSQ_selePayCardViewController ()<UITableViewDataSource,UITableViewDelegate,PullingRefreshTableViewDelegate>

{
    PullingRefreshTableView* m_tableView;
    NSMutableArray* m_dataSource;
    int pageNumber; //当前页码
    BOOL refreshing;
}

@property(nonatomic, strong)UITableView *m_tableView;
@end

@implementation BMSQ_selePayCardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"我的银行卡"];
    
    m_tableView = [[PullingRefreshTableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT - 10) pullingDelegate:self];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:m_tableView];
    
    pageNumber = 1;
    m_dataSource = [[NSMutableArray alloc]init];
    [self getBankAccountList];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshMyCard:) name:@"refreshMyCard" object:nil];
    
}
-(void)refreshMyCard:(NSNotification *)notification{
    NSArray *array = notification.object;
    [m_dataSource removeAllObjects];
    [m_dataSource addObjectsFromArray:array];
    [m_tableView reloadData];
}
- (void)getBankAccountList
{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[NSNumber numberWithInt:pageNumber] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"getBankAccountList" strParams:[NSString stringWithFormat:@"%@",[gloabFunction getUserCode]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [ProgressManage openProgressText:nil];
    [self.jsonPrcClient invokeMethod:@"getBankAccountList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [ProgressManage closeProgress];
        [m_tableView tableViewDidFinishedLoading];
        
        if (pageNumber == 1)
        {
            [m_dataSource removeAllObjects];
        }
        
        [m_dataSource addObjectsFromArray:[responseObject objectForKey:@"bankAccountList"]];
        
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
        
        [self getBankAccountList];
    });
    
}

#pragma mark tableview dataSource and delegate

//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
//{
//    if(section == 1)
//        return 0;
//    return 20;
//}
//
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
//{
//    if(section == 1)
//        return nil;
//
//    UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
//    v.backgroundColor = APP_VIEW_BACKCOLOR;
//    return v;
//}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (section == 1)
    {
        return 1;
    }
    return m_dataSource.count;
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0)
    {
        return 62;
    }
    return 64;
}

// Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
// Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    //操作区域
    if (indexPath.section == 0)
    {
        static NSString *cellIdentifier = @"Cell";
        BMSQ_MyBankCell *cell = (BMSQ_MyBankCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil) {
            
            cell = [[BMSQ_MyBankCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        
        cell.backgroundColor = [UIColor whiteColor];
        cell.selectionStyle=UITableViewCellSelectionStyleNone ;
        [cell setCellValue:[m_dataSource objectAtIndex:indexPath.row]];
        return cell;
    }
    else
    {
        static NSString* cellIdentify = @"cellIdentify3";
        UITableViewCell* cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:@"添加银行卡*仅支持工行卡*"];
            
            [str addAttribute:NSForegroundColorAttributeName value:[UIColor redColor] range:NSMakeRange(5,8)];
            
            UILabel *lb_addCard = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
            lb_addCard.backgroundColor = [UIColor whiteColor];
            lb_addCard.attributedText = str;
            lb_addCard.textAlignment = NSTextAlignmentCenter;
            lb_addCard.font = [UIFont systemFontOfSize:12];
            [cell addSubview:lb_addCard];
        }
        
        
        return cell;
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    
    if (indexPath.section ==0) {
        
        BMSQ_MyBankCell *cell = (BMSQ_MyBankCell *)[tableView cellForRowAtIndexPath:indexPath];
        [[NSNotificationCenter defaultCenter]postNotificationName:@"seleCard" object:cell.dicBank];
        [self.navigationController popViewControllerAnimated:YES];

    }else if (indexPath.section ==1){
        BMSQ_AddBankCardController* addCtrl = [[BMSQ_AddBankCardController alloc]init];
        addCtrl.fromvc = (int)self.navigationController.viewControllers.count;
        [self.navigationController pushViewController:addCtrl animated:YES];
    }
    
}



@end
