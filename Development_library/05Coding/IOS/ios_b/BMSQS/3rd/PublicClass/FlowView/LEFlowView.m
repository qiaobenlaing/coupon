//
//  LPHuiyuanyouhuiFlowView.m
//  leju_platform
//
//  Created by tiny wang on 13-3-20.
//  Copyright (c) 2013年 Leju. All rights reserved.
//

#import "LEFlowView.h"

@interface LEFlowView (){
    Boolean _needStopTime;
    Boolean _needStopTimeOnce;
}
@end

@implementation LEFlowView
@synthesize dataSource = _dataSource;
@synthesize delegate = _delegate;
@synthesize pageControl;
@synthesize minimumPageAlpha = _minimumPageAlpha;
@synthesize minimumPageScale = _minimumPageScale;
@synthesize currentPageIndex = _currentPageIndex;
@synthesize cacheCount = _cacheCount;
@synthesize needsReload = _needsReload;
@synthesize cells = _cells;

////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark Private Methods

- (void)initialize{
    self.clipsToBounds = YES;
    
    _needsReload = YES;
    _pageSize = self.bounds.size;
    _pageCount = 0;
    _currentPageIndex = 0;
    
    _minimumPageAlpha = 1.0;
    _minimumPageScale = 1.0;
    _cacheCount = 1;
    _visibleRange = NSMakeRange(0, 0);
    
    //reusableCells 源代码中有bug，暂时废弃不用（同时也不值得）
    //_reusableCells = [[NSMutableArray alloc] initWithCapacity:0];
    _cells = [[NSMutableArray alloc] initWithCapacity:0];
    
    _scrollView = [[UIScrollView alloc] initWithFrame:self.bounds];
    _scrollView.delegate = self;
    _scrollView.pagingEnabled = YES;
    _scrollView.clipsToBounds = NO;
    _scrollView.showsHorizontalScrollIndicator = NO;
    _scrollView.showsVerticalScrollIndicator = NO;
    _scrollView.alwaysBounceHorizontal = YES;

    /*由于UIScrollView在滚动之后会调用自己的layoutSubviews以及父View的layoutSubviews
    这里为了避免scrollview滚动带来自己layoutSubviews的调用,所以给scrollView加了一层父View
     */
    UIView *superViewOfScrollView = [[UIView alloc] initWithFrame:self.bounds];
    [superViewOfScrollView setAutoresizingMask:UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight];
    [superViewOfScrollView setBackgroundColor:[UIColor clearColor]];
    [superViewOfScrollView addSubview:_scrollView];
    [self addSubview:superViewOfScrollView];
    
    [superViewOfScrollView release];
}

//是否需要添加手势来响应点击事件，默认是不响应的
-(void)addClickViewAbility{
    UITapGestureRecognizer * tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickScrollView:)];
    [_scrollView addGestureRecognizer:tap];
    [tap release];
}

-(void)setTimer:(CGFloat)timeInterval
{
    _timeInterval = timeInterval;
    if (timeInterval<=0) {
        return;
    }
    if(!_timer){
        _timer = [NSTimer scheduledTimerWithTimeInterval: timeInterval
                                                 target: self
                                               selector: @selector(handleTimer:)
                                               userInfo: nil
                                                repeats: YES];
    }
    _needStopTime = NO;
}

-(void)stopTimer:(BOOL)immediately
{
    if (immediately && _timer) {
        [_timer invalidate];
        _timer = nil;
    }
    else{
        _needStopTime = YES;
    }
}

- (void) handleTimer: (NSTimer *) timer
{
    if (_needStopTimeOnce) {
        _needStopTimeOnce = NO;
        return;
    }
    if (_needStopTime && _timer && [_timer isValid]) {
        [_timer invalidate];
        _timer = nil;
        return;
    }
    
    if (_currentPageIndex == 0) {
        _turnDirection = YES;
    }
    if (_currentPageIndex == _pageCount-1) {
        _turnDirection = NO;
    }
    
    NSInteger nextPage = _turnDirection?_currentPageIndex+1:_currentPageIndex-1;
    [self scrollToPage:nextPage animated:YES];
    //[self setHiddenAnimatedForView];
}


//特殊动画效果，现在隐藏第一张，在展示第二张。效果不好，弃用
-(void)setHiddenAnimatedForView
{
    UIView* view = [_cells objectAtIndex:_currentPageIndex];
    if (_timeInterval>0) {
        [UIView animateWithDuration:_timeInterval/5 animations:^{
            view.alpha = 0.2;
        }
        completion:^(BOOL finished) {
            if (_currentPageIndex+1 <_pageCount){
                [self scrollToPage:_currentPageIndex+1 animated:NO];
            }
            else{
                [self scrollToPage:0 animated:NO];
            }
            [self setShowAnimatedForView];
            view.alpha = 1.0;
        }];
    }
}

