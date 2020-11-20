//
//  BMSQ_CouponController.m
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponController.h"
#import "BMSQ_CuponHistoryCell.h"
#import <AGCommon/CMImageView.h>
#import <ShareSDK/ShareSDK.h>
#import <AGCommon/UINavigationBar+Common.h>
#import <AGCommon/UIImage+Common.h>
#import <AGCommon/UIColor+Common.h>
#import <AGCommon/UIDevice+Common.h>
#import <AGCommon/NSString+Common.h>
#import "BMSQ_ShopDetailController.h"
#import "BMSQ_CouponQRcodeControllerViewController.h"

#import "BMSQ_PayDetailViewController.h"   //外包



#import "BMSQ_PayDetailSViewController.h"   //lq
#import "BMSQ_QR.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "BMSQ_Share.h"
#import "MobClick.h"
#import "BMSQ_CouponDetailViewController.h"
#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_educationViewController.h"
@interface BMSQ_CouponController ()
{
    UIButton* btn_leftItem;
    UIButton* btn_rightItem;
    UIImageView* iv_topTitle;
    UITableView* m_tableView;
    
    //    NSMutableArray* m_tablSelectIndexPath;
    UIView* v_historyTop; //顶部历史类型选择
    UIButton* btn_invalid; //失效的
    UIButton* btn_used;//已使用的
    UIImageView* iv_topBack;
}

@property(nonatomic,strong)NSDictionary *contentDic;  //实物券 体验券 使用




@property(nonatomic,assign)int currentstauts;  //1 有效；1过期；2已使用

@property(nonatomic,assign)int effpage;// 有效
@property(nonatomic,assign)int exppage;// 过期
@property(nonatomic,assign)int usepage;// 使用

@property (nonatomic, strong)NSMutableArray *effArray;
@property (nonatomic, strong)NSMutableArray *expArray;
@property (nonatomic, strong)NSMutableArray *useArray;



@end

@implementation BMSQ_CouponController

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CouponCont"];
    if (![gloabFunction isLogin])
    {
        [self getLogin];

    }else{
        
//        if (self.effArray.count ==0) {
//            [self freshEff];
//        }else{
//            [m_tableView reloadData];
//        }
    }
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CouponCont"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.contentDic = [[NSDictionary alloc]init];
    
    [self setViewUp];
    if ([gloabFunction isLogin])
    {
        [self freshEff];
        
    }
    
    //刷新有效优惠券
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(freshEff) name:@"freshEff" object:nil];
    //退出登录
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(logoff) name: @"logoff" object:nil];
    //店铺详情对优惠券操作
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshData) name:@"refreshCoupon" object:nil];

}
-(void)refreshData{

//    [self.useArray removeAllObjects];
//    [self.effArray removeAllObjects];

}

//退出登录 数据清除
-(void)logoff{
    
    self.currentstauts = 1;
    
    self.effpage = 1;
    self.exppage = 1;
    self.usepage = 1;
    
    [self.effArray removeAllObjects];
    [self.expArray removeAllObjects];
    [self.useArray removeAllObjects];
    
    [m_tableView reloadData];
    
    
}
//刷新有效优惠券
-(void)freshEff{
    
    [self.effArray removeAllObjects];
    int page = 0;
    self.currentstauts = 1;
    page = 0;
    self.effpage = self.effpage +1;
    
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:@"" forKey:@"shopCode"];
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",page] forKey:@"page"];
    [params setObject:[NSString stringWithFormat:@"%d",self.currentstauts]  forKey:@"status"];
    NSString* vcode = [gloabFunction getSign:@"listUserCouponByStatus" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself =self;
    
    [self.jsonPrcClient invokeMethod:@"listUserCouponByStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [wself.effArray addObjectsFromArray:[responseObject objectForKey:@"userCouponList"]];
            
            if (wself.effArray.count ==0) {
                self.m_noDataView.frame = m_tableView.frame;
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
            }
            
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [SVProgressHUD dismiss];
        
    }];
    
}


