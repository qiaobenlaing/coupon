//
//  BMSQ_PayDetailSViewController.h
//  BMSQC
//
//  Created by liuqin on 15/8/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_PayDetailSViewController : UIViewControllerEx


//@property (nonatomic, strong)NSDictionary *couponDic;
//@property (nonatomic, strong)NSString *cardDiscount;//工商折扣
//@property (nonatomic, strong)NSString *disMoney; //最高优惠多少钱
//@property(nonatomic, assign)float countMoney;  //消费总额




@property(nonatomic, assign)BOOL isTakeOut;  //堂食，外卖
@property (nonatomic, assign)BOOL isDinning;
@property (nonatomic, strong)NSString *orderCode;
@property (nonatomic, strong)NSString *orderNbr;







@property (nonatomic, strong)NSString *shopCode;
@property (nonatomic, strong)NSString *shopName;
@property(nonatomic, strong)NSString *batchCouponCode;
@property(nonatomic, strong)NSString *consumAmount;   ///////////////////////花费金额
@property (nonatomic, assign)int fromVC;



@property(nonatomic, assign)int type;
@property(nonatomic, assign)BOOL isNeedDiscount;//外卖NO ,其它YES //是否有参与优惠券金额


@end
