//
//  BMSQ_MarketActivityController.m
//  BMSQS
//
//  Created by djx on 15/7/19.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MarketActivityController.h"
#import "BMSQ_MarketCell.h"
#import "BMSQ_RedPackageCell.h"
#import "BMSQ_ActivityDetailViewController.h"
#import "BMSQ_ActivitySettingViewController.h"
#import "BMSQ_RedPackageSettingViewController.h"
#import "BMSQ_ActivityMemberViewController.h"
#import "BMSQ_RedDetailViewController.h"
#import "BMSQ_redMemberViewController.h"

#import "MJRefresh.h"
#import "SVProgressHUD.h"

@interface BMSQ_MarketActivityController ()<BMSQ_MarketCellDelegate,BMSQ_RedPackageCellDelegate>
{
    NSMutableArray* m_tableDataSource;
    UITableView * m_tableView;
    int maxPage;
    int actPage;
    int redPage;
    UIButton* btn_activity;
    UIButton* btn_redPacket;
    UIImageView* iv_topTitle;
}

@property(nonatomic, strong)NSMutableArray *actArray;
@property(nonatomic, strong)NSMutableArray *redArray;

@property(nonatomic, strong)UIButton* btnSetting;


@property(nonatomic, strong)UITableView *m_tableView;
@property(nonatomic, strong)UIImageView *m_noDataView;

@end

@implementation BMSQ_MarketActivityController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.actArray = [[NSMutableArray alloc]init];
    self.redArray = [[NSMutableArray alloc]init];
    actPage = 1;
    redPage = 1;
    [self setViewUp];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(actRefresh) name:@"actRefresh" object:nil];
}
-(void)actRefresh{
    
    actPage = 1;
    [self.actArray removeAllObjects];
    [self GetActivityList];
}



- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavBackItem];
    
    actPage = 1;
    redPage = 1;
    
    m_tableDataSource = [[NSMutableArray alloc]init];
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSelectionStyleNone;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.m_tableView];
    [self.m_tableView addFooterWithTarget:self action:@selector(footRefresh)];
    
    self.m_noDataView = [[UIImageView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y)];
    self.m_noDataView.hidden = YES;
    self.m_noDataView.backgroundColor = [UIColor clearColor];;
    self.m_noDataView.image = [UIImage imageNamed:@"iv_noMessage"];
    
    [self.view addSubview:self.m_noDataView];
    [self.view bringSubviewToFront:self.m_noDataView];
    self.m_noDataView.hidden = YES;
    
    
    iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH - 174)/2, APP_STATUSBAR_HEIGHT + 6, 174, 30)];
    iv_topTitle.userInteractionEnabled = YES;
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    
    btn_activity = [[UIButton alloc]initWithFrame:CGRectMake(0, 1, 87, 30)];
    [btn_activity setBackgroundColor:[UIColor clearColor]];
    [btn_activity setTitle:@"活动" forState:UIControlStateNormal];
    [btn_activity setTitleColor:UICOLOR(168, 55, 58, 1.0) forState:UIControlStateSelected];
    [btn_activity setTitleColor:UICOLOR(255, 255, 255, 1.0) forState:UIControlStateNormal];
    btn_activity.selected = YES;
    [btn_activity addTarget:self action:@selector(btnActivityClick) forControlEvents:UIControlEventTouchUpInside];
    
    btn_redPacket = [[UIButton alloc]initWithFrame:CGRectMake(87, 1, 87, 30)];
    [btn_redPacket setTitle:@"红包" forState:UIControlStateNormal];
    [btn_redPacket setTitleColor:UICOLOR(168, 55, 58, 1.0) forState:UIControlStateSelected];
    [btn_redPacket setTitleColor:UICOLOR(255, 255, 255, 1.0) forState:UIControlStateNormal];
    [btn_redPacket addTarget:self action:@selector(btnRedPacketClick) forControlEvents:UIControlEventTouchUpInside];
    
    [iv_topTitle addSubview:btn_activity];
    [iv_topTitle addSubview:btn_redPacket];
    [self setNavCustomerView:iv_topTitle];
    
    [self GetActivityList];
    
    self.btnSetting = [UIButton buttonWithType:UIButtonTypeCustom];
    self.btnSetting.frame = CGRectMake(APP_VIEW_WIDTH - 50, APP_STATUSBAR_HEIGHT, 44, 44);
    [self.btnSetting setBackgroundImage:[UIImage imageNamed:@"iv_activityRight"] forState:UIControlStateNormal];
    [self.btnSetting addTarget:self action:@selector(btnSettingClick) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:self.btnSetting];
    
}

-(void)footRefresh{
    [self.m_tableView footerEndRefreshing];
    
    if (btn_activity.selected) {
        [self GetActivityList];
    }else{
        [self GetListBonus];
    }
    
    
}

