//
//  plistFileOperation.h
//  IcardEnglish
//
//  Created by djx on 12-7-2.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "PageScrollView.h"

#define CONSIZE_HEGHT 0 //偏移的高度
@implementation PageScrollView
@synthesize pageRegion = _pageRegion;
@synthesize controlRegin = _controlRegin;

-(id)initWithFrame:(CGRect)frame pageRect:(CGRect)pageRegionRect controlRect:(CGRect)controlReginRect
{
    self=[super initWithFrame:frame];
    if(self!=nil)
    {
        _pages=nil;
        _zeroPage=0;
		offset = 0.0;
        _pageRegion = pageRegionRect;//CGRectMake(0,0,iCard_VIEW_WIDTH,400);
		
        //_pageRegion=CGRectMake(frame.origin.x, frame.origin.y, frame.size.width, frame.size.height-60);
        //_controlRegin=CGRectMake(frame.origin.x, frame.size.height-60, frame.size.width, 60);
        //////NSLog(@"x-%f,y-%f,w-%f,h-%f",frame.origin.x,frame.origin.y,frame.size.width,frame.size.height);
      
		_controlRegin = controlReginRect;//CGRectMake(130,405,60,10);
        self.delegate=nil;
        
        scroll=[[UIScrollView alloc]initWithFrame:_pageRegion];
		scroll.frame = frame;//CGRectMake(0,0,iCard_VIEW_WIDTH,420);
        scroll.pagingEnabled=YES;
        scroll.delegate=self;
		[scroll setBackgroundColor:[UIColor clearColor]];
		scroll.minimumZoomScale = 1.0;
		scroll.maximumZoomScale = 3.0;
        scroll.bounces = NO;
        [self addSubview:scroll];

        pageControl=[[UIPageControl alloc]initWithFrame:_controlRegin];
        [pageControl addTarget:self action:@selector(pageControlDidChange:) forControlEvents:UIControlEventValueChanged];
        pageControl.hidesForSinglePage = YES;
        //[pageControl setBackgroundColor:[UIColor clearColor]];
		//[self addSubview:pageControl];
		[pageControl setBackgroundColor:[UIColor clearColor]];

        //pageControl.hidden=TRUE;
    }
    return self;
}

-(void)setPages:(NSMutableArray *)pages
{
    if(_pages!=nil)
    {
        for(int i=0;i<[_pages count];i++)
        {
            [[_pages objectAtIndex:i] removeFromSuperview];
        }
    }
    _pages=pages;
    scroll.contentOffset=CGPointMake(0, 0);
    
    if([_pages count]<4)
    {
        scroll.contentSize=CGSizeMake(_pageRegion.size.width*[_pages count], _pageRegion.size.height + CONSIZE_HEGHT);
    }
    else
    {
        scroll.contentSize=CGSizeMake(_pageRegion.size.width*3, _pageRegion.size.height + CONSIZE_HEGHT);
        scroll.showsHorizontalScrollIndicator=NO;
    }
     
    
    //scroll.contentSize=CGSizeMake(_pageRegion.size.width*[_pages count], _pageRegion.size.height + CONSIZE_HEGHT);
    pageControl.numberOfPages=[_pages count];
    pageControl.currentPage=0;
    [self layoutViews];
}

-(void)layoutViews
{
    
    if([_pages count]<=3)
    {
        for(int i=0;i<[_pages count];i++)
        {
            UIView *page=[_pages objectAtIndex:i];
            //CGRect bounds=page.bounds;
            CGRect frame=CGRectMake(_pageRegion.size.width*i, 0, _pageRegion.size.width, _pageRegion.size.height);
            page.frame=frame;
            //page.bounds=bounds;
            [scroll addSubview:page];
        }
        return;
    }
    
    for(int i=0;i<[_pages count];i++)
    {
        UIView *page=[_pages objectAtIndex:i];
       // CGRect bounds=page.bounds;
        CGRect frame=CGRectMake(0, 0, _pageRegion.size.width, _pageRegion.size.height);
        page.frame=frame;
        //page.bounds=bounds;
        page.hidden=YES;
        [scroll addSubview:page];
    }

    [self layoutScroller];
}

