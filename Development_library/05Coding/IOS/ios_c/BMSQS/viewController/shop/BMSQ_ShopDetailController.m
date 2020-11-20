//
//  BMSQ_ShopDetailController.m
//  BMSQC
//
//  Created by djx on 15/8/5.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopDetailController.h"



#import "BMSQ_BuyOrderController.h"

#import "BMSQ_PayDetailSViewController.h"

#import "BMSQ_TicketViewController.h"

#import "SVProgressHUD.h"

#import "UIImageView+WebCache.h"
#import "BMSQ_PhotoViewController.h"

#import "BMSQ_shopCouponTableViewCell.h"
#import "BMSQ_shopCouponNTableViewCell.h"
#import "BMSQ_CouponQRcodeControllerViewController.h"
#import "PhotoScrollView.h"
#import "BMSQ_QR.h"

#import "BMSQ_PayDetailViewController.h"

#import "BMSQ_PayCardViewController.h"

#import "BMSQ_OrderViewController.h"

#import "Pay_SecViewController.h"   //折扣 抵扣 使用VC

#import "BMSQ_MemberChartViewController.h"



#import "BMSQ_DinningViewController.h"


#import "BMSQ_ScanViewController.h"
#import "BMSQ_ScanSuccessViewController.h"
#import "BMSQ_ScanLoseViewController.h"


#import "BMSQ_correctionViewController.h"
#import "BMSQ_BuyCouponViewController.h"

#import "BMSQ_CouponDetailSingleViewController.h"  //单张
#import "BMSQ_CouponDetailViewController.h"        //批次
#import "BMSQ_ActivityWebViewController.h"

#import "BMSQ_Share.h"
#import "MobClick.h"
@interface BMSQ_ShopDetailController ()<BMSQ_shopCouponTableViewCellDelegate,BMSQ_shopCouponNTableViewCellDelegate,UIWebViewDelegate, ScanDelegate, scanSuccessDelegate, scanLoseDelegate>{
    BOOL upOrdown;
    NSTimer * timer;
    UIImageView* _line;
    int num;
    NSDictionary *dic_Ordering;
}
@property (nonatomic, assign)float discount;;  //打几折
@property (nonatomic, assign)float disMoney;  //最高优惠多少元

@property (nonatomic, strong)UIWebView *webView;
@property (nonatomic, strong)NSDictionary *takeoutDic;

@end

@implementation BMSQ_ShopDetailController

@synthesize shopCode;

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ShopDetail"];//
}



- (void)viewDidLoad {
    [super viewDidLoad];

    
    [self setNavigationBar];
    
    //刷新
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(refreshWebView) name:@"loginSuccess" object:nil];
    //查看订单
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(findOrderInfo) name:@"findOrderInfo" object:nil];
    //查看优惠券订单
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(findCouponInfo:) name:@"findCouponInfo" object:nil];
    
    //用户领取 使用 优惠券需要 刷新新优惠券页面数据
    [[NSNotificationCenter defaultCenter]postNotificationName:@"refreshCoupon" object:nil];


    self.view.backgroundColor = [UIColor redColor];
    
    
