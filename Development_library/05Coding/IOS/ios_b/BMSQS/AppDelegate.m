//
//  AppDelegate.m
//  RRCShop
//
//  Created by djx on 14-12-2.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "AppDelegate.h"
#import "BMSQ_HomeController.h"
#import "BMSQ_BenefitCardController.h"
#import "BMSQ_MyBusinessController.h"
#import "ChineseToPinyin.h"
#import "UMessage.h"
#import "MobClick.h"
#import "AppDelegate+ScreenLayout.h"
#import "BaseNavViewController.h"

//ShareSDK必要头文件
#import <ShareSDK/ShareSDK.h>

//腾讯开放平台（对应QQ和QQ空间）SDK头文件
#import <TencentOpenAPI/TencentOAuth.h>
#import <TencentOpenAPI/QQApiInterface.h>

//微信SDK头文件
#import "WXApi.h"

//新浪微博SDK头文件

#import <QZoneConnection/ISSQZoneApp.h>

#import "APService.h"


#define UMSYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)

#define _IPHONE80_ 80000

//BMKMapManager* _mapManager;

@interface AppDelegate ()
{
//    BMKMapView* _mapView;
//    BMKLocationService* _locService;
//    BMKGeoCodeSearch* _geocodesearch;
//    UIImageView* imageView_TabBar;
}

@end

@implementation AppDelegate

@synthesize currentCityCode;
@synthesize currentCityName;
@synthesize latitude;
@synthesize longitude;
@synthesize arrayCity;
@synthesize currentAddress;

-(void)addAPService:(NSDictionary *)launchOptions{
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
    
    
    //极光

    
//    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
//
//    [userDefault setObject:[APService registrationID] forKey:RegistratcionID];
//    NSLog(@"get RegistrationID:%@",[APService registrationID]);
//    [userDefault synchronize];
}




- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    [self addAPService:launchOptions];
    
    [self AppDelegateScreenLayout];
    
    [ShareSDK registerApp:@"86e43e546dfa"];
    
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleLightContent;
//    _mapManager = [[BMKMapManager alloc]init];
//    BOOL ret = [_mapManager start:APP_MAP_KEY generalDelegate:self];
//    if (!ret) {
//        NSLog(@"manager start failed!");
//    }
//    
//    _mapView = [[BMKMapView alloc]init];
//    _mapView.delegate = self;
//    _locService = [[BMKLocationService alloc]init];
//    _locService.delegate = self;
//    [_locService startUserLocationService];
    
