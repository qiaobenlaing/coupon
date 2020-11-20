//
//  AppDelegate.m
//  RRCShop
//
//  Created by djx on 14-12-2.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "AppDelegate.h"

#import "RootViewController.h"

#import "BMSQ_HomeController.h"
#import "BMSQ_CouponController.h"
#import "BMSQ_BenefitCardController.h"
#import "BMSQ_MyBusinessController.h"


#import "AppDelegate+ScreenLayout.h"
#import "BMSQ_LoginViewController.h"

#import "BaseNavViewController.h"
#import "actionPerform.h"
#import "ChineseToPinyin.h"

#import "APService.h"

//ShareSDK必要头文件
#import <ShareSDK/ShareSDK.h>

//腾讯开放平台（对应QQ和QQ空间）SDK头文件
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>

//微信SDK头文件
#import "WXApi.h"

//新浪微博SDK头文件

#import <QZoneConnection/ISSQZoneApp.h>

#import "AFJSONRPCClient.h"
#import "MobClick.h"

#import "CustomNotificationView.h"
#import "NotifiCationViewController.h"
#import <CoreLocation/CoreLocation.h>   //定位

#import "BMSQ_JPushVCWebVIewController.h"


#import "SDWebImageManager.h"


#define UMSYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)

#define _IPHONE80_ 80000

#define IOS8 [[UIDevice currentDevice].systemVersion doubleValue] >= 8.0


#define LOGINTAG 100

@interface AppDelegate ()<CLLocationManagerDelegate,UIAlertViewDelegate,CustomNotificationViewDeleagte>
{
  
    CLLocationManager *_locationManager;
    UIImageView* imageView_TabBar;
}

@property (nonatomic, strong)CustomNotificationView *notiView;
@property (nonatomic, strong)NSArray *openCityS;
@property (nonatomic, strong)NSArray *tabbarS;


@property (nonatomic, strong)UITabBarItem *tabbar1;
@property (nonatomic, strong)UITabBarItem *tabbar2;
@property (nonatomic, strong)UITabBarItem *tabbar3;
@property (nonatomic, strong)UITabBarItem *tabbar4;


@property (nonatomic, strong)NSMutableArray *seleTabImage;
@property (nonatomic, strong)NSMutableArray *norTabImage;

@property (nonatomic, strong)UITabBarController *tabBarController;




@end

@implementation AppDelegate


@synthesize viewDelegate = _viewDelegate;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    //
    
    self.seleTabImage = [[NSMutableArray alloc]init];
    self.norTabImage = [[NSMutableArray alloc]init];
    
    /* 开通城市  */
    [self listOpenCity];
    [self aotoLogin];
    [self AppDelegateScreenLayout];
    
    /* 默认值 */
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    [userDefaults setObject:@"杭州市" forKey:CITY];
    [userDefaults setObject:@"119.990712"  forKey:LONGITUDE];
    [userDefaults setObject:@"30.275237" forKey:LATITUDE];
    [userDefaults synchronize];
    
    /* 定位 */
    [self initializeLocationService];
    
    //分享
    [ShareSDK registerApp:@"86e43e546dfa"];
    //极光
    //JPush
#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1
    if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
        //可以添加自定义categories
        [APService registerForRemoteNotificationTypes:(UIUserNotificationTypeBadge |
                                                       UIUserNotificationTypeSound |
                                                       UIUserNotificationTypeAlert)
                                           categories:nil];
    } else {
        //categories 必须为nil
        [APService registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
                                                       UIRemoteNotificationTypeSound |
                                                       UIRemoteNotificationTypeAlert)
                                           categories:nil];
    }
#else
    //categories 必须为nil
    [APService registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
                                                   UIRemoteNotificationTypeSound |
                                                   UIRemoteNotificationTypeAlert)
                                       categories:nil];
