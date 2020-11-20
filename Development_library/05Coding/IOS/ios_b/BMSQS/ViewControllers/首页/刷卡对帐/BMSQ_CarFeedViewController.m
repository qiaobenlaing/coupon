//
//  BMSQ_CarFeedViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CarFeedViewController.h"
#import "CardFeed_Cell.h"
#import "SVProgressHUD.h"
#import "BMSQ_OrderDetailEXViewController.h"
#import "MJRefresh.h"

@interface BMSQ_CarFeedViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView *myTableView;
@property (nonatomic, strong)NSMutableArray *dataArray;
@property (nonatomic, strong)UIImageView *m_noDataView;

@property (nonatomic, assign)int page;

@end

@implementation BMSQ_CarFeedViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavTitle:@"银行卡对账"];
    [self setNavBackItem];
    
    self.dataArray = [[NSMutableArray alloc]init];
    self.page = 1;
    self.myTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.myTableView.backgroundColor = [UIColor clearColor];
    self.myTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.myTableView.dataSource =self;
    self.myTableView.delegate =self;
    [self.view addSubview:self.myTableView];
    [self.myTableView addFooterWithTarget:self action:@selector(footRefresh)];
    
    
    self.m_noDataView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.m_noDataView.hidden = YES;
    self.m_noDataView.backgroundColor = [UIColor clearColor];;
    self.m_noDataView.image = [UIImage imageNamed:@"iv_noMessage"];
    
    [self.view addSubview:self.m_noDataView];
    [self.view bringSubviewToFront:self.m_noDataView];
    self.m_noDataView.hidden = YES;
    
    [self getShopOrderList];
    
}


-(void)footRefresh{
    
    [self getShopOrderList];
}

#pragma mark UITableViewDelegate && UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 110;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"CardFeed_Cell_idenfier";
    CardFeed_Cell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[CardFeed_Cell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    [cell setCareFeedCell:[self.dataArray objectAtIndex:indexPath.row]];
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    BMSQ_OrderDetailEXViewController *vc = [[BMSQ_OrderDetailEXViewController alloc]init];
    NSDictionary *dic = [self.dataArray objectAtIndex:indexPath.row];
    vc.consumeCode = [dic objectForKey:@"consumeCode"];
    if ([[dic objectForKey:@"usedCouponName"] length] > 0) {
        vc.coupon78 = YES;
    }
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}

-(void)getShopOrderList{
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.page] forKey:@"page"];

    NSString* vcode = [gloabFunction getSign:@"getAccount" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getAccount" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [self.myTableView footerEndRefreshing];
        [SVProgressHUD dismiss];
        
        weakSelf.page = weakSelf.page +1;
        if ([responseObject objectForKey:@"orderList"]) {
            [weakSelf.dataArray addObjectsFromArray:[responseObject objectForKey:@"orderList"]];
        }
        if (weakSelf.dataArray.count ==0) {
            weakSelf.m_noDataView.hidden = NO;
        }else{
            weakSelf.m_noDataView.hidden = YES;
        }
        
        [weakSelf.myTableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [self.myTableView footerEndRefreshing];
        [SVProgressHUD dismiss];
    }];
}

@end
