//
//  BMSQ_HomeActivityView.h
//  BMSQC
//
//  Created by gh on 15/11/21.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HomeActivityViewDelegate <NSObject>

- (void)clickHomeActivity:(id)object;

@end


@interface BMSQ_HomeActivityView : UIView

@property(nonatomic)id<HomeActivityViewDelegate> delegate;
- (void)setHomeActivityView:(NSDictionary *)dic;


@end
