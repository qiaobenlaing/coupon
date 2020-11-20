//
//  ShopLunboView.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BMSQ_adCellDelegate <NSObject>

-(void)clickAD:(int)tag;

@end

@interface ShopLunboView : UIView

-(void)setHomeAdCell:(NSArray *)subList height:(float)height;
@property (nonatomic,weak)id<BMSQ_adCellDelegate>adDelegate;

@end
