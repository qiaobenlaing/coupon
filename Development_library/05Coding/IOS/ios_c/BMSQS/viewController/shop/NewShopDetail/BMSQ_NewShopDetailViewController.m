//
//  BMSQ_NewShopDetailViewController.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_NewShopDetailViewController.h"
#import "BMSQ_VisitorViewController.h"
#import "ShopLunboView.h"
#import "BMSQ_RecommendShopCell.h" // 附近其他商家
#import "Benefit_activityCell.h" // 商家活动
#import "DetailsViewCell.h"
#import "RecentlyVisitViewCell.h"
#import "GoodsViewCell.h"
#import "HomePageGoodsViewCell.h"
#import "BMSQ_PhotoViewController.h"
#import "BMSQ_MemberChartViewController.h"
#import "RRC_ShopViewController.h"
#import "BMSQ_PayDetailSViewController.h"
#import "NullViewCell.h"
#import "BMSQ_homeShopZeroRowCell.h"
#import "NewMyCouponViewCell.h"
#import "MobClick.h"
@interface BMSQ_NewShopDetailViewController ()<UITableViewDataSource, UITableViewDelegate, BMSQ_adCellDelegate,NewMyCouponViewCellDelegate, UIScrollViewDelegate>
{
    CGFloat _oldOffset;
    UIView * hearView;
    UIView * headerView;
    UILabel * shopNameLB;
    UIImageView * iconImage;
    UILabel * rateLB;
    UIButton * callPhoneBut;// 拨打电话
    UIButton * homePageBut;// 店铺首页
    UIButton * couponBut;// 优惠券
    UIButton * shopActivityBut;//店铺活动
    UIButton * goodsBut;//商品/服务
    
    UIButton * orderBut;//点餐
    UIButton * takeOutBut;//外卖
    UIButton * reserveBut;//预定
    UIButton * indentBut;//订单
    NSArray * shopCouponAry; // 商家优惠券列表
    NSArray * userCouponAry; // 用户优惠券列表
    int optionNum;// 选项卡标示，0:店铺首页 1:优惠券 2:店铺活动 3:商品/服务
    NSString * actionType;// 判断是提醒商家上商品（3）  还是邀请商家入驻（2）
    
    
}
@property (nonatomic, strong) UITableView * tableView;
@property (nonatomic, strong) ShopLunboView * shopLunboView;// 页面轮播

@property (nonatomic, strong) NSDictionary * shopInfoDic;//商店首页信息
@property (nonatomic, strong) NSDictionary * couponListDic;// 优惠券
@property (nonatomic, strong) NSMutableArray * shopDecorationAry;//商家环境图片
@property (nonatomic, strong) NSMutableArray * actListAry;// 商家活动
@property (nonatomic, strong) NSMutableArray * shopPhotoListAry;// 商品服务
@property (nonatomic, strong) NSMutableArray * recentVisitorAry;// 最近访问
@property (nonatomic, strong) NSMutableArray * aroundShopAry;// 附近商户

@property (nonatomic, strong)NSString *shopName;

@property (nonatomic, assign)BOOL isHaveData;

@end

@implementation BMSQ_NewShopDetailViewController

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    // #import "MobClick.h"
    [MobClick beginLogPageView:@"NewShopDetail"];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"NewShopDetail"];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(clickMyOrder) name:@"findOrderInfo" object:nil];
    self.view.backgroundColor = [UIColor clearColor];
    [self setNavigationBar];
    [self setNavBackItem];
    
    [self setNavBackGroundColor:[UIColor clearColor]];
    
    self.isHaveData = NO;
    [self customRightBtn];
    
    [self setViewUp];
    [self getShopInfo];// 获取店铺详情页API
    
    optionNum = 0; // 默认进入店铺首页
    //
    
}

#pragma mark --外卖/堂食支付完成这页面--
-(void)clickMyOrder{
    RRC_ShopViewController * webVC = [[RRC_ShopViewController alloc] init];
    webVC.isHidenNav = YES;
    webVC.requestUrl = [NSString stringWithFormat:@"%@Browser/cGetOrder?shopCode=%@&userCode=%@",H5_URL, self.shopInfoDic[@"shopCode"], self.userCode];
    [self.navigationController pushViewController:webVC animated:YES];
 
    
}
- (void)payButton
{

    UIButton * payBut = [UIButton buttonWithType:UIButtonTypeCustom];
    payBut.frame = CGRectMake(0, self.view.frame.size.height - 45, APP_VIEW_WIDTH, 45);
    payBut.layer.cornerRadius = 6.f;
    NSString * ifCanPay = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"ifCanPay"]];
    if (ifCanPay.intValue == 1) {
        [payBut setImage:[UIImage imageNamed:@"使用惠支付"] forState:UIControlStateNormal];
        [payBut addTarget:self action:@selector(payButClick:) forControlEvents:UIControlEventTouchUpInside];
        payBut.backgroundColor = [UIColor clearColor];
    }else if (ifCanPay.intValue == 0){
        [payBut setImage:[UIImage imageNamed:@"使用惠支付-不可点击"] forState:UIControlStateNormal];
        payBut.backgroundColor = [UIColor clearColor];
    }

    [self.view addSubview:payBut];
}

