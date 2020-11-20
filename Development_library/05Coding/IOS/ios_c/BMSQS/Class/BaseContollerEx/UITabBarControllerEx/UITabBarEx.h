//
//  UITabBarEx.h
//  51ZhangDan
//
//  Created by cq on 12-12-26.
//  Copyright (c) 2012年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol LeveyTabBarDelegate;

@interface UITabBarEx : UIView


@property (nonatomic, retain) UIImageView *backgroundView;
@property (nonatomic, assign) id<LeveyTabBarDelegate> delegate;
@property (nonatomic, retain) NSMutableArray *buttons;


- (id)initWithFrame:(CGRect)frame buttonImages:(NSArray *)imageArray;
- (void)selectTabAtIndex:(NSInteger)index;
- (void)removeTabAtIndex:(NSInteger)index;
- (void)insertTabWithImageDic:(NSDictionary *)dict atIndex:(NSUInteger)index;
- (void)setBackgroundImage:(UIImage *)img;
- (void)setBackgroundColor:(UIColor *)backgroundColor;
@end


@protocol LeveyTabBarDelegate<NSObject>
@optional
- (void)tabBar:(UITabBarEx *)tabBar didSelectIndex:(NSInteger)index; 
@end
