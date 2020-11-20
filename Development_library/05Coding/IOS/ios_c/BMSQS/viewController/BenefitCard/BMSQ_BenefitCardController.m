//
//  BMSQ_BenefitCardController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_BenefitCardController.h"

#import "BMSQ_ShopDetailController.h"
#import "BMSQ_ActvityDetailViewController.h"

#import "MJRefresh.h"
#import "SVProgressHUD.h"
#import "MobClick.h"
#import "benTopView.h"
#import "BMSQ_RecommendShopCell.h"
#import "Benefit_activityCell.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_ActivityWebViewController.h"

#define FOLLEWED 100
#define FOOT 101
#define NEAR 102
#define ACTIVITY 103

@interface BMSQ_BenefitCardController ()<UITableViewDataSource,UITableViewDelegate,benTopViewDelegate>


@property (nonatomic, strong)UITableView *benefiTableView;

@property (nonatomic, strong)NSMutableArray *FollowedShopArray;  //关注
@property (nonatomic, strong)NSMutableArray *listFootprintArray; //足迹
@property (nonatomic, strong)NSMutableArray *listNearShopArray;  //附近
@property (nonatomic, strong)NSMutableArray *actArray;  //活动



@property (nonatomic, assign)int courrenType;  //0 关注；1足迹；2附近
@property (nonatomic, assign)int follewedPage;
@property (nonatomic, assign)int listFootPage;
@property (nonatomic, assign)int listNearPage;
@property (nonatomic, assign)int actPage;

@property (nonatomic, assign)BOOL folleIsHave;
@property (nonatomic, assign)BOOL footIsHave;
@property (nonatomic, assign)BOOL NearIsHave;
@property (nonatomic, assign)BOOL actIsHave;


@property (nonatomic, strong)UIImageView *backImage;

@end

@implementation BMSQ_BenefitCardController

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"BenefitCard"];
    [[NSNotificationCenter defaultCenter]postNotificationName:@"text" object:nil];;
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        
    }else{
        
        if (self.FollowedShopArray.count ==0 && self.listFootprintArray.count ==0 && self.listNearShopArray.count ==0) {
            self.follewedPage = 1;
            [self listFollowedShop];
        }
    }

}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"BenefitCard"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.courrenType = FOLLEWED;
    self.follewedPage = 1;
    self.listFootPage = 1;
    self.listNearPage = 1;
    self.actPage = 1;
    
    [self setViewUp];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(logoff) name: @"logoff" object:nil];
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(frshFollewed) name: @"frshFollewed" object:nil];


}
-(void)frshFollewed{
    
    [self.FollowedShopArray removeAllObjects];
    
    self.follewedPage = 1;
    [SVProgressHUD showWithStatus:@"正在加载"];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",self.follewedPage] forKey:@"page"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listFollowedShop" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listFollowedShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
         [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"shopList"]) {
            [wself.FollowedShopArray addObjectsFromArray:[responseObject objectForKey:@"shopList"]];
            [wself.benefiTableView reloadData];
            wself.follewedPage += 1;
            NSArray *array = [responseObject objectForKey:@"shopList"];
            if (array.count<10) {
                wself.folleIsHave = NO;  //不让刷新
            }else{
                wself.folleIsHave = YES;
                
            }
            
            if (wself.FollowedShopArray.count ==0) {
                [wself showNoDataView];
            }else{
                [wself hiddenNoDataView];
            }
            
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        
    }];

}
-(void)logoff{
    self.courrenType = FOLLEWED;
    self.follewedPage = 1;
    self.listFootPage = 1;
    self.listNearPage = 1;
    
    [self.FollowedShopArray removeAllObjects];
    [self.listFootprintArray removeAllObjects];
    [self.listNearShopArray removeAllObjects];
    [self.benefiTableView reloadData];
    
    
    
}

