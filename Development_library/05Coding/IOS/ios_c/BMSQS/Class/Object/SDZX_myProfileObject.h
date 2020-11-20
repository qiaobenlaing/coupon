//
//  SDZX_myProfileObject.h
//  SDBooking
//
//  Created by djx on 14-4-18.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SDZX_myProfileObject : NSObject

@property(nonatomic,strong)NSString* profile_address;
@property(nonatomic,strong)NSString* profile_birthday;
@property(nonatomic,strong)NSString* profile_contactphone;
@property(nonatomic,strong)NSString* profile_email;
@property(nonatomic,strong)NSString* profile_fetion;
@property(nonatomic,strong)NSString* profile_frequencycinemacode;
@property(nonatomic,strong)NSString* profile_frequencycinemaname;//常去影院
@property(nonatomic,strong)NSString* profile_imgurl; //头像
@property(nonatomic,strong)NSString* profile_isvip;
@property(nonatomic,strong)NSString* profile_likefilmtype;//喜欢的电影类型
@property(nonatomic,strong)NSString* profile_mobile;
@property(nonatomic,strong)NSString* profile_nickname;
@property(nonatomic,strong)NSString* profile_qq;
@property(nonatomic,strong)NSString* profile_sex;//性别
@property(nonatomic,strong)NSString* profile_remark;//备注
@property(nonatomic,strong)NSString* profile_userlevel;
@property(nonatomic,strong)NSString* profile_userid;
@property(nonatomic,strong)NSString* profile_usertype;
@property(nonatomic,strong)NSDictionary* profile_vipdata;

@end
