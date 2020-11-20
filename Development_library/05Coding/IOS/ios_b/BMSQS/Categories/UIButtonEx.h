//
//  UIButton_SDZX.h
//  SDBooking
//
//  Created by icarddjx on 14-1-21.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIButtonEx : UIButton

@property(nonatomic,strong)id object; //存储一些特殊对象，用于回传或者获取一些数据

@property (nonatomic, assign)int page;

@end