#endif
    // Required
    [APService setupWithOption:launchOptions];
    [APService registrationID];
    
    
    NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kJPFNetworkDidReceiveMessageNotification object:nil];
    
    //极光
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
    
    
    
    [self initializePlat];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"] forKey:@"currenVersion"];
    [userDefault synchronize];
    
    NSString *version = [userDefault objectForKey:@"Version"];
    
    //友盟统计
    //    564005d167e58ea9df000197
    NSString *mobClickAppKey;
    if ([BASE_URL  isEqual: @"http://baomi.suanzi.cn/Wechat"]) {
        //测试版
        mobClickAppKey = @"56372c8de0f55a3f9c005b8e";
        
    }else if ([BASE_URL isEqual:@"http://huiquan.suanzi.cn/Wechat"]){
        //生产环境appKey
        mobClickAppKey = @"564005d167e58ea9df000197";
        
    }
    
    [MobClick startWithAppkey:mobClickAppKey reportPolicy:BATCH   channelId:nil];
    [MobClick setAppVersion:version];
    
    return YES;
}



#pragma mark 自动登录
-(void)aotoLogin{
    NSUserDefaults *user = [NSUserDefaults standardUserDefaults];
    NSDictionary *userDic = [user objectForKey:APP_USER_AOCNUM_KEY];
    if (userDic.count<=0) {
        return;
    }
    
    AFJSONRPCClient *jsoncline = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject: [userDic objectForKey:@"mobileNbr"] forKey:@"mobileNbr"];
    [params setObject:[userDic objectForKey:@"password"] forKey:@"password"];
    [params setObject:@"1" forKey:@"loginType"];
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *regid = [userDefaults objectForKey:RegistratcionID];
    if (regid.length>0) {
        [params setObject:regid forKey:@"registrationId"];
        
    }else{
        [params setObject:@"" forKey:@"registrationId"];
        
    }
    [jsoncline invokeMethod:@"login" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                NSUserDefaults *uD = [NSUserDefaults standardUserDefaults];
                [uD setObject:responseObject forKey:APP_USER_INFO_KEY];
                [uD setObject:params forKey:APP_USER_AOCNUM_KEY];
                [uD synchronize];

            }
                break;
            default:
                break;
        }
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {

        NSLog(@"9090");
        
        
    }];

    
}

#pragma mark 退出登录
- (void)loginOut
{
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *regid = [userDefaults objectForKey:RegistratcionID];

    
    AFJSONRPCClient *jsoncline = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_COMM_URL]];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:@"1" forKey:@"appType"]; //0B 1C
    [params setObject:regid forKey:@"registrationId"];

  
    NSString* vcode = [gloabFunction getSign:@"logoff" strParams:[gloabFunction getUserToken]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [jsoncline invokeMethod:@"logoff" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:APP_USER_INFO_KEY];
        [defaults removeObjectForKey:APP_USER_AOCNUM_KEY];
        
        [self initRootViewController];

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults removeObjectForKey:APP_USER_INFO_KEY];
        [defaults removeObjectForKey:APP_USER_AOCNUM_KEY];
        
        [self initRootViewController];

    }];
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"logoff" object:nil];
}
//获取已开通的城市
- (void)listOpenCity{
    
    [SVProgressHUD showWithStatus:@""];
    
    __weak typeof(self) weakSelf = self;
    AFJSONRPCClient *jsoncline = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    [jsoncline invokeMethod:@"listOpenCity" withParameters:nil success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        weakSelf.openCityS = responseObject;
        
        if (weakSelf.openCityS.count ==1) {
            NSDictionary *dic = [weakSelf.openCityS objectAtIndex:0];
            NSString *cityName = [dic objectForKey:@"name"];
            NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
            [userDefaults setObject:cityName forKey:SELECITY];
            [userDefaults synchronize];
            
        }else{
            NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
            NSString *cityName = [userDefaults objectForKey:CITY];
            NSString *seleCityName = [userDefaults objectForKey:SELECITY];
            
            if (!seleCityName.length==0) {
                BOOL ishaveCity = NO;
                for (NSDictionary *dic in weakSelf.openCityS) {
                    NSString *values = [dic objectForKey:@"name"];
                    if ([cityName isEqualToString:values]) {
                        ishaveCity = YES;
                        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
                        [userDefaults setObject:cityName forKey:SELECITY];
                        [userDefaults synchronize];
                    }
                }
            }else{
                BOOL ishaveCity = NO;
                for (NSDictionary *dic in weakSelf.openCityS) {
                    NSString *values = [dic objectForKey:@"name"];
                    if ([cityName isEqualToString:values]) {
                        ishaveCity = YES;
                        NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
                        [userDefaults setObject:cityName forKey:SELECITY];
                        [userDefaults synchronize];
                    }
                }
                if (!ishaveCity) {
                    NSDictionary *dic = [weakSelf.openCityS objectAtIndex:0];
                    NSString *cityName = [dic objectForKey:@"name"];
                    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
                    [userDefaults setObject:cityName forKey:SELECITY];
                    [userDefaults synchronize];
                }
                
            }
        }
//        getOpenListCity
        [[NSNotificationCenter defaultCenter]postNotificationName:@"getOpenListCity" object:weakSelf.openCityS];
        [weakSelf getHomeTabList];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
    }];
}

