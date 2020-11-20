//
//  UIImage+SplitImageIntoTwoParts.m
//  TapRepublic
//
//
//  Created by djx on 14-2-8.
//  Copyright (c) 2014年 djx. All rights reserved.
//
#define SAWTOOTH_COUNT 1
#define SAWTOOTH_WIDTH_FACTOR 1
#import "UIImage+SplitImageIntoTwoParts.h"

@implementation UIImage (SplitImageIntoTwoParts)

+(NSArray *)splitImageIntoTwoParts:(UIImage *)image
{    
    CGFloat scale = [[UIScreen mainScreen] scale];
    NSMutableArray *array = [NSMutableArray arrayWithCapacity:2];
    CGFloat width,height,widthgap,heightgap;
    int piceCount = SAWTOOTH_COUNT;
    width = image.size.width;
    height = image.size.height;
    widthgap = width/SAWTOOTH_WIDTH_FACTOR;
    heightgap = height/piceCount;
    //    CGRect rect = CGRectMake(0, 0, width, height);
    CGContextRef context;
    CGImageRef imageMasked;
    UIImage *leftImage,*rightImage;
    
    //part one
    UIGraphicsBeginImageContext(CGSizeMake(width*scale, height*scale));
    context = UIGraphicsGetCurrentContext();
    CGContextScaleCTM(context, scale, scale);
    CGContextMoveToPoint(context, 0, 0);
    CGContextAddLineToPoint(context, 0, height/2);
    CGContextAddLineToPoint(context, width, height/2);
    CGContextAddLineToPoint(context, width, 0);
    
    CGContextClosePath(context);
    CGContextClip(context);
    [image drawAtPoint:CGPointMake(0, 0)];
    imageMasked = CGBitmapContextCreateImage(context);
    leftImage = [UIImage imageWithCGImage:imageMasked scale:scale orientation:UIImageOrientationUp];
    [array addObject:leftImage];
    UIGraphicsEndImageContext();
    
    //part two
    UIGraphicsBeginImageContext(CGSizeMake(width*scale, height*scale));
    context = UIGraphicsGetCurrentContext();
    CGContextScaleCTM(context, scale, scale);
    CGContextMoveToPoint(context, 0, height/2);

    CGContextAddLineToPoint(context, 0, height);
    CGContextAddLineToPoint(context, width, height);
    CGContextAddLineToPoint(context, width, height/2);
    CGContextClosePath(context);
    CGContextClip(context);
    [image drawAtPoint:CGPointMake(0, 0)];
    imageMasked = CGBitmapContextCreateImage(context);
    rightImage = [UIImage imageWithCGImage:imageMasked scale:scale orientation:UIImageOrientationUp];
    [array addObject:rightImage];
    UIGraphicsEndImageContext();
    
    
    return array;
}

+ (UIImage *)addImage:(UIImage *)image1 toImage:(UIImage *)image2 toImage2:(UIImage*)image3
{
    UIGraphicsBeginImageContext(image1.size);
    
    // Draw image1
    [image1 drawInRect:CGRectMake(0, 0, image1.size.width, image1.size.height)];
    CGSize size = image1.size;
    // Draw image2
    [image2 drawInRect:CGRectMake(5, 5, image2.size.width, image2.size.height)];
    
    [image3 drawInRect:CGRectMake(0, 0, image3.size.width, image3.size.height)];
    CGSize size2 = image2.size;
    
    UIImage *resultingImage = UIGraphicsGetImageFromCurrentImageContext();
    
    CGSize size3 = resultingImage.size;
    
    UIGraphicsEndImageContext();
    
    return resultingImage;
}

@end
