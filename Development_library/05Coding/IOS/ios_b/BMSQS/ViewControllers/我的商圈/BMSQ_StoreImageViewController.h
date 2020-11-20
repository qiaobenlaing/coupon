//
//  BMSQ_StoreImageViewController.h
//  BMSQS
//
//  Created by lxm on 15/8/8.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BaseViewController.h"
#import "LEFlowView.h"
@interface BMSQ_StoreImageViewController : BaseViewController<PagedFlowViewDelegate,PagedFlowViewDataSource>
{
    LEFlowView *aFlowView;
    LEFlowView *bFlowView;
    __weak IBOutlet UITableView *_tableView;
}
@property (nonatomic,strong)NSDictionary *shopAllMsg;
@property(nonatomic,strong)NSMutableArray *couponArray;
@end