//    if (self.isRefund) {
//        self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
//        NSString *questStr = [NSString stringWithFormat:@"%@%@",H5_URL,self.urlStr];
//        [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:questStr]]];
//        self.webView.delegate = self;
//        [self.view addSubview:self.webView];
//        [self.view bringSubviewToFront:self.webView];
//
//    }else{
        NSString *userCode = [gloabFunction getUserCode];
        
        if(![userCode isEqualToString:@"(null)"]){
            self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
            NSString *str = [NSString stringWithFormat:@"%@Browser/getShopInfo/shopCode/%@/userCode/%@/operationType",H5_URL,self.shopCode,[gloabFunction getUserCode]];
            [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
            self.webView.delegate = self;
            [self.view addSubview:self.webView];
            [self.view bringSubviewToFront:self.webView];
        }else{
            self.webView = [[UIWebView alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-20)];
            NSString *str = [NSString stringWithFormat:@"%@Browser/getShopInfo/shopCode/%@/operationType",H5_URL,self.shopCode];
            [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
            self.webView.delegate = self;
            [self.view addSubview:self.webView];
            [self.view bringSubviewToFront:self.webView];
        }
//    }
    
    self.webView.scrollView.bounces = NO;
}
//支付成功 加载定单详情 外卖
-(void)findOrderInfo{
    
    NSString *str = [NSString stringWithFormat:@"%@Browser/unReceiveOrder/orderCode/%@",H5_URL,[self.takeoutDic objectForKey:@"orderCode"]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];

}

-(void)refreshWebView{
    NSString *str = [NSString stringWithFormat:@"%@Browser/getShopInfo/shopCode/%@/userCode/%@",H5_URL,self.shopCode,[gloabFunction getUserCode]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];

}

-(void)findCouponInfo:(NSNotification *)notification{
    
    NSString *url = notification.object;
    NSString *str = [NSString stringWithFormat:@"%@%@",H5_URL,url];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];

}


-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    [SVProgressHUD dismiss];
    [MobClick endLogPageView:@"ShopDetail"];

}

-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}
-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
}
-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];
        if ([urlString isEqualToString:@"hs://follow"]) {  //关注、取消关注
           //关注事件
            [[NSNotificationCenter defaultCenter]postNotificationName:@"frshFollewed" object:nil];;
        }
        else if ([urlString hasPrefix:@"hs://batchCouponInfo"]) { //优惠券详情
            
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSArray *array = [str componentsSeparatedByString:@"&"]; //从字符A中分隔成2个元素的数组
            NSString *batchCouponCode = [array objectAtIndex:0];
            array = [batchCouponCode componentsSeparatedByString:@"="];
            NSString *batchCouponCodeStr = [array objectAtIndex:1];
            
            BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
            couponVC.userCouponCode = batchCouponCodeStr;
            [self.navigationController pushViewController:couponVC animated:YES];

            
        }
        else if ([urlString hasPrefix:@"hs://couponPay?batchCouponCode"]) { //抢购
#pragma mark ----- 购买优惠券
            if (![gloabFunction isLogin]) {
                     [self getLogin];
            }else{
                NSURL *url = [NSURL URLWithString:urlString];
                NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
                [mutableStr deleteCharactersInRange:NSMakeRange(0, 16)];
                BMSQ_BuyCouponViewController *vc = [[BMSQ_BuyCouponViewController alloc]init];
                vc.batchCouponCode = mutableStr;
                vc.fromVC = (int)self.navigationController.viewControllers.count;
                [self.navigationController pushViewController:vc animated:YES];
            }
           
        }
        else if ([urlString isEqualToString:@"hs://jiucuo"]) { //纠错

            BMSQ_correctionViewController *vc =[[BMSQ_correctionViewController alloc] init];
            vc.hidesBottomBarWhenPushed = YES;
            vc.shopCode = self.shopCode;
            [self.navigationController pushViewController:vc animated:YES];
        }
        else if ([urlString isEqualToString:@"hs://backup"]) { //去看看
            [self.navigationController popViewControllerAnimated:YES];
        }
