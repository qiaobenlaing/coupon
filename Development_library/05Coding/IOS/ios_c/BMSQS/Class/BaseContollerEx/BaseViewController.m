//
//  BaseViewController.m
//  BMSQS
//
//  Created by lxm on 15/7/23.
//  Copyright (c) 2015年 djx. All rights reserved.
//
#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#import "BaseViewController.h"
@interface BaseViewController ()

@end

@implementation BaseViewController
@synthesize jsonPrcClient;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.view setBackgroundColor:UICOLOR(241, 240, 245, 1)];
    [self customBackBtn];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
/*自定义后退按钮*/
- (void)customBackBtn
{
//    UIImage *backImg = [UIImage imageNamed:@"Common_backBtn"];
//    backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
//    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithImage:backImg style:UIBarButtonItemStylePlain target:self action:@selector(btnBackClicked) ];
//    self.navigationItem.leftBarButtonItem = item;
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT - APP_STATUSBAR_HEIGHT), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(-16, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(btnBackClicked) forControlEvents:UIControlEventTouchUpInside];

    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
    self.navigationItem.leftBarButtonItem = backButtonItem;
}

- (void)btnBackClicked
{
    [self.navigationController popViewControllerAnimated:YES];
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

- (void)intEmptyView
{
    if (_emptyLabel == nil) {
        _emptyLabel = [[UILabel alloc] init];
        [self.view addSubview:_emptyLabel];
    }
}

- (void)showEmptyView:(NSString *)str
{
    [self intEmptyView];
    if (!isShownEmpty) {
        if (![[self.view subviews] containsObject:_emptyLabel]) {
            [self.view addSubview:_emptyLabel];
        }
        
        [_emptyLabel setFrame:CGRectMake(0, 0, SCREEN_WIDTH, 40)];
        [_emptyLabel setTag:80000];
        [_emptyLabel setBackgroundColor:[UIColor clearColor]];
        [_emptyLabel setHidden:NO];
        [_emptyLabel setFont:[UIFont systemFontOfSize:17.f]];
        [_emptyLabel setTextColor:[UIColor grayColor]];
        [_emptyLabel setTextAlignment:NSTextAlignmentCenter];
        [_emptyLabel setCenter:CGPointMake(SCREEN_WIDTH/2, (SCREEN_HEIGHT-128)/2)];
        [_emptyLabel setText:str];
        [self.view bringSubviewToFront:_emptyLabel];
        
        isShownEmpty = YES;
    }
    
}

- (void)hideEmpthView
{
    isShownEmpty = NO;
    [_emptyLabel removeFromSuperview];
}
@end
