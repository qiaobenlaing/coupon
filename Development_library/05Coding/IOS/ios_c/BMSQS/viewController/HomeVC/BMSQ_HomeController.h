//
//  BMSQ_HomeController.h
//  BMSQS
//
//  Created by djx on 15/7/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "ZBarSDK.h"
#import "QRCodeGenerator.h"

@interface BMSQ_HomeController : UIViewControllerEx<UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate, ZBarReaderDelegate, UIAlertViewDelegate>


@property (nonatomic,strong)NSArray *m_dataSourceCity; //惠圈开放城市

@end
