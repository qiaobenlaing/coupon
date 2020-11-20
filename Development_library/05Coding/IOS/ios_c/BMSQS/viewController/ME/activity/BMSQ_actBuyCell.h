//
//  BMSQ_actBuyCell.h
//  BMSQC
//
//  Created by liuqin on 16/1/26.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>





@protocol BMSQ_actBuyCellDelegate <NSObject>

-(void)clickCountButton:(int)tag row:(int)row;

@end


@interface BMSQ_actBuyCell : UITableViewCell

@property (nonatomic, assign)int myRow;
@property (nonatomic, weak)id<BMSQ_actBuyCellDelegate>buyDelegate;

-(void)setCell:(NSDictionary *)dic count:(NSString *)count indexRow:(int)row;


@end
