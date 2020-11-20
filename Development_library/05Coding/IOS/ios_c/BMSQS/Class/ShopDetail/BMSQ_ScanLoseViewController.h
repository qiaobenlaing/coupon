//
//  BMSQ_ScanLoseViewController.h
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@protocol scanLoseDelegate <NSObject>

- (void)btnScanLReturn;
- (void)btnScanLAgain;

@end

@interface BMSQ_ScanLoseViewController : UIViewControllerEx

@property (nonatomic)id<scanLoseDelegate>ScanLdelegate;

@end
