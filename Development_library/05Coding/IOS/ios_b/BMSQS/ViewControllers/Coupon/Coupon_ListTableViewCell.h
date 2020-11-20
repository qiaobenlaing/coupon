//
//  Coupon_ListTableViewCell.h
//  BMSQS
//
//  Created by liuqin on 15/10/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol Coupon_ListTableViewCellDelegate <NSObject>

-(void)shareCoupon:(NSDictionary *)dic;
-(void)receiveCoupon:(NSDictionary *)dic;

@end

@interface Coupon_ListTableViewCell : UITableViewCell

@property (nonatomic)BOOL hiddenBtn;
@property (nonatomic, strong)NSDictionary *couponDic;

@property (nonatomic, weak)id<Coupon_ListTableViewCellDelegate>listDelegate;

-(void)setListCoupon:(NSDictionary *)dic;

@end