- (void)setViewUp
{
    [self setNavigationBar];
    
    self.currentstauts = 1;
    
    self.effArray = [[NSMutableArray alloc]init];
    self.expArray = [[NSMutableArray alloc]init];
    self.useArray = [[NSMutableArray alloc]init];
    
    self.effpage = 1;
    self.exppage = 1;
    self.usepage = 1;
    
    iv_topTitle = [[UIImageView alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH - 174)/2, APP_STATUSBAR_HEIGHT + 6, 174, 30)];
    iv_topTitle.userInteractionEnabled = YES;
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    
    btn_leftItem = [[UIButton alloc]initWithFrame:CGRectMake(0, 1, 87, 30)];
    [btn_leftItem setBackgroundColor:[UIColor clearColor]];
    [btn_leftItem setTitle:@"有效的" forState:UIControlStateNormal];
    [btn_leftItem.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btn_leftItem setTitleColor:UICOLOR(168, 55, 58, 1.0) forState:UIControlStateSelected];
    [btn_leftItem setTitleColor:UICOLOR(255, 255, 255, 1.0) forState:UIControlStateNormal];
    btn_leftItem.selected = YES;
    [btn_leftItem addTarget:self action:@selector(btnLeftItemClick) forControlEvents:UIControlEventTouchUpInside];
    
    btn_rightItem = [[UIButton alloc]initWithFrame:CGRectMake(87, 1, 87, 30)];
    [btn_rightItem setTitle:@"历史的" forState:UIControlStateNormal];
    [btn_rightItem setTitleColor:UICOLOR(168, 55, 58, 1.0) forState:UIControlStateSelected];
    [btn_rightItem setTitleColor:UICOLOR(255, 255, 255, 1.0) forState:UIControlStateNormal];
    [btn_rightItem.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btn_rightItem addTarget:self action:@selector(btnRightItemClick) forControlEvents:UIControlEventTouchUpInside];
    
    [iv_topTitle addSubview:btn_leftItem];
    [iv_topTitle addSubview:btn_rightItem];
    [self setNavCustomerView:iv_topTitle];
    
        
    m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-self.tabBarController.tabBar.frame.size.height) ];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [m_tableView addFooterWithTarget:self action:@selector(footResh)];
    [self.view addSubview:m_tableView];
    
    v_historyTop = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 40)];
    v_historyTop.backgroundColor = [UIColor whiteColor];
    
    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
    iv_topBack.image = [UIImage imageNamed:@"iv_invalid"];
    [v_historyTop addSubview:iv_topBack];
    
    btn_invalid = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 40)];
    [btn_invalid setTitle:@"已过期" forState:UIControlStateNormal];
    [btn_invalid.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btn_invalid setTitleColor:UICOLOR(0, 0, 0, 1.0) forState:UIControlStateNormal];
    [btn_invalid setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateSelected];
    [btn_invalid addTarget:self action:@selector(btnInvalidClick) forControlEvents:UIControlEventTouchUpInside];
    btn_invalid.selected = YES;
    [v_historyTop addSubview:btn_invalid];
    
    btn_used = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, 40)];
    [btn_used setTitle:@"已使用" forState:UIControlStateNormal];
    [btn_used.titleLabel setFont:[UIFont systemFontOfSize:14]];
    [btn_used addTarget:self action:@selector(btnUseClick) forControlEvents:UIControlEventTouchUpInside];
    [btn_used setTitleColor:UICOLOR(0, 0, 0, 1.0) forState:UIControlStateNormal];
    [btn_used setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateSelected];
    [v_historyTop addSubview:btn_used];
    
    v_historyTop.hidden = YES;
    [self.view addSubview:v_historyTop];
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(getMyAvailableCoupon) name:@"REQUESTDATA" object:nil];
}
//1 有效；；2已使用  3过期
- (void)btnLeftItemClick  //有效
{
    v_historyTop.hidden = YES;
    
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_activity"]];
    btn_leftItem.selected = YES;
    btn_rightItem.selected = NO;
    m_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-self.tabBarController.tabBar.frame.size.height);
    self.currentstauts =1;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    if (self.effArray.count==0) {
        [self freshEff];

    }
    [m_tableView reloadData];
}

