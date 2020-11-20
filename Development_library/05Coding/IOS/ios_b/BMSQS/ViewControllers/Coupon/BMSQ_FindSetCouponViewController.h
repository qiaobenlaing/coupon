//
//  BMSQ_FindSetCouponViewController.h
//  BMSQS
//
//  Created by liuqin on 15/10/16.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_FindSetCouponViewController : UIViewControllerEx

@property (nonatomic, assign)int type;

@property (nonatomic, strong)NSDictionary *mdic;

@property (nonatomic, strong)NSString *couponTypeName; //优惠券名称

@property (nonatomic, strong)NSString *Desctription;

@property (nonatomic, strong)NSString *useDate;
@property (nonatomic, strong)NSString *useTime;

@property (nonatomic, strong)NSString *remark;

@property (nonatomic, strong)NSString *disCount;  //优惠


@property (nonatomic, strong)NSString *totalVolume; //发行张数
@property (nonatomic, strong)NSString *startUsingTime; //开始使用日期
@property (nonatomic, strong)NSString *expireTime; //结束使用日期
@property (nonatomic, strong)NSString *dayStartUsingTime; //每日开始使用时间
@property (nonatomic, strong)NSString *dayEndUsingTime; //每日结束使用时间
@property (nonatomic, strong)NSString *startTakingTime;//开始领取日期
@property (nonatomic, strong)NSString *endTakingTime; //结束领取日期


@property (nonatomic, strong)NSString *isSend;
@property (nonatomic, strong)NSString *sendRequired;
@property (nonatomic, strong)NSString *creatorCode;
@property (nonatomic, strong)NSString *discountPercent;
@property (nonatomic, strong)NSString *insteadPrice;
@property (nonatomic, strong)NSString *availablePrice;
@property (nonatomic, strong)NSString *function;


@property (nonatomic, strong)NSString *limitedNbr;
@property (nonatomic, strong)NSString *nbrPerPerson;
@property (nonatomic, strong)NSString *limitedSendNbr;


@property (nonatomic, strong)NSString *payPrice;

//商店编码	shopCode
//优惠券类型	couponType
//共发多少张	totalVolume
//开始使用日期	startUsingTime
//结束使用日期	expireTime
//每日开始使用时间	dayStartUsingTime
//每日结束使用时间	dayEndUsingTime
//
//优惠券开始领用日期	startTakingTime
//截至领用日期	endTakingTime
//
//是否满就送	isSend  //是否满就送
//每满多少金额送一张优惠券	sendRequired //满就送的金额
//
//优惠券说明	remark  //优惠券说明
//创建者编码 	creatorCode  //用户编码
//每张享受多少折	discountPercent //折扣券需要值 其他传0
//每张减免多少元或者每张面值多少元	insteadPrice
//每满多少元可使用一张	availablePrice //抵扣券需要
//每张可以干什么	function //折扣券抵扣券N元购传空字符串
//每单限用多少张	limitedNbr
//每人限领多少张	nbrPerPerson
//每单最多送多少张     	limitedSendNbr
//
//得到一张优惠券需要多少钱  payPrice


@end