#pragma mark 请求选项卡
- (void)getHomeTabList
{
    
    NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
    NSString *cityName = [userDefaults objectForKey:SELECITY];
    
    AFJSONRPCClient *jsoncline = [[AFJSONRPCClient alloc] initWithEndpointURL:[NSURL URLWithString:APP_SERVERCE_CLIENT_URL]];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:cityName forKey:@"city"]; //0B 1C
    
    
    NSString* vcode = [gloabFunction getSign:@"getHomeTabList" strParams:[gloabFunction getUserToken]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    __weak typeof(self) weakSelf = self;
    [jsoncline invokeMethod:@"getHomeTabList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        weakSelf.tabbarS = responseObject;
        [weakSelf initRootViewController];
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"错误");
        
    }];
}


/** 定位 **/
- (void)initializeLocationService {
    // 初始化定位管理器
    _locationManager = [[CLLocationManager alloc] init];
    // 设置代理
    _locationManager.delegate = self;
    // 设置定位精确度到米
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    // 设置过滤器为无
    _locationManager.distanceFilter = kCLDistanceFilterNone;
    // 开始定位
    // 取得定位权限，有两个方法，取决于你的定位使用情况
    // 一个是requestAlwaysAuthorization，一个是requestWhenInUseAuthorization
#ifdef __IPHONE_8_0 // 这里判断是否使用iOS8 SDK进行开发
    // 这里判断是否为在iOS8系统上运行
    // 宏内容为：
    //
    if (IOS8) {
        // 取得定位权限，有两个方法，取决于你的定位使用情况
        // 一个是requestAlwaysAuthorization，一个是requestWhenInUseAuthorization
        [_locationManager requestAlwaysAuthorization];
    }
#endif
    
    
    [_locationManager startUpdatingLocation];
}
-(void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations{
    
    CLGeocoder *geoCoder = [[CLGeocoder alloc]init];
    
    [geoCoder reverseGeocodeLocation:[locations lastObject] completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        
        if(error == nil){
            CLPlacemark *mark = [placemarks lastObject];
            NSString *cityName = mark.locality;
            CLLocationCoordinate2D coordinate2d = mark.location.coordinate;
            NSUserDefaults *userDefaults = [NSUserDefaults standardUserDefaults];
            [userDefaults setObject:cityName forKey:CITY];
            [userDefaults setObject:[NSString stringWithFormat:@"%f",coordinate2d.longitude]  forKey:LONGITUDE];
            [userDefaults setObject:[NSString stringWithFormat:@"%f",coordinate2d.latitude] forKey:LATITUDE];
            [userDefaults synchronize];
 
        }
        
        
    }];
    
    
}

