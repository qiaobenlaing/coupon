//
//  BMSQ_MemberCardListViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MemberCardListViewController.h"
#import "EGOImageView.h"
#import "BMSQ_MemberChartViewController.h"
#import "MJRefresh.h"

#import "PullingRefreshTableView.h"

@interface BMSQ_MemberCardListViewController () {
    
    
    
    
}

@end

@implementation BMSQ_MemberCardListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self createModel];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    
    if([segue.identifier isEqualToString:@"BMSQ_MemberChart"]){
        
        NSDictionary *dicTemp = (NSDictionary *)[_dataSource objectAtIndex:[(NSString *)sender intValue]];
        
        BMSQ_MemberChartViewController *aVC = (BMSQ_MemberChartViewController*)segue.destinationViewController;
        
        aVC.userID = [dicTemp objectForKey:@"userCode"];
        [aVC popRefreshData:^(NSString *str) {
        }];
    }
    
}
- (void)createModel
{
    _orderType = @"3";/*默认按消费金额排序*/
    [self customRightBtn];
    UIView *view = [self.view viewWithTag:10];
    view.backgroundColor = [UIColor colorWithWhite:0 alpha:0.6];
    
    [self.tableView setSeparatorColor:[UIColor clearColor]];
    /**
     *   初始化数据
     *
     */
    _currentIndex = 1;
    _dataSource = [[NSMutableArray alloc] initWithCapacity:4];
//        [self createHeaderView];
//        [self showRefreshHeader:YES];
//        [self performSelector:@selector(firstLoadDataSource) withObject:nil afterDelay:0.1f];
    
    [self.tableView addHeaderWithTarget:self action:@selector(refreshDataSource)];
    [self.tableView addFooterWithTarget:self action:@selector(loadMoreDataSource)];
    
    
    [self.navigationItem setTitle:self.cardTitle];
    
    [self firstLoadDataSource];
    
}
#pragma mark -CustomView
- (void)customRightBtn
{
    UIImage *backImg = [UIImage imageNamed:@"Member_add"];
    backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"Member_add"] style:UIBarButtonItemStylePlain target:self action:@selector(rightButtonClicked:)];
    [item setTintColor:[UIColor whiteColor]];
    
//    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    [self.navigationItem setRightBarButtonItem:item];
}


#pragma mark - Action

- (void)rightButtonClicked:(id)sender
{
    //    1-办卡时间；2-消费时间；3-消费金额；4-消费次数；
    //    默认等于3，按会员消费金额、消费次数 依次排序
    PopoverView *popView = [[PopoverView alloc] init];
    popView.delegate = self;
    [popView showAtPoint:CGPointMake((self.view.frame.size.width ), 0)
                  inView:self.view
         withStringArray:@[@"按办卡时间排序",
                           @"按消费时间排序",
                           @"按消费金额排序",
                           @"按消费次数排序"]];
}


- (void)popoverView:(PopoverView *)popoverView didSelectItemAtIndex:(NSInteger)index{
    switch (index) {
        case 0:
            _orderType = @"1";
            break;
        case 1:
            _orderType = @"2";
            break;
        case 2:
            _orderType = @"3";
            break;
        case 3:
            _orderType = @"4";
            break;
        default:
            break;
    }
    [popoverView dismiss];
    //    [self showRefreshHeader:YES];
    //    [self performSelector:@selector(refreshDataSource) withObject:nil afterDelay:0.3f];
    [self refreshDataSource];
    [self.tableView setContentOffset:CGPointMake(0,0) animated:YES];
//    [self.tableView.header beginRefreshing];
}


- (void)popoverViewDidDismiss:(PopoverView *)popoverView{
    
    
}