//    _geocodesearch = [[BMKGeoCodeSearch alloc]init];
//    _geocodesearch.delegate = self;
    
    arrayCity = [[NSMutableArray alloc]init];
    
    UITabBarController *tabBarController = (UITabBarController *)self.window.rootViewController;
    tabBarController.delegate = self;
    tabBarController.tabBar.tintColor = UICOLOR(195, 10, 24, 1);
    tabBarController.tabBar.translucent = NO;

    if ([[tabBarController viewControllers] count] > 0)
    {
        //首页
        UIImage *backImg = [UIImage imageNamed:@"tabbar_1H"];
        backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];

        UITabBarItem *item1= [[UITabBarItem alloc] initWithTitle:@"首页" image:[UIImage imageNamed:@"tabbar_1"] selectedImage:backImg];
        [item1 setTitlePositionAdjustment:UIOffsetMake(0, -3)];
        UIViewController* vc1 = [tabBarController viewControllers][0];
        vc1.tabBarItem = item1;
        
        backImg = [UIImage imageNamed:@"tabbar_2H"];
        backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        UITabBarItem *item2= [[UITabBarItem alloc] initWithTitle:@"惠员卡" image:[UIImage imageNamed:@"tabbar_2"] selectedImage:backImg];
        [item2 setTitlePositionAdjustment:UIOffsetMake(0, -3)];
        UIViewController* vc2 = [tabBarController viewControllers][1];
        vc2.tabBarItem = item2;
        
        backImg = [UIImage imageNamed:@"tabbar_3H"];
        backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        UITabBarItem *item3= [[UITabBarItem alloc] initWithTitle:@"优惠券" image:[UIImage imageNamed:@"tabbar_3"] selectedImage:backImg];
        [item3 setTitlePositionAdjustment:UIOffsetMake(0, -3)];
        UIViewController* vc3 = [tabBarController viewControllers][2];
        vc3.tabBarItem = item3;
        
        backImg = [UIImage imageNamed:@"tabbar_4H"];
        backImg = [backImg imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        UITabBarItem *item4= [[UITabBarItem alloc] initWithTitle:@"我的" image:[UIImage imageNamed:@"tabbar_4"] selectedImage:backImg];
        [item4 setTitlePositionAdjustment:UIOffsetMake(0, -3)];
        UIViewController* vc4 = [tabBarController viewControllers][3];
        vc4.tabBarItem = item4;
        

    }

    
    
    currentCityName = [[NSString alloc]initWithFormat:@"%@",@"建水"];
    currentAddress = [[NSString alloc]initWithFormat:@"%@",@"浙江省杭州市西湖区文二路"];
    latitude = [[NSString alloc]initWithFormat:@"%@",@"30.280102"];//[NSString stringWithFormat:@"%f",userLocation.location.coordinate.latitude];
    longitude = [[NSString alloc]initWithFormat:@"%@",@"120.076782"];//[NSString stringWithFormat:@"%f",userLocation.location.coordinate.longitude];
    // Override point for customization after application launch.
    
    

    [self.window makeKeyAndVisible];
    
    
    [self initializePlat];
    
    
//    [self setUMShare:launchOptions];
    
    NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
    [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kJPFNetworkDidReceiveMessageNotification object:nil];
    
    
    return YES;
}

#pragma mark ------ 极光推送自定义推送 ------
- (void)networkDidReceiveMessage:(NSNotification *)notification {
    
    NSDictionary * userInfo = [notification userInfo];
    NSString *content = [userInfo valueForKey:@"content"];
    NSDictionary *extras = [userInfo valueForKey:@"extras"];
    NSString *customizeField1 = [extras valueForKey:@"customizeField1"]; //自定义参数，key是自己定义的
    
    // gathering
    if ([userInfo[@"content"] isEqualToString:@"CONSUME"]) {
        
        [[NSNotificationCenter defaultCenter] postNotificationName:@"gatheringList" object: userInfo];
        
    }
    
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
    [ShareSDK connectQZoneWithAppKey:@"1104882980"
                           appSecret:@"a13YiPgRQkydB7Xi"
                   qqApiInterfaceCls:[QQApiInterface class]
                     tencentOAuthCls:[TencentOAuth class]];
    
    /**
     连接微信应用以使用相关功能，此应用需要引用WeChatConnection.framework和微信官方SDK
     http://open.weixin.qq.com上注册应用，并将相关信息填写以下字段
     **/
//        [ShareSDK connectWeChatWithAppId:@"wx4868b35061f87885" wechatCls:[WXApi class]];
    [ShareSDK connectWeChatWithAppId:@"wxceca5dbb693343f0"
                           appSecret:@"d4624c36b6795d1d99dcf0547af5443d"
                           wechatCls:[WXApi class]];
    /**
     连接QQ应用以使用相关功能，此应用需要引用QQConnection.framework和QQApi.framework库
     http://mobile.qq.com/api/上注册应用，并将相关信息填写到以下字段
     **/
    //旧版中申请的AppId（如：QQxxxxxx类型），可以通过下面方法进行初始化
    //    [ShareSDK connectQQWithAppId:@"QQ075BCD15" qqApiCls:[QQApi class]];
    
    [ShareSDK connectQQWithQZoneAppKey:@"1104882980"
                     qqApiInterfaceCls:[QQApiInterface class]
                       tencentOAuthCls:[TencentOAuth class]];
    
    
    
}

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


