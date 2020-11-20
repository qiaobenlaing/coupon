//
//  BMSQ_ShopDetailController.h
//  BMSQC
//
//  Created by djx on 15/8/5.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "XLCycleScrollView.h"
#import "SwipeView.h"
//#import "BMSQ_BenefitCell.h"
#import "ZBarSDK.h"
@interface BMSQ_ShopDetailController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate,XLCycleScrollViewDatasource,XLCycleScrollViewDelegate,UISearchBarDelegate,SwipeViewDataSource,SwipeViewDelegate,ZBarReaderDelegate>

@property(nonatomic,strong)NSString* shopCode;
@property (nonatomic, strong)NSDictionary *couponDic;
@property (nonatomic, strong)NSString *shopName;
@property (nonatomic, strong)NSString *shopImage;


@property (nonatomic, assign)BOOL isRefund;
@property(nonatomic, strong)NSString *urlStr;


@end
