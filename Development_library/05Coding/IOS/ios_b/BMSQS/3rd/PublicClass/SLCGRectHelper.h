//
//  RectHelper.h
//  BMSQS
//
//  Created by 兆祥 孔 on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "AppDelegate+ScreenLayout.h"

// 以iphone6屏幕为适配基础
CG_INLINE CGRect
SLRectMake(CGFloat x, CGFloat y, CGFloat width, CGFloat height)
{
    SLSizeScale scale = [AppDelegate autoSizeScale];
    CGRect rect;
    rect.origin.x = x *scale.x;
    rect.origin.y = y *scale.y;
    rect.size.width = width *scale.x;
    rect.size.height = height *scale.y;
    return rect;
}


