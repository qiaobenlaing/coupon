//
//  UIBottomView.m
//  SDBooking
//
//  Created by djx on 14-2-8.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "UIBottomView.h"

@implementation UIBottomView

@synthesize delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self setViewUp];
        
        
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)setViewUp
{
    [self setBackgroundColor:UICOLOR(0, 0, 0, 0.7)];
    
    if (btn_bottom == nil)
    {
        btn_bottom = [[UIButton alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-200)/2, (self.frame.size.height - 30)/2, 200, 30)];
    }
    
    [btn_bottom addTarget:self action:@selector(btnBottomClick:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:btn_bottom];
}

- (void)btnBottomClick:(id)sender
{
    if (delegate != nil)
    {
        [delegate bottomClick];
    }
}

- (void)setButtonBackImage:(NSString*)imageName
{
    if (imageName == nil || imageName.length <= 0)
    {
        [btn_bottom setBackgroundImage:[UIImage imageNamed:@"btn_bottom"] forState:UIControlStateNormal];
        return;
    }
    
    if (btn_bottom != nil)
    {
        [btn_bottom setBackgroundImage:[UIImage imageNamed:imageName] forState:UIControlStateNormal];
    }
    
}

//设置按钮标题
- (void)setButtonTitle:(NSString*)title
{
    if (title == nil || title.length <= 0)
    {
        return;
    }
    
    [btn_bottom setTitle:title forState:UIControlStateNormal];
}
@end
