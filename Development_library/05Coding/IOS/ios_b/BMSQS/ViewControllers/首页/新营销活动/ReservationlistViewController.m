//
//  reservationlistViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ReservationlistViewController.h"
#import "ReservationlistViewCell.h"
#import "ListReservationlistViewCell.h"
#import "SVProgressHUD.h"
#import "UIImageView+WebCache.h"
#import "MJRefresh.h"
@interface ReservationlistViewController ()<UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSDictionary * dataSource;
@property (nonatomic, strong) NSArray * dataDetail;
@property (nonatomic, assign) int  page;
@property (nonatomic, strong) NSIndexPath * indexPath;
@property (nonatomic, assign) BOOL isSelect;


@end

@implementation ReservationlistViewController

- (void)viewDidLoad {
    [super viewDidLoad];
   
    [self setNavTitle:@"预定名单"];
    [self setNavBackItem];
    
    if (self.totalPayment.intValue == 0) {
        CSAlert(@"该活动为免费,无预定人员名单");
    }else{
        self.page = 1;
        self.isSelect = NO;
        [self listActParticipant];
        
    }
    
    
}


- (void)setViewTable
{
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
//    _baseView.rowHeight = 80;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    [self.baseView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.baseView addFooterWithTarget:self action:@selector(footerRereshing)];
    [self.view addSubview:self.baseView];
    
    
}
#pragma mark ---- UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataDetail.count == 0) {
        return 1;
    }
    return self.dataDetail.count;
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.isSelect && self.indexPath.row == indexPath.row) {
        
    static NSString * cell_id = @"ReservationlistViewCell";
    ReservationlistViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        
        cell = [[ReservationlistViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell setCellReservationWithDic:self.dataDetail[indexPath.row]];
    
    return cell;
        
        
    }else{
        
        static NSString * cell_id = @"ListReservationlistViewCell";
        ListReservationlistViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[ListReservationlistViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setCellReservationWithDic:self.dataDetail[indexPath.row]];
        
        return cell;

        
    }
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView reloadData];
    
    if (self.isSelect) {
        self.isSelect = NO;
    }else{
        self.isSelect = YES;
    }
    
    
    NSString * number = [NSString stringWithFormat:@"%@", self.dataDetail[indexPath.row][@"totalNbr"]];
    
    if (number.intValue != 0) {
        
        if (self.isSelect) {
            self.indexPath = indexPath;
            
        }
        
    }

    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray * number = [NSArray arrayWithArray:self.dataDetail[indexPath.row][@"feeScale"]];
    
    if (indexPath.row == self.indexPath.row) {
        if (self.isSelect) {
            return 85 + number.count * 30;
        }else{
            return 85;
        }
        
    }
    return 85;
}


- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

#pragma mark ------ listActParticipant($activityCode, $page)
- (void)listActParticipant
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.activityCode forKey:@"activityCode"];
    [params setObject:[NSString stringWithFormat:@"%d", self.page] forKey:@"page"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"listActParticipant" strParams:self.activityCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"listActParticipant" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        
        self.dataSource = [NSDictionary dictionaryWithDictionary:responseObject];
        self.dataDetail = [NSArray arrayWithArray:responseObject[@"participantList"]];
        if (self.dataDetail.count == 0) {
            CSAlert(@"暂时没有预定人员");
            
        }else{
            
            [self setViewTable];
        }
        
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}


#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    [self.baseView headerEndRefreshing];
    [self listActParticipant];
}
- (void)footerRereshing{
    [self.baseView footerEndRefreshing];
    [self listActParticipant];
}





#pragma mark ---- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
