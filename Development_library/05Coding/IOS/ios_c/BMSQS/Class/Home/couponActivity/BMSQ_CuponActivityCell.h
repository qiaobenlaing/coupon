//
//  BMSQ_CuponCell.h
//  BMSQC
//
//  Created by djx on 15/7/27.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"
#import "BMSQ_CuponCell.h"

@interface BMSQ_CuponActivityCell : UITableViewCell
{
    UIImageView* iv_shopLogo;
    UILabel* lb_shopName;
    UILabel* lb_distance;
    UILabel* lb_useDes;
    UILabel* lb_endTime;
    
    UIButton* btn_expansion;
    UILabel* lb_price;
    UIImageView* iv_jtDown;
    UIImageView* iv_topBack;
    UIImageView* iv_bottomBack;
    UILabel* lb_useShop;
    UILabel* lb_phone;
    UILabel* lb_address;
    UILabel* lb_vip;
    UILabel* lb_state;
    UILabel* lb_status;
    UILabel *lb_stateLabel;
    UILabel *lb_couponTime;
    
    UIImageView* iv_rightBack;
    
}

@property (nonatomic,strong)UIButtonEx* btn_grab;

@property (nonatomic, strong)NSString *type;


@property(nonatomic, strong)NSDictionary *dicCupon;
@property(nonatomic, assign)int row;

- (void)setCellValue:(NSDictionary*)dicCupon indexPath:(NSIndexPath*)path;
- (void)setCellExpansion:(BOOL)isExpansion;

@property(nonatomic,assign)NSObject<cuponCellDelegate>* delegate;

@end
