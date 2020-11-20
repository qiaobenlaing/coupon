//
//  BMSQ_CardRuleViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"

@interface BMSQ_CardRuleViewController : BaseViewController
{
    __weak IBOutlet UITableView *_tableView;
}
@property(nonatomic,strong)NSArray *cardDataArray;

@property(nonatomic,strong)NSMutableArray *dataSource;
@property(nonatomic,strong)NSMutableDictionary *cellDataSource;
@end
