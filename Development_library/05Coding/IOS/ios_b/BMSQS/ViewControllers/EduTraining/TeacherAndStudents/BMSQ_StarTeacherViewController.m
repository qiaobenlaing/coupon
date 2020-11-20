//
//  BMSQ_StarTeacherViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_StarTeacherViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "StarTeacherViewCell.h"
#import "BMSQ_TeacherDetailViewController.h"
#import "BMSQ_AddStarViewController.h"
@interface BMSQ_StarTeacherViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * baseView;
@property (nonatomic, strong) NSMutableArray * dataSource;
@property (nonatomic, assign)int page;

@end

@implementation BMSQ_StarTeacherViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavTitle:@"名师堂"];
    [self setNavBackItem];
    self.dataSource = [@[] mutableCopy];
     self.page = 1;
    [self customRightBtn];
    [self setViewUp];
    
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
//    [item setImage:[UIImage imageNamed:@"right_add"] forState:UIControlStateNormal];
    [item setTitle:@"增加" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemAddClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)setViewUp
{
    self.baseView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 64) style:UITableViewStyleGrouped];
    _baseView.dataSource = self;
    _baseView.delegate = self;
    [self.baseView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.baseView addFooterWithTarget:self action:@selector(footerRereshing)];
    [self.view addSubview:self.baseView];
    
    [self getShopTeacherList];
}


#pragma mark ----- UITableViewDataSource, UITableViewDelegate

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
    static NSString * cell_id = @"StarTeacherViewCellss";
    StarTeacherViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[StarTeacherViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
        [cell setCellValueDic:self.dataSource[indexPath.row]];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    BMSQ_TeacherDetailViewController * teacherVC = [[BMSQ_TeacherDetailViewController alloc] init];
    teacherVC.nameStr = self.dataSource[indexPath.row][@"teacherName"];
    teacherVC.teacherCode = self.dataSource[indexPath.row][@"teacherCode"];
    teacherVC.number = 1;
    [self.navigationController pushViewController:teacherVC animated:YES];
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * str = self.dataSource[indexPath.row][@"teacherInfo"];
    CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH/5*2 - 10, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:12.f]} context:nil].size;
    if (size.height > APP_VIEW_WIDTH/5*3 - 70) {
        return size.height + 70;
    }else{
        return APP_VIEW_WIDTH/5*3;
    }
    
}

#pragma mark -----  getShopTeacherList 名师堂列表
- (void)getShopTeacherList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:self.page] forKey:@"page"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopTeacherList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopTeacherList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        
        if(self.page == 1) {
            [self.dataSource removeAllObjects];
        }
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            [self.dataSource addObjectsFromArray:responseObject[@"shopTeacherList"]];
        }
        
        [self.baseView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self.baseView headerEndRefreshing];
        [self.baseView footerEndRefreshing];
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];
    

}


#pragma mark ---- 增加按钮
- (void)itemAddClick:(UIButton *)sender
{
    BMSQ_AddStarViewController * addStarVC = [[BMSQ_AddStarViewController alloc] init];
    [self.navigationController pushViewController:addStarVC animated:YES];
}

#pragma mark ---- 下拉刷新和上拉加载更多
- (void)headerRereshing{
    
    self.page = 1;
    [self getShopTeacherList];
}
- (void)footerRereshing{
    self.page ++;
    [self getShopTeacherList];
}







#pragma mark ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
