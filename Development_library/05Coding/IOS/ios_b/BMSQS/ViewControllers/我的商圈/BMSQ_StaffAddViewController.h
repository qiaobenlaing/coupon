//
//  BMSQ_StaffAddViewController.h
//  BMSQS
//
//  Created by Sencho Kong on 15/8/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"

@protocol BMSQ_StaffAddViewControllerDelegate <NSObject>

- (void)didSuccessUpdateStuff:(NSDictionary *)stuffInfo;

@end

@interface BMSQ_StaffAddViewController : BaseViewController

@property (nonatomic ,strong) NSDictionary *stuffInfoDic;

@property (nonatomic ,assign) id<BMSQ_StaffAddViewControllerDelegate> delegate;

@end
