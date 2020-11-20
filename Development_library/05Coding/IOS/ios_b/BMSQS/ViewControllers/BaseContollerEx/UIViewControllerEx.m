//
//  UIViewController_SDZX.m
//  SDBooking
//
//  Created by icarddjx on 14-1-21.
//  Copyright (c) 2014年 djx. All rights reserved.
//



#import "UIViewControllerEx.h"

@interface UIViewControllerEx ()
{
    float statusPosition;
    RRC_cityListView* cityView;
    
    
}

@end

@implementation UIViewControllerEx

@synthesize navigationView;
@synthesize navTitleView;
@synthesize jsonPrcClient;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    //engine = [[ASIBaseEngine alloc]init];
    statusPosition = APP_STATUSBAR_HEIGHT;
    if (IOS7)
    {
        statusPosition = 0;
    }
    
    [self setStutusbar];
    [self setNavigationBar];

    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
	// Do any additional setup after loading the view.
    
    self.noDateImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 200 , 200)];
    [self.noDateImage setImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    self.noDateImage.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
    self.noDateImage.hidden = YES;
    [self.view addSubview:self.noDateImage];

}

- (void)initJsonPrcClient:(NSString *)requestType
{
    if ([requestType isEqualToString:@"0"])
    {
        jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
    }
    else if([requestType isEqualToString:@"1"])
    {
        jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_SHOP_URL]];
    }
    else
    {
        jsonPrcClient =  [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    }
    
}

- (void)setStutusbar
{
    UIView* v_status = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_STATUSBAR_HEIGHT)];
    [v_status setBackgroundColor:UICOLOR(248, 248, 248, 1.0)];
    [self.view addSubview:v_status];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:YES];
    [ProgressManage closeProgress];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    //self.tabBarController.tabBar.hidden = YES;
}

//设置导航条
- (void)setNavigationBar
{
    if (navigationView == nil)
    {
        
        navigationView = [[UIView alloc]initWithFrame:CGRectMake(0, statusPosition, APP_VIEW_WIDTH, APP_NAVGATION_HEIGHT + (APP_STATUSBAR_HEIGHT - statusPosition))];
        
        UIImageView* iv = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_NAVGATION_HEIGHT + (APP_STATUSBAR_HEIGHT - statusPosition))];
        [iv setBackgroundColor:UICOLOR(182, 0, 12, 1.0)];
        [navigationView addSubview:iv];
    }
    
    [self.view addSubview:navigationView];
}

- (void)setNavBackGroundColor:(UIColor*)color
{
    if (navigationView == nil)
    {
        return;
    }
    
    [navigationView setBackgroundColor:color];
}

//导航条显示/隐藏 isHidden为YES，隐藏，NO显示
- (void)setNavHidden:(BOOL)isHidden
{
    if (navigationView != nil)
    {
        navigationView.hidden = isHidden;
    }
}

//导航条返回按钮
- (void)setNavBackItem
{
    if (navigationView == nil)
    {
        return;
    }
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - statusPosition), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(goBack) forControlEvents:UIControlEventTouchUpInside];
    //UIImage *Login_image = [UIImage imageNamed:@"btn_backNormal"];
    //[btnback setBackgroundImage:Login_image forState:UIControlStateNormal];
    btnback.tag = 99;
    //[btnback setBackgroundImage:[UIImage imageNamed:@"btn_backHighlight"] forState:UIControlStateHighlighted];
    [navigationView addSubview:btnback];
}

- (void)setNavBackItemHidden:(BOOL)isHidden
{
    UIButton* btn = (UIButton*)[self.view viewWithTag:99];
    if (btn != nil)
    {
        [btn setHidden:isHidden];
    }
}

//设置左侧按钮
- (void)setNavLeftBarItem:(UIView*)leftBarItem
{
    if (leftBarItem != nil && navigationView != nil)
    {
        [navigationView addSubview:leftBarItem];
    }
}

//设置右侧按钮
- (void)setNavRightBarItem:(UIView*)rightBarItem
{
    if (rightBarItem != nil && navigationView != nil)
    {
        [navigationView addSubview:rightBarItem];
    }
}

//设置标题
- (void)setNavTitle:(NSString*)strTitle
{
    if (navTitleView == nil)
    {
        navTitleView = [[UILabel alloc]initWithFrame:CGRectMake(44, APP_STATUSBAR_HEIGHT - statusPosition, APP_VIEW_WIDTH-88, APP_NAVGATION_HEIGHT)];
        [navTitleView setBackgroundColor:[UIColor clearColor]];//clearColor
        [navTitleView setFont:[UIFont fontWithName:@"TrebuchetMS-Bold" size:18]];
        [navTitleView setTextAlignment:NSTextAlignmentCenter];
        [navTitleView setTextColor:[UIColor whiteColor]];
        [navigationView addSubview:navTitleView];
    }
    
    [navTitleView setText:strTitle];
    
    [navigationView bringSubviewToFront:navTitleView];
    
}
- (void)setRight:(UIButton *)button{
    [navigationView addSubview:button];
}

//自定义view
- (void)setNavCustomerView:(id)customerView
{
    if (customerView != nil)
    {
        [navigationView addSubview:customerView];
    }
}

- (void)setNavLeftMapItem
{
    
    if (navigationView == nil)
    {
        return;
    }
    
    UIButton* btn_leftMap = [UIButton buttonWithType:UIButtonTypeCustom];
    btn_leftMap.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - statusPosition), 80, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, (APP_NAV_LEFT_ITEM_HEIGHT-37)/2, 44, 37)];
    btnBackView.image = [UIImage imageNamed:@"icon_area"];
    [btn_leftMap addSubview:btnBackView];
    [btn_leftMap addTarget:self action:@selector(btnLeftMapClick) forControlEvents:UIControlEventTouchUpInside];

    UILabel* lbMap = [[UILabel alloc]initWithFrame:CGRectMake(44, 0, 36, APP_NAV_LEFT_ITEM_HEIGHT)];
    [lbMap setTextAlignment:NSTextAlignmentLeft];
    [lbMap setTextColor:[UIColor whiteColor]];
    [lbMap setText:appDelegate.currentCityName];
    [lbMap setFont:[UIFont systemFontOfSize:14]];
    lbMap.tag = 100;
    btn_leftMap.tag = 199;
    [btn_leftMap addSubview:lbMap];
    
    [navigationView addSubview:btn_leftMap];
}

- (void)setMapCity
{
    UIButton* btn_leftMap = (UIButton*)[navigationView viewWithTag:199];
    UILabel* lbMap = (UILabel*)[btn_leftMap viewWithTag:100];
    [lbMap setText:appDelegate.currentCityName];
}

#pragma mark 用户操作触发事件

- (void)goBack
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)btnLeftMapClick
{

}




#pragma mark 弹窗消息
- (void)showAlertView:(NSString *)msg
{
    UIAlertView* alert = [[UIAlertView alloc]initWithTitle:nil message:msg delegate:nil cancelButtonTitle:@"确定" otherButtonTitles:nil, nil];
    [alert show];
}

//
//- (void)showNoDataView
//{
//    m_noDataView.hidden = NO;
//    [self.view bringSubviewToFront:m_noDataView];
//}
//
//
//- (void)hiddenNoDataView
//{
//    m_noDataView.hidden = YES;
//    [self.view sendSubviewToBack:m_noDataView];
//}
//
//- (void)setNoDataImg:(NSString*)imgName
//{
//    [m_noDataView setImageViewName:imgName];
//}


@end
