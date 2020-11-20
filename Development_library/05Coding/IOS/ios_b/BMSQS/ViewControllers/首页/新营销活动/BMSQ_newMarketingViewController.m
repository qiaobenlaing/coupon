//
//  BMSQ_newMarketingViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_newMarketingViewController.h"
#import "NewActivityViewController.h"

#import "NewMarketingViewCell.h"
#import "ActivityManagementViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
@interface BMSQ_newMarketingViewController ()<UITableViewDataSource, UITableViewDelegate>


@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic, assign)int page;
@property (nonatomic, strong)UIImageView * iconImage;


@end

@implementation BMSQ_newMarketingViewController

- (void)viewWillAppear:(BOOL)animated
{
    [self sGetActList];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"营销活动"];
    [self setNavBackItem];
    self.page = 1;
    [self customRightBtn];
    [self setViewUp];
    
    self.dataSource = [[NSMutableArray alloc] init];
    
//    [self sGetActList];
    
}


- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setImage:[UIImage imageNamed:@"right_add"] forState:UIControlStateNormal];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)setViewUp
{
    
    
    
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    _baseView.rowHeight = APP_VIEW_WIDTH*32.0/75 + 40;
    _baseView.dataSource = self;
    _baseView.delegate = self;
    [self.baseView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.baseView addFooterWithTarget:self action:@selector(footerRereshing)];
    [self.view addSubview:self.baseView];
    
    self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    _iconImage.image = [UIImage imageNamed:@"iv_noMessage"];
    [self.view addSubview:_iconImage];
    [self.view bringSubviewToFront:_iconImage];
    _iconImage.hidden = YES;
    
    [self sGetActList];
    
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
//    if (self.dataSource.count == 0) {
//        return 1;
//    }
    return self.dataSource.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"NewMarketingViewCell";
    NewMarketingViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[NewMarketingViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    
    [cell setCellWithDic:self.dataSource[indexPath.row]];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    ActivityManagementViewController * activityVC = [[ActivityManagementViewController alloc] init];
    activityVC.activityCode = self.dataSource[indexPath.row][@"activityCode"];
    activityVC.totalPayment = [NSString stringWithFormat:@"%@", self.dataSource[indexPath.row][@"totalPayment"]];
    [self.navigationController pushViewController:activityVC animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

#pragma mark ------  sGetActList($shopCode, $page)
- (void)sGetActList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"sGetActList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sGetActList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        
        if(self.page == 1) {
            [self.dataSource removeAllObjects];
        }
        
//        NSLog(@"%@", responseObject);
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            [self.dataSource addObjectsFromArray:responseObject[@"activityList"]];
        }
        
        if (self.dataSource.count < 1) {
            
//            CSAlert(@"暂无数据");
            self.iconImage.hidden = NO;
            [self.baseView reloadData];
            
        }else{
            self.iconImage.hidden = YES;
            [self.baseView reloadData];
            
            
        }

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

    
    
}










#pragma mark ---- 按钮方法
//添加营销活动
- (void)itemClick:(UIButton *)sender
{
    NSLog(@"添加营销活动");
    NewActivityViewController *activityVC = [[NewActivityViewController alloc] init];
    
    [self.navigationController pushViewController:activityVC animated:YES];

}

#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    self.page = 1;
    
    [self sGetActList];
}
- (void)footerRereshing{
    self.page ++;

    [self sGetActList];
}



#pragma mark ----- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
