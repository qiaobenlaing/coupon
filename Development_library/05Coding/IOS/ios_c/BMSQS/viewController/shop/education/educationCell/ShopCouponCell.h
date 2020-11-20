//
//  ShopCouponCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/23.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol ShopCouponCellDelegate <NSObject>

-(void)shareCoupon:(NSDictionary *)dic;
-(void)receivceCoupon:(NSDictionary *)dic;
-(void)useCoupon:(NSDictionary *)dic;
@end


@interface ShopCouponCell : UITableViewCell

@property (nonatomic, strong)NSDictionary *couponDic;
@property (nonatomic, assign)BOOL isUserCoupon;//yes 立即使用 no立即使用
@property (nonatomic, weak)id<ShopCouponCellDelegate>couponDelegate;

-(void)setShopCouponCell:(NSDictionary *)couponDic heigh:(CGFloat)heigh status:(BOOL )isUserCoupon;
@end










@interface couponRemark : UIView
@property (nonatomic, strong)NSString *remarkstr;


-(id)initWithFrame:(CGRect)frame remark:(NSString *)str ;
@end