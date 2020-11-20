//
//  BMSQ_CuponController.h
//  BMSQC
//
//  Created by djx on 15/7/27.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "PullingRefreshTableView.h"
#import "BMSQ_CuponActivityCell.h"
#import "BMSQ_CuponCell.h"

@interface BMSQ_CuponActivityController : UIViewControllerEx<UISearchBarDelegate,UITableViewDataSource,UITableViewDelegate,PullingRefreshTableViewDelegate,cuponCellDelegate>

@property(nonatomic)int insertPage; //4限时购，6体验馆


@end
