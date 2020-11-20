//
//  XLCycleScrollView.m
//  CycleScrollViewDemo
//
//  Created by xie liang on 9/14/12.
//  Copyright (c) 2012 xie liang. All rights reserved.
//

#import "XLCycleScrollView.h"

@implementation XLCycleScrollView

@synthesize scrollView = _scrollView;
@synthesize pageControl = _pageControl;
@synthesize currentPage = _curPage;
@synthesize datasource = _datasource;
@synthesize delegate = _delegate;
@synthesize maskImageView=_maskImageView;
@synthesize canZoom = _canZoom;
static CGFloat SWITCH_FOCUS_PICTURE_INTERVAL ; //switch interval time

- (id)initWithFrame:(CGRect)frame {
  
    return [self initWithFrame:frame time:3.0];
}

- (id)initWithFrame:(CGRect)frame time:(CGFloat)time
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
          SWITCH_FOCUS_PICTURE_INTERVAL = time;
        
        _scrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
        _scrollView.delegate = self;
        _scrollView.contentSize = CGSizeMake(self.bounds.size.width * 3, self.bounds.size.height);
        _scrollView.showsHorizontalScrollIndicator = NO;
        _scrollView.contentOffset = CGPointMake(self.bounds.size.width, 0);
        _scrollView.pagingEnabled = YES;
        [_scrollView setMinimumZoomScale:1.0];
        [_scrollView setMaximumZoomScale:1.0];
        [self addSubview:_scrollView];
        
//        _maskImageView= [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"news_banner_topmask"]];
//        [_maskImageView setFrame:CGRectMake(0, self.bounds.size.height-30, self.bounds.size.width, 30)];
//        [self addSubview:_maskImageView];
        
        
        CGRect rect = self.bounds;
        rect.origin.x=250;
        rect.origin.y = rect.size.height - 30;
        rect.size.width=80;
        rect.size.height = 30;
        
        _pageControl = [[UIPageControl alloc] initWithFrame:rect];
        _pageControl.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleRightMargin;
        _pageControl.userInteractionEnabled = NO;
       // _pageControl.currentPageIndicatorTintColor=[UIColor blackColor];
       // _pageControl.pageIndicatorTintColor=[UIColor darkGrayColor];
        
        [self addSubview:_pageControl];
        
        _curPage = 0;
    }
    return self;
}

- (void)setDataource:(id<XLCycleScrollViewDatasource>)datasource
{
    _datasource = datasource;
    [self reloadData];
}

- (void)setCanZoom:(BOOL)canZoom
{
     _canZoom = canZoom;
    if (_canZoom)
    {
//        [_scrollView setMinimumZoomScale:1.0];
//        [_scrollView setMaximumZoomScale:3.0];
//    }
//    else
//    {
        [_scrollView setMinimumZoomScale:1.0];
        [_scrollView setMaximumZoomScale:1.0];
    }
}

- (void)reloadData
{
    if (_datasource==nil) {
        return;
    }
    _totalPages = [_datasource numberOfPagesScrollView:self];
    if (_totalPages == 0) {
        return;
    }
    _pageControl.numberOfPages = _totalPages;
    if (_delegate) {
         [self loadData];
    }
   
}

- (void)loadData
{
    
    _pageControl.currentPage = _curPage;
    if (_delegate&&[_delegate respondsToSelector:@selector(didScrollToPage:)] ) {
        [_delegate didScrollToPage:_curPage];
    }
    
    //从scrollView上移除所有的subview
    NSArray *subViews = [_scrollView subviews];
    if([subViews count] != 0) {
        [subViews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    }
    
    [self getDisplayImagesWithCurpage:_curPage];
    
    for (int i = 0; i < 3; i++) {
        UIView *v = [_curViews objectAtIndex:i];
        v.userInteractionEnabled = YES;
        UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                                    action:@selector(handleTap:)];
        [v addGestureRecognizer:singleTap];

        v.frame = CGRectOffset(v.frame, v.frame.size.width * i, 0);
        [_scrollView addSubview:v];
    }
    
    [_scrollView setContentOffset:CGPointMake(_scrollView.frame.size.width, 0)];
    
    //if (_totalPages > 1)
    {
        [self performSelector:@selector(switchFocusImageItems) withObject:nil afterDelay:SWITCH_FOCUS_PICTURE_INTERVAL];
    }
    
}

