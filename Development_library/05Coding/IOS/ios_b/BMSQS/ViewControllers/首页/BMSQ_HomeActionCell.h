//
//  BMSQ_HomeActionCell.h
//  BMSQS
//
//  Created by djx on 15/7/5.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BMSQ_HomeActionCellDelegate <NSObject>

-(void)gotoOrderVC;
- (void)gotoCountVC;

@end



@interface BMSQ_HomeActionCell : UITableViewCell


@property (nonatomic, weak)id<BMSQ_HomeActionCellDelegate>homeDelegate;

- (void)setBottomView;
- (void)removeBottomView;

@end
