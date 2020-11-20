//
//  OpenClassDatePickerView.h
//  BMSQS
//
//  Created by gh on 16/3/11.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol OpenClassDateDelegate <NSObject>

- (void)getSelectDate:(NSString *)date row:(int)row;

@end


@interface OpenClassDatePickerView : UIView


- (void)showDateView:(int)row;
- (void)disMiss;
@property (nonatomic, assign)id<OpenClassDateDelegate>delegate;



@end
