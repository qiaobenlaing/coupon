//
//  SDZX_citySingle.m
//  SDBooking
//
//  Created by djx on 14-3-3.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "SDZX_citySingle.h"
#import "ChineseToPinyin.h"
@implementation SDZX_citySingle

static SDZX_citySingle * getInstance;

+ (id) sharedCityInstance
{
    @synchronized ([SDZX_citySingle class]) {
        if (getInstance == nil) {
            getInstance = [[SDZX_citySingle alloc] init];
        }
    }
    return getInstance;
}

- (id)init
{
    self = [super init];
    if (self) {
        [self getCityInfo];
        
    }
    return self;
}

- (void)getCityInfo
{
    if (arrayCity == nil)
    {
        arrayCity = [[NSMutableArray alloc]init];
    }

    if (arrayCity.count <= 0)
    {
        NSDictionary* dic = [cacheFileOperation readFromPlistFile:APP_SAVEFILE_CITY_NAME];
        NSDictionary* resultDic = [dic objectForKey:APP_SAVEFILE_CITY_KEY];
        NSArray* array = [resultDic objectForKey:@"cityList"];
        for (int i = 0; i < array.count; i++)
        {
            SDZX_cityObject* cityObj = [[SDZX_cityObject alloc]init];
            NSDictionary* dicResult = [array objectAtIndex:i];
            cityObj.cityId = [dicResult objectForKey:@"id"];
            cityObj.cityName = [dicResult objectForKey:@"cityName"];
            cityObj.defaultCity = [dicResult objectForKey:@"defaultCity"];
            cityObj.ifshow = [dicResult objectForKey:@"ifshow"];
            NSString* pingYin = [ChineseToPinyin pinyinFromChiniseString:[cityObj.cityName substringToIndex:1]];
            cityObj.city_firstWord = [pingYin substringToIndex:1];
            [arrayCity addObject:cityObj];
        }
    }

}

- (SDZX_cityObject*)getCityByCode:(NSString*)cityCode
{
    if (cityCode == nil || cityCode.length <= 0)
    {
        return nil;
    }
    
    SDZX_cityObject* cityObj = [[SDZX_cityObject alloc]init];
    
    [self getCityInfo];
    
    for (int i = 0; i < arrayCity.count; i++)
    {
        SDZX_cityObject* obj = [arrayCity objectAtIndex:i];
        if ([obj.cityId isEqualToString:cityCode])
        {
            cityObj = obj;
            break;
        }
    }
    return cityObj;
}

- (SDZX_cityObject*)getCityByName:(NSString*)cityName
{
    if (cityName == nil || cityName.length <= 0)
    {
        return nil;
    }
    
    [self getCityInfo];
    
    NSString* name = [cityName substringToIndex:(cityName.length )];
    SDZX_cityObject* cityObj = [[SDZX_cityObject alloc]init];
    
    for (int i = 0; i < arrayCity.count; i++)
    {
        SDZX_cityObject* obj = [arrayCity objectAtIndex:i];
        if ([obj.cityName isEqualToString:name])
        {
            cityObj = obj;
            break;
        }
    }
    return cityObj;
}

- (NSMutableArray*)getAllCity
{
    [self getCityInfo];
    return arrayCity;
}

- (NSMutableDictionary*)getAllCitySortFirstWord
{
    [self getCityInfo];
    
    NSMutableDictionary* resultDic = [[NSMutableDictionary alloc]init];
    for (int i = 0 ; i < arrayCity.count ; i++)
    {
        SDZX_cityObject* obj = [arrayCity objectAtIndex:i];
        NSMutableArray* array = [resultDic objectForKey:obj.city_firstWord];
        if (array == nil)
        {
            array = [[NSMutableArray alloc]init];
        }
        
        [array addObject:obj];
        [resultDic setObject:array forKey:obj.city_firstWord];
    }
    
    return resultDic;
}

@end
