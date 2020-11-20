//
//  BMSQ_CouponNbrCell.h
//  BMSQC
//
//  Created by liuqin on 15/12/22.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_CouponNbrCellDelegate <NSObject>

-(void)clickCouponType:(int)i;
-(void)applicationCoupon:(NSDictionary *)dic;


@end

@interface BMSQ_CouponNbrCell : UITableViewCell

@property(nonatomic,assign)int indexpath;
@property(nonatomic,strong)NSDictionary *couponDic;
@property(nonatomic,assign)BOOL isAct; //yes 是

@property(nonatomic,weak)id<BMSQ_CouponNbrCellDelegate>NbrDelegate;
-(void)creatNbrCell:(NSDictionary *)couponDic isShow:(BOOL)isShow;


@end
