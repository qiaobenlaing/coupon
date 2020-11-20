//
//  LEKnowChartVC.h
//  气泡
//
//  Created by zzy on 14-5-13.
//  Copyright (c) 2014年 zzy. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
typedef void (^PopRefreshData)(NSString *str);
@interface BMSQ_MemberChartViewController : UIViewControllerEx
{
    NSString *_unRead;
    UIRefreshControl *_refresh;
}
@property(nonatomic,strong)NSDictionary *model;
@property(nonatomic,assign)BOOL isSendMsg;
@property(nonatomic,strong)NSString *userID;/*会员id*/
@property(nonatomic,strong)NSMutableArray *dataArray;
@property(nonatomic,strong)NSString *boptype;
@property (nonatomic, copy)PopRefreshData pop;
@property(nonatomic,assign)CGRect keyboardRect;
@property(nonatomic,assign)CGRect tableViewRect;

@property(nonatomic,strong)NSString *titleNav;

- (void)popRefreshData:(PopRefreshData)block;
- (void)httpContentRequest;



@end
