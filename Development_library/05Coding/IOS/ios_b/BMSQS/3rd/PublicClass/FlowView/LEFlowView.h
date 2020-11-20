//
//  LPHuiyuanyouhuiFlowView.h
//  leju_platform
//
//  Created by tiny wang on 13-3-20.
//  Copyright (c) 2013年 Leju. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol PagedFlowViewDataSource;
@protocol PagedFlowViewDelegate;

@interface LEFlowView : UIView<UIScrollViewDelegate>{
    
    UIScrollView        *_scrollView;
    CGSize              _pageSize;
    NSInteger           _pageCount;
    NSInteger           _currentPageIndex;

    NSRange              _visibleRange;

    UIPageControl       *pageControl;
    
    CGFloat _minimumPageAlpha;
    CGFloat _minimumPageScale;
    
    NSTimer *_timer;
    CGFloat _timeInterval;
    
    Boolean _turnDirection;
    
    id <PagedFlowViewDataSource>__unsafe_unretained _dataSource;
    id <PagedFlowViewDelegate> __unsafe_unretained  _delegate;
}

@property(nonatomic,assign)   BOOL needsReload;
@property(strong,nonatomic)   NSMutableArray* cells;
@property(nonatomic,assign)   id <PagedFlowViewDataSource> dataSource;
@property(nonatomic,assign)   id <PagedFlowViewDelegate>   delegate;
@property(nonatomic,retain)    UIPageControl       *pageControl;
@property (nonatomic, assign) CGFloat minimumPageAlpha;
@property (nonatomic, assign) CGFloat minimumPageScale;
@property int cacheCount;
@property (nonatomic, assign) NSInteger currentPageIndex;

- (void)reloadData;
- (void)scrollToPage:(NSUInteger)pageNumber animated:(Boolean)animated;

-(void)setTimer:(CGFloat)TimeInterval;
-(void)stopTimer:(BOOL)immediately;
-(void)addClickViewAbility;
@end


@protocol  PagedFlowViewDelegate<NSObject>

- (CGSize)sizeForPageInFlowView:(LEFlowView *)flowView;

@optional
- (void)didScrollToPage:(NSInteger)pageNumber inFlowView:(LEFlowView *)flowView;
- (void)didFinishInitialize:(LEFlowView *)flowView;
- (void)viewClickedWithIndex:(int)viewIndex;

@end


@protocol PagedFlowViewDataSource <NSObject>
//返回显示View的个数
- (NSInteger)numberOfPagesInFlowView:(LEFlowView *)flowView;
//返回给某列使用的View
- (UIView *)flowView:(LEFlowView *)flowView cellForPageAtIndex:(NSInteger)index;

@end