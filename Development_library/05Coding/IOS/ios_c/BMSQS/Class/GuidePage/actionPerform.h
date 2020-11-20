//
//  actionPerform.h
//  IcardEnglish
//
//  Created by djx on 12-8-30.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PageScrollView.h"

@interface actionPerform : UIViewControllerEx<PageScrollViewDelegate>
{
	NSMutableArray *pages;
    PageScrollView *scrollView;
    UIPageControl* pageControl ;
}
@property(nonatomic,copy)void(^loadingViewControllerFinish)();

- (void)closeSelf;
@end
