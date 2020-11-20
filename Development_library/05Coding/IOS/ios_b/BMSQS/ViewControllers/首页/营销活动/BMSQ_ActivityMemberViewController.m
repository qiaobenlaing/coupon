//
//  BMSQ_ActivityMemberViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivityMemberViewController.h"


#import "ActMemberTableViewCell.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"

@interface BMSQ_ActivityMemberViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, assign)int page;

@property (nonatomic, strong)UITableView *dataTable;
@property (nonatomic, strong)NSMutableArray  *dataArray;


@end

@implementation BMSQ_ActivityMemberViewController


-(void)viewDidDisappear:(BOOL)animated{
    [SVProgressHUD dismiss];

}

- (void)viewDidLoad {
    [super viewDidLoad];

    [super setNavBackItem];
    [super setNavTitle:@"报名人数"];
    
    self.page = 1;
    
    self.dataArray = [[NSMutableArray alloc]init];
    
    self.dataTable = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.dataTable.backgroundColor = [UIColor clearColor];
    self.dataTable.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.dataTable.dataSource =self;
    self.dataTable.delegate =self;
    [self.view addSubview:self.dataTable];
    [self.dataTable addFooterWithTarget:self action:@selector(footRefresh)];
    
    [self listShopCoupon];


}
#pragma mark UITableViewDelegate && UITableViewDataSource
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 120;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"Coupon__ActivityMemberCell";
    ActMemberTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell ==nil) {
        cell = [[ActMemberTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
    }
    [cell setActMemeberCell:[self.dataArray objectAtIndex:indexPath.row]];
    return cell;
}

- (void)listShopCoupon
{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.actCode forKey:@"actCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"listActParticipant" strParams:self.actCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"listActParticipant" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        weakSelf.page = weakSelf.page +1;
        
        if ([responseObject objectForKey:@"participantList"]) {
            [weakSelf.dataArray addObjectsFromArray:[responseObject objectForKey:@"participantList"]];
        }
        [weakSelf.dataTable reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

    }];
}
-(void)footRefresh{
    [self.dataTable footerEndRefreshing];
    [self listShopCoupon];
    
}

@end
