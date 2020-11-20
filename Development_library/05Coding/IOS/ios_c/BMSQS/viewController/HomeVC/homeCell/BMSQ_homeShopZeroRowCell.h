//
//  BMSQ_homeShopZeroRowCell.h
//  BMSQC
//
//  Created by liuqin on 16/1/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_homeShopZeroRowCellDelegate <NSObject>

-(void)gotoShopVC;

@end


@interface BMSQ_homeShopZeroRowCell : UITableViewCell



@property(nonatomic, weak)id<BMSQ_homeShopZeroRowCellDelegate> zeroDelegate;
+(CGFloat )cellHeigh:(NSDictionary *)dic;
- (void)setCellValue:(NSDictionary *)dictionary titleDic:(NSDictionary *)titleDic;
@end
