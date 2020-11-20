//
//  AppDelegate+ScreenLayout.m
//  BMSQS
//
//  Created by 兆祥 孔 on 15/7/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <objc/runtime.h>
#import "AppDelegate+ScreenLayout.h"

static void *sizeScaleXKey = &sizeScaleXKey;
static void *sizeScaleYKey = &sizeScaleYKey;

@implementation AppDelegate (ScreenLayout)

- (void)AppDelegateScreenLayout{
    
    // 屏幕适配
    float screenHeight = [[UIScreen mainScreen] bounds].size.height;
    float screenWidth  = [[UIScreen mainScreen] bounds].size.width ;
    
    NSNumber *scaleX = objc_getAssociatedObject(self, sizeScaleXKey);
    NSNumber *scaleY = objc_getAssociatedObject(self, sizeScaleYKey);
    
    if (scaleX == nil) {
        scaleX = [NSNumber numberWithFloat:1.0];
        if(screenHeight != 667){
            scaleX = [NSNumber numberWithFloat:screenWidth/375.0];
        }
        objc_setAssociatedObject(self, sizeScaleXKey, scaleX, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    
    if (scaleY == nil) {
        scaleY = [NSNumber numberWithFloat:1.0];
        if (screenHeight != 667) {
            scaleY = [NSNumber numberWithFloat:screenHeight/667.0];
        }
        objc_setAssociatedObject(self, sizeScaleYKey, scaleY, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    }
    
}


- (SLSizeScale)autoSizeScale{
    
    NSNumber *scaleX = objc_getAssociatedObject(self, sizeScaleXKey);
    NSNumber *scaleY = objc_getAssociatedObject(self, sizeScaleYKey);
    
    SLSizeScale scale;
    scale.x = [scaleX floatValue];
    scale.y = [scaleY floatValue];
    
    return scale;
    
}


@end
