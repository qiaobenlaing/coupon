//
//  BMSQ_MyActivityViewController.m
//  BMSQC
//
//  Created by liuqin on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_MyActivityViewController.h"

#import "benTopView.h"
#import "Benefit_activityCell.h"

#import "MJRefresh.h"
#import "BMSQ_ActivityWebViewController.h"
#import "BMSQ_ActvityDetailViewController.h"
#import "MobClick.h"

@interface BMSQ_MyActivityViewController ()<benTopViewDelegate,UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong)UITableView *actTableView;
@property (nonatomic, strong)NSMutableArray *processingS;//进行中
@property (nonatomic, strong)NSMutableArray *completeS;//完成
@property (nonatomic, strong)NSMutableArray *collectS;//收藏

@property (nonatomic, strong)NSString *currState;//当前状态
@property (nonatomic, strong)UIImageView *backImage;






@end

@implementation BMSQ_MyActivityViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"MyActivity"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"MyActivity"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"我的活动"];
    
    self.processingS = [[NSMutableArray alloc]init];
    self.completeS = [[NSMutableArray alloc]init];
    self.collectS = [[NSMutableArray alloc]init];
    
    NSArray *array = @[@"进行中",@"已完成",@"收藏"];
    benTopView *topView = [[benTopView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 45)];
    [topView createTopViewtitleArray:array];
    topView.typeDelegate = self;
    [self.view addSubview:topView];
    
    self.actTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, topView.frame.origin.y+topView.frame.size.height+5, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-(topView.frame.origin.y+topView.frame.size.height)-5)];
    self.actTableView.backgroundColor = [UIColor clearColor];
    self.actTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.actTableView.dataSource = self;
    self.actTableView.delegate = self;
    
    [self.actTableView addFooterWithTarget:self action:@selector(freshFoot)];
    [self.view addSubview:self.actTableView];
    self.backImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 150, 150)];
    [self.backImage setImage:[UIImage imageNamed:@"iv_noData"]];
    [self.view addSubview:self.backImage];
    self.backImage.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
    self.backImage.hidden = YES;

    
    
    self.currState = @"1";
    [self getUserActList];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if ([self.currState isEqualToString:@"1"]) {
        return self.processingS.count;
    }else if ([self.currState isEqualToString:@"2"]){
        return self.completeS.count;
    }else{
        return self.collectS.count;
    }
    return 10;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 140;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *actIdentifier = @"actIdentifier";
    Benefit_activityCell *cell = [tableView dequeueReusableCellWithIdentifier:actIdentifier];
    if (cell ==nil) {
        cell = [[Benefit_activityCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:actIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    if ([self.currState isEqualToString:@"1"]) {
        NSDictionary *dic = [self.processingS objectAtIndex:indexPath.row];
        [cell initActivityCell:dic];

    }else if ([self.currState isEqualToString:@"2"]){
        NSDictionary *dic = [self.completeS objectAtIndex:indexPath.row];
        [cell initActivityCell:dic];

    }else{
        NSDictionary *dic = [self.collectS objectAtIndex:indexPath.row];
        [cell initActivityCell:dic];
    }
    return cell;
    
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if ([self.currState isEqualToString:@"1"]) {
        NSDictionary *dic = [self.processingS objectAtIndex:indexPath.row];
        NSString *activityCode =[dic objectForKey:@"userActivityCode"];
        BMSQ_ActvityDetailViewController *vc = [[BMSQ_ActvityDetailViewController alloc]init];
        vc.userActivityCode = activityCode;
       [self.navigationController pushViewController:vc animated:YES];
    
    }else if ([self.currState isEqualToString:@"2"]){

        NSDictionary *dic = [self.completeS objectAtIndex:indexPath.row];
        NSString *activityCode =[dic objectForKey:@"activityCode"];
        BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
        vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
        [self.navigationController pushViewController:vc animated:YES];

    
    }else{
        
        NSDictionary *dic = [self.collectS objectAtIndex:indexPath.row];
        NSString *activityCode =[dic objectForKey:@"activityCode"];
        BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
        vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
        [self.navigationController pushViewController:vc animated:YES];
  
        
    }
    
}
-(void)freshFoot{
    
    [self.actTableView footerEndRefreshing];
}

-(void)changeType:(int)i{
    if(i==100){ //进行中
        self.currState = @"1";
        if (self.processingS.count==0) {
            [self getUserActList];
        }else{
            self.backImage.hidden = YES;
            [self.actTableView reloadData];
        }
        
    }else if (i==101){//完成
        self.currState = @"2";
        if (self.completeS.count ==0) {
            [self getUserActList];
        }else{
            self.backImage.hidden = YES;

            [self.actTableView reloadData];
        }

    }else if (i==102){//收藏
        self.currState = @"3";
        if (self.collectS.count==0) {
            [self getUserActList];
        }else{
            self.backImage.hidden = YES;

            [self.actTableView reloadData];
        }

    }
    
}

-(void)getUserActList{
    
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.currState forKey:@"type"];
    [params setObject:@"0" forKey:@"page"];

    NSString* vcode = [gloabFunction getSign:@"getUserActList" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getUserActList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        NSArray *array =[responseObject objectForKey:@"list"];
        
        if ([weakSelf.currState isEqualToString:@"1"]) {
            if (array.count>0) {
                [weakSelf.processingS addObjectsFromArray:array];
            }
            if (weakSelf.processingS.count==0) {
                weakSelf.backImage.hidden = NO;
            }else{
                weakSelf.backImage.hidden = YES;
            }
            
        }else if ([weakSelf.currState isEqualToString:@"2"]){
            if (array.count>0) {
                [weakSelf.completeS addObjectsFromArray:array];
            }
            
            if (weakSelf.completeS.count==0) {
                weakSelf.backImage.hidden = NO;
            }else{
                weakSelf.backImage.hidden = YES;
            }
            
        }else{
            if (array.count>0) {
                [weakSelf.collectS addObjectsFromArray:array];
            }
            
            if (weakSelf.collectS.count==0) {
                weakSelf.backImage.hidden = NO;
            }else{
                weakSelf.backImage.hidden = YES;
            }
            
        }
        
        [weakSelf.actTableView reloadData];
  
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];

    
    
}
@end