- (void)setViewUp
{
    [self setNavigationBar];
    [self setNavTitle:@"圈广场"];
    self.FollowedShopArray = [[NSMutableArray alloc]init];
    self.listFootprintArray = [[NSMutableArray alloc]init];
    self.listNearShopArray = [[NSMutableArray alloc]init];
    self.actArray = [[NSMutableArray alloc]init];
    
    NSArray *array =@[@"关注",@"足迹",@"附近",@"活动"];// @[@"关注",@"足迹",@"附近"];
    benTopView *topView = [[benTopView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 45)];
    [topView createTopViewtitleArray:array];
    topView.typeDelegate = self;
    [self.view addSubview:topView];
    
    self.benefiTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, topView.frame.origin.y+topView.frame.size.height+5, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_TABBAR_HEIGHT-(topView.frame.origin.y+topView.frame.size.height)-5)];
    self.benefiTableView.backgroundColor = [UIColor clearColor];
    self.benefiTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.benefiTableView.dataSource = self;
    self.benefiTableView.delegate = self;
    [self.view addSubview:self.benefiTableView];
    
    self.m_noDataView.frame = self.benefiTableView.frame;
    
    
    [self.benefiTableView addFooterWithTarget:self action:@selector(footerRefreshing)];
    
    self.backImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 150, 150)];
    [self.backImage setImage:[UIImage imageNamed:@"iv_noData"]];
    [self.view addSubview:self.backImage];
    self.backImage.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
    self.backImage.hidden = YES;
    
    self.folleIsHave = YES;
    self.NearIsHave = YES;
    self.footIsHave = YES;
    
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }

}
#pragma mark
-(void)changeType:(int)i{

    self.courrenType = i;
    switch (i) {
        case FOLLEWED:
        {
            if (self.FollowedShopArray.count <= 0) {
                self.courrenType = FOLLEWED;
                [self listFollowedShop];
            }
            
            if (self.FollowedShopArray.count ==0) {
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
                
            }
        }
            break;
        case FOOT:
        {
            if (self.listFootprintArray.count <=0) {
                [self listFootprint];
                
            }
            
            if (self.listFootprintArray.count ==0) {
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
                
            }
        }
            break;
        case NEAR:
        {
            if (self.listNearShopArray.count <=0) {
                [self listNearShop];
                
            }
            
            if (self.listNearShopArray.count ==0) {
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
            }
        }
            break;
        case ACTIVITY:
        {
            if (self.actArray.count<=0) {
                [self getActList];
            }
            
        }
            break;
            
        default:
            break;
    }
    [self.benefiTableView reloadData];
    


}

-(void)footerRefreshing{
    
    if (self.courrenType ==FOLLEWED) {
        if(self.folleIsHave){
            self.courrenType = FOLLEWED;
            [self listFollowedShop];
        }else{
            [self.benefiTableView footerEndRefreshing];
        }
    }else if (self.courrenType == FOOT){
        if(self.folleIsHave){
            
            [self listFootprint];
        }else{
            [self.benefiTableView footerEndRefreshing];
            
        }
    }else if(self.courrenType == NEAR){
        if (self.NearIsHave) {
            [self listNearShop];
        }else{
            [self.benefiTableView footerEndRefreshing];
            
        }
    }else if(self.courrenType == NEAR){
    
        [self.benefiTableView footerEndRefreshing];

    }else{
        [self.benefiTableView footerEndRefreshing];
    }
    
    
    
}

