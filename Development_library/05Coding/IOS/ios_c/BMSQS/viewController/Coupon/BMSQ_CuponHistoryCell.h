//
//  BMSQ_CuponHistoryCell.h
//  BMSQC
//
//  Created by djx on 15/7/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_CuponHistoryCell : UITableViewCell
{
    UIImageView* iv_shopLogo;
    UILabel* lb_shopName;
    UILabel* lb_useDes;
    UILabel* lb_endTime;
    UIImageView* iv_topBack;
    UIImageView* iv_cuponStatus; //优惠券状态
}

@property (nonatomic, strong)NSDictionary *dic;
- (void)setCellValue:(NSDictionary*)dicCupon indexPath:(NSIndexPath*)path cuponType:(NSString*)type;

@end
