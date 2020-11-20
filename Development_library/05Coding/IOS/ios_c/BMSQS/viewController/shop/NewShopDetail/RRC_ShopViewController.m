//
//  RRC_ShopViewController.m
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/22.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RRC_ShopViewController.h"
#import "SVProgressHUD.h"
#import <ShareSDK/ShareSDK.h>
#import "BMSQ_PayDetailSViewController.h"

#import "ZBarSDK.h"
#import "BMSQ_ScanViewController.h"
#import "BMSQ_ScanLoseViewController.h"
#import "BMSQ_ScanSuccessViewController.h"
#import "MobClick.h"
@interface RRC_ShopViewController () <ScanDelegate, scanLoseDelegate, scanSuccessDelegate, ZBarReaderDelegate>

{
    UIWebviewEx*  webView; //webView，加载网页
    BOOL upOrdown;
    NSTimer * timer;
    UIImageView* _line;
    int num;
    NSDictionary *dic_Ordering;
    

}

@property(nonatomic,strong)NSString* shopCode;
@property (nonatomic, strong)NSString *shopName;

@property (nonatomic, assign)BOOL isFinsh;
@property (nonatomic, strong)NSDictionary *takeoutDic;

@property (nonatomic,strong)NSString *urlStr;


@end

@implementation RRC_ShopViewController
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


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"ShopViewController"];// 
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    
}

-(void)viewWillDisappear:(BOOL)animated{
    [SVProgressHUD dismiss];
    [MobClick endLogPageView:@"ShopViewController"];
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
        
        
        webView = [[UIWebviewEx alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 20)];
        [webView setBackgroundColor:[UIColor whiteColor]];
        [self.view addSubview:webView];
        
        
        
        
    }
    
    webView.delegate = self;
    [webView stopLoading];
    
    if (self.requestUrl != nil && self.requestUrl.length > 0)
    {
        [webView loadRequestWithString:self.requestUrl];
    }
    
    
    
}


-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    if (navigationType==UIWebViewNavigationTypeLinkClicked) {
        
        NSString *urlString=[[request URL] absoluteString];// 支付买单

        if ([urlString hasPrefix:@"hs://call?tel="]){

            
        }else if ([urlString hasPrefix:@"ho://icbcPay?params="]){  // 下一步支付页面
            NSURL *url = [NSURL URLWithString:urlString];
            NSString *str =[url.query stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
            
            NSMutableString *mutableStr = [[NSMutableString alloc]initWithString:str];
            [mutableStr deleteCharactersInRange:NSMakeRange(0, 7)];
            NSDictionary *dic = [self dictionaryWithJsonString:mutableStr];
            if (dic) {
                self.takeoutDic = dic;
                if ([dic objectForKey:@"userAddressId"]) {   //外卖
                    
                    [self addTakeoutOrderOtherInfo:dic];
                }else{
                    dic_Ordering = dic;
                    [self getUserShopRecord:dic];
                }
                
            }
            
        }else if ([urlString hasPrefix:@"http://baomi.suanzi.cn/Api/Browser/getShopInfo?shopCode="]){
            [self.navigationController popViewControllerAnimated:YES];
        
        } else if ([urlString hasPrefix:@"hs://backup"]){
            [self.navigationController popViewControllerAnimated:YES];
        }
        else if ([urlString hasPrefix:@"hs://getShopInfo"]){
            [self.navigationController popViewControllerAnimated:YES];
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
                    vc.shopCode =[dic objectForKey:@"shopCode"];
                    vc.shopName = [dic objectForKey:@"shopName"];
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
                        [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
                        
                    }
                    
                    
                }
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


#pragma mark - ScanviewController delegate 点击扫码
- (void)btnClick {
    [self setZbar];
    
    
}

#pragma mark - scanSuccess Delegate
//返回加菜
- (void)btnScanSuccessReturn {
    //    http://baomi.suanzi.cn/Api/Browser/cEatOrder?shopCode=3a4e4a05-198e-0d03-ce7e-a7036ce70082&userCode=0b7afc86-3188-4e47-abd3-54f52e91f272&orderCode=dfa45d9c-f265-10a4-ee55-56f3dbe98638
    
    NSString *str = [NSString stringWithFormat:@"%@/Browser/cEatOrder?shopCode=%@&userCode=%@&orderCode=%@",H5_URL,[dic_Ordering objectForKey:@"shopCode"],[gloabFunction getUserCode],[dic_Ordering objectForKey:@"orderCode"]];
    [webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:str]]];
    
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


-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [SVProgressHUD dismiss];
    self.isFinsh = YES;
}





-(void)webViewDidStartLoad:(UIWebView *)webView{
    [SVProgressHUD showWithStatus:@""];
}









@end
