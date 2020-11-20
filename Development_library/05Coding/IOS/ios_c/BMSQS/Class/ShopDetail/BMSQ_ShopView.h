//
//  BMSQ_ShopView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BMSQ_circleModel.h"
#import "BMSQ_ShopDetailView.h"



@protocol BMSQ_ShopViewDelegate <NSObject>

-(void)changeValue:(NSArray *)array;
- (void)transferValue:(int)value moduleStr:(NSString *)moduleStr queryName:(NSString *)queryStr;
@end


@interface BMSQ_ShopView : UIView
@property (nonatomic, strong) NSMutableArray * m_classifyAry;// 分类数组
@property (nonatomic, weak)id<BMSQ_ShopViewDelegate>leftDelegate;

- (void)setForDicObject:(NSDictionary *)dicObject;

@end
