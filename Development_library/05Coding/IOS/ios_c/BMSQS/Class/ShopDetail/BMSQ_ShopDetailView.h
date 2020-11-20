//
//  BMSQ_ShopDetailView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 15/11/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
typedef void(^Success)(NSArray *);

@protocol  BMSQ_ShopDetailViewDelegate <NSObject>

- (void)detailViewTheValue:(int)value moduleValue:(NSNumber *)moduleValue valueNumber:(NSNumber *)valueNumber;

@end

@interface BMSQ_ShopDetailView : UIView

@property (nonatomic, strong) NSMutableArray * detailAry;

@property (nonatomic, weak) id <BMSQ_ShopDetailViewDelegate> delegate;
- (void)setForArray:(NSArray *)forArray success:(Success)success;



@end