-(void)test{
    
    BMSQ_JPushVCWebVIewController *vc = [[BMSQ_JPushVCWebVIewController alloc]init];
    vc.myTitle = @"扫码支付结果 ";
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    
    [[[tabBarController viewControllers] objectAtIndex:0] presentViewController:vc animated:YES completion:^{
        
    }];
    
}

-(void)initRootViewController{
    
    
    self.window = [[UIWindow alloc]initWithFrame:[[UIScreen mainScreen]bounds]];
    self.window.backgroundColor = [UIColor whiteColor];
    
    RootViewController *vc = [[RootViewController alloc]init];
    __weak typeof(self) weakSelf = self;
    vc.loadingViewControllerFinish = ^(void){
        [weakSelf initTabbarViewController];
    };
    
    self.window.rootViewController = vc;
    [self.window makeKeyAndVisible];
 
    
}

#pragma mark --下载Tabbar图片
-(void)downTabbarImage{
    NSMutableArray *norimageSurl = [[NSMutableArray alloc]init]; //正常图片
    NSMutableArray *seleimageSurl = [[NSMutableArray alloc]init]; //选中图片
    
    for (NSDictionary *dic in self.tabbarS) {
        [norimageSurl addObject:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"notFocusedUrl"]]];
        [seleimageSurl addObject:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"focusedUrl"]]];
        
    }
    
    SDWebImageManager *imageManager = [[SDWebImageManager alloc]init];
    __weak typeof(self) weakSelf = self;
    for (NSString *str in norimageSurl) {
        [imageManager downloadImageWithURL:[NSURL URLWithString:str] options:SDWebImageTransformAnimatedImage progress:^(NSInteger receivedSize, NSInteger expectedSize) {
            
        } completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, BOOL finished, NSURL *imageURL) {
            if(image !=nil){
                NSString *urlImage = [imageURL absoluteString];

                NSString *urlStr0 = [norimageSurl objectAtIndex:0];
                NSString *urlStr1 = [norimageSurl objectAtIndex:1];
                NSString *urlStr2 = [norimageSurl objectAtIndex:2];
                NSString *urlStr3 = [norimageSurl objectAtIndex:3];

                if ([urlStr0 isEqualToString:urlImage]) {

//                    [weakSelf.norTabImage insertObject:image atIndex:0];
                    self.tabbar1.image = image;

                }else if ([urlStr1 isEqualToString:urlImage]){
//                    [weakSelf.norTabImage insertObject:image atIndex:1];
                    self.tabbar2.image = image;


                }else if ([urlStr2 isEqualToString:urlImage]){
//                    [weakSelf.norTabImage insertObject:image atIndex:2];
                    self.tabbar3.image = image;


                }else if ([urlStr3 isEqualToString:urlImage]){
//                    [weakSelf.norTabImage insertObject:image atIndex:3];
                    self.tabbar4.image = image;

                }
                
//                if (weakSelf.norTabImage.count ==4) {
//                    [weakSelf setTabBarnorImage];
//                }
                
                
//                self.tabbar1.image = [self.norTabImage objectAtIndex:0];
//                self.tabbar2.image = [self.norTabImage objectAtIndex:1];
//                self.tabbar3.image = [self.norTabImage objectAtIndex:2];
//                self.tabbar4.image = [self.norTabImage objectAtIndex:3];
            }
         
            
        }];
        
        
        
    }
    
    for (NSString *str in seleimageSurl) {
        [imageManager downloadImageWithURL:[NSURL URLWithString:str] options:SDWebImageTransformAnimatedImage progress:^(NSInteger receivedSize, NSInteger expectedSize) {
            
        } completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, BOOL finished, NSURL *imageURL) {
            if(image != nil){
                
                
                NSString *urlImage = [imageURL absoluteString];
                
                NSString *urlStr0 = [seleimageSurl objectAtIndex:0];
                NSString *urlStr1 = [seleimageSurl objectAtIndex:1];
                NSString *urlStr2 = [seleimageSurl objectAtIndex:2];
                NSString *urlStr3 = [seleimageSurl objectAtIndex:3];
                
                if ([urlStr0 isEqualToString:urlImage]) {
                    
                    //                    [weakSelf.norTabImage insertObject:image atIndex:0];
                    self.tabbar1.selectedImage = image;
                    
                }else if ([urlStr1 isEqualToString:urlImage]){
                    //                    [weakSelf.norTabImage insertObject:image atIndex:1];
                    self.tabbar2.selectedImage = image;
                    
                    
                }else if ([urlStr2 isEqualToString:urlImage]){
                    //                    [weakSelf.norTabImage insertObject:image atIndex:2];
                    self.tabbar3.selectedImage = image;
                    
                    
                }else if ([urlStr3 isEqualToString:urlImage]){
                    //                    [weakSelf.norTabImage insertObject:image atIndex:3];
                    self.tabbar4.selectedImage = image;
                    
                }
                

                
//                [weakSelf.seleTabImage addObject:image];
//                if (weakSelf.seleTabImage.count ==4) {
//                    [weakSelf setTabBarSeleImage];
//                }
            }
        
        }];
        
    }
    
   

}
-(void)setTabBarnorImage{
    self.tabbar1.image = [self.norTabImage objectAtIndex:0];
    self.tabbar2.image = [self.norTabImage objectAtIndex:1];
    self.tabbar3.image = [self.norTabImage objectAtIndex:2];
    self.tabbar4.image = [self.norTabImage objectAtIndex:3];

    
}
-(void)setTabBarSeleImage{
    self.tabbar1.selectedImage = [self.seleTabImage objectAtIndex:0];
    self.tabbar2.selectedImage = [self.seleTabImage objectAtIndex:1];
    self.tabbar3.selectedImage = [self.seleTabImage objectAtIndex:2];
    self.tabbar4.selectedImage = [self.seleTabImage objectAtIndex:3];

    
}
-(void)initTabbarViewController{
    
   
    NSMutableArray *titleArray = [[NSMutableArray alloc]init]; //选中图片
    for (NSDictionary *dic in self.tabbarS) {
        [titleArray addObject:[dic objectForKey:@"title"]];
        
    }
    self.tabBarController = [[UITabBarController alloc]init];
    self.tabBarController.delegate = self;
    self.tabBarController.tabBar.tintColor = UICOLOR(195, 10, 24, 1);
    self.tabBarController.tabBar.translucent = NO;
    
    BMSQ_HomeController *homeVC = [[BMSQ_HomeController alloc]init];
    homeVC.m_dataSourceCity = self.openCityS;
    UINavigationController *nc1 = [[UINavigationController alloc]initWithRootViewController:homeVC];
    UINavigationController *nc2 = [[UINavigationController alloc]initWithRootViewController:[[BMSQ_BenefitCardController alloc]init]];
    UINavigationController *nc3 = [[UINavigationController alloc]initWithRootViewController:[[BMSQ_CouponController alloc]init]];
    UINavigationController *nc4 = [[UINavigationController alloc]initWithRootViewController:[[BMSQ_MyBusinessController alloc]init]];
    NSArray *VCs = @[nc1,nc2,nc3,nc4];

    self.tabbar1 = [[UITabBarItem alloc]initWithTitle:titleArray.count>0 ?[titleArray objectAtIndex:0]:@"首页" image:[UIImage imageNamed:@"tabbar_1"] selectedImage:[UIImage imageNamed:@"tabbar_1H"]];
    nc1.tabBarItem = self.tabbar1;
    
    self.tabbar2 = [[UITabBarItem alloc]initWithTitle:titleArray.count>1 ?[titleArray objectAtIndex:1]:@"圈广场" image:[UIImage imageNamed:@"tabbar_2"] selectedImage:[UIImage imageNamed:@"tabbar_2H"]];
    nc2.tabBarItem = self.tabbar2;
    
    self.tabbar3 = [[UITabBarItem alloc]initWithTitle:titleArray.count>2 ?[titleArray objectAtIndex:2]:@"优惠券" image:[UIImage imageNamed:@"tabbar_3"] selectedImage:[UIImage imageNamed:@"tabbar_3H"]];
    nc3.tabBarItem = self.tabbar3;
    
    self.tabbar4 = [[UITabBarItem alloc]initWithTitle:titleArray.count>3 ?[titleArray objectAtIndex:3]:@"我的" image:[UIImage imageNamed:@"tabbar_4"] selectedImage:[UIImage imageNamed:@"tabbar_4H"]];
    nc4.tabBarItem = self.tabbar4;
    
    
    self.tabBarController.viewControllers = VCs;
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"] forKey:@"currenVersion"];
    [userDefault synchronize];
    
    NSString *currentVersion = [userDefault objectForKey:@"currenVersion"];
    NSString *version = [userDefault objectForKey:@"Version"];
    
    if ([currentVersion isEqualToString:version]) {
        
        self.window.rootViewController = self.tabBarController;
        [self.window makeKeyAndVisible];
        
        
        [userDefault setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"] forKey:@"currenVersion"];
        [userDefault setObject:[[[NSBundle mainBundle] infoDictionary]objectForKey:@"CFBundleShortVersionString"] forKey:@"Version"];
        [userDefault synchronize];
        
        
    }else{
        actionPerform* ctroller = [[actionPerform alloc]init];
        ctroller.loadingViewControllerFinish  = ^(void){
            self.window.rootViewController = self.tabBarController;
            
        };
        [userDefault setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"] forKey:@"currenVersion"];
        [userDefault setObject:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"]forKey:@"Version"];
        [userDefault synchronize];
        
        self.window.rootViewController = ctroller;
        [self.window makeKeyAndVisible];
        
    }
    
    [self downTabbarImage];

}

- (void)initializePlat
{
    /**
     连接新浪微博开放平台应用以使用相关功能，此应用需要引用SinaWeiboConnection.framework
     http://open.weibo.com上注册新浪微博开放平台应用，并将相关信息填写到以下字段
     **/
    //    [ShareSDK connectSinaWeiboWithAppKey:@"568898243"
    //                               appSecret:@"38a4f8204cc784f81f9f0daaf31e02e3"
    //                             redirectUri:@"http://www.sharesdk.cn"
    //                             weiboSDKCls:[WeiboSDK class]];
    //    /**
    //     连接腾讯微博开放平台应用以使用相关功能，此应用需要引用TencentWeiboConnection.framework
    //     http://dev.t.qq.com上注册腾讯微博开放平台应用，并将相关信息填写到以下字段
    //     **/
    //    [ShareSDK connectTencentWeiboWithAppKey:@"801307650"
    //                                  appSecret:@"ae36f4ee3946e1cbb98d6965b0b2ff5c"
    //                                redirectUri:@"http://www.sharesdk.cn"];
    
    
    /**
     连接QQ空间应用以使用相关功能，此应用需要引用QZoneConnection.framework
     http://connect.qq.com/intro/login/上申请加入QQ登录，并将相关信息填写到以下字段
     
     如果需要实现SSO，需要导入TencentOpenAPI.framework,并引入QQApiInterface.h和TencentOAuth.h，将QQApiInterface和TencentOAuth的类型传入接口
     **/
    [ShareSDK connectQZoneWithAppKey:@"1104860582"
                           appSecret:@"3UGJ5uQvWkoQmXdK"
                   qqApiInterfaceCls:[QQApiInterface class]
                     tencentOAuthCls:[TencentOAuth class]];
    
    /**
     连接微信应用以使用相关功能，此应用需要引用WeChatConnection.framework和微信官方SDK
     http://open.weixin.qq.com上注册应用，并将相关信息填写以下字段
     **/
    //    [ShareSDK connectWeChatWithAppId:@"wx4868b35061f87885" wechatCls:[WXApi class]];
    [ShareSDK connectWeChatWithAppId:@"wx156ca5fcb9ebc520"
                           appSecret:@"253f9bf5057804c529034a918ff6b98c"
                           wechatCls:[WXApi class]];
    /**
     连接QQ应用以使用相关功能，此应用需要引用QQConnection.framework和QQApi.framework库
     http://mobile.qq.com/api/上注册应用，并将相关信息填写到以下字段
     **/
    //旧版中申请的AppId（如：QQxxxxxx类型），可以通过下面方法进行初始化
    //    [ShareSDK connectQQWithAppId:@"QQ075BCD15" qqApiCls:[QQApi class]];
    
    [ShareSDK connectQQWithQZoneAppKey:@"1104860582"
                     qqApiInterfaceCls:[QQApiInterface class]
                       tencentOAuthCls:[TencentOAuth class]];
    
}

/**
 *	@brief	托管模式下的初始化平台
 */
- (void)initializePlatForTrusteeship
{
    //导入QQ互联和QQ好友分享需要的外部库类型，如果不需要QQ空间SSO和QQ好友分享可以不调用此方法
    [ShareSDK importQQClass:[QQApiInterface class] tencentOAuthCls:[TencentOAuth class]];
    
    //导入腾讯微博需要的外部库类型，如果不需要腾讯微博SSO可以不调用此方法
    //[ShareSDK importTencentWeiboClass:[WeiboApi class]];
    
    //导入微信需要的外部库类型，如果不需要微信分享可以不调用此方法
    [ShareSDK importWeChatClass:[WXApi class]];
    
}

#pragma mark - 如果使用SSO（可以简单理解成客户端授权），以下方法是必要的
- (BOOL)application:(UIApplication *)application
      handleOpenURL:(NSURL *)url
{
    return [ShareSDK handleOpenURL:url
                        wxDelegate:self];
}




//iOS 4.2+
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation
{
    return [ShareSDK handleOpenURL:url
                 sourceApplication:sourceApplication
                        annotation:annotation
                        wxDelegate:self];
}

- (void)applicationWillResignActive:(UIApplication *)application {
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    NSLog(@"后台后台后台");

}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    
    NSLog(@"前台前台前台");
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;

    [self aotoLogin];


}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark 数据请求部分

- (void)onGetNetworkState:(int)iError
{
    if (0 == iError) {
        NSLog(@"联网成功");
    }
    else{
        NSLog(@"onGetNetworkState %d",iError);
    }
    
}

- (void)onGetPermissionState:(int)iError
{
    if (0 == iError) {
        NSLog(@"授权成功");
    }
    else {
        NSLog(@"onGetPermissionState %d",iError);
    }
}


#pragma mark UITabBarDelegate
- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
{
    
    //负责替换选择后的图片
    if (tabBarController.selectedIndex == 0)
    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_mainHome"];
    }
    else if (tabBarController.selectedIndex == 1)
    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_expert"];
        if (![gloabFunction isLogin])
        {
            [self getLogin];
            return;
        }
    }
    else if (tabBarController.selectedIndex == 2)
    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_mainHome"];
        if (![gloabFunction isLogin])
        {
            [self getLogin];
            return;
        }
    }
    else if (tabBarController.selectedIndex == 3)
    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_address"];
        if (![gloabFunction isLogin])
        {
            [self getLogin];
            return;
        }
    }
    else if (tabBarController.selectedIndex == 4)
    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_createCustomer"];
        if (![gloabFunction isLogin])
        {
            [self getLogin];
            return;
        }
    }
    
}