- (void)setUMShare:(NSDictionary *)launchOptions
{
    [UMessage startWithAppkey:APP_YM_KEY launchOptions:launchOptions];
    
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= _IPHONE80_
    if(UMSYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(@"8.0"))
    {
        //register remoteNotification types
        UIMutableUserNotificationAction *action1 = [[UIMutableUserNotificationAction alloc] init];
        action1.identifier = @"action1_identifier";
        action1.title=@"Accept";
        action1.activationMode = UIUserNotificationActivationModeForeground;//当点击的时候启动程序
        
        UIMutableUserNotificationAction *action2 = [[UIMutableUserNotificationAction alloc] init];  //第二按钮
        action2.identifier = @"action2_identifier";
        action2.title=@"Reject";
        action2.activationMode = UIUserNotificationActivationModeBackground;//当点击的时候不启动程序，在后台处理
        action2.authenticationRequired = YES;//需要解锁才能处理，如果action.activationMode = UIUserNotificationActivationModeForeground;则这个属性被忽略；
        action2.destructive = YES;
        
        UIMutableUserNotificationCategory *categorys = [[UIMutableUserNotificationCategory alloc] init];
        categorys.identifier = @"category1";//这组动作的唯一标示
        [categorys setActions:@[action1,action2] forContext:(UIUserNotificationActionContextDefault)];
        
        UIUserNotificationSettings *userSettings = [UIUserNotificationSettings settingsForTypes:UIUserNotificationTypeBadge|UIUserNotificationTypeSound|UIUserNotificationTypeAlert
                                                                                     categories:[NSSet setWithObject:categorys]];
        [UMessage registerRemoteNotificationAndUserNotificationSettings:userSettings];
        
    } else{
        //register remoteNotification types
        [UMessage registerForRemoteNotificationTypes:UIRemoteNotificationTypeBadge
         |UIRemoteNotificationTypeSound
         |UIRemoteNotificationTypeAlert];
    }
#else
    
    //register remoteNotification types
    [UMessage registerForRemoteNotificationTypes:UIRemoteNotificationTypeBadge
     |UIRemoteNotificationTypeSound
     |UIRemoteNotificationTypeAlert];
    
#endif
    
    //for log
    [UMessage setLogEnabled:YES];
    
//    //设置友盟社会化组件appkey
//    [UMSocialData setAppKey:APP_YM_KEY];
//    
//    //打开调试log的开关
//    [UMSocialData openLog:YES];
//    
//    //如果你要支持不同的屏幕方向，需要这样设置，否则在iPhone只支持一个竖屏方向
//    //[UMSocialConfig setSupportedInterfaceOrientations:UIInterfaceOrientationMaskAll];
//    
//    //设置微信AppId，设置分享url，默认使用友盟的网址
////    [UMSocialWechatHandler setWXAppId:APP_WX_ID appSecret:APP_WX_SECRET  url:@"http://www.umeng.com/social"];
//    
//    //打开新浪微博的SSO开关
//    [UMSocialSinaHandler openSSOWithRedirectURL:@"http://sns.whalecloud.com/sina2/callback"];
//
    
    //    //设置分享到QQ空间的应用Id，和分享url 链接
//    [UMSocialQQHandler setQQWithAppId:APP_QQ_KEY appKey:@"c7394704798a158208a74ab60104f0ba" url:@"http://www.umeng.com/social"];
    //    //设置支持没有客户端情况下使用SSO授权
//    [UMSocialQQHandler setSupportWebView:YES];
    
    //使用友盟统计
    [MobClick startWithAppkey:APP_YM_KEY];
    
    
    
    
}

