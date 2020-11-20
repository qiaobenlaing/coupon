//
//  gloabFunction.h
//  SDBooking
//
//  Created by djx on 14-3-3.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface gloabFunction : NSObject


//获取当前日期 yyyy-mm-dd格式
+(NSString*)getNowDate;
+(NSString*)getTimestamp;

+(NSString*)getNowDateAndTime;

//获取openudid
+(NSString*)getOpenUDID;

+(NSString*)getUserAuthData;

//获取安全签名sign
+(NSString*)getSign:(NSString*)methodName strParams:(NSString*)params;

//判断是否登录成功,只是做本地数据判断
+(BOOL)isLogin;
+(BOOL)isApprove; //是否认证

//计算某一个时间到当前的日期差
+ (double)intervalSinceNow: (NSString *) theDate;

+ (BOOL)checkTel:(NSString *)str;
//地址
+(NSString*)getStreet;
+(NSString*)getTel;
+(NSString*)getShopName;
+(NSString*)getStaffCode;
+(NSString*)getShopCode;
+(NSString*)getUserToken;
+(NSString *)getisCatering;


//转换null字符串为空字符串
+(NSString*)changeNullToBlank:(NSString*)strSource;

+(NSString*)getUserLvl;

+(UIView *)ggsetLineView:(CGRect)frame view:(UIView *)view;
@end