- (void)btnSettingClick
{
//    if (btn_activity.selected) {
        BMSQ_ActivitySettingViewController *pushVC = [[BMSQ_ActivitySettingViewController alloc] initWithNibName:@"BMSQ_ActivitySettingViewController" bundle:nil];
        [self.navigationController pushViewController:pushVC animated:YES];
        
//    }else{
//        
//        BMSQ_RedPackageSettingViewController *pushVC = [[BMSQ_RedPackageSettingViewController alloc] init];
//        [self.navigationController pushViewController:pushVC animated:YES];
//    }
    
}

- (void)btnActivityClick
{
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    btn_activity.selected = YES;
    btn_redPacket.selected = NO;
    self.btnSetting.hidden = NO;


    
    if (self.redArray.count ==0) {
        [self GetActivityList];
        
    }
    [self.m_tableView reloadData];
}

- (void)btnRedPacketClick
{
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_redPackage"]];
    btn_redPacket.selected = YES;
    btn_activity.selected = NO;
    
    self.btnSetting.hidden = YES;
    
    if (self.redArray.count ==0) {
        [self GetListBonus];

    }
    [self.m_tableView reloadData];

}
#pragma mark ---------
- (void)GetActivityList
{
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:@"3" forKey:@"activityBelonging"];
    [params setObject:[NSNumber numberWithInt:actPage] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"sGetActivityList" strParams:@"3"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sGetActivityList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        actPage = actPage +1;
        if([responseObject objectForKey:@"activityList"]){
            
            [weakSelf.actArray addObjectsFromArray:[responseObject objectForKey:@"activityList"]];
        }
        
        if (weakSelf.actArray.count ==0) {
            self.m_noDataView.hidden  = NO;
        }else{
            self.m_noDataView.hidden  = YES;

        }
     
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

        int i =0 ;
        i++;
    }];
}

- (void)GetListBonus
{
    [SVProgressHUD showWithStatus:@""];

    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[NSNumber numberWithInt:redPage] forKey:@"page"];
    NSString* vcode = [gloabFunction getSign:@"listBonus" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"listBonus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];

        redPage = redPage+1;
        if([responseObject objectForKey:@"bonusList"]){
            [weakSelf.redArray addObjectsFromArray:[responseObject objectForKey:@"bonusList"]];
        }
        
        if (weakSelf.redArray.count ==0) {
            self.m_noDataView.hidden  = NO;
        }else{
            self.m_noDataView.hidden  = YES;
        }
        
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

        int i =0 ;
        i++;
    }];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 90;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (btn_activity.selected){

    return self.actArray.count;
    }else{
        return self.redArray.count;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (btn_activity.selected)
    {
        static NSString* cellIdentify = @"cellIdentify";
        BMSQ_MarketCell* cell = (BMSQ_MarketCell*)[m_tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[BMSQ_MarketCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }

        [cell setCellValue:[self.actArray objectAtIndex:indexPath.row]];
        cell.findDelegate = self;
        return cell;
    }
    else
    {
        static NSString* cellIdentify = @"cellRedPackageIdentify";
        BMSQ_RedPackageCell* cell = (BMSQ_RedPackageCell*)[m_tableView dequeueReusableCellWithIdentifier:cellIdentify];
        if (cell == nil)
        {
            cell = [[BMSQ_RedPackageCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentify];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            cell.redMebDelegate=self;
        }
        
        [cell setCellValue:[self.redArray objectAtIndex:indexPath.row]];
        return cell;
    }

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (btn_activity.selected) {
        
        BMSQ_ActivityDetailViewController *pushVC = [[BMSQ_ActivityDetailViewController alloc] init];
        pushVC.activityCode = [self.actArray objectAtIndex:indexPath.row][@"activityCode"];
        [self.navigationController pushViewController:pushVC animated:YES];
        
    }else{
        
        BMSQ_RedDetailViewController *pushVC = [[BMSQ_RedDetailViewController alloc] init];
        pushVC.activityCode = [self.redArray objectAtIndex:indexPath.row][@"bonusCode"];
        [self.navigationController pushViewController:pushVC animated:YES];
    }
   
}


-(void)findJoinPeople:(NSDictionary *)dic{
    BMSQ_ActivityMemberViewController *pushVC = [[BMSQ_ActivityMemberViewController alloc] init];
    pushVC.actCode = [dic objectForKey:@"activityCode"];
    [self.navigationController pushViewController:pushVC animated:YES];
}
-(void)gotofindRedMemeber:(NSDictionary *)dic{
    BMSQ_redMemberViewController *pushVC = [[BMSQ_redMemberViewController alloc] init];
    pushVC.bonusCode = [dic objectForKey:@"bonusCode"];
    [self.navigationController pushViewController:pushVC animated:YES];
    
}



@end
