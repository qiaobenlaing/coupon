//
//  BMSQ_startView.h
//  BMSQC
//
//  Created by liuqin on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CWStarRateView.h"


@interface BMSQ_startView : UIView


@end


@interface StartView : UIView<CWStarRateViewDelegate>

- (instancetype)initWithFrame:(CGRect)frame numberOfStars:(NSInteger)numberOfStars  seleImage:(NSString *)seleImagestr norImage:(NSString *)norImageStr titel:(NSString *)titel tag:(int)tag;



@end