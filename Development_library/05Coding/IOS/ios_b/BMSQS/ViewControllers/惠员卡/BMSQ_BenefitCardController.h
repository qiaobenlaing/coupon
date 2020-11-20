//
//  BMSQ_BenefitCardController.h
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "BaseViewController.h"
#import "PNBarChart.h"

@interface BMSQ_BenefitCardController : BaseViewController
{
    __weak IBOutlet UITableView *_tableView;
}
@property(nonatomic,strong)NSMutableDictionary *shopCountDic;
@property(nonatomic,strong)NSMutableArray *cardDataArray;
@property(nonatomic,strong)NSMutableArray *memberMonthArray;
@property(nonatomic,strong)NSMutableArray *memberMonthDataArray;
@property(nonatomic,strong)NSMutableArray *memberMoneyDataArray;
@property(nonatomic,strong)NSMutableArray *memberMoneyArray;

@property(nonatomic,strong)PNBarChart *barChart;
- (void)reloadCardData;
@end
