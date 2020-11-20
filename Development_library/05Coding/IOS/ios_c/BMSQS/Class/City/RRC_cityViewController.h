//
//  SDZX_cityViewController.h
//  SDBooking
//
//  Created by djx on 14-3-18.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"
#import "RRC_cityListView.h"
#import "SDZX_cityObject.h"
#import "SDZX_cityObject.h"

@protocol cityViewContrllerDelegate <NSObject>

- (void)selectCity:(SDZX_cityObject*)cityDic;

@end

@interface RRC_cityViewController : UIViewControllerEx<cityListDelegate>

@property(nonatomic,assign)NSObject<cityViewContrllerDelegate>* delegate;
@end
