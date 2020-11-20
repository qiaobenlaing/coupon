//
//  BMSQ_ScanViewController.h
//  BMSQC
//
//  Created by gh on 15/11/10.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@protocol ScanDelegate <NSObject>


- (void)btnClick;


@end

@interface BMSQ_ScanViewController : UIViewControllerEx


@property(nonatomic)id<ScanDelegate>scDelegate;

@end
