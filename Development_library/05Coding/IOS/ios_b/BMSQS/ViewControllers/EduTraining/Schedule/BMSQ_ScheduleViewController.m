//
//  BMSQ_ ScheduleViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ScheduleViewController.h"
#import "BMSQ_ScheduleDetailViewController.h"
#import "ScheduleViewCell.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"

@interface BMSQ_ScheduleViewController ()<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UITableView * tableView;

@property (nonatomic, strong) NSMutableArray * dataSource;
//@property (nonatomic, strong) NSString * classCode;// 班级编码
//@property (nonatomic, strong) NSString * className;// 班级名称
//@property (nonatomic, strong) NSMutableArray * classWeekInfo; // 上课时间
//@property (nonatomic, strong) NSString * learnEndDate;//
//@property (nonatomic, strong) NSString * learnStartDate;//

@end

@implementation BMSQ_ScheduleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:@"课程表"];
    [self setNavBackItem];
    self.dataSource = [@[] mutableCopy];
    [self setViewUp];
    [self getShopClassList];
}

- (void)setViewUp
{
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 65) style:UITableViewStyleGrouped];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.tableView];
    
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
    static NSString * cell_id = @"ScheduleViewCellsdf";
    ScheduleViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[ScheduleViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    [cell.detailsBut addTarget:self action:@selector(detailsClick:) forControlEvents:UIControlEventTouchUpInside];
    [cell setCellWithScheduleDic:self.dataSource[indexPath.row]];
    
    return cell;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView * topView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
    topView.backgroundColor = [UIColor whiteColor];
    
    UILabel * classType = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, 40)];
    classType.text = @"所开班级";
    classType.textAlignment = NSTextAlignmentCenter;
    classType.font = [UIFont systemFontOfSize:12.0];
    [topView addSubview:classType];
    
    UILabel * lineLB1 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 10, 0, 1, 40)];
    lineLB1.backgroundColor = [UIColor grayColor];
    [topView addSubview:lineLB1];
    
    UILabel * goToClassTime = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 9, 0, (APP_VIEW_WIDTH - 3)/9*4 - 10, 40)];
    goToClassTime.text = @"上课时间";
    goToClassTime.textAlignment = NSTextAlignmentCenter;
    goToClassTime.font = [UIFont systemFontOfSize:12.0];
    [topView addSubview:goToClassTime];
    
    UILabel * lineLB2 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 19, 0, 1, 40)];
    lineLB2.backgroundColor = [UIColor grayColor];
    [topView addSubview:lineLB2];
    
    UILabel * teacherName = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 18, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, 40)];
    teacherName.text = @"任课老师";
    teacherName.textAlignment = NSTextAlignmentCenter;
    teacherName.font = [UIFont systemFontOfSize:12.0];
    [topView addSubview:teacherName];
    
    UILabel * lineLB3 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 28, 0, 1, 40)];
    lineLB3.backgroundColor = [UIColor grayColor];
    [topView addSubview:lineLB3];
    
    UILabel * detailsLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 27, 0, (APP_VIEW_WIDTH - 3)/9 + 27, 40)];
    detailsLB.text = @"查看详情";
    detailsLB.textAlignment = NSTextAlignmentCenter;
    detailsLB.font = [UIFont systemFontOfSize:12.0];
    [topView addSubview:detailsLB];
    
    return topView;
}



- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 40;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSMutableDictionary * dic = [NSMutableDictionary dictionaryWithDictionary:self.dataSource[indexPath.row]];
    NSArray * array = [NSArray arrayWithArray:dic[@"classWeekInfo"]];
    NSString * Str = @"";
    for (int i = 0; i<array.count; i++) {
        NSArray * newAry = [NSArray arrayWithArray:array[i][@"learnTime"]];
        NSString * weekStr = @"";
        NSString * string = @"";
        NSString * weekName = @"";
        for (int j = 0; j<newAry.count; j++) {
            NSString * learnStr = [NSString stringWithFormat:@"%@至%@        ", newAry[j][@"startTime"], newAry[j][@"endTime"]];
            string = [string stringByAppendingString:learnStr];
        }
        weekName = [NSString stringWithFormat:@"%@", array[i][@"weekName"]];
        weekStr = [weekName stringByAppendingString:string];
        
        Str = [Str stringByAppendingString:weekStr];
    }
    NSString * timeStr = [NSString stringWithFormat:@"%@",Str];
     CGSize size = [timeStr boundingRectWithSize:CGSizeMake((APP_VIEW_WIDTH - 3)/9*4 - 30, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:12.f]} context:nil].size;
    
    return size.height + 30;
    
}

#pragma mark ------  getShopClassList 课程表
- (void)getShopClassList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:@"0" forKey:@"page"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopClassList" strParams:[gloabFunction getShopCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getShopClassList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);
        
        
        if ([responseObject isKindOfClass:[NSArray class]]) {
            [self.dataSource addObjectsFromArray:responseObject];
        }
        
        if (self.dataSource.count == 0) {
            CSAlert(@"暂无课程信息");
        }
        
        [self.tableView reloadData];
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}

#pragma mark -------- detailsClick 详情
- (void)detailsClick:(UIButton *)sender
{
    ScheduleViewCell * cell = (ScheduleViewCell *)[sender superview];
    NSIndexPath * path = [self.tableView indexPathForCell:cell];
    BMSQ_ScheduleDetailViewController * VC = [[BMSQ_ScheduleDetailViewController alloc] init];
    VC.classCode = self.dataSource[path.row][@"classCode"];
    VC.className = self.dataSource[path.row][@"className"];
    [self.navigationController pushViewController:VC animated:YES];

}



#pragma mark ------ 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
