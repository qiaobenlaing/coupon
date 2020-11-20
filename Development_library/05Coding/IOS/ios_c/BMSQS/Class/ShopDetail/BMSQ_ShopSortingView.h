//
//  BMSQ_ShopIndustryView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMSQ_intelligntModel.h"
#import "SortingViewCell.h"

@protocol BMSQ_ShopSortingViewDelegate <NSObject>

- (void)sortingViewTheValue:(int)value valueNumber:(NSNumber *)valueNumber;

@end

@interface BMSQ_ShopSortingView : UIView

- (void)setForDicSorting:(NSDictionary *)sortingDic;
@property (nonatomic, weak)id<BMSQ_ShopSortingViewDelegate>delegate;

@end