- (void)customRightBtn
{
    UIButton * shareItem = [UIButton buttonWithType:UIButtonTypeCustom];
    shareItem.frame = CGRectMake(APP_VIEW_WIDTH - 70, 25, 30, 30);
    [shareItem setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [shareItem setImage:[UIImage imageNamed:@"icon_share"] forState:UIControlStateNormal];
    [shareItem addTarget:self action:@selector(shareClick:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton * chartItem = [UIButton buttonWithType:UIButtonTypeCustom];
    chartItem.frame = CGRectMake(APP_VIEW_WIDTH - 30, 25, 30, 30);
    [chartItem setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [chartItem setImage:[UIImage imageNamed:@"icon_chat"] forState:UIControlStateNormal];
    [chartItem addTarget:self action:@selector(chartClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:shareItem];
    [self setNavRightBarItem:chartItem];
    
}

#pragma mark ----- BMSQ_adCellDelegate
-(void)clickAD:(int)tag
{
    BMSQ_PhotoViewController *vc = [[BMSQ_PhotoViewController alloc]init];
    vc.shopCode = self.shopCode;
    [self.navigationController pushViewController:vc animated:YES];
}
#pragma mark ---- 按钮的触发方法
- (void)shareClick:(UIButton *)sender
{
    NSString *title = [NSString stringWithFormat:@"【 %@ 】这家店服务很好,而去还很优惠哦!",self.shopInfoDic[@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"shortDes"]];
    
    NSString* url = [NSString stringWithFormat:@"%@Browser/getShopInfo/shopCode/%@/userCode/%@/operationType",H5_URL,self.shopCode,[gloabFunction getUserCode]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    [BMSQ_Share shareClick:remark imagePath:imagePath title:title url:url];
    [MobClick event:@"NewShopDetail_Share"]; // 友盟统计
    
    
}

- (void)chartClick:(UIButton *)sender
{
    if(![gloabFunction isLogin]){
        [self getLogin];
        
    }else{
    BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
    vc.shopID = self.shopCode;
    vc.myTitle = [NSString stringWithFormat:@"给%@留言", self.shopInfoDic[@"shopName"]];
    vc.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:vc animated:YES];
    [MobClick event:@"NewShopDetail_Chart"]; // 友盟统计
    }
}

#pragma mark --点击使用惠支付按扭--
- (void)payButClick:(UIButton *)sender // 使用惠支付
{
    if(![gloabFunction isLogin]){
        [self getLogin];
        
    }else{
        
        BMSQ_PayDetailSViewController * payDetailVC = [[BMSQ_PayDetailSViewController alloc] init];
        payDetailVC.shopCode = self.shopCode;
        payDetailVC.shopName = self.shopName;
        payDetailVC.fromVC = (int)self.navigationController.viewControllers.count;
        [self.navigationController pushViewController:payDetailVC animated:YES];
        [MobClick event:@"NewShopDetail_Pay"]; // 友盟统计
    }
    
    
}

- (void)callPhoneButClick:(UIButton *)sender
{
    NSLog(@"拨打电话");
    
    UIWebView *callWebView = [[UIWebView alloc] init];
    NSNumber * number = self.shopInfoDic[@"tel"];
    NSURL *telURL = [NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",number]];
    [callWebView loadRequest:[NSURLRequest requestWithURL:telURL]];
    [self.view addSubview:callWebView];
    [MobClick event:@"NewShopDetail_Call"]; // 友盟统计
    
}
- (void)homePageButClick:(UIButton *)sender
{
    NSLog(@"店铺首页");
    optionNum = 0;
    [self.tableView reloadData];
        [homePageBut setTitleColor:UICOLOR(166, 16, 21, 1.0) forState:UIControlStateNormal];
    [couponBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [shopActivityBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [goodsBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];

}

- (void)couponButClick:(UIButton *)sender
{
    NSLog(@"优惠券");
    optionNum = 1;
    [self.tableView reloadData];
    [homePageBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [couponBut setTitleColor:UICOLOR(166, 16, 21, 1.0) forState:UIControlStateNormal];
     [shopActivityBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [goodsBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
}
#pragma mark
- (void)shopActivityButClick:(UIButton *)sender
{
    NSLog(@"店铺活动");
    optionNum = 2;
    [self.tableView reloadData];
    [shopActivityBut setTitleColor:UICOLOR(166, 16, 21, 1.0) forState:UIControlStateNormal];
    [homePageBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [couponBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [goodsBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
}
- (void)goodsButClick:(UIButton *)sender
{
    NSLog(@"商品/服务");
    optionNum = 3;
    [self.tableView reloadData];
    [goodsBut setTitleColor:UICOLOR(166, 16, 21, 1.0) forState:UIControlStateNormal];
    [shopActivityBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [homePageBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [couponBut setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    
}
#pragma mark --- 点餐 ---
- (void)orderButClick:(UIButton *)sender
{
    RRC_ShopViewController * webVC = [[RRC_ShopViewController alloc] init];
    webVC.isHidenNav = YES;
    webVC.requestUrl = [NSString stringWithFormat:@"%@Browser/cEatOrder?shopCode=%@&userCode=%@",H5_URL, self.shopInfoDic[@"shopCode"], self.userCode];
    [self.navigationController pushViewController:webVC animated:YES];
    [MobClick event:@"NewShopDetail_Order"]; // 友盟统计
    
}
#pragma mark --- 外卖 ---
- (void)takeOutButClick:(UIButton *)sender
{
    RRC_ShopViewController * webVC = [[RRC_ShopViewController alloc] init];
    webVC.isHidenNav = YES;
    webVC.requestUrl = [NSString stringWithFormat:@"%@Browser/cTakeawayOrder?shopCode=%@&userCode=%@",H5_URL, self.shopInfoDic[@"shopCode"], self.userCode];
    [self.navigationController pushViewController:webVC animated:YES];
    [MobClick event:@"NewShopDetail_TakeOut"]; // 友盟统计
    
}
#pragma mark --- 预定 ---
- (void)reserveButClick:(UIButton *)sender
{
    CSAlert(@"敬请期待");
}
#pragma mark --- 订单 ---
- (void)indentButClick:(UIButton *)sender
{
    RRC_ShopViewController * webVC = [[RRC_ShopViewController alloc] init];
    webVC.isHidenNav = YES;
    webVC.requestUrl = [NSString stringWithFormat:@"%@Browser/cGetOrder?shopCode=%@&userCode=%@",H5_URL, self.shopInfoDic[@"shopCode"], self.userCode];
    [self.navigationController pushViewController:webVC animated:YES];
    [MobClick event:@"NewShopDetail_Indent"]; // 友盟统计
}

- (void)alertButClick:(UIButton *)sender
{
    NSLog(@"提醒更新服务");
    actionType = @"3";
    [self remindToShop];

    
}

#pragma mark ----- setViewUp
- (void)setViewUp
{
    
    
    self.shopLunboView = [[ShopLunboView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT / 2)];
    _shopLunboView.adDelegate = self;
    NSArray *subList = [NSArray arrayWithArray:self.shopDecorationAry];
    [_shopLunboView setHomeAdCell:subList height:APP_VIEW_HEIGHT / 2];

    
    UIView * botView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
    botView.backgroundColor = [UIColor clearColor];// 

    
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, self.view.frame.size.height) style:UITableViewStyleGrouped];
    //  APP_VIEW_ORIGIN_Y  APP_VIEW_HEIGHT - 65
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    _tableView.delegate = self;
    _tableView.dataSource = self;
    _tableView.tableHeaderView = self.shopLunboView;
    
    NSString * showPayBtn = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"showPayBtn"]];
    if (showPayBtn.intValue == 1){
        
        _tableView.tableFooterView = botView;
    }
    [self.view insertSubview:self.tableView belowSubview:self.navigationView];
    
    [self topUpView];
    
    
}

- (void)topUpView
{
    NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
    if (isCatering.intValue == 0) {
        hearView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 85)];
    }else if (isCatering.intValue == 1){
        hearView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 155)];
    }
    
    hearView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:hearView];
    [self.view bringSubviewToFront:hearView];
    hearView.hidden = YES;
    
    UIView * topShopView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
    topShopView.backgroundColor = [UIColor whiteColor];
    [hearView addSubview:topShopView];
    
    shopNameLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH/5*3, 30)];
    shopNameLB.font = [UIFont boldSystemFontOfSize:15.0];
    shopNameLB.text = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"shopName"]];
    [topShopView addSubview:shopNameLB];
    
    NSString * online = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"onlinePaymentDiscount"]];// 当为10则没有折扣
    if (online.intValue != 10) {
        
        iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 10, 10, 20, 20)];
        iconImage.image = [UIImage imageNamed:@"ICBCcardicon"];
        [topShopView addSubview:iconImage];
        rateLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 40, 10, 35, 20)];
        rateLB.font = [UIFont systemFontOfSize:12.0];
        rateLB.text = [NSString stringWithFormat:@"%.1f折",[online floatValue]];
        rateLB.textColor = UICOLOR(183, 34, 26, 1.0);
        [topShopView addSubview:rateLB];
    }
    
    callPhoneBut = [UIButton buttonWithType:UIButtonTypeCustom];
    callPhoneBut.frame = CGRectMake(APP_VIEW_WIDTH - 40, 5, 30, 30);
    [callPhoneBut addTarget:self action:@selector(callPhoneButClick:) forControlEvents:UIControlEventTouchUpInside];
    [callPhoneBut setImage:[UIImage imageNamed:@"phoneIcon"] forState:UIControlStateNormal];
    [topShopView addSubview:callPhoneBut];
    
    UIView * middleView = [[UIView alloc] initWithFrame:CGRectMake(0, 45, APP_VIEW_WIDTH, 60)];
    middleView.backgroundColor = [UIColor whiteColor];
    if (isCatering.intValue == 1){
        [hearView addSubview:middleView];
    }
    
    
    orderBut = [UIButton buttonWithType:UIButtonTypeCustom];
    orderBut.frame = CGRectMake(20, 5, (APP_VIEW_WIDTH - 160)/4 - 10, 30);
    [orderBut addTarget:self action:@selector(orderButClick:) forControlEvents:UIControlEventTouchUpInside];
    [orderBut setImage:[UIImage imageNamed:@"take_order"] forState:UIControlStateNormal];
    [middleView addSubview:orderBut];
    
    UILabel * orderLB = [[UILabel alloc] initWithFrame:CGRectMake(20, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
    orderLB.text = @"点餐";
    orderLB.textColor = UICOLOR(71, 71, 71, 1.0);
    orderLB.textAlignment = NSTextAlignmentCenter;
    orderLB.font = [UIFont systemFontOfSize:12.0];
    [middleView addSubview:orderLB];
    
    takeOutBut = [UIButton buttonWithType:UIButtonTypeCustom];
    takeOutBut.frame = CGRectMake((APP_VIEW_WIDTH - 160)/4 + 60, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
    [takeOutBut addTarget:self action:@selector(takeOutButClick:) forControlEvents:UIControlEventTouchUpInside];
    [takeOutBut setImage:[UIImage imageNamed:@"take_out"] forState:UIControlStateNormal];
    [middleView addSubview:takeOutBut];
    
    UILabel * takeOutLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/4 + 60, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
    takeOutLB.text = @"外卖";
    takeOutLB.textColor = UICOLOR(71, 71, 71, 1.0);
    takeOutLB.textAlignment = NSTextAlignmentCenter;
    takeOutLB.font = [UIFont systemFontOfSize:12.0];
    [middleView addSubview:takeOutLB];
    
    reserveBut = [UIButton buttonWithType:UIButtonTypeCustom];
    reserveBut.frame = CGRectMake((APP_VIEW_WIDTH - 160)/2 + 100, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
    [reserveBut addTarget:self action:@selector(reserveButClick:) forControlEvents:UIControlEventTouchUpInside];
    [reserveBut setImage:[UIImage imageNamed:@"book_food"] forState:UIControlStateNormal];
    [middleView addSubview:reserveBut];
    
    UILabel * reserveLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/2 + 100, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
    reserveLB.text = @"预定";
    reserveLB.textColor = UICOLOR(71, 71, 71, 1.0);
    reserveLB.textAlignment = NSTextAlignmentCenter;
    reserveLB.font = [UIFont systemFontOfSize:12.0];
    [middleView addSubview:reserveLB];
    
    indentBut = [UIButton buttonWithType:UIButtonTypeCustom];
    indentBut.frame = CGRectMake((APP_VIEW_WIDTH - 160)/4*3 + 140, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
    [indentBut addTarget:self action:@selector(indentButClick:) forControlEvents:UIControlEventTouchUpInside];
    [indentBut setImage:[UIImage imageNamed:@"order_food"] forState:UIControlStateNormal];
    [middleView addSubview:indentBut];
    
    UILabel * indentLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/4*3 + 140, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
    indentLB.text = @"订单";
    indentLB.textColor = UICOLOR(71, 71, 71, 1.0);
    indentLB.textAlignment = NSTextAlignmentCenter;
    indentLB.font = [UIFont systemFontOfSize:12.0];
    [middleView addSubview:indentLB];
    
    if (isCatering.intValue == 0) {
        orderBut.hidden = YES;
        orderLB.hidden = YES;
        takeOutBut.hidden = YES;
        takeOutLB.hidden = YES;
        reserveBut.hidden = YES;
        reserveLB.hidden = YES;
        indentBut.hidden = YES;
        indentLB.hidden = YES;
    }
    
    UIView * bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, hearView.frame.size.height - 40, APP_VIEW_WIDTH, 35)];
    bottomView.backgroundColor = [UIColor whiteColor];
    [hearView addSubview:bottomView];
    
    homePageBut = [UIButton buttonWithType:UIButtonTypeCustom];
    homePageBut.frame = CGRectMake(5, 5, (APP_VIEW_WIDTH-40)/4, 30);
    [homePageBut setTitle:@"店铺首页" forState:UIControlStateNormal];
    if (optionNum == 0) {
        [homePageBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [homePageBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    homePageBut.titleLabel.font = [UIFont systemFontOfSize: 14.0];
    [homePageBut addTarget:self action:@selector(homePageButClick:) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:homePageBut];
    
    couponBut = [UIButton buttonWithType:UIButtonTypeCustom];
    couponBut.frame = CGRectMake((APP_VIEW_WIDTH-40)/4 + 15, 5, (APP_VIEW_WIDTH-40)/4, 30);
    [couponBut setTitle:@"优惠券" forState:UIControlStateNormal];
    if (optionNum == 1) {
        [couponBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [couponBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    couponBut.titleLabel.font = [UIFont systemFontOfSize: 14.0];
    [couponBut addTarget:self action:@selector(couponButClick:) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:couponBut];
    
    shopActivityBut = [UIButton buttonWithType:UIButtonTypeCustom];
    shopActivityBut.frame = CGRectMake((APP_VIEW_WIDTH-40)/2 + 25, 5, (APP_VIEW_WIDTH-40)/4, 30);
    [shopActivityBut setTitle:@"店铺活动" forState:UIControlStateNormal];
    if (optionNum == 2) {
        [shopActivityBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [shopActivityBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    shopActivityBut.titleLabel.font = [UIFont systemFontOfSize: 14.0];
    [shopActivityBut addTarget:self action:@selector(shopActivityButClick:) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:shopActivityBut];
    
    goodsBut = [UIButton buttonWithType:UIButtonTypeCustom];
    goodsBut.frame = CGRectMake((APP_VIEW_WIDTH-40)/4*3 + 35, 5, (APP_VIEW_WIDTH-40)/4, 30);
    [goodsBut setTitle:@"商品/服务" forState:UIControlStateNormal];
    if (optionNum == 3) {
        [goodsBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
    }else{
        [goodsBut setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
    }
    goodsBut.titleLabel.font = [UIFont systemFontOfSize: 14.0];
    [goodsBut addTarget:self action:@selector(goodsButClick:) forControlEvents:UIControlEventTouchUpInside];
    [bottomView addSubview:goodsBut];
    
    
    shopCouponAry = [NSArray arrayWithArray:self.couponListDic[@"shopCoupon"]];
    userCouponAry = [NSArray arrayWithArray:self.couponListDic[@"userCoupon"]];
    

}


#pragma mark ----  UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (optionNum == 0) {
        
        return 5;
        
    }else if (optionNum == 1){
        
        if (shopCouponAry.count != 0 && userCouponAry.count != 0){
            
            return 3;
            
        }else if ((shopCouponAry.count != 0 && userCouponAry.count == 0) || (shopCouponAry.count == 0 && userCouponAry.count != 0)){
            
            return 2;
        }else{
            
            return 1;
        }
        
    }else if (optionNum == 2){
        
        return 1;
        
    }else if (optionNum == 3){
        if (self.shopPhotoListAry.count == 0) {
            return 1;
        }
        return 1;
    }else{
        return 0;
    }
    
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (optionNum == 0) {
        
        if (section == 0 || section == 2) {
            return 2;
        }else if (section == 1){
            
            if (self.shopPhotoListAry.count == 0) {// 当该店家没有商品或服务时，该区返回一行
                return 1;
            }else{
                return 2;
            }
                        
        }else if (section == 4){
            if (self.aroundShopAry.count == 0) {
                return 0;
            }else{
                return self.aroundShopAry.count;
            }
            
        }
        return 1;
        
    }else if (optionNum == 1){
        if (shopCouponAry.count != 0 && userCouponAry.count != 0){
            
            if (section == 1) {
                return userCouponAry.count;
            }else if (section == 2){
                return shopCouponAry.count;
            }else{
                return 0;
            }
            
        }else if (shopCouponAry.count != 0 && userCouponAry.count == 0){
            if (section == 0) {
                return 0;
            }else{
                return shopCouponAry.count;
            }
            
        }else if (shopCouponAry.count == 0 && userCouponAry.count != 0){
            if (section == 0) {
                return 0;
            }else{
                return userCouponAry.count;
            }
        }else{
            return 1;
        }
        
        
    }else if (optionNum == 2){
        
        if (self.actListAry.count == 0) {
            return 1;
        }else{
            return self.actListAry.count;
        }
        
        
    }else if (optionNum == 3){
        
        if (self.shopPhotoListAry.count == 0) {
            return 1;
        }else{
            return self.shopPhotoListAry.count;
        }
        
        
    }else{
        return 0;
    }
    
    
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (optionNum == 0) {
        
    
    
    if (indexPath.section == 0 && indexPath.row == 0) {
        static NSString * cell_id = @"DetailsViewCell";
        DetailsViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[DetailsViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        [cell setDetailsValueDic:self.shopInfoDic];

        return cell;
        
    }else if ((indexPath.section == 0 && indexPath.row == 1)){
        static NSString * cell_id = @"RecentlyVisitViewCell";
        RecentlyVisitViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[RecentlyVisitViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        [cell setRecentlyValueAry:self.recentVisitorAry];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;        
        return cell;

    }else if (indexPath.section == 1 && indexPath.row == 1){
        
        static NSString * cell_id = @"HomePageGoodsViewCellss";
        HomePageGoodsViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[HomePageGoodsViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        [cell setValueHomePageAry:self.shopPhotoListAry];
        return cell;
        
    }else if (indexPath.section == 4){
        static NSString * cell_id = @"BMSQ_RecommendShopCellss";
        BMSQ_RecommendShopCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[BMSQ_RecommendShopCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            
            

        }
        [cell setCellValue:self.aroundShopAry[indexPath.row]];
        return cell;
        
    }else if (indexPath.section == 3){
        
        static NSString *idenftier = @"shopInfoCell4";
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:idenftier];
        if (cell == nil) {
            cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:idenftier];
            UILabel *labelTitle = [[UILabel alloc]initWithFrame:CGRectMake(12, 0, 100, 30)];
            labelTitle.text = @"店铺简介:";
            labelTitle.font = [UIFont systemFontOfSize:14];
            [cell addSubview:labelTitle];
            
            
            UILabel *desLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 30, APP_VIEW_WIDTH-20, 0)];
            desLabel.textColor = APP_TEXTCOLOR;
            desLabel.font = [UIFont systemFontOfSize:13];
            desLabel.numberOfLines = 0;
            desLabel.tag = 100;
            [cell addSubview:desLabel];
            
            cell.selectionStyle = UITableViewCellSelectionStyleNone;

        }
        UILabel *deslabel = (UILabel *)[cell viewWithTag:100];
        deslabel.text = self.shopInfoDic[@"shortDes"];
        CGSize size = [deslabel.text boundingRectWithSize:CGSizeMake(deslabel.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: deslabel.font} context:nil].size;
        deslabel.frame = CGRectMake(10, 30, size.width, size.height);
        
        
        return cell;
        
    }
    
    else{
        static NSString * cell_id = @"cells";
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        UIView * lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 39, APP_VIEW_WIDTH, 1)];
        lineView.backgroundColor = APP_VIEWCOLOR;
        [cell addSubview:lineView];
        cell.textLabel.font = [UIFont systemFontOfSize:15.0];
        if (indexPath.section == 1 && indexPath.row == 0) {
            cell.textLabel.text = @"商品/服务";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }else if (indexPath.section == 2 && indexPath.row == 0){
            cell.textLabel.text = @"领取优惠券";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }else if (indexPath.section == 2 && indexPath.row == 1){
            cell.textLabel.text = @"店铺活动";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        }
        
        
        return cell;
        
    }
        
        
        
    }else if (optionNum == 1){
        
        if (shopCouponAry.count == 0 && userCouponAry.count == 0) {
            
            static NSString * cell_id = @"NullViewCellss";
            NullViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[NullViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                cell.backgroundColor = APP_VIEW_BACKCOLOR;
                cell.selectionStyle = UITableViewCellSelectionStyleNone;

            }
            cell.alertBut.hidden = YES;
            cell.bellBut.hidden = YES;
            cell.nullImage.image = [UIImage imageNamed:@"暂时没有优惠券"];
            cell.conentLB.text = @"本店暂时没有优惠券哦!";
            return cell;
            
        }else{
        
        static NSString * cell_id = @"NewMyCouponViewCellsss";
        NewMyCouponViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[NewMyCouponViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            cell.coupondelegate = self;
            cell.selectionStyle=UITableViewCellSelectionStyleNone;

        }
            if(shopCouponAry.count != 0 && userCouponAry.count != 0){
                if (indexPath.section == 1) {
                    [cell setCellCouponDic:userCouponAry[indexPath.row] row:(int)indexPath.row num:1];// num:判断是商家优惠券还是已领取的 num: 1:已领取  2:商家优惠券
                }else if (indexPath.section == 2){
                    [cell setCellCouponDic:shopCouponAry[indexPath.row] row:(int)indexPath.row num:2];
                }
            }else if (shopCouponAry.count == 0 && userCouponAry.count != 0){
                [cell setCellCouponDic:userCouponAry[indexPath.row] row:(int)indexPath.row num:1];
            }else if (shopCouponAry.count != 0 && userCouponAry.count == 0){
                [cell setCellCouponDic:shopCouponAry[indexPath.row] row:(int)indexPath.row num:2];
            }
            
            
        return cell;
            
        }

    }else if (optionNum == 2){
        
        if (self.actListAry != nil && self.actListAry.count != 0 && ![self.actListAry isKindOfClass:[NSNull class]]) {
            
            static NSString * cell_id = @"Benefit_activityCellsss";
            Benefit_activityCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[Benefit_activityCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            NSDictionary *dic = [self.actListAry objectAtIndex:indexPath.row];
                    [cell initActivityCell:dic];
            return cell;
            
        }else{
            
            static NSString * cell_id = @"NullViewCellss";
            NullViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[NullViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                cell.backgroundColor = APP_VIEW_BACKCOLOR;
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
            }
            
            cell.alertBut.hidden = YES;
            cell.bellBut.hidden = YES;
            cell.nullImage.image = [UIImage imageNamed:@"店铺活动暂无状态"];
            cell.conentLB.text = @"本店近期没有组织活动哦!";
            return cell;
            
            
        }
        
        
        
        
    }else if (optionNum == 3){
        
        if (self.shopPhotoListAry != nil && self.shopPhotoListAry.count != 0 && ![self.shopPhotoListAry isKindOfClass:[NSNull class]]){
            
           
            
            static NSString * cell_id = @"GoodsViewCellsss";
            GoodsViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[GoodsViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            [cell setValueGoodsDic:self.shopPhotoListAry[indexPath.row]];
            
            return cell;
            
            
        }else{
            
            static NSString * cell_id = @"NullViewCellss";
            NullViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
            if (!cell) {
                cell = [[NullViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
                cell.backgroundColor = APP_VIEW_BACKCOLOR;
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
            }
            
            cell.conentLB.text = @"掌柜有点懒哦，还没有更新商品/服务内容!";
            cell.nullImage.image = [UIImage imageNamed:@"商铺暂无状态"];
            cell.bellBut.hidden = NO;
            cell.alertBut.hidden = NO;
            [cell.bellBut setImage:[UIImage imageNamed:@"tixing"] forState:UIControlStateNormal];
            [cell.bellBut addTarget:self action:@selector(alertButClick:) forControlEvents:UIControlEventTouchUpInside];
            [cell.alertBut addTarget:self action:@selector(alertButClick:) forControlEvents:UIControlEventTouchUpInside];
            return cell;
            
        }
        
        
        
    }else{
        
        static NSString * cell_id = @"cellsss";
        UITableViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
        if (!cell) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
            
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        return cell;
        
    }

        
}
    

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (optionNum == 0) {
        
        if (indexPath.section == 0 && indexPath.row == 1) {
            BMSQ_VisitorViewController * visitorVC = [[BMSQ_VisitorViewController alloc] init];
            visitorVC.visitorAry = [NSArray arrayWithArray:self.recentVisitorAry];
            visitorVC.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:visitorVC animated:YES];
        }else if (indexPath.section == 1){
            optionNum = 3;
            hearView.hidden = YES;
            [self setViewUp];
            [self payButton];
        }else if (indexPath.section == 2 && indexPath.row == 0){
            optionNum = 1;
            hearView.hidden = YES;
            [self setViewUp];
            [self payButton];
        }else if (indexPath.section == 2 && indexPath.row == 1){
            optionNum = 2;
            hearView.hidden = YES;
            [self setViewUp];
            [self payButton];
        }else if (indexPath.section == 4){
            BMSQ_NewShopDetailViewController * newShopVC = [[BMSQ_NewShopDetailViewController alloc] init];
            newShopVC.shopCode = self.aroundShopAry[indexPath.row][@"shopCode"];
            newShopVC.userCode = [gloabFunction getUserCode];
            [self.navigationController pushViewController:newShopVC animated:YES];
        }
        
    }else if (optionNum == 1){
        if(![gloabFunction isLogin]){
            [self getLogin];
        }else{
            if (shopCouponAry.count != 0 || userCouponAry.count != 0) {
                
                BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
                
                if (shopCouponAry.count != 0 && userCouponAry.count != 0) {
                    if (indexPath.section == 1) {
                        couponVC.userCouponCode = userCouponAry[indexPath.row][@"batchCouponCode"];
                        [self.navigationController pushViewController:couponVC animated:YES];
                    }else if (indexPath.section == 2){
//                        couponVC.userCouponCode = shopCouponAry[indexPath.row][@"batchCouponCode"];
                    }
                }else if (shopCouponAry.count == 0 && userCouponAry.count != 0){
                    if (indexPath.section == 1) {
                        couponVC.userCouponCode = userCouponAry[indexPath.row][@"batchCouponCode"];
                        [self.navigationController pushViewController:couponVC animated:YES];
                    }
                }else if (shopCouponAry.count != 0 && userCouponAry.count == 0){
                    if (indexPath.section == 1) {
//                        couponVC.userCouponCode = shopCouponAry[indexPath.row][@"batchCouponCode"];
                    }
                }
                
                
                
                
            }
        }

        
        
    }else if (optionNum == 2){
        if(![gloabFunction isLogin]){
            [self getLogin];
            
        }else{
            if (self.actListAry.count != 0) {
                NSDictionary *dic = [self.actListAry objectAtIndex:indexPath.row];
                NSString *activityCode =[dic objectForKey:@"activityCode"];
                BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
                vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
            }
            

            
        }
    }else if (optionNum == 3){
        
        if (self.shopPhotoListAry.count != 0) {
            RRC_ShopViewController * VC = [[RRC_ShopViewController alloc] init];
            NSString * productId = self.shopPhotoListAry[indexPath.row][@"productId"];
            VC.isHidenNav = YES;
            VC.requestUrl = [NSString stringWithFormat:@"%@Browser/shopProductInfo?productId=%@&userCode=%@",H5_URL, productId,self.userCode];
            [self.navigationController pushViewController:VC animated:YES];
        }
        
        
    }
   
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (optionNum == 0) {
        
        
        
        if (indexPath.section == 0 && indexPath.row == 0) {
            return 85;
        }else if (indexPath.section == 3){
            
            NSString *str = self.shopInfoDic[@"shortDes"];
            CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;
            return  40+size.height;
            
        }else if (indexPath.section == 4){
            
            return [BMSQ_RecommendShopCell cellHeigh:[self.aroundShopAry objectAtIndex:indexPath.row]];
            
        }else if (indexPath.section == 1 && indexPath.row == 1){
            
            return (APP_VIEW_WIDTH - 95)/3 + 50;
            
        }
        return 40;
        
        
    }else if (optionNum == 1){
        if (shopCouponAry.count == 0 && userCouponAry.count == 0){
            
            return APP_VIEW_HEIGHT - APP_VIEW_HEIGHT/4 - 55 - 80;
            
        }else{
            NSString *str = [[NSString alloc] init];
            if(shopCouponAry.count != 0 && userCouponAry.count != 0)
            {
                if (indexPath.section == 1) {
                    str = userCouponAry[indexPath.row][@"remark"];
                }else if (indexPath.section == 2){
                    str = shopCouponAry[indexPath.row][@"remark"];
                }
                
            }else if (shopCouponAry.count == 0 && userCouponAry.count != 0){
                if (indexPath.section == 1) {
                    str = userCouponAry[indexPath.row][@"remark"];
                }
                
            }else if (shopCouponAry.count != 0 && userCouponAry.count == 0){
                if (indexPath.section == 1) {
                    str = shopCouponAry[indexPath.row][@"remark"];
                }
            }
            CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:13.f]} context:nil].size;
            
            if ([str length] == 0) { // 判断字符串是否为空
                return 255;
            }else{
                return 210 + size.height;
            }
            
        }
        
        
    }else if (optionNum == 2){
        if (self.actListAry.count == 0) {
            
            return APP_VIEW_HEIGHT - APP_VIEW_HEIGHT/4 - 55 - 80;
            
        }else{
            
            return 140;
        }
        
        
    }else if (optionNum == 3){
        
        if (self.shopPhotoListAry.count == 0) {
            
            return APP_VIEW_HEIGHT - APP_VIEW_HEIGHT/4 - 55 - 80;
            
        }else{
            
            return 80;
        }
        
    }else{
        
        return 0;
        
    }
    
    
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
        
    
     if (section == 0) {
         
         NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
         if (isCatering.intValue == 0) {
             headerView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_WIDTH/4, APP_VIEW_WIDTH, 85)];
         }else if (isCatering.intValue == 1){
             headerView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_WIDTH/4, APP_VIEW_WIDTH, 155)];
         }
         
        headerView.backgroundColor = APP_VIEW_BACKCOLOR;
        headerView.hidden = NO;
         UIView * topShopView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 35)];
         topShopView.backgroundColor = [UIColor whiteColor];
         [headerView addSubview:topShopView];
         UIView * middleView = [[UIView alloc] initWithFrame:CGRectMake(0, 45, APP_VIEW_WIDTH, 60)];
         middleView.backgroundColor = [UIColor whiteColor];
         
         if (isCatering.intValue == 1){
             [headerView addSubview:middleView];
         }
         
         UIView * bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, headerView.frame.size.height - 40, APP_VIEW_WIDTH, 35)];
         bottomView.backgroundColor = [UIColor whiteColor];
         [headerView addSubview:bottomView];
         
         UILabel * shopNameLB1 = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, APP_VIEW_WIDTH/5*3, 30)];
         shopNameLB1.font = [UIFont boldSystemFontOfSize:15.0];
         shopNameLB1.text = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"shopName"]];
         [topShopView addSubview:shopNameLB1];
         
         NSString * online = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"onlinePaymentDiscount"]];// 当为10则没有折扣
         if (online.intValue != 10) {
             
            UIImageView * iconImage1 = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 10, 10, 20, 20)];
             iconImage1.image = [UIImage imageNamed:@"ICBCcardicon"];
             [topShopView addSubview:iconImage1];
            UILabel * rateLB1 = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 40, 10, 35, 20)];
             rateLB1.font = [UIFont systemFontOfSize:12.0];
             rateLB1.text = [NSString stringWithFormat:@"%.1f折",[online floatValue]];
             rateLB1.textColor = UICOLOR(183, 34, 26, 1.0);
             [topShopView addSubview:rateLB1];
         }
         
        UIButton * callPhoneBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         callPhoneBut1.frame = CGRectMake(APP_VIEW_WIDTH - 40, 5, 30, 30);
         [callPhoneBut1 addTarget:self action:@selector(callPhoneButClick:) forControlEvents:UIControlEventTouchUpInside];
         [callPhoneBut1 setImage:[UIImage imageNamed:@"phoneIcon"] forState:UIControlStateNormal];
         [topShopView addSubview:callPhoneBut1];
        
         
        UIButton * orderBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         orderBut1.frame = CGRectMake(20, 5, (APP_VIEW_WIDTH - 160)/4 - 10, 30);
         [orderBut1 addTarget:self action:@selector(orderButClick:) forControlEvents:UIControlEventTouchUpInside];
         [orderBut1 setImage:[UIImage imageNamed:@"take_order"] forState:UIControlStateNormal];
         [middleView addSubview:orderBut1];
         
         UILabel * orderLB = [[UILabel alloc] initWithFrame:CGRectMake(20, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
         orderLB.text = @"点餐";
         orderLB.textColor = UICOLOR(71, 71, 71, 1.0);
         orderLB.textAlignment = NSTextAlignmentCenter;
         orderLB.font = [UIFont systemFontOfSize:12.0];
         [middleView addSubview:orderLB];
         
        UIButton * takeOutBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         takeOutBut1.frame = CGRectMake((APP_VIEW_WIDTH - 160)/4 + 60, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
         [takeOutBut1 addTarget:self action:@selector(takeOutButClick:) forControlEvents:UIControlEventTouchUpInside];
         [takeOutBut1 setImage:[UIImage imageNamed:@"take_out"] forState:UIControlStateNormal];
         [middleView addSubview:takeOutBut1];
         
         UILabel * takeOutLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/4 + 60, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
         takeOutLB.text = @"外卖";
         takeOutLB.textColor = UICOLOR(71, 71, 71, 1.0);
         takeOutLB.textAlignment = NSTextAlignmentCenter;
         takeOutLB.font = [UIFont systemFontOfSize:12.0];
         [middleView addSubview:takeOutLB];
         
        UIButton * reserveBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         reserveBut1.frame = CGRectMake((APP_VIEW_WIDTH - 160)/2 + 100, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
         [reserveBut1 addTarget:self action:@selector(reserveButClick:) forControlEvents:UIControlEventTouchUpInside];
         [reserveBut1 setImage:[UIImage imageNamed:@"book_food"] forState:UIControlStateNormal];
         [middleView addSubview:reserveBut1];
         
         UILabel * reserveLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/2 + 100, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
         reserveLB.text = @"预定";
         reserveLB.textColor = UICOLOR(71, 71, 71, 1.0);
         reserveLB.textAlignment = NSTextAlignmentCenter;
         reserveLB.font = [UIFont systemFontOfSize:12.0];
         [middleView addSubview:reserveLB];
         
        UIButton * indentBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         indentBut1.frame = CGRectMake((APP_VIEW_WIDTH - 160)/4*3 + 140, 5, (APP_VIEW_WIDTH - 160)/4-10, 30);
         [indentBut1 addTarget:self action:@selector(indentButClick:) forControlEvents:UIControlEventTouchUpInside];
         [indentBut1 setImage:[UIImage imageNamed:@"order_food"] forState:UIControlStateNormal];
         [middleView addSubview:indentBut1];
         
         UILabel * indentLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 160)/4*3 + 140, 40, (APP_VIEW_WIDTH - 160)/4 - 10, 15)];
         indentLB.text = @"订单";
         indentLB.textColor = UICOLOR(71, 71, 71, 1.0);
         indentLB.textAlignment = NSTextAlignmentCenter;
         indentLB.font = [UIFont systemFontOfSize:12.0];
         [middleView addSubview:indentLB];
         
         if (isCatering.intValue == 0) {
             orderBut1.hidden = YES;
             orderLB.hidden = YES;
             takeOutBut1.hidden = YES;
             takeOutLB.hidden = YES;
             reserveBut1.hidden = YES;
             reserveLB.hidden = YES;
             indentBut1.hidden = YES;
             indentLB.hidden = YES;
         }
         
         
        UIButton * homePageBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         homePageBut1.frame = CGRectMake(5, 5, (APP_VIEW_WIDTH-40)/4, 30);
         [homePageBut1 setTitle:@"店铺首页" forState:UIControlStateNormal];
         if (optionNum == 0) {
             [homePageBut1 setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
         }else{
             [homePageBut1 setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
         }
         homePageBut1.titleLabel.font = [UIFont systemFontOfSize: 14.0];
         [homePageBut1 addTarget:self action:@selector(homePageButClick:) forControlEvents:UIControlEventTouchUpInside];
         [bottomView addSubview:homePageBut1];
         
        UIButton * couponBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         couponBut1.frame = CGRectMake((APP_VIEW_WIDTH-40)/4 + 15, 5, (APP_VIEW_WIDTH-40)/4, 30);
         [couponBut1 setTitle:@"优惠券" forState:UIControlStateNormal];
         if (optionNum == 1) {
             [couponBut1 setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
         }else{
             [couponBut1 setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
         }
         couponBut1.titleLabel.font = [UIFont systemFontOfSize: 14.0];
         [couponBut1 addTarget:self action:@selector(couponButClick:) forControlEvents:UIControlEventTouchUpInside];
         [bottomView addSubview:couponBut1];
         
        UIButton * shopActivityBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         shopActivityBut1.frame = CGRectMake((APP_VIEW_WIDTH-40)/2 + 25, 5, (APP_VIEW_WIDTH-40)/4, 30);
         [shopActivityBut1 setTitle:@"店铺活动" forState:UIControlStateNormal];
         if (optionNum == 2) {
             [shopActivityBut1 setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
         }else{
             [shopActivityBut1 setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
         }
         shopActivityBut1.titleLabel.font = [UIFont systemFontOfSize: 14.0];
         [shopActivityBut1 addTarget:self action:@selector(shopActivityButClick:) forControlEvents:UIControlEventTouchUpInside];
         [bottomView addSubview:shopActivityBut1];
         
        UIButton * goodsBut1 = [UIButton buttonWithType:UIButtonTypeCustom];
         goodsBut1.frame = CGRectMake((APP_VIEW_WIDTH-40)/4*3 + 35, 5, (APP_VIEW_WIDTH-40)/4, 30);
         [goodsBut1 setTitle:@"商品/服务" forState:UIControlStateNormal];
         if (optionNum == 3) {
             [goodsBut1 setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
         }else{
             [goodsBut1 setTitleColor:UICOLOR(51, 51, 51, 1.0) forState:UIControlStateNormal];
         }
         goodsBut1.titleLabel.font = [UIFont systemFontOfSize: 14.0];
         [goodsBut1 addTarget:self action:@selector(goodsButClick:) forControlEvents:UIControlEventTouchUpInside];
         [bottomView addSubview:goodsBut1];
        
        
      return headerView;
    }
    
    if (optionNum == 0) {
        
      if (section == 4){
        
        UIView * titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
          titleView.backgroundColor = [UIColor whiteColor];
        UILabel * titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
        titleLabel.font = [UIFont systemFontOfSize:15.0];
        titleLabel.text = @"附近其他商家";
        [titleView addSubview:titleLabel];
        
        return titleView;
        
    }else{
        return nil;
    }
        
        
    }else if (optionNum == 1){
        if (shopCouponAry.count != 0 && userCouponAry.count != 0){
            if (section == 1){
                UIView * titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
                titleView.backgroundColor = [UIColor whiteColor];
                UILabel * titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
                titleLabel.text = @"我的优惠券";
                titleLabel.font = [UIFont systemFontOfSize:15.0];
                [titleView addSubview:titleLabel];
                return titleView;
            }else if (section == 2){
                UIView * titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
                titleView.backgroundColor = [UIColor whiteColor];
                UILabel * titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
                titleLabel.text = @"未领取的优惠券";
                titleLabel.font = [UIFont systemFontOfSize:15.0];
                [titleView addSubview:titleLabel];
                return titleView;
            }else{
                return nil;
            }
            
        }else if (shopCouponAry.count != 0 && userCouponAry.count == 0){
            if (section == 0) {
                return nil;
            }else{
                UIView * titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
                titleView.backgroundColor = [UIColor whiteColor];
                UILabel * titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
                titleLabel.text = @"未领取的优惠券";
                titleLabel.font = [UIFont systemFontOfSize:15.0];
                [titleView addSubview:titleLabel];
                return titleView;
            }
            
        }else if (shopCouponAry.count == 0 && userCouponAry.count != 0){
            if (section == 0) {
                return nil;
            }else{
                UIView * titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
                titleView.backgroundColor = [UIColor whiteColor];
                UILabel * titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 150, 40)];
                titleLabel.text = @"我的优惠券";
                titleLabel.font = [UIFont systemFontOfSize:15.0];
                [titleView addSubview:titleLabel];
                return titleView;
            }
            
        }else{
            return nil;
        }
        
        
    }else if (optionNum == 2){
        
        return nil;
        
    }else if (optionNum == 3){
        
        return nil;
        
    }else{
        
        return nil;
    }
    
    
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    
        if (scrollView.contentOffset.y >= APP_VIEW_HEIGHT / 2) {
            hearView.hidden = NO;
            headerView.hidden = YES;
            [self setNavBackGroundColor:APP_NAVCOLOR];
        }else{
            hearView.hidden = YES;
            headerView.hidden = NO;
            [self setNavBackGroundColor:[UIColor clearColor]];
        }
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1.0f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    if (optionNum == 0) {
        
        if (section == 0) {
            
            NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
            if (isCatering.intValue == 1) {
                return 155;// 125
            }else{
                return 85;
            }
            
        }else if (section == 4){
            return 40;
        }
        return 10.0f;
        
    }else if (optionNum == 1){
        if (section == 0) {
            NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
            if (isCatering.intValue == 1) {
                return 155;
            }else{
                return 85;
            }
        }else{
            return 40.0f;
        }
        
    }else if (optionNum == 2){
        if (section == 0) {
            NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
            if (isCatering.intValue == 1) {
                return 155;
            }else{
                return 85;
            }
        }else{
            return 10.0f;
        }
    }else if (optionNum == 3){
        if (section == 0) {
            NSString * isCatering = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"isCatering"]];
            if (isCatering.intValue == 1) {
                return 155;
            }else{
                return 85;
            }
        }else{
            return 10.0f;
        }
    }else{
        return 1.0f;
    }
    
}

#pragma mark ----- getShopInfo  商店详情
- (void)getShopInfo
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.userCode forKey:@"userCode"];
    NSString* vcode = [gloabFunction getSign:@"getShopInfo" strParams:self.shopCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getShopInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
//        NSLog(@"%@", responseObject);

        weakSelf.shopInfoDic = [NSDictionary dictionaryWithDictionary:responseObject[@"shopInfo"]];//商店首页信息
        weakSelf.shopName = [weakSelf.shopInfoDic objectForKey:@"shopName"];
        self.couponListDic = [NSDictionary dictionaryWithDictionary:responseObject[@"couponList"]];// 优惠券
        self.shopDecorationAry = [NSMutableArray arrayWithArray:responseObject[@"shopDecoration"]];//商家环境图片
        self.shopPhotoListAry = [NSMutableArray arrayWithArray:responseObject[@"shopPhotoList"]];// 商家产品
        self.actListAry = [NSMutableArray arrayWithArray:responseObject[@"actList"]];// 商家活动
        self.recentVisitorAry = [NSMutableArray arrayWithArray:responseObject[@"recentVisitor"]];// 最近访问
        self.aroundShopAry = [NSMutableArray arrayWithArray:responseObject[@"aroundShop"]];// 附近商户
        
        hearView.hidden = YES;
        [self setViewUp];
//        NSArray *subList = [NSArray arrayWithArray:self.shopDecorationAry];
//        [_shopLunboView setHomeAdCell:subList height:APP_VIEW_HEIGHT / 2];
//        UIView * botView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
//        botView.backgroundColor = [UIColor clearColor];
//        NSString * showPayBtn = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"showPayBtn"]];
//        if (showPayBtn.intValue == 1){
//            
//            _tableView.tableFooterView = botView;
//        }
//        [self.tableView reloadData];
        
        NSString * showPayBtn = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"showPayBtn"]];
        if (showPayBtn.intValue == 1) {
            [self payButton];// 支付
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
        CSAlert(@"修改失败");
    }];

    
    
    
}

