//
//  BMSQ_MemberCardListViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "EGORefreshVC.h"
#import "PopoverView.h"
#import "BaseViewController.h"
@interface BMSQ_MemberCardListViewController : BaseViewController<PopoverViewDelegate>
{
    NSMutableArray *_dataSource;
    NSInteger _currentIndex;
    NSInteger _left_count;
    NSString *_orderType;
    IBOutlet __weak UILabel *_countLabel;
}
@property(nonatomic,weak)IBOutlet UITableView *tableView;
@property(nonatomic,strong)NSString *cardCode;
@property(nonatomic,strong)NSString *cardTitle;
@end