- (void)getLogin
{
    UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
    BMSQ_LoginViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Login"];
    BaseNavViewController *nav = [[BaseNavViewController alloc] initWithRootViewController:vc];
    
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    
    [[[tabBarController viewControllers] objectAtIndex:0] presentViewController:nav animated:YES completion:^{
        
    }];
}



#pragma mark --推送--
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
    
    [APService registerDeviceToken:deviceToken];
    if ([APService registrationID].length>0) {
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        [userDefault setObject:[APService registrationID] forKey:RegistratcionID];
        [userDefault synchronize];
    }
    
}


- (void)networkDidReceiveMessage:(NSNotification *)notification {
    NSDictionary * userInfo = [notification userInfo];
    NSString *content = [userInfo valueForKey:@"content"];
    NSDictionary *extras = [userInfo valueForKey:@"extras"];
    
    /* 多人登录推送的标识 */
    if([content isEqualToString:@"LOGIN"]){
        NSString *content = [extras objectForKey:@"content"];
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:content delegate:self cancelButtonTitle:@"退出登录" otherButtonTitles:@"重新登录", nil];
        alterView.tag = LOGINTAG;
        [alterView show];  //确定 登录  取消 取消
    }
    /*  修改活动推送的标识 */
    else if([content isEqualToString:@"ACT_UPDATE"])
    {
        NSLog(@"userInfo = %@",userInfo);
        
    }
    /* 使用优惠券推送的标识 */
    else if([content isEqualToString:@"PAY_COUPON_USER"])
    {
        NSLog(@"userInfo = %@",userInfo);
        NSString *content = [extras objectForKey:@"content"];
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:content delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];  //确定 登录  取消 取消
        
    }
    /* 退款期推送的标识 */
    else if([content isEqualToString:@"PAY_COUPON_REFUND"])
    {
        NSLog(@"userInfo = %@",userInfo);
        
    }
    /* 优惠券即将过期推送的标识 */
    else if([content isEqualToString:@"COUPON_TO_BE_EXPIRED"])
    {
        NSLog(@"userInfo = %@",userInfo);
        NSString *content = [extras objectForKey:@"content"];
        UIAlertView *alterView = [[UIAlertView alloc]initWithTitle:@"提示" message:content delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
        [alterView show];  //确定 登录  取消 取消
        
        
    }
    /* 扫码成功 付款 */
    else if([content isEqualToString:@"CONSUME"])
    {
        NSLog(@"userInfo = %@",userInfo);
        NSDictionary *extras = [userInfo objectForKey:@"extras"];
        BMSQ_JPushVCWebVIewController *vc = [[BMSQ_JPushVCWebVIewController alloc]init];
        vc.myTitle = @"扫码支付结果 ";
        vc.urlStr =[NSString stringWithFormat:@"%@%@",H5_URL,[extras objectForKey:@"webUrl"]];
        UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
        
        [[[tabBarController viewControllers] objectAtIndex:0] presentViewController:vc animated:YES completion:^{
            
        }];
        
    }
    
//    [self.notiView addNoti:userInfo];
    
}
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [APService handleRemoteNotification:userInfo];
    NSLog(@"收到通知:%@", [self logDic:userInfo]);
    
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    
    [APService handleRemoteNotification:userInfo];
    NSLog(@"收到通知:%@", [self logDic:userInfo]);
}

- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
    [APService showLocalNotificationAtFront:notification identifierKey:nil];
    
    
    
}
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSLog(@"did Fail To Register For Remote Notifications With Error: %@", error);
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (alertView.tag == LOGINTAG) {
        if (buttonIndex == 0) { //退出登录
            [self loginOut];
        }else if(buttonIndex == 1){ //登录
            [self aotoLogin];
            
        }
    }
 
    
}

- (NSString *)logDic:(NSDictionary *)dic {
    if (![dic count]) {
        return nil;
    }
    NSString *tempStr1 =
    [[dic description] stringByReplacingOccurrencesOfString:@"\\u"
                                                 withString:@"\\U"];
    NSString *tempStr2 =
    [tempStr1 stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""];
    NSString *tempStr3 =
    [[@"\"" stringByAppendingString:tempStr2] stringByAppendingString:@"\""];
    NSData *tempData = [tempStr3 dataUsingEncoding:NSUTF8StringEncoding];
    NSString *str =
    [NSPropertyListSerialization propertyListFromData:tempData
                                     mutabilityOption:NSPropertyListImmutable
                                               format:NULL
                                     errorDescription:NULL];
    return str;
}
@end