#pragma mark ----- 禁止调用第三方键盘
- (BOOL)application:(UIApplication *)application shouldAllowExtensionPointIdentifier:(NSString *)extensionPointIdentifier
{
    return NO;
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

/**
 *在地图View将要启动定位时，会调用此函数
 *@param mapView 地图View
 */
- (void)willStartLocatingUser
{
    NSLog(@"start locate");
}

/**
 *用户方向更新后，会调用此函数
 *@param userLocation 新的用户位置
 */
//- (void)didUpdateUserHeading:(BMKUserLocation *)userLocation
//{
//    [_mapView updateLocationData:userLocation];
////    NSLog(@"heading is %@",userLocation.heading);
//}
//
///**
// *用户位置更新后，会调用此函数
// *@param userLocation 新的用户位置
// */
//- (void)didUpdateUserLocation:(BMKUserLocation *)userLocation
//{
//    //    NSLog(@"didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);
//    [_mapView updateLocationData:userLocation];
//    
//    CLLocationCoordinate2D pt = (CLLocationCoordinate2D){userLocation.location.coordinate.latitude, userLocation.location.coordinate.longitude};
//
//    latitude = [[NSString alloc]initWithFormat:@"%@",@"30.280102"];//[NSString stringWithFormat:@"%f",userLocation.location.coordinate.latitude];
//    longitude = [[NSString alloc]initWithFormat:@"%@",@"120.076782"];//[NSString stringWithFormat:@"%f",userLocation.location.coordinate.longitude];
//    BMKReverseGeoCodeOption *reverseGeocodeSearchOption = [[BMKReverseGeoCodeOption alloc]init];
//    reverseGeocodeSearchOption.reverseGeoPoint = pt;
//    [_geocodesearch reverseGeoCode:reverseGeocodeSearchOption];
//    
//}

/**
 *在地图View停止定位后，会调用此函数
 *@param mapView 地图View
 */
- (void)didStopLocatingUser
{
    NSLog(@"stop locate");
}

/**
 *定位失败后，会调用此函数
 *@param mapView 地图View
 *@param error 错误号，参考CLError.h中定义的错误号
 */
- (void)didFailToLocateUserWithError:(NSError *)error
{
    NSLog(@"location error");
}


//-(void) onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeResult *)result errorCode:(BMKSearchErrorCode)error
//{
//    BMKAddressComponent* addressDetail = result.addressDetail;
//    NSString* tmpCity = [NSString stringWithFormat:@"%@",addressDetail.city];
//    if (tmpCity == nil || tmpCity.length <= 1)
//    {
//        return;
//    }
//    currentCityName = [NSString stringWithFormat:@"%@",[tmpCity substringToIndex:tmpCity.length - 1]];
//    currentAddress = [NSString stringWithFormat:@"%@",result.address];
//}


#pragma mark --推送-------
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{

    [APService registerDeviceToken:deviceToken];
    
    if ([APService registrationID].length>0) {
        NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
        [userDefault setObject:[APService registrationID] forKey:RegistratcionID];

        [userDefault synchronize];
    }
    
}




- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    [APService handleRemoteNotification:userInfo];
    NSLog(@"收到通知:%@", [self logDic:userInfo]);
    
    
    
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    
    [APService handleRemoteNotification:userInfo];
    
     NSLog(@"收到通知:%@", [self logDic:userInfo]);
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"theValueOf" object: userInfo];

   
    
}



- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
    [APService showLocalNotificationAtFront:notification identifierKey:nil];
}
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSLog(@"did Fail To Register For Remote Notifications With Error: %@", error);
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

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark UITabBarDelegate
//- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
//{
//
//    //负责替换选择后的图片
//    if (tabBarController.selectedIndex == 0)
//    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_mainHome"];
//    }
//    else if (tabBarController.selectedIndex == 1)
//    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_expert"];
//    }
//    else if (tabBarController.selectedIndex == 2)
//    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_mainHome"];
//    }
//    else if (tabBarController.selectedIndex == 3)
//    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_address"];
//    }
//    else if (tabBarController.selectedIndex == 4)
//    {
//        imageView_TabBar.image = [UIImage imageNamed:@"tb_createCustomer"];
//    }
//
//}


@end
