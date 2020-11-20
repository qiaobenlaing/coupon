//
//  BMSQ_ShopIndustryView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IndustryViewCell.h"
#import "BMSQ_industryModel.h"

@protocol BMSQ_ShopIndustryViewDelegate <NSObject>

- (void)industryViewTheValue:(int)value valueNumber:(NSNumber *)valueNumber;

@end

@interface BMSQ_ShopIndustryView : UIView


- (void)setForDicIndustry:(NSDictionary *)IndustryDic;
@property (nonatomic, weak)id<BMSQ_ShopIndustryViewDelegate>delegate;

@end
