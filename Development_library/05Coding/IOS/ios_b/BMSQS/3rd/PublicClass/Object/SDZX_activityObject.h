//
//  SDZX_activityObject.h
//  SDBooking
//
//  Created by djx on 14-4-9.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SDZX_activityObject : NSObject

@property(nonatomic,strong)NSString* activity_image;
@property(nonatomic,strong)NSString* activity_name;
@property(nonatomic,strong)NSString* activity_endtime;
@property(nonatomic,strong)NSString* activity_code;
@property(nonatomic,strong)NSString* activity_description;
@property(nonatomic,strong)NSString* activity_isActive;
@property(nonatomic,strong)NSString* activity_banner;

@end


@interface SDZX_activityShakeObject : NSObject

@property(nonatomic,strong)NSString* activity_id;
@property(nonatomic,strong)NSString* activity_content;
@property(nonatomic,strong)NSString* activity_endtime;
@property(nonatomic,strong)NSString* activity_begintime;
@property(nonatomic,strong)NSString* activity_code;
@property(nonatomic,strong)NSString* activity_changeContent;
@property(nonatomic,strong)NSString* activity_name;
@property(nonatomic,strong)NSString* activity_notice;

@end