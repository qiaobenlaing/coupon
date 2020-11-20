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
+ (double)intervalSinceNow: (NSString *) theDate;
+ (NSString *)timeFormatted:(int)totalSeconds;
+(NSString*)getNowDateAndTime;

//获取openudid
+(NSString*)getOpenUDID;

+(NSString*)getUserAuthData;

//获取安全签名sign
+(NSString*)getSign:(NSString*)methodName strParams:(NSString*)params;

//判断是否登录成功,只是做本地数据判断
+(BOOL)isLogin;
+(BOOL)isApprove; //是否认证

+ (BOOL)checkTel:(NSString *)str;

+(NSString*)getStaffCode;
+(NSString*)getShopCode;
+(NSString*)getUserToken;
+(NSString*)getUserCode;
//转换null字符串为空字符串
+(NSString*)changeNullToBlank:(NSString*)strSource;
@end