#pragma mark ----- 提醒商家
- (void)remindToShop
{
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.shopCode forKey:@"shopCode"];
    [params setObject:self.userCode forKey:@"userCode"];
    [params setObject:actionType forKey:@"actionType"];
    NSString* vcode = [gloabFunction getSign:@"remindToShop" strParams:self.shopCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    
    __block typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"remindToShop" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        NSLog(@"%@", responseObject);
        NSString * count = responseObject[@"count"];
        NSString * code = responseObject[@"code"];
        if (code.intValue == 50500) {
            CSAlert(@"请登录");
        }else if (code.intValue == 78001){
            UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:nil message:[NSString stringWithFormat:@"已提醒%@次", count] delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
            [alertView show];
        }else if (code.intValue == 20000){
            CSAlert(@"操作失败");
        }else if (code.intValue == 50000){
            UIAlertView * alertView = [[UIAlertView alloc] initWithTitle:nil message:[NSString stringWithFormat:@"提醒成功"] delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alertView show];
        }
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
        CSAlert(@"提醒失败");
    }];

}

#pragma mark ----- NewMyCouponViewCellDelegate
#pragma mark ---- 分享优惠券
- (void)btnNewShareClick:(NSDictionary*)dicShare
{
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
    NSString *shopName = [NSString stringWithFormat:@"%@", [dicShare objectForKey:@"shopName"]];
    
    NSString* remark = [NSString stringWithFormat:@"%@，我分享你一张%@的优惠券，到惠圈，惠生活！",[gloabFunction changeNullToBlank:str],shopName];
    
    NSString* url = [NSString stringWithFormat:@"%@/BatchCoupon/share?batchCouponCode=%@",BASE_URL,[dicShare objectForKey:@"batchCouponCode"]];
    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"share" ofType:@"png"];
    [BMSQ_Share shareClick:remark imagePath:imagePath title:title url:url];
    [MobClick event:@"NewShopDetail_Coupon_Share"]; // 友盟统计
}
#pragma mark ---- 购买券
- (void)grabBuyNewCoupon:(NSDictionary *)dicCoupon; // 购买券
{
    if (![gloabFunction isLogin]) {
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nav animated:YES completion:nil];
        
        return;
    }
    
    BMSQ_BuyCouponViewController *buyCouponVC = [[BMSQ_BuyCouponViewController alloc]init];
    buyCouponVC.batchCouponCode = dicCoupon[@"batchCouponCode"];
    buyCouponVC.fromVC = (int)self.navigationController.viewControllers.count;
    [self.navigationController pushViewController:buyCouponVC animated:YES];
    [MobClick event:@"NewShopDetail_Coupon_Buy"]; // 友盟统计

}
#pragma mark ------ 领券
- (void)grabNewCupon:(NSDictionary*)dicCupon currenRow:(int)row //领券
{
    if (![gloabFunction isLogin]) {
        UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
        BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
        BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
        
        [self presentViewController:nav animated:YES completion:nil];
        
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dicCupon objectForKey:@"batchCouponCode"] forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:[dicCupon objectForKey:@"batchCouponCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            CSAlert(@"抢券成功");
            [self getShopInfo];
            [MobClick event:@"NewShopDetail_Coupon_Grab"]; // 友盟统计

        }
        else
        {
            
            int i = [code intValue];
            
            switch (i) {
                case 80218:
                    [self showAlertView:@"优惠券开始使用日期不正确"];
                    break;
                case 80219:
                    [self showAlertView:@"优惠券已经被领取"];
                    break;
                case 80220:
                    [self showAlertView:@"优惠券已过期"];
                    break;
                case 80221:
                    [self showAlertView:@"优惠券已领走"];
                    break;
                case 80222:
                    [self showAlertView:@"您领用的数量已经达上限"];
                    break;
                case 80223:
                    [self showAlertView:@"优惠券不存在"];
                    break;
                    
                default:
                    [self showAlertView:@"优惠券状态不对，过段时间再领吧"];
                    
                    break;
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"抢券失败"];
    }];
    

}
#pragma mark ----- 使用优惠券
- (void)grabUserCoupon:(NSDictionary *)dicCupon
{
    
     NSString * ifCanPay = [NSString stringWithFormat:@"%@", self.shopInfoDic[@"ifCanPay"]];
    if (ifCanPay.intValue == 0) {
        
        CSAlert(@"商家还未营业");
    }else if (ifCanPay.intValue == 1){
        [MobClick event:@"NewShopDetail_Coupon_Use"]; // 友盟统计
        int type =(int)[[dicCupon objectForKey:@"couponType"]integerValue];
        switch (type) {
            case 1:  //N元购
            {
                
                [self payCard:dicCupon];
            }
                break;
            case 2:
            {
            }
                break;
            case 3:   //抵扣券 折扣券 一样
            {
                [self buyClick:dicCupon];
                
            }
                break;
            case 4:   //折扣券 抵扣券 一样
            {
                [self buyClick:dicCupon];
            }
                break;
            case 5:    //实物券 体验券 一样
            {
                [self gotoPay_secVC:dicCupon];
                
            }
                break;
            case 6:
            {
                [self gotoPay_secVC:dicCupon];
                
            }
                break;
            case 32:
            {
                [self buyClick:dicCupon];
                
            }
                break;
            case 33:
            {
                [self buyClick:dicCupon];
                
            }
                break;
            case 7:
            {
                
                BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
                couponVC.userCouponCode = [dicCupon objectForKey:@"batchCouponCode"];
                [self.navigationController pushViewController:couponVC animated:YES];
                
            }
                break;
            case 8:
            {
                BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
                couponVC.userCouponCode = [dicCupon objectForKey:@"batchCouponCode"];
                [self.navigationController pushViewController:couponVC animated:YES];
                
                
            }
                break;
            default:
                break;
        }

        
    }
    
    
    
}

