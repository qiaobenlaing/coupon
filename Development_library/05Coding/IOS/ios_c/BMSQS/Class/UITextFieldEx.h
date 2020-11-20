//
//  UITextFieldEx.h
//  BMSQC
//
//  Created by gh on 15/11/8.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol TextFieldDelegate <NSObject>



@optional
-(void)keyboardHiddend;


@end


@interface UITextFieldEx : UITextField

@property(nonatomic,assign)id<TextFieldDelegate>  textDelegate;


@end