- (void)getDisplayImagesWithCurpage:(int)page {
    
    int pre = [self validPageValue:_curPage-1];
    int last = [self validPageValue:_curPage+1];
    
    if (!_curViews) {
        _curViews = [[NSMutableArray alloc] init];
    }
    
    [_curViews removeAllObjects];
    
    if (_datasource==nil) return;

    [_curViews addObject:[_datasource scrollView:self PageAtIndex:pre]];
    [_curViews addObject:[_datasource scrollView:self PageAtIndex:page]];
    [_curViews addObject:[_datasource scrollView:self PageAtIndex:last]];
}

- (int)validPageValue:(NSInteger)value {
    
    if(value == -1) value = _totalPages - 1;
    if(value == _totalPages) value = 0;
    
    return value;
    
}

- (void)handleTap:(UITapGestureRecognizer *)tap {
    
    if ([_delegate respondsToSelector:@selector(didClickPage:atIndex:)]) {
        [_delegate didClickPage:self atIndex:_curPage];
    }
    
}

- (void)setViewContent:(UIView *)view atIndex:(NSInteger)index
{
    if (index == _curPage) {
        [_curViews replaceObjectAtIndex:1 withObject:view];
        for (int i = 0; i < 3; i++) {
            UIView *v = [_curViews objectAtIndex:i];
            v.userInteractionEnabled = YES;
            UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                                                        action:@selector(handleTap:)];
            [v addGestureRecognizer:singleTap];

            v.frame = CGRectOffset(v.frame, v.frame.size.width * i, 0);
            [_scrollView addSubview:v];
        }
    }
}

#pragma mark - 自动跳到下一页
- (void)switchFocusImageItems
{
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(switchFocusImageItems) object:nil];
    
    CGFloat targetX = _scrollView.contentOffset.x + _scrollView.frame.size.width;
    [self moveToTargetPosition:targetX];
    
    [self performSelector:@selector(switchFocusImageItems) withObject:nil afterDelay:SWITCH_FOCUS_PICTURE_INTERVAL];
}

- (void)moveToTargetPosition:(CGFloat)targetX
{
   // NSLog(@"moveToTargetPosition : %f" , targetX);
    if (targetX >= _scrollView.contentSize.width) {
        targetX = 0.0;
    }
    
    if (_totalPages > 1)
    {
        [_scrollView setContentOffset:CGPointMake(targetX, 0) animated:YES] ;
    }
    else
    {
        [_scrollView setContentOffset:CGPointMake(targetX, 0) animated:NO] ;
    }
    
    _pageControl.currentPage = _curPage;
}

#pragma mark - UIScrollViewDelegate
- (void)scrollViewDidScroll:(UIScrollView *)aScrollView {
    int x = aScrollView.contentOffset.x;
    
    //往下翻一张
    if(x >= (2*self.frame.size.width)) {
        _curPage = [self validPageValue:_curPage+1];
        if (_delegate) {
            [self loadData];
        }
        
    }
    
    //往上翻
    if(x <= 0) {
        _curPage = [self validPageValue:_curPage-1];
        if (_delegate) {
            [self loadData];
        }
    }
}

- (void)scrollViewDidEndDecelerating:(UIScrollView *)aScrollView {
    [NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(switchFocusImageItems) object:nil];
    [_scrollView setContentOffset:CGPointMake(_scrollView.frame.size.width, 0) animated:YES];
    [self performSelector:@selector(switchFocusImageItems) withObject:nil afterDelay:SWITCH_FOCUS_PICTURE_INTERVAL];
    
}

//- (UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
//{
//    return [_curViews objectAtIndex:_curPage];
//}

@end
