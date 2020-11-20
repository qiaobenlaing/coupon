//
//  DetailViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/7.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "DetailViewController.h"

@interface DetailViewController ()<UIActionSheetDelegate, UIAlertViewDelegate>

@property (nonatomic, strong) UIView * detailView;
@property (nonatomic, strong) NSArray * orderDataAry;//
@property (nonatomic, strong) NSArray * distributionAry;//
@end

@implementation DetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    
    self.orderDataAry = [NSArray arrayWithObjects:@"订单类型",@"支付状态",@"用户",@"电话",@"下单时间",@"订单号",@"配送信息", nil];
    self.distributionAry = [NSArray arrayWithObjects:@"收货人",@"电话",@"地址", nil];
    
    
    self.detailView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 300)];
    
    [self detailesView];
    [self customRightBtn];
    [self.view addSubview:self.detailView];
    
    
    
    
}

- (void)detailesView
{
    UIView * mealView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y - 63, APP_VIEW_WIDTH, 50)];
    [self.detailView addSubview:mealView];
    
    UILabel * mealLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH / 2 - 60, mealView.frame.origin.y - 1, 120, 50)];
    mealLabel.text = [NSString stringWithFormat:@"餐号：%@",nil];
    mealLabel.font = [UIFont systemFontOfSize:20];
    [mealView addSubview:mealLabel];
    
    
    
    UIView * orderView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y - 13, APP_VIEW_WIDTH, 175)];
    orderView.backgroundColor = UICOLOR(173, 173, 173, 1.0);
    [self.detailView addSubview:orderView];
    for (int i = 0; i < 7; i++) {
        UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(10, orderView.frame.origin.y - 50 + i * 25, 300, 25)];
        label.text = [NSString stringWithFormat:@"%@:%@",self.orderDataAry[i], nil];
        label.font = [UIFont systemFontOfSize:14];
        [orderView addSubview:label];
    }
  
   
    UIView * distributionView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 163, APP_VIEW_WIDTH, 75)];
    distributionView.backgroundColor = [UIColor whiteColor];
    [self.detailView addSubview:distributionView];

    for (int i = 0; i < 3; i++) {
        UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, distributionView.frame.origin.y - 225 + i * 25, 300, 25)];
        label.text = [NSString stringWithFormat:@"%@:%@",self.distributionAry[i], nil];
        label.font = [UIFont systemFontOfSize:14];
        [distributionView addSubview:label];
    }
    
    
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 44, 20, 44, 44);
    [item setImage:[UIImage imageNamed:@"order_more"] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)itemClick:(UIButton *)sender
{
    UIActionSheet * action = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:@"联系用户" otherButtonTitles:@"取消订单", nil];
    [action showInView:self.view];
    
}
#pragma mark ---- UIActionSheetDelegate,

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:{
            NSLog(@"联系用户");
            UIAlertView * alertView1 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"你确定拨打电话！" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alertView1.tag = 5001;
            [alertView1 show];
        }
            
            break;
            
        case 1:{
            NSLog(@"取消订单");
            UIAlertView * alertView2 = [[UIAlertView alloc] initWithTitle:@"温馨提示" message:@"确定要撤销订单吗？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
            alertView2.tag = 5002;
            [alertView2 show];
        }
            
            break;
            
        default:
            break;
    }
}


#pragma mark ----  UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    switch (buttonIndex) {
        case 0:
            
            break;
        case 1:{
            NSLog(@"确定联系用户");
            if (alertView.tag == 5001) {
                // 方法1:
                UIWebView *callWebView = [[UIWebView alloc] init];
                
                NSURL *telURL = [NSURL URLWithString:@"tel:10086"];
                
                [callWebView loadRequest:[NSURLRequest requestWithURL:telURL]];
                [self.view addSubview:callWebView];
                
                //方法2:
                //        NSString *allString = [NSString stringWithFormat:@"tel:10086"];
                //        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:allString]];
            }else if (alertView.tag == 5002){
                NSLog(@"确定取消订单");
                
                
            }
            
           
            
        }
            
            break;
            
        default:
            break;
    }
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