//        else if ([urlString hasPrefix:@"hs://couponInfo?userCouponCode"]){
//            NSURL *url = [NSURL URLWithString:urlString];
//            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
//            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
//            [mutableStr deleteCharactersInRange:NSMakeRange(0, 15)];
//    
//            BMSQ_CouponDetailSingleViewController *vc = [[BMSQ_CouponDetailSingleViewController alloc]init];
//            vc.userCouponCode = mutableStr;
//            [self.navigationController pushViewController:vc animated:YES];
//
//        }
        else if ([urlString isEqualToString:@"hs://login"]){
            [self getLogin];
            
        }else if ([urlString hasPrefix:@"hs://icbcPay"]){  // 使用工行卡支付
            
            if (![gloabFunction isLogin]) {
                [self getLogin];
            }else{
                
                
                NSURL *url = [NSURL URLWithString:urlString];
                NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
                [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
                NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
                if (dic) {
                    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
                    vc.shopCode = [dic objectForKey:@"shopCode"];
                    vc.shopName = [dic objectForKey:@"shopName"];
                    vc.isNeedDiscount = YES;
                    if([dic objectForKey:@"orderCode"]){
                        vc.consumAmount =[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderAmount"]];
                        vc.isTakeOut = YES;
                        vc.orderCode = [dic objectForKey:@"orderCode"];
                        vc.orderNbr = [dic objectForKey:@"orderNbr"];
                    }else{
                        vc.isTakeOut = NO;
                        
                    }
                    vc.fromVC = (int)self.navigationController.viewControllers.count;
                    [self.navigationController pushViewController:vc animated:YES];
                }
            }
            
            
        }else if ([urlString hasPrefix:@"hs://useCoupon"]){  // 使用
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                [self UserCoupon:dic];
            }
            
            
            ///////
        
        }else if ([urlString hasPrefix:@"ho://icbcPay"]){  // 下一步支付页面
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                self.takeoutDic = dic;
                if ([dic objectForKey:@"userAddressId"]) {                     //是否已扫码

                    [self addTakeoutOrderOtherInfo:dic];
                }else{
                    dic_Ordering = dic;
                    [self getUserShopRecord:dic];
                }

            }
            
        }else if ([urlString hasPrefix:@"hs://share"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                [BMSQ_Share shareContent:dic];
            }

        }else if ([urlString isEqualToString:@"hs://shopAlbum"]) { //去看看

            BMSQ_PhotoViewController *vc = [[BMSQ_PhotoViewController alloc]init];
            vc.shopCode = self.shopCode;
            [self.navigationController pushViewController:vc animated:YES];
        
        
        }else if ([urlString hasPrefix:@"hs://call"]){
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 4)];
            
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"telprompt://%@",mutableStr]]];
            
        }else if ([urlString hasPrefix:@"hs://feedback"]){  //给商家留言
            BMSQ_MemberChartViewController *vc = [[BMSQ_MemberChartViewController alloc] init];
            vc.shopID = self.shopCode;
            NSString *strl = @"shopName=";
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, strl.length)];
            vc.myTitle = [NSString stringWithFormat:@"给%@留言", mutableStr];
            vc.hidesBottomBarWhenPushed = YES;
            [self.navigationController pushViewController:vc animated:YES];

            
        }else if ([urlString hasPrefix:@"hs://getActInfo"]){  //给商家留言
         
            if (![gloabFunction isLogin]) {
                [self getLogin];
            }else{
                NSURL *url = [NSURL URLWithString:urlString];
                NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
                NSArray *array = [str componentsSeparatedByString:@"="];
                NSString *activityCode = [array objectAtIndex:1];
   
                BMSQ_ActivityWebViewController *vc = [[BMSQ_ActivityWebViewController alloc]init];
                vc.urlStr = [NSString stringWithFormat:@"%@Browser/getActInfo?activityCode=%@&appType=1&userCode=%@",H5_URL,activityCode,[gloabFunction getUserCode]];
                vc.hidesBottomBarWhenPushed = YES;
                [self.navigationController pushViewController:vc animated:YES];
                
            }
        }
        
        
    }
    else if (navigationType == UIWebViewNavigationTypeOther){
        NSString *urlString=[[request URL] absoluteString];
        
        if ([urlString isEqualToString:@"ho://login"]){
            [self getLogin];
            
        }

        
        
    }
    
    return YES;
}

