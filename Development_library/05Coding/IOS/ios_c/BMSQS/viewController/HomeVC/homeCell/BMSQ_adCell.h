//
//  BMSQ_adCell.h
//  BMSQC
//
//  Created by liuqin on 15/12/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BMSQ_adCellDelegate <NSObject>

-(void)clickAD:(int)tag;

@end

@interface BMSQ_adCell : UITableViewCell
-(void)setHomeAdCell:(NSArray *)subList height:(float)height;
@property (nonatomic,weak)id<BMSQ_adCellDelegate>adDelegate;

@end