-(void)layoutScroller
{
   // return;
    UIView *page;
    CGRect bounds,frame;
    int pageNum=[self getCurrentPage];
    
    if([_pages count]<=3)
    {
        return;
    }
    //////NSLog(@"laying out scroller for page %d\n",pageNum);
    
    if(pageNum==0)
    {
        for(int i=0;i<3;i++)
        {
            page=[_pages objectAtIndex:i];
            bounds=page.bounds;
            frame=CGRectMake(_pageRegion.size.width*i, 0, _pageRegion.size.width, _pageRegion.size.height);
           // ////NSLog(@"\tOffset for Page %d=%f\n",i,frame.origin.x);
            page.frame=frame;
            //page.bounds=bounds;
            page.hidden=NO;
        }
        page=[_pages objectAtIndex:3];
        page.hidden=YES;
        _zeroPage=0;
    }
    else if(pageNum==[_pages count]-1)
    {
        for(int i=pageNum-2;i<=pageNum;i++)
        {
            page=[_pages objectAtIndex:i];
            bounds=page.bounds;
            frame=CGRectMake(_pageRegion.size.width*(2-(pageNum-i)), 0, _pageRegion.size.width, _pageRegion.size.height);
            //////NSLog(@"\tOffset for page %d =%f\n",i,frame.origin.x);
            page.frame=frame;
           // page.bounds=bounds;
            page.hidden=NO;
        }
        page=[_pages objectAtIndex:[_pages count]-3];
        page.hidden=YES;
        _zeroPage=pageNum-2;
    }
    else
    {
        for(int i=pageNum-1;i<=pageNum+1;i++)
        {
            page=[_pages objectAtIndex:i];
            bounds=page.bounds;
            frame=CGRectMake(_pageRegion.size.width*(i-(pageNum-1)), 0, _pageRegion.size.width, _pageRegion.size.height);
            //////NSLog(@"\tOffset for page %d = %f\n",i,frame.origin.x);
            page.frame=frame;
            page.bounds=bounds;
            page.hidden=NO;
        }
        for(int i=0;i<[_pages count];i++)
        {
            if(i<pageNum-1||i>pageNum+1)
            {
                page=[_pages objectAtIndex:i];
                page.hidden=YES;
            }
        }
        scroll.contentOffset=CGPointMake(_pageRegion.size.width, 0);
        _zeroPage=pageNum-1;
    }
}

-(id)getDelegate
{
    return _delegate;
}

-(void)setDelegate:(id)delegate
{
    _delegate=delegate;
}

-(BOOL)getShowsPageControl
{
    return _showsPageControl;
}

-(void)setShowsPageControl:(BOOL)showsPageControl
{
    _showsPageControl=showsPageControl;
    if(_showsPageControl == NO)
    {
        _pageRegion=CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, self.frame.size.height - 20);
        pageControl.hidden=YES;
        scroll.frame=_pageRegion;
    }
    else
    {
        _pageRegion=CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, self.frame.size.height-60);
        pageControl.hidden=YES;
        scroll.frame=_pageRegion;
    }
}

-(NSMutableArray *)getPages
{
    return _pages;
}

-(UIView*)getCurrentPageView
{
	int index = [self getCurrentPage];
	return [_pages objectAtIndex:index];
}

-(void)setCurrentPage:(int)page
{
    [scroll setContentOffset:CGPointMake(0,0)];
    _zeroPage=page;
    [self layoutScroller];
    pageControl.currentPage=page;
}

-(int)getCurrentPage
{
    return (int)(scroll.contentOffset.x/_pageRegion.size.width)+_zeroPage;
}

#pragma mark UIScrollViewdelegate


-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView
{
    pageControl.currentPage=self.currentPage;
	
    [self layoutScroller];
    [self notifyPageChange];
	/*
	if (scrollView == scroll) 
	{
		for (UIView* v in scrollView.subviews)
		{
			if ([v isKindOfClass:[UIScrollView class]])
			{
				UIScrollView* sv = (UIScrollView*)v;
				CGFloat x = sv.contentOffset.x;
				
				if (x == offset)
				{
					
				}
				else 
				{
					offset = x;
					
					//[sv setZoomScale:1.0];
				}
				
			}
		}
	}*/
}

-(void)pageControlDidChange:(id)sender
{
    UIPageControl *control=(UIPageControl *)sender;
    if(control==pageControl)
    {
        [scroll setContentOffset:CGPointMake(_pageRegion.size.width*(control.currentPage-_zeroPage), 0)
         animated:YES
         ];
    }
}

-(void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView
{
    [self layoutScroller];
    [self notifyPageChange];
}

-(void)notifyPageChange
{
    if(self.delegate!=nil)
    {
        if([_delegate conformsToProtocol:@protocol(PageScrollViewDelegate)])
        {
            if([_delegate respondsToSelector:@selector(pageScrollViewDidChangeCurrentPage:currentPage:)])
            {
                [self.delegate pageScrollViewDidChangeCurrentPage:(PageScrollView *)self currentPage:self.currentPage];
            }
        }
    }
}

@end
