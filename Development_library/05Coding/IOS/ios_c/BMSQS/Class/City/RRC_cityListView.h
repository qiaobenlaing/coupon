//
//  SDZX_cityListView.h
//  SDBooking
//
//  Created by djx on 14-3-12.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SDZX_cityObject.h"
@protocol cityListDelegate <NSObject>

- (void)selectCity:(SDZX_cityObject*)cityObj;

@end

@interface RRC_cityListView : UIView<UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate,UIScrollViewDelegate>
{
    UITableView* m_tableView;
    NSMutableArray* m_dataSource;
    NSMutableArray* m_searchDataSource;
    UISearchBar* mySearchBar;
    NSMutableDictionary* m_cityDicDataSource;
    NSArray* m_cityKeys;
}

@property(nonatomic,assign)NSObject<cityListDelegate>* delegate;
- (void)reloadData;
@end
