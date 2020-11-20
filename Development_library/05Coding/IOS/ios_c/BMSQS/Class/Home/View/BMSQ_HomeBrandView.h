//
//  BMSQ_HomeBrandView.h
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"

@protocol BrandViewDelegate <NSObject>

- (void)touchBrandView:(id)object;


@end


@interface BMSQ_HomeBrandView : UIView

@property(nonatomic)id<BrandViewDelegate> delegate;

- (void)setBrandView:(NSDictionary *)dictionary;


@end