-(void)setShowAnimatedForView
{
    UIView* view = [_cells objectAtIndex:_currentPageIndex];
    if (_timeInterval>0) {
        view.alpha = 0.2;
        [UIView animateWithDuration:_timeInterval/5 animations:^{
            view.alpha = 1.0;
        }
        completion:^(BOOL finished) {
        }];
    }
}


- (void)dealloc
{
    _scrollView.delegate = nil;
    [_scrollView removeFromSuperview];
    SAFELY_RELEASE(_scrollView);
    
    SAFELY_RELEASE(_cells);
    
    if (_timer) {
        [_timer invalidate];
        _timer = nil;
    }
    [super dealloc];
}

- (void)removeCellAtIndex:(NSInteger)index{
    UIView *cell = [_cells objectAtIndex:index];
    if ((NSObject *)cell == [NSNull null]) {
        return;
    }
    
    if (cell.superview) {
        [cell removeFromSuperview];
    }
    
    [_cells replaceObjectAtIndex:index withObject:[NSNull null]];
}

- (void)refreshVisibleCellAppearance{
    
    if (_minimumPageAlpha == 1.0 && _minimumPageScale == 1.0) {
        return;//无需更新
    }

    CGFloat offset = _scrollView.contentOffset.x;
    
    for (int i = _visibleRange.location; i < _visibleRange.location + _visibleRange.length; i++) {
        if (i<0 || i>= [_cells count]) {
            return;
        }
        UIView *cell = [_cells objectAtIndex:i];
        CGFloat origin = cell.frame.origin.x;
        CGFloat delta = fabs(origin - offset);
                
        CGRect originCellFrame = CGRectMake(_pageSize.width * i, 0, _pageSize.width, _pageSize.height);//如果没有缩小效果的情况下的本该的Frame
                
        [UIView beginAnimations:@"CellAnimation" context:nil];
        if (delta < _pageSize.width) {
            [cell setAlpha:1 - (delta / _pageSize.width) * (1 - _minimumPageAlpha) ];
            
            CGFloat inset = (_pageSize.width * (1 - _minimumPageScale)) * (delta / _pageSize.width)/2.0;
            cell.frame = UIEdgeInsetsInsetRect(originCellFrame, UIEdgeInsetsMake(inset, inset, inset, inset));
        }
        else {
            [cell setAlpha: _minimumPageAlpha];
            CGFloat inset = _pageSize.width * (1 - _minimumPageScale) / 2.0 ;
            cell.frame = UIEdgeInsetsInsetRect(originCellFrame, UIEdgeInsetsMake(inset, inset, inset, inset));
            }
            [UIView commitAnimations];
        }
}

- (void)setPageAtIndex:(NSInteger)pageIndex{
    NSParameterAssert(pageIndex >= 0 && pageIndex < [_cells count]);
    
    UIView *cell = [_cells objectAtIndex:pageIndex];
    
    if ((NSObject *)cell == [NSNull null]) {
        cell = [[_dataSource flowView:self cellForPageAtIndex:pageIndex] retain];
        NSAssert(cell!=nil, @"datasource must not return nil");
        [_cells replaceObjectAtIndex:pageIndex withObject:cell];
        
        
       cell.frame = CGRectMake(_pageSize.width * pageIndex, 0, _pageSize.width, _pageSize.height);
        
        if (!cell.superview) {
            [_scrollView addSubview:cell];
        }
    }
}


//根据当前scrollView的offset设置cell
- (void)setPagesAtContentOffset:(CGPoint)offset{
   
    if (!_pageCount ||_pageCount==0) {
        return;
    }
    int imageLength = lroundf(self.frame.size.width/_pageSize.width) ;
    
    if (imageLength%2 == 0) {
        imageLength = imageLength+1;
    }
    
    //正中间正在被展示的那一个
    int showIndex = roundf(offset.x/_pageSize.width);
    
    int startIndex = 0;
    startIndex = MAX(showIndex - (imageLength-1)/2 - _cacheCount,0);
    
    int endIndex = MIN(showIndex + (imageLength-1)/2 +_cacheCount,[_cells count] - 1);
    
    _visibleRange.location = MAX(showIndex - (imageLength-1)/2-1,0);
    _visibleRange.length = MIN(showIndex + (imageLength-1)/2+1,[_cells count] - 1) - _visibleRange.location + 1;
    
    for (int i = startIndex; i <= endIndex; i++) {
        [self setPageAtIndex:i];
    }
    
    for (int i = 0; i < startIndex; i ++) {
        [self removeCellAtIndex:i];
    }
    
    for (int i = endIndex + 1; i < [_cells count]; i ++) {
        [self removeCellAtIndex:i];
    }
    
    _currentPageIndex = showIndex;
    
    if ([_delegate respondsToSelector:@selector(didScrollToPage:inFlowView:)]) {
        [_delegate didScrollToPage:showIndex inFlowView:self];
    }

}




