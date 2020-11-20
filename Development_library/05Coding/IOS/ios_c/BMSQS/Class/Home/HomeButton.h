//
//  HomeButton.h
//  BMSQC
//
//  Created by gh on 15/11/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "UIButtonEx.h"
#import "UIColor+Tools.h"

@interface HomeButton : NSObject

//首页活动模块中的按钮
+ (UIButtonEx *)createHomeActivityBtn:(CGRect)frame
                               object:(NSDictionary *)dictionary
                                  tag:(NSInteger)tag;

//垂直的活动按钮
+ (UIButtonEx *)createUprightActivityBtn:(CGRect)frame
                                  object:(NSDictionary *)dictionary
                                     tag:(NSInteger)tag;

//平行的活动按钮
+ (UIButtonEx *)createParallelActivityBtn:(CGRect)frame
                                   object:(NSDictionary *)dictionary
                                      tag:(NSInteger)tag;



@end
