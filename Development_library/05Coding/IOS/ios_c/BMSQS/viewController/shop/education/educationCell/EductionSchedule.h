//
//  EductionSchedule.h
//  BMSQC
//
//  Created by liuqin on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol EductionScheduleDelegate <NSObject>

-(void)findSchduleDetail:(NSDictionary *)schDic;

@end





@interface EductionSchedule : UITableViewCell


@property (nonatomic, strong)NSDictionary *schduleDic;
@property (nonatomic, weak)id<EductionScheduleDelegate> schDelegate;

-(void)setSchedule:(NSDictionary *)schDic heigh:(float)heigh;




@end
