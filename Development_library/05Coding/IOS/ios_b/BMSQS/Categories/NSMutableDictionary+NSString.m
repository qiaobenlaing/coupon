//
//  NSMutableDictionary+NSString.m
//  SDBooking
//
//  Created by djx on 14-3-24.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "NSMutableDictionary+NSString.h"

@implementation NSMutableDictionary (NSString)

-(NSMutableString*)dicToJson
{
    if (self == nil)
    {
        return nil;
    }
    
    NSMutableString* resultStr = [[NSMutableString alloc]init];
    [resultStr appendString:@"{"];
    //NSArray* keys = self.keys;
    for (int i = 0; i < self.allKeys.count; i++)
    {
        NSString* key = [self.allKeys objectAtIndex:i];
        NSString* value = [self objectForKey:key];
        
        if (key != nil && key.length > 0 && [key rangeOfString:@"null"].length <= 0)
        {
            [resultStr appendString:[NSString stringWithFormat:@"\"%@\":",key]];
            
            if (value == nil || value.length <= 0 || [value rangeOfString:@"null"].length > 0)
            {
                value = @"";
            }
            [resultStr appendString:[NSString stringWithFormat:@"\"%@\"",value]];
            
            if (i < self.allKeys.count - 1)
            {
                [resultStr appendString:@","];
            }
            
        }
    }
    
    [resultStr appendString:@"}"];
    return resultStr;
}

@end
