//
//  BMSQ_shopCouponNTableViewCell.h
//  BMSQC
//
//  Created by liuqin on 15/9/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol BMSQ_shopCouponNTableViewCellDelegate <NSObject>

- (void)setCellSelect:(BOOL)isSelect section:(int)section indexPath:(int)row dic:(NSDictionary*)dicupon;

- (void)useCouponRow:(int)row section:(int)section;

@end


@interface BMSQ_shopCouponNTableViewCell : UITableViewCell


@property(nonatomic, strong)NSDictionary *shopCouponDic;
@property(nonatomic, assign)int row;
@property(nonatomic, assign)int section;
@property(nonatomic, strong)NSString *type;
@property(nonatomic, assign)int height;
@property(nonatomic, assign)int height1;
@property(nonatomic, strong)NSString *buttonStr;


@property(nonatomic, strong)id<BMSQ_shopCouponNTableViewCellDelegate>sdelegate;

- (void)setCellValue:(NSDictionary*)dicCupon row:(int)row;
+(CGFloat )noromlH:(NSDictionary *)dic;
+(CGFloat )seleH:(NSDictionary *)dic;


@end
