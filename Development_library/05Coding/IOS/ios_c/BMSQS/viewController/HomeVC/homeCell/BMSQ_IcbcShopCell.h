//
//  BMSQ_IcbcShopCell.h
//  BMSQC
//
//  Created by dongzhonghui on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_IcbcShopCell : UITableViewCell
+(CGFloat)cellHeight:(NSDictionary *)dic;
-(void)setCellValue:(NSDictionary *)dictionary;
@end
