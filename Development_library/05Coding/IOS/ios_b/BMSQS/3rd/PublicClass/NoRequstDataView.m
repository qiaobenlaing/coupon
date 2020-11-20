//
//  NoRequstDataView.m
//  YCQR
//
//  Created by djx on 15/8/14.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "NoRequstDataView.h"

@implementation NoRequstDataView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

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
    self.backgroundColor = [UIColor redColor];
    
    UIImageView* ivLogo = [[UIImageView alloc]initWithFrame:CGRectMake(0,  0, 200, 200)];
    ivLogo.image = [UIImage imageNamed:@"iv_loadingLogo"];
    ivLogo.center= CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
    [self addSubview:ivLogo];
    
}

- (void)setImageViewName:(NSString*)imgName
{
    UIImageView* ivLogo = (UIImageView*)[self viewWithTag:100];
    ivLogo.image = [UIImage imageNamed:imgName];
}
@end
