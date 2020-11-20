//
//  BMSQ_CouponReceiveViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponReceiveViewController.h"

#import "Coupon_ReceiveTableViewCell.h"

#import "SVProgressHUD.h"
@interface BMSQ_CouponReceiveViewController ()<UITableViewDelegate,UITableViewDataSource>

@property (nonatomic, strong)UITableView *receiveTable;
@property (nonatomic, strong)NSMutableArray *receiveData;
@property (nonatomic, assign)int page;

@property (nonatomic, strong)UIImageView *m_noDataView;

@end

@implementation BMSQ_CouponReceiveViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavBackItem];
    [self setNavTitle:@"优惠券领取详情"];
    
    self.receiveData = [[NSMutableArray alloc]init];
    self.page = 1;
    self.receiveTable = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];

    self.receiveTable.backgroundColor = [UIColor clearColor];
    self.receiveTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.receiveTable.dataSource =self;
    self.receiveTable.delegate =self;
    [self.view addSubview:self.receiveTable];
    [self.receiveTable addFooterWithTarget:self action:@selector(footRefresh)];
    
    
    self.m_noDataView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT)];
    self.m_noDataView.hidden = YES;
    self.m_noDataView.backgroundColor = [UIColor clearColor];;
    self.m_noDataView.image = [UIImage imageNamed:@"iv_noMessage"];
    
    
    [self.view addSubview:self.m_noDataView];
    [self.view bringSubviewToFront:self.m_noDataView];
    [self listGrabCoupon];


}
-(void)footRefresh{
    [self.receiveTable footerEndRefreshing];
    [self listGrabCoupon];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [SVProgressHUD dismiss];
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.receiveData.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 100;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"Coupon_ReceiveTableViewCell";
    Coupon_ReceiveTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[Coupon_ReceiveTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    [cell setReceiveCell:[self.receiveData objectAtIndex:indexPath.row]type:Coupon_receiveCell];
    return cell;
}

#pragma mark --请求数据--
-(void)listGrabCoupon{
   
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.batchCouponCode forKey:@"batchCouponCode"];
    [params setObject:[NSString stringWithFormat:@"%d",self.page] forKey:@"page"];
    
    NSString* vcode = [gloabFunction getSign:@"listGrabCoupon" strParams:self.batchCouponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
     __weak typeof(self) weakSelf = self;
    [self initJsonPrcClient:@"1"];
    [self.jsonPrcClient invokeMethod:@"listGrabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([responseObject objectForKey:@"count"]) {
            int count = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"count"]]intValue];
            if (count >0) {
                [weakSelf.receiveData addObjectsFromArray:[responseObject objectForKey:@"couponList"]];
                weakSelf.page = [[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"page"]]intValue]+1;
            }
            
            if (weakSelf.receiveData.count ==0) {
                weakSelf.m_noDataView.hidden = NO;
            }
            
            [weakSelf.receiveTable reloadData];
        }
        weakSelf.page = weakSelf.page +1;
        
        [SVProgressHUD dismiss];
        

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
}

@end
