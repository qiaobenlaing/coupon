//
//  BMSQ_SelectShopViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_SelectShopViewController.h"
#import "SVProgressHUD.h"
#import "SelectShopModel.h"
#import "BMSQ_HomeController.h"
#import "MJRefresh.h"

@interface BMSQ_SelectShopViewController ()<UITableViewDataSource, UITableViewDelegate>
{
    int page;
    NSNumber * count;
}

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;

@end

@implementation BMSQ_SelectShopViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"选择店铺"];
    self.dataSource = [@[] mutableCopy];
    page = 1;
    
    [self getStaffShopList];
    [self setViewUp];
}

- (void)setViewUp
{
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y) style:UITableViewStyleGrouped];
    _baseView.rowHeight = (APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)/10;
    _baseView.dataSource = self;
    _baseView.delegate = self;
//    [self.baseView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.baseView addFooterWithTarget:self action:@selector(footerRereshing)];
    [self.view addSubview:self.baseView];
    
    
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataSource.count == 0) {
        return 0;
    }else{
        return self.dataSource.count;
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"cells";
    UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    if (self.dataSource.count > 1) {
        cell.textLabel.font = [UIFont systemFontOfSize:14.0];
        cell.textLabel.text = self.dataSource[indexPath.row][@"shopName"];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
//    BMSQ_HomeController * homeVC = [[BMSQ_HomeController alloc] init];
//    homeVC.shopCodeStr = ;
//    [self.navigationController pushViewController:homeVC animated:YES];
    
    
//    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
//    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
//    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"shopCode"]];

    
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSMutableDictionary* userDic = [NSMutableDictionary dictionaryWithDictionary:[SaveDefaults objectForKey:APP_USER_INFO_KEY]];
    [userDic setObject:self.dataSource[indexPath.row][@"shopCode"] forKey:@"shopCode"];
    [SaveDefaults setObject:userDic forKey:APP_USER_INFO_KEY];
    [SaveDefaults setObject:self.dataSource[indexPath.row][@"shopName"] forKey:@"shopName"];
    
    [SaveDefaults synchronize];
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"ShopNameFinish" object:nil];
    [[NSNotificationCenter defaultCenter]postNotificationName:@"setMyTitle" object:nil];
    [[NSNotificationCenter defaultCenter]postNotificationName:@"refrshCoupon" object:nil];
    [self.navigationController popViewControllerAnimated:YES];
    
    
}



- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1.0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}
#pragma mark ---- getStaffShopList 查询分店列表
// staffCode ,page,tokenCode
- (void)getStaffShopList
{
    [SVProgressHUD showWithStatus:@""];
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getStaffCode] forKey:@"staffCode"];
    
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getStaffShopList" strParams:[gloabFunction getStaffCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [params setObject:[NSNumber numberWithInt:page] forKey:@"page"];
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getStaffShopList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        SelectShopModel * model = [[SelectShopModel alloc] initWithInforDic:responseObject];
        count = model.count;
        [self.dataSource addObjectsFromArray:model.shopList];
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        [self.baseView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        
        CSAlert(@"数据刷新失败");
        
    }];


}

//#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    [self.dataSource removeAllObjects];
     page = 1;
    [self getStaffShopList];
}
- (void)footerRereshing{
    page ++;
    [self getStaffShopList];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
