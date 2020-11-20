//
//  plistFileOperation.h
//  IcardEnglish
//
//  Created by djx on 12-7-2.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface PageScrollView : UIView<UIScrollViewDelegate> {
    UIScrollView *scroll;
    UIPageControl *pageControl;
    
    CGRect _pageRegion,_controlRegin;
    NSMutableArray *_pages;
    id _delegate;
    BOOL _showsPageControl;
    int _zeroPage;
	 CGFloat offset;
}

-(void)layoutViews;
-(void)notifyPageChange;
-(void)layoutScroller;
-(UIView*)getCurrentPageView;

@property(nonatomic,assign,getter = getPages) NSMutableArray *pages;
@property(nonatomic,assign,getter =getCurrentPage) int currentPage;
@property(nonatomic,assign,getter = getDelegate) id delegate;
@property(nonatomic,assign,getter = getShowsPageControl) BOOL showsPageControl;
@property(nonatomic)CGRect pageRegion;
@property(nonatomic)CGRect controlRegin;

-(id)initWithFrame:(CGRect)frame pageRect:(CGRect)pageRegionRect controlRect:(CGRect)controlReginRect;
@end

@protocol PageScrollViewDelegate <NSObject>

@optional

-(void)pageScrollViewDidChangeCurrentPage:
    (PageScrollView *)pagescrollView currentPage:(int)currentPage;

@end