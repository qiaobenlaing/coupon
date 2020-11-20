//
//  NSDate+DateHelper.m
//  DateHelper
//
//  Created by rang on 13-1-7.
//  Copyright (c) 2013年 rang. All rights reserved.
//

#import "NSDate+CZExtend.h"

@implementation NSDate (CZExtend)
//取得今天是星期幾
- (NSInteger)dayOfWeek {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *weekdayComponents = [calendar components:NSWeekdayCalendarUnit fromDate:self];

    NSInteger week = weekdayComponents.weekday;
    week -= 2;
    if(week < 0){
        week += 7;
    }
    
    return week;
}

//取得每月有多少天
- (NSInteger)monthOfDay {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSRange range = [calendar rangeOfUnit:NSDayCalendarUnit inUnit:NSMonthCalendarUnit forDate:self];
    NSInteger num = range.length;
    return num;
}

//取得当前月第一天
- (NSDate *)monthFirstDay {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *weekdayComponents = [calendar components:NSYearCalendarUnit|NSMonthCalendarUnit fromDate:self];

    weekdayComponents.day = 2;
    NSDate *firstDate = [calendar dateFromComponents:weekdayComponents];
    
    return firstDate;
}

//取得当前月最后一天
- (NSDate *)monthLastDay {
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *weekdayComponents = [calendar components:NSYearCalendarUnit|NSMonthCalendarUnit fromDate:self];

    weekdayComponents.day = [self monthOfDay]+1;
    NSDate *lastDate = [calendar dateFromComponents:weekdayComponents];
    return lastDate;
}

//本周開始時間
- (NSDate*)beginningOfWeek {
    NSInteger weekday=[self dayOfWeek];
    return  [self dateByAddingDays:(weekday-1)*-1];
}

//本周结束時間
- (NSDate *)endOfWeek {
    NSInteger weekday=[self dayOfWeek];
    if (weekday==7) {
        return self;
    }
    return [self dateByAddingDays:7-weekday];
}

//日期增加幾月
- (NSDate*)dateByAddingMonths:(NSInteger)month {
    NSDateComponents *c = [[NSDateComponents alloc] init];
	c.month = month;
    
    NSDate *resultDate=[[NSCalendar currentCalendar] dateByAddingComponents:c toDate:self options:0];
    
    return resultDate;
}

//日期增加幾天
- (NSDate*)dateByAddingDays:(NSInteger)days {
    NSDateComponents *c = [[NSDateComponents alloc] init];
	c.day = days;
    
    NSDate *resultDate=[[NSCalendar currentCalendar] dateByAddingComponents:c toDate:self options:0];
    
    return resultDate;
}

/**
 *  日期添加几分钟
 *  @param minute 添加几个小时
 *  @return       日期添加几天的时间
 */
- (NSDate*)dateByAddingHour:(NSInteger)hour {
    NSTimeInterval interval =60*60;//表示1小时
    return  [self dateByAddingTimeInterval:hour*interval];
}


//日期增加幾分鐘
- (NSDate*)dateByAddingMinutes:(NSInteger)minute {
    NSTimeInterval interval =60;//表示1分鐘
    return  [self dateByAddingTimeInterval:minute*interval];
}

//日期格式化
- (NSString *)stringWithFormat:(NSString *)format {
	NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
	[outputFormatter setDateFormat:format];
	NSString *timestamp_str = [outputFormatter stringFromDate:self];
	return timestamp_str;
}

//字串轉換成時間
+ (NSDate *)dateFromString:(NSString *)string withFormat:(NSString *)format {
	NSDateFormatter *inputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [inputFormatter setTimeZone:timeZone];
    
	[inputFormatter setDateFormat:format];
	NSDate *date = [inputFormatter dateFromString:string];
	return date;
}

//時間轉換成字串
+ (NSString *)stringFromDate:(NSDate *)date withFormat:(NSString *)format {
	return [date stringWithFormat:format];
}

//日期轉換成民國時間
- (NSString*)dateToTW:(NSString *)string {
    NSString *str=[self stringWithFormat:string];
    int y=[[str substringWithRange:NSMakeRange(0, 4)] intValue];
    return [NSString stringWithFormat:@"%d%@",y-1911,[str stringByReplacingCharactersInRange:NSMakeRange(0, 4) withString:@""]];
}

- (TKDateInformation) dateInformationWithTimeZone:(NSTimeZone*)tz {
	TKDateInformation info;
	
	NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
	[gregorian setTimeZone:tz];
	NSDateComponents *comp = [gregorian components:(NSMonthCalendarUnit | NSMinuteCalendarUnit | NSYearCalendarUnit |
													NSDayCalendarUnit | NSWeekdayCalendarUnit | NSHourCalendarUnit | NSSecondCalendarUnit)
										  fromDate:self];
	info.day = (int)[comp day];
	info.month = (int)[comp month];
	info.year = (int)[comp year];
	
	info.hour = (int)[comp hour];
	info.minute = (int)[comp minute];
	info.second = (int)[comp second];
	
	info.weekday = (int)[comp weekday];
	
	
	return info;
}

- (TKDateInformation) dateInformation {
	TKDateInformation info;
	
	NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
	NSDateComponents *comp = [gregorian components:(NSMonthCalendarUnit | NSMinuteCalendarUnit | NSYearCalendarUnit |
													NSDayCalendarUnit | NSWeekdayCalendarUnit | NSHourCalendarUnit | NSSecondCalendarUnit)
										  fromDate:self];
	info.day = (int)[comp day];
	info.month = (int)[comp month];
	info.year = (int)[comp year];
	
	info.hour = (int)[comp hour];
	info.minute = (int)[comp minute];
	info.second = (int)[comp second];
	
	info.weekday = (int)[comp weekday];
	
    
	return info;
}

+ (NSDate*) dateFromDateInformation:(TKDateInformation)info timeZone:(NSTimeZone*)tz {
	NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
	[gregorian setTimeZone:tz];
	NSDateComponents *comp = [gregorian components:(NSYearCalendarUnit | NSMonthCalendarUnit) fromDate:[NSDate date]];
	
	[comp setDay:info.day];
	[comp setMonth:info.month];
	[comp setYear:info.year];
	[comp setHour:info.hour];
	[comp setMinute:info.minute];
	[comp setSecond:info.second];
	[comp setTimeZone:tz];
	
	return [gregorian dateFromComponents:comp];
}

+ (NSDate*) dateFromDateInformation:(TKDateInformation)info {
	NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
	NSDateComponents *comp = [gregorian components:(NSYearCalendarUnit | NSMonthCalendarUnit) fromDate:[NSDate date]];
	
	[comp setDay:info.day];
	[comp setMonth:info.month];
	[comp setYear:info.year];
	[comp setHour:info.hour];
	[comp setMinute:info.minute];
	[comp setSecond:info.second];
	
	return [gregorian dateFromComponents:comp];
}

+ (NSString*) dateInformationDescriptionWithInformation:(TKDateInformation)info {
	return [NSString stringWithFormat:@"%d %d %d %d:%d:%d",info.month,info.day,info.year,info.hour,info.minute,info.second];
}

- (BOOL)compareToDate:(NSDate*)date {
    if ([self compare:date]==NSOrderedDescending) {
        return YES;
    }
    return NO;
}
@end
