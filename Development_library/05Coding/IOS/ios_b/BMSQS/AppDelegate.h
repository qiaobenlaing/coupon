//
//  AppDelegate.h
//  RRCShop
//
//  Created by djx on 14-12-2.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "BMapKit.h"
//#import "BMKMapManager.h"
//#import "BMKGeocodeSearch.h"
#import "AGViewDelegate.h"
//,BMKGeneralDelegate,BMKMapViewDelegate,BMKLocationServiceDelegate,BMKGeoCodeSearchDelegate


@interface AppDelegate : UIResponder <UIApplicationDelegate,UITabBarControllerDelegate> {
    
    AGViewDelegate * _viewDelegate;
    
}

@property (nonatomic,readonly)AGViewDelegate *viewDelegate;

@property (strong, nonatomic) UIWindow *window;
@property (strong, nonatomic) NSString* currentCityCode; //当前城市编码
@property (strong, nonatomic) NSString* currentCityName; //当前城市
@property (strong, nonatomic) NSString* currentAddress;
@property (strong, nonatomic) NSString* latitude; //纬度
@property (strong, nonatomic) NSString* longitude; //经度
@property (strong, nonatomic) NSMutableArray* arrayCity; //城市信息


@end

