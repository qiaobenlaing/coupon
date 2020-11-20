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


@interface AppDelegate : UIResponder <UIApplicationDelegate,UITabBarControllerDelegate>
{
    AGViewDelegate * _viewDelegate;
}
@property (nonatomic,readonly) AGViewDelegate *viewDelegate;
@property (strong, nonatomic) UIWindow *window;
//@property (strong, nonatomic) NSMutableArray* arrayCity; //城市信息

@end

