//
//  SDZX_webViewController.m
//  SDBooking
//
//  Created by djx on 14-3-28.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "RRC_webViewController.h"
#import "SVProgressHUD.h"
#import "BMSQ_RegistViewController.h"

#import "BMSQ_LoginViewController.h"
#import "BaseNavViewController.h"

#import "BMSQ_CouponDetailViewController.h"


#import "BMSQ_joinActivityViewController.h"

//#import <ShareSDK/ShareSDK.h>
#import "BMSQ_CouponOPViewController.h"
#import "BMSQ_Share.h"
@interface RRC_webViewController ()
{
    UIWebviewEx*  webView; //webView，加载网页
}

@property (nonatomic, assign)BOOL isFinsh;


@property (nonatomic,strong)NSString *urlStr;

@end

@implementation RRC_webViewController
@synthesize requestUrl;
@synthesize navtitle;

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
    // Do any additional setup after loading the view.
    
    [self setViewUp];

    
    
    //支付完成刷新优惠券界面
    [[NSNotificationCenter defaultCenter]postNotificationName:@"freshEff" object:nil];
    
    
    
    if (self.isJoin) {
        
        [self isUserJoinAct];
//        UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 25, 40, 35)];
//        button.backgroundColor = [UIColor clearColor];
//        [button setTitle:@"报名" forState:UIControlStateNormal];
//        [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//        button.titleLabel.font = [UIFont fontWithName:@"TrebuchetMS-Bold" size:15];
//        [button addTarget:self action:@selector(clickjoin) forControlEvents:UIControlEventTouchUpInside];
//        [self setNavCustomerView:button];
    }
    
    if (self.isShare) {
        UIButton *sharebutton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 25, 40, 35)];
        sharebutton.backgroundColor = [UIColor clearColor];
        [sharebutton setImage:[UIImage imageNamed:@"icon_share"] forState:UIControlStateNormal];
//        [sharebutton addTarget:self action:@selector(clickShare) forControlEvents:UIControlEventTouchUpInside];
        [self setNavCustomerView:sharebutton];
    }
  
    if (self.isPayResult) {
        UIButton *finseButton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 25, 40, 35)];
        finseButton.backgroundColor = [UIColor clearColor];
        [finseButton setTitle:@"完成" forState:UIControlStateNormal];
        finseButton.titleLabel.font = [UIFont fontWithName:@"TrebuchetMS-Bold" size:14];

        [finseButton addTarget:self action:@selector(finseButton) forControlEvents:UIControlEventTouchUpInside];
        [self setNavCustomerView:finseButton];
    }
    

}


-(void)isUserJoinAct{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.actCode forKey:@"actCode"];
   
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"isUserJoinAct" strParams:[gloabFunction getUserCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"isUserJoinAct" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *boola = [NSString stringWithFormat:@"%@",responseObject ];
        
        if (![boola isEqualToString:@"1"]) {     //1-是； 0-否；
            UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, 25, 40, 35)];
            button.backgroundColor = [UIColor clearColor];
            [button setTitle:@"报名" forState:UIControlStateNormal];
            [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            button.titleLabel.font = [UIFont fontWithName:@"TrebuchetMS-Bold" size:15];
            [button addTarget:self action:@selector(clickjoin) forControlEvents:UIControlEventTouchUpInside];
            [wself setNavCustomerView:button];
        }
       
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        
        
    }];
    

}




-(void)clickjoin{
    BMSQ_joinActivityViewController *vc = [[BMSQ_joinActivityViewController alloc]init];
    vc.activityCode = self.actCode;
    [self.navigationController pushViewController:vc animated:YES];
    
}
-(void)viewWillDisappear:(BOOL)animated{
    [SVProgressHUD dismiss];
}
-(void)popVC{
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)setViewUp
{
    if (!self.isHidenNav) {
        [self setNavigationBar];
        [self setNavBackItem];
        [self setNavTitle:navtitle];
        self.view.backgroundColor = [UIColor whiteColor];
        webView = [[UIWebviewEx alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-self.navigationView.frame.size.height)];
        [webView setBackgroundColor:[UIColor whiteColor]];
        webView.delegate = self;
        [self.view addSubview:webView];
    }else{
        [self setNavHidden:YES];
        
        if (IOS7) {
            UIView *statusView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 20)];
            [statusView setBackgroundColor:UICOLOR(182, 0, 12, 1.0)];
            [self.view addSubview:statusView];
            
        }
        
        
        webView = [[UIWebviewEx alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT+49+20)];
        [webView setBackgroundColor:[UIColor whiteColor]];
        [self.view addSubview:webView];
        
        UIButton *backBtn = [[UIButton alloc]initWithFrame:CGRectMake(0,30 , 30, 30)];
        backBtn.backgroundColor = [UIColor clearColor];
        [backBtn setImage:[UIImage imageNamed:@"btn_backNormal"] forState:UIControlStateNormal];
        [backBtn addTarget:self action:@selector(popVC) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:backBtn];
        
    
        
        
    }
    
    webView.delegate = self;
    [webView stopLoading];
    
    //requestUrl = @"http://202.91.249.194:9898/member.html?type=login";
    if (requestUrl != nil && requestUrl.length > 0)
    {
        [webView loadRequestWithString:requestUrl];
    }
    
    
    
}