#pragma mark --外卖支付
-(void)addTakeoutOrderOtherInfo:(NSDictionary *)dic{
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[dic objectForKey:@"orderCode"] forKey:@"orderCode"];
    [params setObject:[dic objectForKey:@"userAddressId"] forKey:@"userAddressId"];
    if([dic objectForKey:@"remark"]){
        [params setObject:[dic objectForKey:@"remark"] forKey:@"remark"];
    }
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"addTakeoutOrderOtherInfo" strParams:[dic objectForKey:@"orderCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self.jsonPrcClient invokeMethod:@"addTakeoutOrderOtherInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        if(responseObject){
            if ([responseObject objectForKey:@"code"]) {
                if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue]== 50000) {
                    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
                    vc.shopCode = self.shopCode;
                    vc.shopName = self.shopName;
                    vc.isNeedDiscount = NO;
                    vc.consumAmount =[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderAmount"]];
                    vc.fromVC = (int)self.navigationController.viewControllers.count;
                    vc.isTakeOut = YES;
                    vc.orderCode = [dic objectForKey:@"orderCode"];
                    vc.orderNbr = [dic objectForKey:@"orderNbr"];
                    [wself.navigationController pushViewController:vc animated:YES];
                }
            }
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
    
}

#pragma mark ---点餐支付
-(void)addNotTakeoutOrderOtherInfo:(NSDictionary *)dic{
   
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[dic objectForKey:@"orderCode"] forKey:@"orderCode"];
    if([dic objectForKey:@"remark"]){
        [params setObject:[dic objectForKey:@"remark"] forKey:@"remark"];
    }
    
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"addNotTakeoutOrderOtherInfo" strParams:[dic objectForKey:@"orderCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    
    [self.jsonPrcClient invokeMethod:@"addNotTakeoutOrderOtherInfo" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if(responseObject){
            if ([responseObject objectForKey:@"code"]) {
                if ([[NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]]intValue]== 50000) {
                    
                    NSString *eatPayType = [NSString stringWithFormat:@"%@",[dic objectForKey:@"eatPayType"]];
                    
                    if ([eatPayType isEqualToString:@"1"]||[eatPayType isEqualToString:@"0"]  ) {
                        
                        BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
                        vc.shopCode = [dic objectForKey:@"shopCode"];
                        vc.shopName = [dic objectForKey:@"shopName"];
                        vc.isTakeOut = YES;
                        vc.isNeedDiscount = YES;
                        vc.orderCode = [dic objectForKey:@"orderCode"];
                        vc.orderNbr = [dic objectForKey:@"orderNbr"];
                        vc.consumAmount =[NSString stringWithFormat:@"%@",[dic objectForKey:@"orderAmount"]];
                        vc.fromVC = (int)self.navigationController.viewControllers.count;
                        vc.isDinning = YES;
                        [wself.navigationController pushViewController:vc animated:YES];
                        
                    }else{
                        
                        NSString *str = [NSString stringWithFormat:@"%@Browser/unReceiveOrder/orderCode/%@",H5_URL,[dic objectForKey:@"orderCode"]];
                        [wself.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
                        
                    }
                    

                }
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];

    
}

#pragma mark -- 使用--

-(void)UserCoupon:(NSDictionary *)dic{
    
  
        int type =(int)[[dic objectForKey:@"couponType"]integerValue];
        
        switch (type) {
            case 1:  //N元购
            {
                
                [self payCard:dic];
            }
                break;
            case 2:
            {
            }
                break;
            case 3:   //抵扣券 折扣券 一样
            {
                [self buyClick:dic];
                
            }
                break;
            case 4:   //折扣券 抵扣券 一样
            {
                [self buyClick:dic];
            }
                break;
            case 5:    //实物券 体验券 一样
            {
                
                [self gotoPay_secVC:dic];
                
                
            }
                break;
            case 6:
            {
                
                [self gotoPay_secVC:dic];
                
                
            }
                break;
            case 32:
            {
                
                [self buyClick:dic];
                
                
            }
                break;
            case 33:
            {
                
                [self buyClick:dic];
                
                
            }
                break;
            case 7:
            {
                
//                [self buyClick:dic];
                BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
                couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
                [self.navigationController pushViewController:couponVC animated:YES];

                
            }
                break;
            case 8:
            {
                
//                [self buyClick:dic];
                BMSQ_CouponDetailViewController *couponVC = [[BMSQ_CouponDetailViewController alloc] init];
                couponVC.userCouponCode = [dic objectForKey:@"batchCouponCode"];
                [self.navigationController pushViewController:couponVC animated:YES];

                
            }
                break;
            default:
                break;
    }

}
#pragma mark N元购
-(void)payCard:(NSDictionary *)dic{
    
    BMSQ_PayDetailViewController *vc = [[BMSQ_PayDetailViewController alloc]init];
    vc.shopCode =self.shopCode;
    vc.shopName = self.shopName;
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
    vc.myTitle =self.shopName ;
    vc.userCouponCode = [dic objectForKey:@"userCouponCode"];
    vc.shopCode = self.shopCode;
    vc.imageUrl =self.shopImage;
    [self.navigationController pushViewController:vc animated:YES];
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

#pragma mark - ScanviewController delegate 点击扫码
- (void)btnClick {
    [self setZbar];
   
    
}

#pragma mark - scanSuccess Delegate
//返回加菜
- (void)btnScanSuccessReturn {
//    http://baomi.suanzi.cn/Api/Browser/cEatOrder?shopCode=3a4e4a05-198e-0d03-ce7e-a7036ce70082&userCode=0b7afc86-3188-4e47-abd3-54f52e91f272&orderCode=dfa45d9c-f265-10a4-ee55-56f3dbe98638
    
    NSString *str = [NSString stringWithFormat:@"%@/Browser/cEatOrder?shopCode=%@&userCode=%@&orderCode=%@",H5_URL,[dic_Ordering objectForKey:@"shopCode"],[gloabFunction getUserCode],[dic_Ordering objectForKey:@"orderCode"]];
    [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
    
}

//确认下单
- (void)btnScanSuccessCon {
    [self addNotTakeoutOrderOtherInfo:dic_Ordering];

}

#pragma mark - scanLoseDelegate
//扫描失败返回
- (void)btnScanLReturn {
    
    
}

//扫描失败重新扫描
- (void)btnScanLAgain {
    BMSQ_ScanViewController *vc = [[BMSQ_ScanViewController alloc] init];
    vc.scDelegate = self;
    [self presentViewController:vc animated:YES completion:^{
        
    }];
    
}


#pragma mark ------------------------ 二维码扫描
- (void)setZbar {
    
    ZBarReaderViewController *reader = [ZBarReaderViewController new];
    reader.readerDelegate = self;
    reader.supportedOrientationsMask = ZBarOrientationMaskAll;
    reader.showsHelpOnFail = NO;
    reader.scanCrop = CGRectMake(0.1, 0.2, 0.8, 0.8);
    
    ZBarImageScanner *scanner = reader.scanner;
    
    [scanner setSymbology: ZBAR_I25
                   config: ZBAR_CFG_ENABLE
                       to: 0];
    
    UIView * view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_HEIGHT)];
    view.backgroundColor = [UIColor clearColor];
    reader.cameraOverlayView = view;
    
    UILabel * label = [[UILabel alloc] initWithFrame:CGRectMake(20, 20, APP_VIEW_WIDTH - 40, 40)];
    label.text = @"请将扫描的二维码至于下面的框内！";
    label.textColor = [UIColor whiteColor];
    label.textAlignment = 1;
    label.lineBreakMode = 0;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
    [view addSubview:label];
    
    UIImageView * image = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"pick_bg.png"]];
    image.frame = CGRectMake(20, 80, APP_VIEW_WIDTH-40, APP_VIEW_WIDTH - 40);
    [view addSubview:image];

    _line = [[UIImageView alloc] initWithFrame:CGRectMake(30, 10, APP_VIEW_WIDTH - 100, 2)];
    _line.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [image addSubview:_line];
    //定时器，设定时间过1.5秒，
    timer = [NSTimer scheduledTimerWithTimeInterval:.02 target:self selector:@selector(animation1) userInfo:nil repeats:YES];
    
    [self presentViewController:reader animated:YES completion:nil];
    
}

