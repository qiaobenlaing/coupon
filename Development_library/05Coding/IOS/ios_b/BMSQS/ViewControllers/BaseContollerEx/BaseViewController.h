//
//  BaseViewController.h
//  BMSQS
//
//  Created by lxm on 15/7/23.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIColor+Tools.h"
#import "SVProgressHUD.h"

@interface BaseViewController : UIViewController
{
    UILabel *_emptyLabel;
    BOOL isShownEmpty;
}
@property(nonatomic,strong)AFJSONRPCClient* jsonPrcClient;
- (void)initJsonPrcClient:(NSString *)requestType;
- (void)customBackBtn;
- (void)showEmptyView:(NSString *)str;
- (void)hideEmpthView;
- (void)btnBackClicked;
@end
