//
//  topSelectView.m
//  51youHui
//
//  Created by cq on 13-2-20.
//  Copyright (c) 2013年 djx. All rights reserved.
//

#import "topSelectView.h"


@implementation topSelectView

@synthesize delegate;

- (id)initWithFrame:(CGRect)frame buttonArray:(NSMutableArray*)btnAry
{
    self = [super initWithFrame:frame];
    if (self)
    {
        if ([btnAry count] <= 0)
        {
            return self;
        }
        
        indexClick = 0;
        m_arraySelectTab = [[NSMutableArray alloc]initWithArray:btnAry];
        m_arraySelectTabButtons = [[NSMutableArray alloc]init];
        
        if ([m_arraySelectTab count] > 4)
        {
            rectTopTab = CGRectMake(0, 0, APP_VIEW_WIDTH/4.5*[m_arraySelectTab count], 40);
        }
        else
            rectTopTab = CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
        
        
        
        UIScrollView* topSelectScroll = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        [topSelectScroll setBackgroundColor:APP_VIEW_BACKCOLOR];
        
        UIView* m_viewtopSelectTab = [[UIView alloc]initWithFrame:rectTopTab];
        m_imageViewSelected =  [[UIImageView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-2, APP_VIEW_WIDTH/[m_arraySelectTab count], 2)];
        [m_imageViewSelected setBackgroundColor:UICOLOR(243, 53, 41, 1.0)];
        [m_viewtopSelectTab addSubview:m_imageViewSelected];
        
        
        topSelectScroll.contentSize = CGSizeMake(rectTopTab.size.width, 0);
        
        UIView* iv_line = [[UIView alloc]initWithFrame:rectTopTab];
        iv_line =  [[UIImageView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-1, APP_VIEW_WIDTH, 1)];
        [iv_line setBackgroundColor:UICOLOR(216, 163, 164, 1.0)];
        [m_viewtopSelectTab addSubview:iv_line];
        
        for (int i = 0; i < [m_arraySelectTab count]; i++)
        {
            selectBtnObject* obj = [m_arraySelectTab objectAtIndex:i];
            UIButton* btn = [UIButton buttonWithType:UIButtonTypeCustom];
            btn.showsTouchWhenHighlighted = YES;
            btn.tag = obj.tag;
            btn.frame = CGRectMake(rectTopTab.size.width/[m_arraySelectTab count] * i, 0, rectTopTab.size.width/[m_arraySelectTab count], rectTopTab.size.height);
            [btn addTarget:self action:@selector(topBarButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
            [btn setTitle:obj.title forState:UIControlStateNormal];
            //[btn setBackgroundImage:[UIImage imageNamed:obj.backImage] forState:UIControlStateNormal];
            [btn setBackgroundColor:APP_VIEW_BACKCOLOR];
            [btn setTitleColor:UICOLOR(170, 0, 38, 1.0) forState:UIControlStateNormal];
            
            btn.titleLabel.font = [UIFont systemFontOfSize:18];
            [m_viewtopSelectTab addSubview:btn];
            
            
            UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(btn.frame.size.width - 25, (btn.frame.size.height - 36)/2, 25, 36)];
            iv_logo.image = [UIImage imageNamed:obj.backImageNormal];
            iv_logo.tag = obj.tag;
            [btn addSubview:iv_logo];
            
            
            if (i < m_arraySelectTab.count-1)
            {
                UIImageView* iv_sep = [[UIImageView alloc]initWithFrame:CGRectMake(btn.frame.size.width - 1, 0, 1, btn.frame.size.height)];
                iv_sep.image = [UIImage imageNamed:@"choosebar_line"];
                iv_sep.tag = 1000;
                [btn addSubview:iv_sep];
            }
            
            [m_arraySelectTabButtons addObject:btn];
        }
        
        [topSelectScroll addSubview:m_viewtopSelectTab];
        [self addSubview:topSelectScroll];
    }
    return self;
}

- (id)initWithFrame:(CGRect)frame buttonArray:(NSMutableArray*)btnAry btbColor:(UIColor*)color
{
    self = [super initWithFrame:frame];
    if (self)
    {
        if ([btnAry count] <= 0)
        {
            return self;
        }
        
        indexClick = 0;
        m_arraySelectTab = [[NSMutableArray alloc]initWithArray:btnAry];
        m_arraySelectTabButtons = [[NSMutableArray alloc]init];
        
        if ([m_arraySelectTab count] > 4)
        {
            rectTopTab = CGRectMake(0, 0, APP_VIEW_WIDTH/4.5*[m_arraySelectTab count], 40);
        }
        else
            rectTopTab = CGRectMake(0, 0, APP_VIEW_WIDTH, 40);
        
        
        
        UIScrollView* topSelectScroll = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        [topSelectScroll setBackgroundColor:APP_VIEW_BACKCOLOR];
        
        UIView* m_viewtopSelectTab = [[UIView alloc]initWithFrame:rectTopTab];
        m_imageViewSelected =  [[UIImageView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-2, APP_VIEW_WIDTH/[m_arraySelectTab count], 2)];
        [m_imageViewSelected setBackgroundColor:UICOLOR(243, 53, 41, 1.0)];
        m_imageViewSelected.hidden = YES;
        [m_viewtopSelectTab addSubview:m_imageViewSelected];
        
        UIView* iv_line = [[UIView alloc]initWithFrame:rectTopTab];
        iv_line =  [[UIImageView alloc]initWithFrame:CGRectMake(0, self.frame.size.height-1, APP_VIEW_WIDTH, 1)];
        [iv_line setBackgroundColor:UICOLOR(216, 163, 164, 1.0)];
        [m_viewtopSelectTab addSubview:iv_line];
        
        topSelectScroll.contentSize = CGSizeMake(rectTopTab.size.width, 0);
        
        
        for (int i = 0; i < [m_arraySelectTab count]; i++)
        {
            selectBtnObject* obj = [m_arraySelectTab objectAtIndex:i];
            
            UIButton* btn = [UIButton buttonWithType:UIButtonTypeCustom];
            btn.showsTouchWhenHighlighted = YES;
            btn.tag = obj.tag;
            btn.frame = CGRectMake(rectTopTab.size.width/[m_arraySelectTab count] * i, 0, rectTopTab.size.width/[m_arraySelectTab count], rectTopTab.size.height);
            [btn addTarget:self action:@selector(topBarButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
            [btn setTitle:obj.title forState:UIControlStateNormal];
            //[btn setBackgroundImage:[UIImage imageNamed:obj.backImage] forState:UIControlStateNormal];
            [btn setBackgroundColor:color];
            [btn setTitleColor:UICOLOR(170, 0, 38, 1.0) forState:UIControlStateNormal];
            
            btn.titleLabel.font = [UIFont systemFontOfSize:18];
            [m_viewtopSelectTab addSubview:btn];
            
            
            UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(btn.frame.size.width - 25, (btn.frame.size.height - 36)/2, 25, 36)];
            iv_logo.image = [UIImage imageNamed:obj.backImageNormal];
            iv_logo.tag = obj.tag;
            [btn addSubview:iv_logo];
            
            
            if (i < m_arraySelectTab.count-1)
            {
                UIImageView* iv_sep = [[UIImageView alloc]initWithFrame:CGRectMake(btn.frame.size.width - 1, 0, 1, btn.frame.size.height)];
                iv_sep.image = [UIImage imageNamed:@"choosebar_line"];
                iv_sep.tag = 1000;
                [btn addSubview:iv_sep];
            }
            
            [m_arraySelectTabButtons addObject:btn];
        }
        
        [topSelectScroll addSubview:m_viewtopSelectTab];
        [self addSubview:topSelectScroll];
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

- (void)topBarButtonClicked:(id)sender
{
    UIButton* btn = (UIButton*)sender;
    CGRect rect = btn.frame;
    indexClick = (int)rect.origin.x / (int)(rectTopTab.size.width / [m_arraySelectTab count]);
    
    for (int i = 0; i < [m_arraySelectTabButtons count]; i++)
    {
        UIButton* btnTemp = (UIButton*)[m_arraySelectTabButtons objectAtIndex:i];
        [btnTemp setTitleColor:UICOLOR(170, 0, 38, 1.0) forState:UIControlStateNormal];
        selectBtnObject* obj = [m_arraySelectTab objectAtIndex:i];
        
        if([btnTemp isEqual:sender])
        {
            for (id iv in btnTemp.subviews)
            {
                if ([iv isKindOfClass:[UIImageView class]])
                {
                    UIImageView* iiv = (UIImageView*)iv;
                    if(iiv.tag == obj.tag)
                    {
                        iiv.image = [UIImage imageNamed:obj.backImage];
                    }
                    
                }
                
            }
        }
        else
        {
            for (UIImageView* iv in btnTemp.subviews)
            {
                
                if ([iv isKindOfClass:[UIImageView class]])
                {
                    UIImageView* iiv = (UIImageView*)iv;
                    if(iiv.tag == obj.tag)
                    {
                        iiv.image = [UIImage imageNamed:obj.backImageNormal];
                    }
                    
                }
                
                
            }
        }
    }
    
    [self setImageViewSelectedAnimation:indexClick];
    
    if (delegate != nil && indexClick < [m_arraySelectTab count])
    {
        selectBtnObject* obj = [m_arraySelectTab objectAtIndex:indexClick];
        
        
        [delegate btnSelect:btn btnObj:obj];
    }
}

- (void)setImageViewSelectedAnimation:(NSInteger)index
{
    [self bringSubviewToFront:m_imageViewSelected];
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3f];
    CGRect rect = CGRectMake(index*(rectTopTab.size.width/[m_arraySelectTab count]), self.frame.size.height-2, APP_VIEW_WIDTH/[m_arraySelectTab count], 2);
    m_imageViewSelected.frame = rect;
    [UIView commitAnimations];
}

- (void)setImageViewSelectedNoAnimation:(NSInteger)index
{
    [self bringSubviewToFront:m_imageViewSelected];
    CGRect rect = CGRectMake(index*(rectTopTab.size.width/[m_arraySelectTab count]), self.frame.size.height-2, APP_VIEW_WIDTH/[m_arraySelectTab count], 2);
    m_imageViewSelected.frame = rect;

}

@end
