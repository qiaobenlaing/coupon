//
//  BMSQ_HomeController.h
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "ZBarSDK.h"
#import "BMSQ_SelectShopViewController.h"
#import "GatheringListViewCell.h"
@interface BMSQ_HomeController : UIViewControllerEx<UITableViewDataSource,UITableViewDelegate, ZBarReaderDelegate>


@end