-(void)animation1
{
    if (upOrdown == NO) {
        num ++;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (2*num == 260) {
            upOrdown = YES;
        }
    }
    else {
        num --;
        _line.frame = CGRectMake(30, 10+2*num, 220, 2);
        if (num == 0) {
            upOrdown = NO;
        }
    }
}

-(void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    [picker dismissViewControllerAnimated:YES completion:^{
        [picker removeFromParentViewController];
    }];
}

- (void) imagePickerController: (UIImagePickerController*) reader didFinishPickingMediaWithInfo: (NSDictionary*) info
{
    [timer invalidate];
    _line.frame = CGRectMake(30, 10, 220, 2);
    num = 0;
    upOrdown = NO;
    
    id<NSFastEnumeration> results =
    [info objectForKey: ZBarReaderControllerResults];
    ZBarSymbol *symbol = nil;
    for(symbol in results)
        break;
    
    [reader dismissViewControllerAnimated:YES completion: nil];
    
    
    NSString *scanResult=symbol.data;
    if(scanResult==nil||scanResult.length==0){
        return ;
    }
    
    NSRange range=[scanResult rangeOfString:@"="];

    if (range.location>0&&range.length>0) {
        scanResult = [scanResult substringFromIndex:NSMaxRange(range)];
        NSLog(@"%@",scanResult);
        
        NSString *strshopCode = [NSString stringWithFormat:@"%@",[dic_Ordering objectForKey:@"shopCode"]];
        
        if ([strshopCode isEqualToString:scanResult]) {
            [self scanCode:scanResult];
            
            
        }else {
            BMSQ_ScanLoseViewController *vc = [[BMSQ_ScanLoseViewController alloc] init];
            vc.ScanLdelegate = self;
            [self presentViewController:vc animated:YES completion:^{
                
            }];
//            CSAlert(@"不是同一家店铺");
            
        }
    }
}






          //-- 使用工行卡支付  --
