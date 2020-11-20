//
//  SDZX_movieObject.h
//  SDBooking
//
//  Created by djx on 14-2-28.
//  Copyright (c) 2014年 djx. All rights reserved.
//

/******************************
 电影对象
 *****************************/
#import <Foundation/Foundation.h>

@interface SDZX_movieObject : NSObject

@property(nonatomic,strong)NSString* movie_filmcode; //电影code
@property(nonatomic,strong)NSString* movie_cinemacount; //正在上映的影院数
@property(nonatomic,strong)NSString* movie_plansum; //剩余场次
@property(nonatomic,strong)NSString* movie_filmname; //电影名称
@property(nonatomic,strong)NSString* movie_director; //导演
@property(nonatomic,strong)NSString* movie_actors; //演员
@property(nonatomic,strong)NSString* movie_commend; //描述
@property(nonatomic,strong)NSString* movie_filmpicturl; //海报Url
@property(nonatomic,strong)NSString* movie_filmcontent; //影片内容
@property(nonatomic,strong)NSString* movie_introduction; //影片详情
@property(nonatomic,strong)NSString* movie_contenttype; //影片类型
@property(nonatomic,strong)NSString* movie_filmarea; //地区
@property(nonatomic,strong)NSString* movie_filmlanguage; //语种
@property(nonatomic,strong)NSString* movie_playtime; //片长
@property(nonatomic,strong)NSString* movie_showtype; //展示类型，2D，3D
@property(nonatomic,strong)NSMutableArray* movie_posterpics; //剧照url
@property(nonatomic,strong)NSString* movie_graise; //点赞数
@property(nonatomic,strong)NSMutableArray* movie_replies; //影片评论
@property(nonatomic,strong)NSString* movie_totalsold;
@property(nonatomic,strong)NSString* movie_releasetime;//上映时间
@property(nonatomic,strong)NSString* movie_zan;

@end
