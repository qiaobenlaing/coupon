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

+(NSString*)getStreet {
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSString* userlvl = [NSString stringWithFormat:@"%@%@%@",[SaveDefaults objectForKey:@"province"], [SaveDefaults objectForKey:@"city"], [SaveDefaults objectForKey:@"street"]];
    return userlvl;
}

+(NSString*)getShopName {
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSString* userlvl = [NSString stringWithFormat:@"%@",[SaveDefaults objectForKey:@"shopName"]];
    return userlvl;
}

+(NSString*)getTel {
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSString* tel = [NSString stringWithFormat:@"%@",[SaveDefaults objectForKey:@"tel"]];
    return tel;
}

+(NSString*)getUserLvl //1-店员，2-店长，3-大店长
{

    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userlvl = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"userLvl"]];
    return userlvl;
}


+(NSString*)getStaffCode
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* userid = [NSString stringWithFormat:@"%@",[userDic objectForKey:@"staffCode"]];
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
    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    if (userDic == nil || userDic.count <= 0)
    {
        return NO;
    }
    return YES;
}



+(NSString *)getisCatering
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
//    NSDictionary* userDic = [SaveDefaults objectForKey:APP_USER_INFO_KEY];
    NSString* str = [NSString stringWithFormat:@"%@",[SaveDefaults objectForKey:@"isCatering"]];
    
    return str;
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

//计算某一个时间到当前的日期差
+ (double)intervalSinceNow: (NSString *) theDate
{
    NSDateFormatter *date=[[NSDateFormatter alloc] init];
    [date setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSDate *d=[date dateFromString:theDate];
    
    NSTimeInterval late = [d timeIntervalSince1970]*1;

    NSDate* dat = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval now = [dat timeIntervalSince1970]*1;
    
    NSTimeInterval cha = now - late;

    return cha;
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
    if([strSource isEqual:[NSNull null]] || strSource == nil || [strSource rangeOfString:@"null"].length > 0)
    {
        return strDes;
    }
    
    return [NSString stringWithFormat:@"%@",strSource];
}

/*
  methodName 接口请求方法
  params 是methodName（接口方法）的第一个参数的值， 不能为空
 */
+(NSString*)getSign:(NSString*)methodName strParams:(NSString*)params
{
    // 规则
    // token + md5( “methodName” + params + md5( userCode 的前六位 ))
    
    NSMutableString* vCode = [[NSMutableString alloc]init];
    NSString* userCode = [gloabFunction getStaffCode];
    NSString* token = [gloabFunction getUserToken];
    NSString* skey = [MD5 MD5Value:[userCode substringToIndex:6]];
    
    [vCode appendString:token];
    
    if (params.length>0) {
        NSString *key = [[methodName stringByAppendingString:params] stringByAppendingString:skey];
        [vCode appendString:[MD5 MD5Value:key]];

    }else{
        NSString *key = [methodName  stringByAppendingString:skey];
        [vCode appendString:[MD5 MD5Value:key]];
    }

    return vCode;
}

+ (UIView *)ggsetLineView:(CGRect)frame view:(UIView *)view{
    UIView *lineView = [[UIView alloc] initWithFrame:frame];
    lineView.backgroundColor = UICOLOR(214, 215, 219, 1);
    [view addSubview:lineView];
    return view;
}


@end