#pragma mark - UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return _dataSource?1:0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _dataSource?_dataSource.count:0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 94.f;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *cellIdentifier = @"MemberListTableViewCell";
    
    UITableViewCell *setCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if (setCell == nil) {
        setCell = [[UITableViewCell alloc] initWithStyle:0 reuseIdentifier:cellIdentifier];
        
    }
    setCell.selectionStyle = 0;
    setCell.backgroundColor = [UIColor colorWithHexString:@"0xf0eff5"];
    if (_dataSource && indexPath.row < _dataSource.count) {
        
        UIView *view = (UIView *)[setCell.contentView viewWithTag:20];
        view.layer.masksToBounds =YES;
        view.layer.cornerRadius = 4.f;
        view.backgroundColor = [UIColor whiteColor];
        //        view.layer.borderColor =
        
        NSDictionary *dicTemp = [_dataSource objectAtIndex:indexPath.row];
        
        NSString *urlStr = [NSString stringWithFormat:@"%@/%@",APP_SERVERCE_HOME,[dicTemp objectForKey:@"avatarUrl"]];
        NSURL *url = [NSURL URLWithString:urlStr];
        EGOImageView *leftImageView = (EGOImageView *)[setCell.contentView viewWithTag:100];
        leftImageView.layer.masksToBounds =YES;
        //        leftImageView.layer.cornerRadius = leftImageView.frame.size.height/2.f;
        [leftImageView setPlaceholderImage:[UIImage imageNamed:@"LEKnowledge_userhead"]];
        [leftImageView setImageURL:url];
        
        NSString *str1 = [dicTemp objectForKey:@"nickName"];
        if ([str1 isKindOfClass:[NSNull class]] || [[dicTemp objectForKey:@"nickName"] length] == 0||str1 ==nil){
            str1 = @"无";
            
        }
        UILabel *label1 = (UILabel *)[setCell viewWithTag:101];
        label1.text = [NSString stringWithFormat:@"昵称：%@",str1];
        
        NSString *str2 = [dicTemp objectForKey:@"cardNbr"]?[dicTemp objectForKey:@"cardNbr"]:@"";
        UILabel *label2 = (UILabel *)[setCell viewWithTag:102];
        label2.text = [NSString stringWithFormat:@"卡号：%@",str2];
        
        NSString *str3 = [dicTemp objectForKey:@"consumeTimes"]?[dicTemp objectForKey:@"consumeTimes"]:@"";
        UILabel *label3 = (UILabel *)[setCell viewWithTag:103];
        label3.text = [NSString stringWithFormat:@"消费次数：%@",str3];
        
        NSString *str4 = [dicTemp objectForKey:@"consumePriceAmount"]?[dicTemp objectForKey:@"consumePriceAmount"]:@"";
        UILabel *label4 = (UILabel *)[setCell viewWithTag:104];
        label4.text = [NSString stringWithFormat:@"消费总金额：%@",str4];
        
        NSString *str5 = [dicTemp objectForKey:@"couponUseAmount"]?[dicTemp objectForKey:@"couponUseAmount"]:@"";
        UILabel *label5 = (UILabel *)[setCell viewWithTag:105];
        label5.text = [NSString stringWithFormat:@"使用优惠券：%@",str5];
        
        NSString *str6 = [dicTemp objectForKey:@"deductionPrice"]?[dicTemp objectForKey:@"deductionPrice"]:@"";
        UILabel *label6 = (UILabel *)[setCell viewWithTag:106];
        label6.text = [NSString stringWithFormat:@"抵扣总额：%@",str6];
        
        
    }
    return setCell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    [self performSegueWithIdentifier:@"BMSQ_MemberChart" sender:[NSString stringWithFormat:@"%d",indexPath.row]];
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if ([cell respondsToSelector:@selector(setSeparatorInset:)]) {
        [cell setSeparatorInset:UIEdgeInsetsZero];
    }
    if ([cell respondsToSelector:@selector(setLayoutMargins:)]) {
        [cell setLayoutMargins:UIEdgeInsetsZero];
    }
}

#pragma mark - EGORefresh
/**
 *  开始加载数据，通过回调判断1、上拉更多 2、下拉刷新
 *
 *  @param aRefreshPos EGORefreshPos
 */
//-(void)beginToReloadData:(EGORefreshPos)aRefreshPos{
//    [super beginToReloadData:aRefreshPos];
//
//    if (aRefreshPos == EGORefreshHeader) {
//        // pull down to refresh data
//        [self performSelector:@selector(refreshDataSource) withObject:nil afterDelay:1.0];
//    }else if(aRefreshPos == EGORefreshFooter){
//        // pull up to load more data
//        [self performSelector:@selector(loadMoreDataSource) withObject:nil afterDelay:1.0];
//    }
//}

/**
 *  第一次请求数据
 */
-(void)firstLoadDataSource
{
    [_dataSource removeAllObjects];
    [self httpRequest];
}

/**
 *  重新刷新加载
 */
-(void)refreshDataSource{
    [_dataSource removeAllObjects];
    _currentIndex = 1;
    
    /**
     *  请求数据
     */
    [self httpRequest];
}

/**
 *  请求更多数据
 */
-(void)loadMoreDataSource{
    _currentIndex++;
    
    /**
     *  请求数据
     */
    [self httpRequest];
}



#pragma mark - HttpRequest
/**
 *   http
 */
- (void)httpRequest
{
    NSString* vcode = [gloabFunction getSign:@"listCardVip" strParams:self.cardCode];
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil||
       self.cardCode.length==0||self.cardCode==nil)
        return;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    NSDictionary *dic = @{@"cardCode":self.cardCode,
                          @"tokenCode":[gloabFunction getUserToken],
                          @"vcode":vcode,
                          @"userName":@"",
                          @"orderType":_orderType,
                          @"page":[NSString stringWithFormat:@"%d",_currentIndex],
                          @"reqtime": [gloabFunction getTimestamp]};
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"listCardVip" withParameters:dic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if(IsNOTNullOrEmptyOfDictionary(responseObject)){
            NSLog(@"respon=%@",responseObject);
            if(((NSArray *)[responseObject objectForKey:@"cardVipList"]).count!=0){
                [_dataSource addObjectsFromArray:[responseObject objectForKey:@"cardVipList"]];
                [self.tableView reloadData];
            }
            
            _left_count = [[responseObject objectForKey:@"totalCount"] intValue] - _dataSource.count;
            [self.tableView headerEndRefreshing];
            [self.tableView footerEndRefreshing];
//            [self.tableView footerendRefreshing];
//
//            if (_left_count>0) {
//            }else{
//                [self.tableView.footer noticeNoMoreData];
//            }
            
            NSString *deductionPrice = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"totalDeductionPrice"]];
            float floatstring = [deductionPrice floatValue];
            
            
            _countLabel.text = [NSString stringWithFormat:@"使用优惠券%d张   抵扣总额%.2f元",[[responseObject objectForKey:@"totalCouponUseAmount"] intValue],floatstring];
            
            /*无数据显示*/
            if(_dataSource.count==0){
                [self.tableView setHidden:YES];
                [self showEmptyView:NSLocalizedString(@"无会员信息", @"无会员信息")];
            }else{
                [self.tableView setHidden:NO];
                [self hideEmpthView];
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        CSAlert(error.localizedDescription);
    }];
}
@end