-(void)goBack{
    
    if(self.isTakeOut){ //外卖 堂食 返回我的订单页面
        NSArray *vcs = self.navigationController.viewControllers;
        [self.navigationController popToViewController:[vcs objectAtIndex:self.fromVC-2] animated:YES];
        [[NSNotificationCenter defaultCenter]postNotificationName:@"findOrderInfo" object:nil];//支付成功/失败 订单详情（外卖）
        
    }
    
  else  if (self.isPayResult) {
        
        NSArray *vcs = self.navigationController.viewControllers;
        [self.navigationController popToViewController:[vcs objectAtIndex:self.fromVC-1] animated:YES];
        [[NSNotificationCenter defaultCenter]postNotificationName:@"loginSuccess" object:self.urlStr];//支付成功/失败 商店刷新
        
        
    }
    else{
        
            [super goBack];
  
    }
    
}
#pragma 支付页面 完成
-(void)finseButton{
    
    if (self.isBuy) {  //是不是购买优惠券
        
        NSArray *vcs = self.navigationController.viewControllers;
        [self.navigationController popToViewController:[vcs objectAtIndex:self.fromVC-1] animated:YES];
        [[NSNotificationCenter defaultCenter]postNotificationName:@"loginSuccess" object:nil];//支付成功/失败 商店刷新

    }else{
        
        if(self.isTakeOut){  //外卖 堂食 返回我的订单页面
            
            NSArray *vcs = self.navigationController.viewControllers;
            [self.navigationController popToViewController:[vcs objectAtIndex:self.fromVC-2] animated:YES];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"findOrderInfo" object:nil];//支付成功/失败 订单详情（外卖）
        }else{
            NSArray *vcs = self.navigationController.viewControllers;
            [self.navigationController popToViewController:[vcs objectAtIndex:self.fromVC-1] animated:YES];
            [[NSNotificationCenter defaultCenter]postNotificationName:@"loginSuccess" object:nil];//支付成功/失败 商店刷新
            
            
        }
    }

}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
    self.isFinsh = YES;
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString isEqualToString:@"hq://register"]) {
            //表示点击了注册按钮，填写要执行的代码
            
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
            BMSQ_RegistViewController *vc = [storyboard instantiateViewControllerWithIdentifier:@"BMSQ_Regist"];
            vc.isJoin = YES;
            BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
            [self presentViewController:nav animated:YES completion:nil];
   
            return NO;
        }else if ([urlString hasPrefix:@"hq://getUserCouponDetail"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
//            [mutableStr deleteCharactersInRange:NSMakeRange(0, 15)];
               NSArray *array = [str componentsSeparatedByString:@"="]; //从字符A中分隔成2个元素的数组
            NSString *mutableStr = [array objectAtIndex:1];
            
            BMSQ_CouponDetailViewController *vc = [[BMSQ_CouponDetailViewController alloc]init];
            vc.userCouponCode = mutableStr;
            [self.navigationController pushViewController: vc animated:YES];
            
            return  NO;
        }else if ([urlString hasPrefix:@"hq://robCoupon?batchCouponCode"]) {
            
            int n = 31;
            NSString *couponCode = [urlString substringFromIndex:n];
            NSLog(@"%@",couponCode);
            NSLog(@"抢券");
            [self btnRobCouponClick:couponCode];
            
            
        }else if ([urlString hasPrefix:@"hg://share?params="]) {
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *shareParams = [self dictionaryWithJsonString:mutableStr];
            if (shareParams) {
                [self clickShare:shareParams];
                
            }
            
        }else if ([urlString hasPrefix:@"hs://refund"]) {   //退款

            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            BMSQ_CouponOPViewController *vc = [[BMSQ_CouponOPViewController alloc]init];
            vc.urlStr =  [NSString stringWithFormat:@"%@%@",H5_URL,mutableStr];
            [self.navigationController pushViewController:vc animated:YES];
            
        }
        
    }
    
    return YES;
}
- (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
    if (jsonString == nil) {
        return nil;
    }
    
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:&err];
    if(err) {
        NSLog(@"json解析失败：%@",err);
        return nil;
    }
    return dic;
}


- (void)btnRobCouponClick:(NSString *)couponCode{
    
    //    NSLog(@"抢");
    
    
    
    if (![gloabFunction isLogin])
    {
        [self getLogin];
        return;
    }
    
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:couponCode forKey:@"batchCouponCode"];
    [params setObject:@"2" forKey:@"sharedLvl"];
    NSString* vcode = [gloabFunction getSign:@"grabCoupon" strParams:couponCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"grabCoupon" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString* code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券成功"];
            
            
        }
        else if ([code isEqualToString:@"50000"])
        {
            [self showAlertView:@"抢券失败"];
        }
        else if ([code isEqualToString:@"20000"])
        {
            [self showAlertView:@"失败，请重试"];
        }
        else if ([code isEqualToString:@"80221"])
        {
            [self showAlertView:@"抢完了"];
        }
        else if ([code isEqualToString:@"80220"])
        {
            [self showAlertView:@"过期了"];
        }
        else if ([code isEqualToString:@"80220"])
        {
            [self showAlertView:@"过期了"];
        }
        else if ([code isEqualToString:@"80222"])
        {
            [self showAlertView:@"领用/抢的数量达到上限"];
        }
        else if ([code isEqualToString:@"80223"])
        {
            [self showAlertView:@"优惠券不存在，不可领"];
        }
        else if ([code isEqualToString:@"80235"])
        {
            [self showAlertView:@"你已享受过该特权"];
        }
        
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"freshEff" object:nil];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [ProgressManage closeProgress];
        [self showAlertView:@"抢券失败"];
    }];
    
}

-(void)clickShare:(NSDictionary *)dicShare{
    
    NSString *remark = [dicShare objectForKey:@"content"];
    NSString *title = [dicShare objectForKey:@"title"];
    NSString *image = [dicShare objectForKey:@"icon"];
    NSString *url = [dicShare objectForKey:@"link"];
    [BMSQ_Share shareClick:remark imagePath:image title:title url:url];
    
   }

-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}


@end
