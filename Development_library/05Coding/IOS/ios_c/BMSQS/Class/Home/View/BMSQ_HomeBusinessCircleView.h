//
//  BMSQ_HomeBusinessCircleView.h
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"


@protocol BusinessCircleDelegate <NSObject>

- (void)clickBusinessView:(id)object;

@end


@interface BMSQ_HomeBusinessCircleView : UIView

@property(nonatomic)id<BusinessCircleDelegate> delegate;
@property (nonatomic,strong)NSDictionary *m_dataSource;

- (void)setBusinessCircleViewUp:(NSDictionary *)dic;


@end
