//
//  SDZX_citySingle.h
//  SDBooking
//
//  Created by djx on 14-3-3.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SDZX_cityObject.h"

@interface SDZX_citySingle : NSObject
{
    NSMutableArray* arrayCity;
}

+(id)sharedCityInstance;

- (SDZX_cityObject*)getCityByCode:(NSString*)cityCode;
- (SDZX_cityObject*)getCityByName:(NSString*)cityName;
- (NSMutableArray*)getAllCity;
- (NSMutableDictionary*)getAllCitySortFirstWord;
@end
