//
//  BMSQ_ShopFilterView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMSQ_filterModel.h"
#import "FilterViewCell.h"

@protocol BMSQ_ShopFilterViewDelegate <NSObject>

- (void)filterViewTheValue:(int)value valueNumber:(NSNumber *)valueNumber;

@end

@interface BMSQ_ShopFilterView : UIView

- (void)setForFilterDic:(NSDictionary *)filterDic;
@property (nonatomic, weak) id<BMSQ_ShopFilterViewDelegate>delegate;
@end
