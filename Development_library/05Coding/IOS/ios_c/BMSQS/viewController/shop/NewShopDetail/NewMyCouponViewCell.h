//
//  NewMyCouponViewCell.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/25.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"
#import "CouponImage.h"
#import "BMSQ_BuyCouponViewController.h"
@protocol NewMyCouponViewCellDelegate <NSObject>

- (void)grabBuyNewCoupon:(NSDictionary *)dicCoupon;// 购买优惠券
- (void)grabNewCupon:(NSDictionary*)dicCupon currenRow:(int)row; //领取优惠券
- (void)grabUserCoupon:(NSDictionary *)dicCupon; // 使用优惠券
- (void)btnNewShareClick:(NSDictionary*)dicShare; // 分享券

@end



@interface NewMyCouponViewCell : UITableViewCell
{
    
    UILabel* lb_useDes;
    UILabel* lb_endTime;
    UIButton* btn_share;
    UIButton* btn_expansion;
    UILabel* lb_price;
//    UILabel* lb_status;
//    UIImageView* iv_jtDown;
    UIImageView* iv_topBack;
//    UIImageView* iv_rightBack;
//    UIImageView* iv_bottomBack;
    UILabel *lb_couponTime;
//    UILabel *lb_state;
    UILabel *lb_stateLabel;
//    UIButtonEx* btn_useRule;// 使用规则
    
    
}


@property (nonatomic, strong)NSString *couponMessage;
@property (nonatomic, assign)int currentRow;
@property (nonatomic, strong)NSString *type;
@property(nonatomic,assign)id<NewMyCouponViewCellDelegate>coupondelegate;
@property(nonatomic, strong)NSDictionary *couponDic;
- (void)setCellValue:(NSDictionary*)dicCupon row:(int)row;
- (void)setCellExpansion:(BOOL)isExpansion;

- (void)setCellCouponDic:(NSDictionary *)couponDic row:(int)row num:(int)num;




@end