#pragma mark N元购
-(void)payCard:(NSDictionary *)dic{
    
    BMSQ_PayDetailViewController *vc = [[BMSQ_PayDetailViewController alloc]init];
    vc.shopCode =dic[@"shopCode"];
    vc.shopName = dic[@"shopName"];
    if ([dic objectForKey:@"userCouponCode"]) {
        vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
        
    }else{
        vc.userCouponCode = @"";
        
    }
    vc.formVc = (int)self.navigationController.viewControllers.count;
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma mark 实物 体验券使用
-(void)gotoPay_secVC:(NSDictionary *)dic{
    
    Pay_SecViewController *vc = [[Pay_SecViewController alloc]init];
    vc.myTitle =dic[@"shopName"];
    vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
    vc.shopCode = self.shopCode;
    vc.imageUrl =dic[@"logoUrl"];
    [self.navigationController pushViewController:vc animated:YES];
}
#pragma mark ------ 使用工行卡支付
-(void)buyClick:(NSDictionary *)dic{
    
    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
    vc.shopCode = self.shopCode;
    vc.shopName = dic[@"shopName"];
    if ([dic objectForKey:@"batchCouponCode"]) {
        vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    }else{
        vc.batchCouponCode = @"";
    }
    vc.type = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"couponType"]] intValue];
    vc.batchCouponCode = [dic objectForKey:@"batchCouponCode"];
    vc.fromVC = (int)self.navigationController.viewControllers.count;
    vc.isNeedDiscount = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}


#pragma mark ---- 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
