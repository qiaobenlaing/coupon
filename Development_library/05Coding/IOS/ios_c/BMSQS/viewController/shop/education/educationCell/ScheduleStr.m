//
//  ScheduleStr.m
//  BMSQC
//
//  Created by liuqin on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ScheduleStr.h"

@implementation ScheduleStr


+(id)scheduleStr:(NSDictionary *)schDic w:(float)width{

    NSString *learnStartDate =[schDic objectForKey:@"learnStartDate"];
    NSArray *timeArray = [learnStartDate componentsSeparatedByString:@"-"];
    NSString *startStr = [NSString stringWithFormat:@"%@月%@日",[timeArray objectAtIndex:1],[timeArray objectAtIndex:2]];
    
    
    learnStartDate =[schDic objectForKey:@"learnEndDate"];
    timeArray = [learnStartDate componentsSeparatedByString:@"-"];
    NSString *endStr = [NSString stringWithFormat:@"%@月%@日",[timeArray objectAtIndex:1],[timeArray objectAtIndex:2]];
    
    
    
    NSMutableString *time = [[NSMutableString alloc]init];
    NSString *data = [NSString stringWithFormat:@"%@-%@\n",startStr,endStr];
    [time appendString:[NSString stringWithFormat:@"%@",data]];
    for (NSDictionary *dic in [schDic objectForKey:@"classWeekInfo"]) {
        NSString *timeDay = [NSString stringWithFormat:@"%@",[dic objectForKey:@"weekName"]];
        NSArray *learnTimeArray = [dic objectForKey:@"learnTime"];
        for (int i=0;i<learnTimeArray.count;i++) {
            NSDictionary *learnTime = [learnTimeArray objectAtIndex:i];
                [time appendString:[NSString stringWithFormat:@"%@ %@-%@\n",timeDay,[learnTime objectForKey:@"startTime"],[learnTime objectForKey:@"endTime"]]];
            
        }
    }

   
    
    [time deleteCharactersInRange:NSMakeRange(time.length-1, 1)];
    
    CGSize size = [time boundingRectWithSize:CGSizeMake(width,MAXFLOAT)
                                     options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading
                                  attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                     context:nil].size;
    
    
    return @{@"str":time,@"height":[NSString stringWithFormat:@"%f",size.height+11]};
}

@end