- (void)btnRightItemClick  //历史
{
    v_historyTop.hidden = NO;
    [iv_topTitle setImage:[UIImage imageNamed:@"iv_redPackage"]];
    btn_rightItem.selected = YES;
    btn_leftItem.selected = NO;
    m_tableView.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y+40, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT-self.tabBarController.tabBar.frame.size.height-40);
    if (self.expArray.count ==0) {
        [self getMyAvailableCoupon];
        
    }
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self btnInvalidClick];
}

- (void)btnInvalidClick  //失效
{
    btn_invalid.selected = YES;
    btn_used.selected = NO;
    self.currentstauts = 3;
    iv_topBack.image = [UIImage imageNamed:@"iv_invalid"];
    if (self.expArray.count ==0) {
        [self getMyAvailableCoupon];

    }
    [m_tableView reloadData];

}

- (void)btnUseClick  // 使用
{
    btn_invalid.selected = NO;
    btn_used.selected = YES;
    self.currentstauts = 2;
    iv_topBack.image = [UIImage imageNamed:@"iv_used"];
    if (self.useArray.count ==0) {
        self.usepage = 1;
        
        [self getMyAvailableCoupon];
    }
    [m_tableView reloadData];

    
}
-(void)footResh{
      [self getMyAvailableCoupon];
}



#pragma mark tableview dataSource and delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.currentstauts == 1) {
        if(self.effArray.count == 0){
            [self showNoDataView];
        }else{
            [self hiddenNoDataView];
        }
       return  self.effArray.count;
    }else if (self.currentstauts == 2){
        if(self.useArray.count == 0){
            [self showNoDataView];
        }else{
            [self hiddenNoDataView];
        }
        return self.useArray.count;
    }else if (self.currentstauts ==3){
        
        if(self.expArray.count == 0){
            [self showNoDataView];
        }else{
            [self hiddenNoDataView];
        }
        return self.expArray.count;
    }else{
        return 0;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSArray *m_dataSource = [[NSMutableArray alloc]init];
    if (self.currentstauts ==1) {
        m_dataSource = self.effArray;
    }else if (self.currentstauts ==3){
        m_dataSource = self.expArray;
    }else if (self.currentstauts ==2){
        m_dataSource = self.useArray;
    }
    
    
    
    NSDictionary *dic = [m_dataSource objectAtIndex:indexPath.row];
    
    if ([dic objectForKey:@"isSelect"]) {
        
        NSString *str = [dic objectForKey:@"isSelect"];
        if ([str isEqualToString:@"YES"]) {
            return 170;
            
        }else{
            return 100;
            
        }
        
    }else{
        return  100;
        
    }
    
    
}


