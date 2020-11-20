//
//  Coupon_ReceiveTableViewCell.h
//  BMSQS
//
//  Created by liuqin on 15/10/13.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, CouponCellType) {
    Coupon_receiveCell,
    Coupon_FindCell ,
    Coupon_CareCell ,
    Coupon_shopCell ,
    Coupon_chatCell,
};

@interface Coupon_ReceiveTableViewCell : UITableViewCell



-(void)setReceiveCell:(NSDictionary *)receiveDic type:(CouponCellType *)type;
@end
