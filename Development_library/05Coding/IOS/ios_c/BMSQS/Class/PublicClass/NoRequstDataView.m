//
//  NoRequstDataView.m
//  YCQR
//
//  Created by djx on 15/8/14.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "NoRequstDataView.h"


@interface NoRequstDataView ()



@end


@implementation NoRequstDataView



- (id)init
{
    self = [super init];
    if (self)
    {
        [self setViewUp];
    }
    
    return self;
}


- (void)setViewUp
{
    self.frame = CGRectMake(0, APP_VIEW_ORIGIN_Y + 44, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT);
    self.backgroundColor = [UIColor clearColor];
    _ivLogo = [[UIImageView alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-237)/2,  APP_VIEW_ORIGIN_Y, 237, 173)];
    [self addSubview:_ivLogo];
    
}


- (void)setImageViewName:(NSString*)imgName
{
    _ivLogo.image = [UIImage imageNamed:imgName];
}
@end
