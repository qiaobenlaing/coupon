//
//  SDZX_movieSessionObject.h
//  SDBooking
//
//  Created by djx on 14-3-10.
//  Copyright (c) 2014年 djx. All rights reserved.
//

//影片排期
#import <Foundation/Foundation.h>

@interface SDZX_movieSessionObject : NSObject

@property(nonatomic,strong)NSString* movie_filmstandardid;
@property(nonatomic,strong)NSString* movie_filmcode;
@property(nonatomic,strong)NSString* movie_filmname;
@property(nonatomic,strong)NSString* movie_hallcode;
@property(nonatomic,strong)NSString* movie_hallname;
@property(nonatomic,strong)NSString* movie_standardprice;
@property(nonatomic,strong)NSString* movie_plantype;
@property(nonatomic,strong)NSString* movie_plancode;
@property(nonatomic,strong)NSString* movie_name;
@property(nonatomic,strong)NSString* movie_playtime;
@property(nonatomic,strong)NSString* movie_issetprice;
@property(nonatomic,strong)NSString* movie_state;
@property(nonatomic,strong)NSString* movie_featureno;
@property(nonatomic,strong)NSString* movie_filmtypecode;
@property(nonatomic,strong)NSString* movie_filmduration; //影片时长
@property(nonatomic,strong)NSString* movie_filmpicurl;
@property(nonatomic,strong)NSString* movie_price; //价格
@property(nonatomic,strong)NSString* movie_memberprice; //普通会员价
@property(nonatomic,strong)NSString* movie_vipprice; //vip价格
@property(nonatomic,strong)NSString* movie_hfhlowestprice;
@property(nonatomic,strong)NSString* movie_seqno;
@property(nonatomic,strong)NSString* movie_filmtypename;
@property(nonatomic,strong)NSString* movie_sectionid;
@property(nonatomic,strong)NSString* movie_cinemalinkid;
@property(nonatomic,strong)NSString* movie_weblowestprice;
@property(nonatomic,strong)NSString* movie_tickettypename;
@property(nonatomic,strong)NSString* movie_date; //日期


@end