//@property(nonatomic,assign)int effpage;// 有效
//@property(nonatomic,assign)int exppage;// 过期
//@property(nonatomic,assign)int usepage;// 使用

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (btn_leftItem.selected)
    {
        //操作区域
        static NSString *cellIdentifier = @"userCouponCell";
        BMSQ_CuponCell *cell = (BMSQ_CuponCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil)
        {
            
            cell = [[BMSQ_CuponCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.coupondelegate = self;
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
            
        }
        cell.isGrabvotes = NO;
        //status 1 有效的
        [cell setCellValue:[self.effArray objectAtIndex:indexPath.row] row:(int)indexPath.row couponStatus:1];
        return cell;
    }
    else
    {
        

        
        static NSString *cellIdentifier = @"userCouponCell";
        BMSQ_CuponCell *cell = (BMSQ_CuponCell*)[tableView dequeueReusableCellWithIdentifier: cellIdentifier];
        if (cell == nil)
        {
            
            cell = [[BMSQ_CuponCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.coupondelegate = self;
            cell.selectionStyle=UITableViewCellSelectionStyleNone ;
            
        }
        cell.isGrabvotes = NO;
        
        if (btn_invalid.selected)  //过期
        {
            [cell setCellValue:[self.expArray objectAtIndex:indexPath.row] row:(int)indexPath.row couponStatus:2];

        }
        else     //使用
        {
            [cell setCellValue:[self.useArray objectAtIndex:indexPath.row] row:(int)indexPath.row couponStatus:3];

        }

        
        return cell;
    }
    
}

-(void)clickCell:(NSDictionary *)dic currenRow:(int)row{
    BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
    couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
    couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[dic objectForKey:@"batchNbr"]];
    couponVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:couponVC animated:YES];

}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (btn_leftItem.selected)
    {
        BMSQ_CuponCell *cell = (BMSQ_CuponCell *)[tableView cellForRowAtIndexPath:indexPath];
        NSDictionary *dic =cell.couponDic;
        BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
        couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
        couponVC.CouponMessage = cell.couponMessage;
        couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[dic objectForKey:@"batchNbr"]];
        couponVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:couponVC animated:YES];

        
    }
    else
    {
        
        
        NSDictionary *couponDic;
        if (btn_invalid.selected)  //过期
        {
            couponDic = [self.expArray objectAtIndex:indexPath.row];
         }
        else     //使用
        {
            couponDic = [self.useArray objectAtIndex:indexPath.row];
         }
        
        
        BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
        couponVC.userCouponCode = [couponDic objectForKey:@"batchCouponCode"];
        couponVC.CouponNbr = [NSString stringWithFormat:@"券码批次:%@",[couponDic objectForKey:@"batchNbr"]];
        couponVC.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:couponVC animated:YES];

        
        
//        //isCatering 0-未知，1-餐饮，2-教育
//        int isCatering = [[NSString stringWithFormat:@"%@",[couponDic objectForKey:@"isCatering"]]intValue];
//        
//        if (isCatering == 2) {
//            BMSQ_educationViewController *vc = [[BMSQ_educationViewController alloc]init];
//            vc.shopCode = [couponDic objectForKey:@"shopCode"];
//            vc.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:vc animated:YES];
//        }else {
//            BMSQ_NewShopDetailViewController * detailCtrl = [[BMSQ_NewShopDetailViewController alloc] init];
//            detailCtrl.shopCode = [couponDic objectForKey:@"shopCode"];
//            detailCtrl.userCode = [gloabFunction getUserCode];
//            detailCtrl.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:detailCtrl animated:YES];
//            
//        }
        
        

        
    }
    
    
  

}

- (void)setCellSelect:(BOOL)isSelect indexPath:(int)row dic:(NSDictionary *)dicupon
{
    
    
    NSMutableDictionary *dic = [[NSMutableDictionary alloc]initWithDictionary:dicupon];
    
    if (isSelect)
    {
        [dic setObject:@"YES" forKey:@"isSelect"];
    }
    else
    {
        [dic setObject:@"NO" forKey:@"isSelect"];
        
    }

    if (self.currentstauts == 1) {
        [self.effArray replaceObjectAtIndex:row withObject:dic];
        
    }else if (self.currentstauts == 2){
        [self.useArray replaceObjectAtIndex:row withObject:dic];

    }else if (self.currentstauts ==3){
       [self.expArray replaceObjectAtIndex:row withObject:dic];

    }
    
    [m_tableView reloadData];
    
    
}

