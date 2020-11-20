//
//  BMSQ_seleTimeView.h
//  BMSQC
//
//  Created by liuqin on 16/3/31.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_seleTimeViewDelegate <NSObject>

-(void)seleWeek:(NSString *)weekStr startTime:(NSString *)startTime endTime:(NSString *)endTime isSubmit:(BOOL)isSubmit;


@end



@interface BMSQ_seleTimeView : UIView
@property (nonatomic, weak)id<BMSQ_seleTimeViewDelegate>delegate;

@end
