//
//  gloabFunction.m
//  SDBooking
//
//  Created by djx on 14-3-3.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "gloabFunction.h"
#import "OpenUDID.h"
#import "MD5.h"

@implementation gloabFunction

//获取当前日期 yyyy-mm-dd格式
+(NSString*)getNowDate
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设定时间格式,这里可以设置成自己需要的格式
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    //用[NSDate date]可以获取系统当前时间
    NSString *currentDateStr = [dateFormatter stringFromDate:[NSDate date]];
    
    return currentDateStr;
}

+(NSString*)getTimestamp
{
    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval a = [dat timeIntervalSince1970];  //  *1000 是精确到毫秒，不乘就是精确到秒
    return [NSString stringWithFormat:@"%.0f", a];
}

+(NSString*)getNowDateAndTime
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设定时间格式,这里可以设置成自己需要的格式
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    //用[NSDate date]可以获取系统当前时间
    NSString *currentDateStr = [dateFormatter stringFromDate:[NSDate date]];
    
    return currentDateStr;
}

//计算某一个时间到当前的日期差
+ (double)intervalSinceNow: (NSString *) theDate
{
    NSDateFormatter *date=[[NSDateFormatter alloc] init];
    NSTimeZone* timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [date setTimeZone:timeZone];
    
    [date setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *d=[date dateFromString:theDate];
    

    
    NSTimeInterval late = [d timeIntervalSince1970]*1;
    
    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval now = [dat timeIntervalSince1970]*1;
    
    NSTimeInterval cha = late - now;
    
    return cha;
}

+ (NSString *)timeFormatted:(int)totalSeconds
{
    
    int seconds = totalSeconds % 60;
    int minutes = (totalSeconds / 60) % 60;
    int hours = totalSeconds / 3600;
    
    return [NSString stringWithFormat:@"%02d:%02d:%02d",hours, minutes, seconds];
}

+(NSString*)getOpenUDID
{
    return [OpenUDID value];
}

//获取用户授权数据
+(NSString*)getUserAuthData
{
    NSString* author = [cacheFileOperation readFromPlistFile:APP_USER_INFO_PLIST key:APP_USER_INFO_KEY];

    if (author.length > 2)
    {
        return [author substringToIndex:(author.length)];
    }
    
    return @"";
}

+(NSString*)getStaffCode
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"staffCode"]];
    return userid;
}

+(NSString*)getUserCode
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"userCode"]];
    if ([userid  isEqual: @"(null)"]) {
        userid = @"";
    }
    
    
    return userid;
}

+(NSString*)getShopCode
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"shopCode"]];
    return userid;
}

+(NSString*)getUserToken
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"tokenCode"]];
    return userid;
}

+(BOOL)isLogin
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userInfo = [SaveDefaults objectForKey:APP_USER_AOCNUM_KEY];
    if (userInfo == nil || userInfo.count <= 0)
    {
        return NO;
    }else{
        return YES;
    }
    

}

+(BOOL)isApprove
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    if (userDic == nil || userDic.count <= 0)
    {
        return NO;
    }
    
    NSString* identity = [NSString  stringWithFormat:@"%@",[userDic objectForKey:@"identity"]];
    if ([identity isEqualToString:@"1"] || [identity isEqualToString:@"2"] || [identity isEqualToString:@"3"])
    {
        return YES;
    }
    
    return NO;
}


+ (BOOL)checkTel:(NSString *)str
{
    NSString *regex = APP_PHONE_REG;
    
    NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regex];
    
    BOOL isMatch = [pred evaluateWithObject:str];
    
    if (!isMatch) {
        
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"请输入正确的手机号码" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        
        [alert show];
        return NO;
        
    }
    return YES;
}

+(NSString*)changeNullToBlank:(NSString*)strSource
{
    NSString* strDes = @"";

    if([strSource isEqual:[NSNull null]] || strSource == nil || [strSource rangeOfString:@"null"].length > 0 || [strSource  isEqual: @"<null>"])
    {
        return strDes;
    }
    
    return [NSString stringWithFormat:@"%@",strSource];
}

+(NSString*)getSign:(NSString*)methodName strParams:(NSString*)params
{
    NSMutableString* vCode = [[NSMutableString alloc]init];
    NSString* userCode = [gloabFunction getUserCode];
    NSString* token = [gloabFunction getUserToken];
    NSString* skey;
    if (userCode.length>0) {
         skey = [MD5 MD5Value:[userCode substringToIndex:6]];
    }else{
        skey = @"";
    }
//    NSString* skey = [MD5 MD5Value:[userCode substringToIndex:6]];
    NSMutableString* strKey = [[NSMutableString alloc]init];
    [strKey appendString:methodName];
    if(![params isKindOfClass:[NSNull class]] ){
        [strKey appendString:params];
    }
    [strKey appendString:skey];
    
    [vCode appendString:token];
    [vCode appendString:[MD5 MD5Value:strKey]];
    return vCode;
}

@end