#pragma mark --UITableViewDelegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (self.courrenType ==FOLLEWED) {
        
        return self.FollowedShopArray.count;
    }else if (self.courrenType == FOOT){
        
        return self.listFootprintArray.count;
    }else if(self.courrenType == NEAR){
        return self.listNearShopArray.count;
    }else if(self.courrenType == ACTIVITY){
        return self.actArray.count;
    }else{
        return 0;
    }
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{

    if (self.courrenType ==FOLLEWED) {
        return [BMSQ_RecommendShopCell cellHeigh:[self.FollowedShopArray objectAtIndex:indexPath.row ]];
    }else if (self.courrenType == FOOT){
        return [BMSQ_RecommendShopCell cellHeigh:[self.listFootprintArray objectAtIndex:indexPath.row ]];
    }else if(self.courrenType ==NEAR){
        return [BMSQ_RecommendShopCell cellHeigh:[self.listNearShopArray objectAtIndex:indexPath.row ]];
    }else if(self.courrenType == ACTIVITY){
        return 140;

    }
    
    return 0;

    
    
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.courrenType ==FOLLEWED ||self.courrenType ==FOOT|| self.courrenType == NEAR) {
        static NSString *cellIdentifier = @"RecommendShopCell";
        BMSQ_RecommendShopCell *shopCell = (BMSQ_RecommendShopCell *)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        
        if (shopCell == nil) {
            shopCell = [[BMSQ_RecommendShopCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            shopCell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        if (self.courrenType ==FOLLEWED) {
            NSDictionary *dic = self.FollowedShopArray[indexPath.row];
            [shopCell setCellValue:dic];
        }else if (self.courrenType == FOOT){
            NSDictionary *dic = self.listFootprintArray[indexPath.row];
            [shopCell setCellValue:dic];
        }else if(self.courrenType == NEAR){
            NSDictionary *dic = self.listNearShopArray[indexPath.row];
            [shopCell setCellValue:dic];
        }
        return shopCell;
    }
    else
    {
        if(self.courrenType == ACTIVITY){
            static NSString *actIdentifier = @"actIdentifier";
            Benefit_activityCell *cell = [tableView dequeueReusableCellWithIdentifier:actIdentifier];
            if (cell ==nil) {
                cell = [[Benefit_activityCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:actIdentifier];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
            }
            NSDictionary *dic = [self.actArray objectAtIndex:indexPath.row];
            [cell initActivityCell:dic];
            return cell;
        }else{
            static NSString *text = @"test";
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:text];
            if (cell == nil) {
                cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:text];
            }
            
            
            return cell;
        }
    }
 

}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (self.courrenType == FOLLEWED) {
        [self gotoDetailVC:[self.FollowedShopArray objectAtIndex:indexPath.row]];
    }else if (self.courrenType == FOOT){
        [self gotoDetailVC:[self.listFootprintArray objectAtIndex:indexPath.row]];

    }else if(self.courrenType == NEAR){
        [self gotoDetailVC:[self.listNearShopArray objectAtIndex:indexPath.row]];

    }else if(self.courrenType == ACTIVITY){
 
        NSDictionary *dic = [self.actArray objectAtIndex:indexPath.row];
        NSString *activityCode =[dic objectForKey:@"activityCode"];
        BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
        vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
        vc.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:vc animated:YES];
        
    }else {
        
    }
    
    
    
    
    
}
-(void)gotoDetail:(NSDictionary *)dic{
    [self gotoDetailVC:dic];
}
-(void)gotoDetailVC:(NSDictionary *)dic{
//    BMSQ_ShopDetailController* shopDetailCtrl = [[BMSQ_ShopDetailController alloc]init];
//    shopDetailCtrl.shopCode = [dic objectForKey:@"shopCode"];
//    shopDetailCtrl.shopName = [dic objectForKey:@"shopName"];
//    shopDetailCtrl.shopImage = [dic objectForKey:@"logoUrl"];
//    shopDetailCtrl.hidesBottomBarWhenPushed = YES;
//    [self.navigationController pushViewController:shopDetailCtrl animated:YES];
    
    BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
    detailCtrl.shopCode = [dic objectForKey:@"shopCode"];
    detailCtrl.userCode = [gloabFunction getUserCode];
    detailCtrl.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:detailCtrl animated:YES];
    
    
}
-(void)gotoActivity:(NSDictionary *)dic{
    
    RRC_webViewController *vc = [[RRC_webViewController alloc]init];
    vc.requestUrl = [NSString stringWithFormat:@"%@Browser/cGetActInfo/actCode/%@",H5_URL,[dic objectForKey:@"actCode"]];
    vc.navtitle = @"活动详情";
    vc.actCode =[dic objectForKey:@"actCode"];
    vc.isHidenNav = NO;
    vc.isShare = YES;
    vc.isJoin = YES;
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
    
}
#pragma mark --请求数据--
//关注
-(void)listFollowedShop{
    

    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeClear];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",self.follewedPage] forKey:@"page"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listFollowedShopB" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listFollowedShopB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"shopList"]) {
            [wself.FollowedShopArray addObjectsFromArray:[responseObject objectForKey:@"shopList"]];
            [wself.benefiTableView reloadData];
            wself.follewedPage += 1;
            NSArray *array = [responseObject objectForKey:@"shopList"];
            if (array.count<10) {
                wself.folleIsHave = NO;  //不让刷新
            }else{
                wself.folleIsHave = YES;
                
            }
            wself.courrenType = FOLLEWED;

            
            if (wself.FollowedShopArray.count ==0) {
                [wself showNoDataView];
            }else{
                [wself hiddenNoDataView];
            }
            
        }
       
        
        [self.benefiTableView footerEndRefreshing];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"请求错误"];

        [self.benefiTableView footerEndRefreshing];

    }];
    
}
//足迹
-(void)listFootprint{
    self.courrenType = FOOT;

    
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeClear];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",self.listFootPage]  forKey:@"page"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listFootprintB" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listFootprintB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
//        [SVProgressHUD showSuccessWithStatus:@"加载成功"];
        [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"shopList"]) {
            [wself.listFootprintArray addObjectsFromArray:[responseObject objectForKey:@"shopList"]];
            [wself.benefiTableView reloadData];
            wself.listFootPage += 1;
            
            NSArray *array = [responseObject objectForKey:@"shopList"];
            if (array.count<10) {
                wself.folleIsHave = NO;  //不让刷新
            }else{
                wself.folleIsHave = YES;
            }
            
            if (wself.listFootprintArray.count ==0) {
                [wself showNoDataView];
            }else{
                [wself hiddenNoDataView];
                
            }
            
        }
        [self.benefiTableView footerEndRefreshing];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"请求错误"];

        [self.benefiTableView footerEndRefreshing];

    }];
    
    
    
    
    
}
//附近
-(void)listNearShop{
    self.courrenType = NEAR;
    
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeClear];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSString *city = [userDefults objectForKey:SELECITY];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:city forKey:@"city"];

    [params setObject:[NSString stringWithFormat:@"%d",self.listNearPage]  forKey:@"page"];
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"listNearShopB" strParams:city];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"listNearShopB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"shopList"]) {
            [wself.listNearShopArray addObjectsFromArray:[responseObject objectForKey:@"shopList"]];
            [wself.benefiTableView reloadData];
            wself.listNearPage +=1;
            
            NSArray *array = [responseObject objectForKey:@"shopList"];
            if (array.count<10) {
                wself.NearIsHave = NO;  //不让刷新
            }else{
                wself.NearIsHave = YES;
            }
            
            if (wself.listNearShopArray.count ==0) {
                [wself showNoDataView];
            }else{
                [wself hiddenNoDataView];

            }
        }
        [self.benefiTableView footerEndRefreshing];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"请求错误"];
        
        [self.benefiTableView footerEndRefreshing];

    }];
}
-(void)getActList{
    self.courrenType = NEAR;
    
    [SVProgressHUD showWithStatus:@"正在加载" maskType:SVProgressHUDMaskTypeClear];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    
    [params setObject:[NSString stringWithFormat:@"%d",self.actPage]  forKey:@"page"];
    
    [self initJsonPrcClient:@"2"];

    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"getActList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        if ([responseObject objectForKey:@"activityList"]) {
            NSArray *array = [responseObject objectForKey:@"activityList"];
            wself.courrenType = ACTIVITY;
            [wself.actArray addObjectsFromArray:array];
            [wself.benefiTableView reloadData];
            wself.actPage +=1;
            
            if (array.count<10) {
                wself.actIsHave = NO;  //不让刷新
            }else{
                wself.actIsHave = YES;
            }
            
            if (wself.actArray.count ==0) {
                [wself showNoDataView];
            }else{
                [wself hiddenNoDataView];
                
            }
        }
        [self.benefiTableView footerEndRefreshing];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD showErrorWithStatus:@"请求错误"];
        
        [self.benefiTableView footerEndRefreshing];
        
    }];
}

@end
