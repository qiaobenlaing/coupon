//
//  BMSQ_ScanSuccessViewController.h
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@protocol scanSuccessDelegate <NSObject>

- (void)btnScanSuccessReturn;
- (void)btnScanSuccessCon;

@end

@interface BMSQ_ScanSuccessViewController : UIViewControllerEx

@property(nonatomic)id<scanSuccessDelegate>scansDelegate;

@end
