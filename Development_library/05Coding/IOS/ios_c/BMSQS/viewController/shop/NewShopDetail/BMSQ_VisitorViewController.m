//
//  BMSQ_VisitorController.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/19.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_VisitorViewController.h"
#import "MJRefresh.h"
#import "VisitorViewCell.h"
#import "MobClick.h"
@interface BMSQ_VisitorViewController ()<UITableViewDataSource, UITableViewDelegate>

{
    int page;
}
@property (nonatomic, strong) UITableView * tableView;
@property (nonatomic, strong) NSMutableArray * dataSource;


@end

@implementation BMSQ_VisitorViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"VisitorView"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"VisitorView"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    [self setNavTitle:@"最近访问"];
    self.dataSource = [@[] mutableCopy];
    page = 1;
    
    if (self.visitorAry.count == 0) {
        
        CSAlert(@"最近没人访问");
        
    }else{
        
        [self setViewUp];
        
    }
    
    
}

- (void)setViewUp
{
    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    [self.tableView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.tableView addFooterWithTarget:self action:@selector(footerRereshing)];

    [self.view addSubview:self.tableView];
    
}

#pragma mark ------  UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.visitorAry.count < 20) {
        
        return self.visitorAry.count;
        
    }else{
        
        return 20;
    }
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"cellsjdh";
    VisitorViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[VisitorViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
    }
    
    [cell.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", IMAGE_URL,self.visitorAry[indexPath.row][@"avatarUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_detailNodata"]];
    cell.shopName.text = [NSString stringWithFormat:@"%@", self.visitorAry[indexPath.row][@"nickName"]];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 60;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1.0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}

#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    [self.dataSource removeAllObjects];
    page = 1;
    //    [self getStaffShopList];
    [self.tableView headerEndRefreshing];
    
}
- (void)footerRereshing{
    page ++;
    //    [self getStaffShopList];
    [self.tableView footerEndRefreshing];
}


#pragma mark ------ 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