-(void)buyClick:(NSDictionary *)dic{
 
    BMSQ_PayDetailSViewController *vc = [[BMSQ_PayDetailSViewController alloc]init];
    vc.shopCode = self.shopCode;
    vc.shopName = self.shopName;
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

#pragma mark - request
- (void)scanCode:(NSString *)str { //扫码
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:str forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"scanCode" strParams:[gloabFunction getUserCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"scanCode" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if(responseObject){
            NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
            if (code.intValue == 50000) { //-----------扫描成功后跳转
                NSLog(@"成功");
                BMSQ_ScanSuccessViewController *vc = [[BMSQ_ScanSuccessViewController alloc] init];
                vc.scansDelegate = self;
                [self presentViewController:vc animated:YES completion:^{
                    
                }];
                
            }else if ((code.intValue == 20000)){
                BMSQ_ScanLoseViewController *vc = [[BMSQ_ScanLoseViewController alloc] init];
                vc.ScanLdelegate = self;
                [self presentViewController:vc animated:YES completion:^{
                    
                }];
                
                
            }else {
                CSAlert(@"未知的错误!");
                
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
    
}

//用户扫码时间是否超过90分钟
- (void)getUserShopRecord:(NSDictionary*)dictionary {
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:[dictionary objectForKey:@"shopCode"] forKey:@"shopCode"];
    [self initJsonPrcClient:@"2"];
    NSString* vcode = [gloabFunction getSign:@"getUserShopRecord" strParams:[gloabFunction getUserCode]];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"getUserShopRecord" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if(responseObject){
            NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
            if (code.intValue == 1) {
                NSLog(@"扫描二维码");
                BMSQ_ScanViewController *vc = [[BMSQ_ScanViewController alloc] init];
                vc.scDelegate = self;
                [self presentViewController:vc animated:NO completion:^{
                    
                }];
                
            }else if ((code.intValue == 0)){
                [self addNotTakeoutOrderOtherInfo:dictionary];
                
            }else {
                CSAlert(@"未知的错误!");
            }
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
    }];
    
    
}



@end
