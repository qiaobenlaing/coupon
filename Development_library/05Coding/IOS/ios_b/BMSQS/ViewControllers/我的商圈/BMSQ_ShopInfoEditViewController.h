//
//  BMSQ_ShopInfoEditViewController.h
//  BMSQS
//
//  Created by Sencho Kong on 15/7/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"

@class BMSQ_ShopInfoEditViewController;

@protocol BMSQ_ShopInfoEditViewControllerDelegate <NSObject>

- (void)controller:(BMSQ_ShopInfoEditViewController *)controller DidSuccessEditInfo:(NSDictionary *)info;

@end

@interface BMSQ_ShopInfoEditViewController : UIViewControllerEx

@property (assign , nonatomic) id<BMSQ_ShopInfoEditViewControllerDelegate> delegate;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UITextField *textField;
@property (strong ,nonatomic) NSDictionary *shopDataDic;

@end
