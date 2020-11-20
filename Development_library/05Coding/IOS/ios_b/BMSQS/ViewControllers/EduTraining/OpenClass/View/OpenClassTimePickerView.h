//
//  OpenClassTimePickerView.h
//  BMSQS
//
//  Created by gh on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol OpenClassTimeDelegate <NSObject>

- (void)getSelectTime:(NSDictionary *)dic reloadArray:(int)tableViewID;

@end


@interface OpenClassTimePickerView : UIView

@property (nonatomic, assign)int tableViewID;

@property (nonatomic, assign)id<OpenClassTimeDelegate> delegate;


- (void)disMiss;
- (void)reloadView;

@end
