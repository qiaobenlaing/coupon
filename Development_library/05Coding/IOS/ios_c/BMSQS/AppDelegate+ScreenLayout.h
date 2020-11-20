//
//  AppDelegate+ScreenLayout.h
//  BMSQS
//
//  Created by 兆祥 孔 on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

// 此分类要配合 SLCGRectHelper 类 的函数 SLRectMake(CGFloat x, CGFloat y, CGFloat width, CGFloat height) 进行使用
// 以iphone6屏幕为适配基础

struct SLSizeScale {
    CGFloat x;
    CGFloat y;
};
typedef struct SLSizeScale SLSizeScale;

#import "AppDelegate.h"

@interface AppDelegate (ScreenLayout)

// App首次启动时计算屏幕适配比例
- (void)AppDelegateScreenLayout;

// 屏幕适配的比例
- (SLSizeScale)autoSizeScale;

@end