- (void)btnShareClick:(NSDictionary*)dicShare
{
    //创建分享内容
    
    NSNumber *couponType = [dicShare objectForKey:@"couponType"];

    NSString *str;
    
    if (couponType.intValue == 1)
    {
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    } else if (couponType.intValue == 3) {
        str = [NSString stringWithFormat:@"满%@元立减%@元",[dicShare objectForKey:@"availablePrice"],[dicShare objectForKey:@"insteadPrice"]];
        
    }else if (couponType.intValue == 4){

        str = [NSString stringWithFormat:@"满%@元打%0.1f折",[dicShare objectForKey:@"availablePrice"],[[dicShare objectForKey:@"discountPercent"] floatValue]];
        
    }else{
        str = [NSString stringWithFormat:@"%@",[dicShare objectForKey:@"function"]];
        
    }
    
    NSString *city = [dicShare objectForKey:@"city"];
    NSString *title = [NSString stringWithFormat:@"【 %@ 】我分享你一张优惠券，手快有，手慢无",city];
//    我分享你一张诺亚方舟电影院的优惠券，到惠圈，惠生活！
    NSString *shopName = [NSString stringWithFormat:@"%@", [dicShare objectForKey:@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@，我分享你一张%@的优惠券，到惠圈，惠生活！",[gloabFunction changeNullToBlank:str],shopName];
    
    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dicShare objectForKey:@"batchCouponCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    
    [BMSQ_Share shareClick:remark imagePath:imagePath title:title url:url];
}


-(void)tishiAction:(UIButton *)button{
    
    if (button.tag == 1000) {
        [button.superview removeFromSuperview];
        
    }else if(button.tag == 1001){
        
        
        BMSQ_PayDetailViewController *vc = [[BMSQ_PayDetailViewController alloc]init];
//        vc.messDic = self.contentDic
        vc.shopCode = [self.contentDic objectForKey:@"shopCode"];
        [self.navigationController pushViewController:vc animated:YES];
        
        
        [button.superview removeFromSuperview];
        
    }else{
        
        [button.superview removeFromSuperview];
    }
    
}



#pragma mark -- 请求有效券 历史券 --
- (void)getMyAvailableCoupon
{
    //1 有效；2已使用  3.过期；

    [SVProgressHUD showWithStatus:@"正在加载"];
    int page = 0;
    if (self.currentstauts == 1) {
        page = self.effpage;
        self.effpage = self.effpage +1;
    }else if (self.currentstauts ==2){
        page = self.usepage;
        self.usepage = self.usepage +1;
    }else if (self.currentstauts ==3){
        page = self.exppage;
        self.exppage = self.exppage +1;
    }
    if (page ==1) {
        [SVProgressHUD showWithStatus:@""];
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:@"" forKey:@"shopCode"];
    
    NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
    NSString *longitude = [userDefults objectForKey:LONGITUDE];
    NSString *latitude  = [userDefults objectForKey:LATITUDE];
    [params setObject:longitude forKey:@"longitude"];
    [params setObject:latitude forKey:@"latitude"];
    [params setObject:[NSString stringWithFormat:@"%d",page] forKey:@"page"];
    [params setObject:[NSString stringWithFormat:@"%d",self.currentstauts]  forKey:@"status"];
    NSString* vcode = [gloabFunction getSign:@"listUserCouponByStatus" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself =self;
    [self.jsonPrcClient invokeMethod:@"listUserCouponByStatus" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        [m_tableView footerEndRefreshing];
        
        if (wself.currentstauts ==1) {
            [wself.effArray addObjectsFromArray:[responseObject objectForKey:@"userCouponList"]];
            
            
            if (wself.effArray.count ==0) {
                self.m_noDataView.frame = m_tableView.frame;
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
            }
            
        }else if(wself.currentstauts ==2){
            [wself.useArray addObjectsFromArray:[responseObject objectForKey:@"userCouponList"]];
            
            if (wself.useArray.count ==0) {
                self.m_noDataView.frame = m_tableView.frame;
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
            }
            
            
        }else if (wself.currentstauts ==3){
            [wself.expArray addObjectsFromArray:[responseObject objectForKey:@"userCouponList"]];
            
            
            if (wself.expArray.count ==0) {
                self.m_noDataView.frame = m_tableView.frame;
                [self showNoDataView];
            }else{
                [self hiddenNoDataView];
            }
        }
        
        [m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [m_tableView footerEndRefreshing];
        
        [SVProgressHUD dismiss];
    }];
}

-(void)clickCell:(NSDictionary *)dic{

    BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
    couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];

    couponVC.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:couponVC animated:YES];

}


@end

