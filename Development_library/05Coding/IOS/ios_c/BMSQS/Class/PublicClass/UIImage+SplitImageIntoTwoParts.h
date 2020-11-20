//
//  UIImage+SplitImageIntoTwoParts.h
//  TapRepublic
//
//
//  Created by djx on 14-2-8.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (SplitImageIntoTwoParts)
+ (NSArray*)splitImageIntoTwoParts:(UIImage*)image;
//合并2张图片
+ (UIImage *)addImage:(UIImage *)image1 toImage:(UIImage *)image2 toImage2:(UIImage*)image3;
@end
