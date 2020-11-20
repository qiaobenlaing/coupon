//
//  BMSQ_CuponCell.h
//  BMSQC
//
//  Created by  on 15/7/30.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"

@protocol cuponCellDelegate <NSObject>

- (void)grabBuyCoupon:(NSDictionary *)dicCoupon; //买
- (void)grabCupon:(NSDictionary*)dicCupon currenRow:(int)row;   //抢
- (void)setCellSelect:(BOOL)isSelect indexPath:(int)row dic:(NSDictionary*)dicupon;
- (void)btnShareClick:(NSDictionary*)dicShare;
-(void)clickCell:(NSDictionary *)dic  currenRow:(int)row;

@end

@interface BMSQ_CuponCell : UITableViewCell
{
    
    UIImageView* iv_shopLogo;
    UILabel* lb_shopName;
    UILabel* lb_useDes;
    UILabel* lb_endTime;
    UIButton* btn_share;
//    UIButtonEx* btn_expansion;
    UIButton* btn_expansion;
    UILabel* lb_price;
    UILabel* lb_status;
    UIImageView* iv_jtDown;
    UIImageView* iv_topBack;
    UIImageView* iv_rightBack;
    UIImageView* iv_bottomBack;
    UILabel *lb_couponTime;
    UILabel *lb_state;
    UILabel *lb_stateLabel;
    UIButtonEx* btn_useRule;// 使用规则
    
    UILabel *lb_distance;
    
}


@property (nonatomic, assign)BOOL isGrabvotes;
@property (nonatomic, strong)NSString *couponMessage;

@property (nonatomic, assign)int currentRow;
@property (nonatomic, strong)NSString *type;
@property(nonatomic,assign)NSObject<cuponCellDelegate>* coupondelegate;
@property(nonatomic, strong)NSDictionary *couponDic;
- (void)setCellValue:(NSDictionary*)dicCupon row:(int)row couponStatus:(int)status;
- (void)setCellExpansion:(BOOL)isExpansion;

@end