////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark Override Methods

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self)
    {
        [self initialize];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self)
    {
        [self initialize];
    }
    return self;
}

- (void)layoutSubviews{
    [super layoutSubviews];
    
    if (_needsReload) {
        //如果需要重新加载数据，则需要清空相关数据全部重新加载
        
        if (_pageCount&&_cells) {
            for (int i = 0; i < _pageCount; i ++) {
                [self removeCellAtIndex:i];
            }
        }
        
        
        //重置pageCount
        if (_dataSource && [_dataSource respondsToSelector:@selector(numberOfPagesInFlowView:)]) {
            _pageCount = [_dataSource numberOfPagesInFlowView:self];
            
            if (pageControl && [pageControl respondsToSelector:@selector(setNumberOfPages:)]) {
                [pageControl setNumberOfPages:_pageCount];
            }
        }
        
        //重置pageWidth
        if (_delegate && [_delegate respondsToSelector:@selector(sizeForPageInFlowView:)]) {
            _pageSize = [_delegate sizeForPageInFlowView:self];
        }
        
        //reusableCells 源代码中有bug，暂时废弃不用（同时也不值得）
        //[_reusableCells removeAllObjects];
        
        _visibleRange = NSMakeRange(0, 0);
        
        //填充cells数组
        [_cells removeAllObjects];
        if (_pageCount == 0 ||!_pageCount) {
            return;
        }
        for (NSInteger index=0; index<_pageCount; index++)
        {
            [_cells addObject:[NSNull null]];
        }
        
        // 重置_scrollView的contentSize
        
        [_scrollView setContentOffset:CGPointMake(_pageSize.width * 0, 0) animated:NO];
        _currentPageIndex = 0;
        [pageControl setCurrentPage:0];

        _scrollView.frame = CGRectMake(0, 0, _pageSize.width, _pageSize.height);
        _scrollView.contentSize = CGSizeMake(_pageSize.width * _pageCount,_pageSize.height);
        CGPoint theCenter = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMidY(self.bounds));
        _scrollView.center = theCenter;
    }


    [self setPagesAtContentOffset:_scrollView.contentOffset];//根据当前scrollView的offset设置cell
    
    [self refreshVisibleCellAppearance];//更新各个可见Cell的显示外貌
    
    //更行完成
    if (_delegate && [_delegate respondsToSelector:@selector(didFinishInitialize:)]) {
        [_delegate didFinishInitialize:self];  
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark PagedFlowView API

- (void)reloadData
{
    _needsReload = YES;
    
    [self setNeedsLayout];
}

- (void)scrollToPage:(NSUInteger)pageNumber animated:(Boolean)animated {
    if (pageNumber < _pageCount) {
        [_scrollView setContentOffset:CGPointMake(_pageSize.width * pageNumber, 0) animated:animated];
        [self setPagesAtContentOffset:_scrollView.contentOffset];
        [self refreshVisibleCellAppearance];
        _currentPageIndex = pageNumber;
        [pageControl setCurrentPage:pageNumber];
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark hitTest

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    if ([self pointInside:point withEvent:event]) {
        CGPoint newPoint = CGPointZero;
        newPoint.x = point.x - _scrollView.frame.origin.x + _scrollView.contentOffset.x;
        newPoint.y = point.y - _scrollView.frame.origin.y + _scrollView.contentOffset.y;
        if ([_scrollView pointInside:newPoint withEvent:event]) {
            return [_scrollView hitTest:newPoint withEvent:event];
        }
        
        return _scrollView;
    }
    
    return nil;
}


////////////////////////////////////////////////////////////////////////////////////////////////////
#pragma mark -
#pragma mark UIScrollView Delegate
-(void)scrollViewWillBeginDragging:(UIScrollView *)scrollView
{
    _needStopTimeOnce = YES;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView{
    [self setPagesAtContentOffset:scrollView.contentOffset];
    [self refreshVisibleCellAppearance];
}


- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    //如果有PageControl，计算出当前页码，并对pageControl进行更新
    
    NSInteger pageIndex;
    
    pageIndex = floorf(_scrollView.contentOffset.x / _pageSize.width);    
    if (pageControl && [pageControl respondsToSelector:@selector(setCurrentPage:)]) {
        [pageControl setCurrentPage:pageIndex];
    }
    
    /*if ([_delegate respondsToSelector:@selector(didScrollToPage:inFlowView:)] && _currentPageIndex != pageIndex) {
        [_delegate didScrollToPage:pageIndex inFlowView:self];
    }*/
}

- (void)clickScrollView:(UITapGestureRecognizer*)gesture {
    [_delegate viewClickedWithIndex:floorf(_scrollView.contentOffset.x / self.frame.size.width)];
}

@end
