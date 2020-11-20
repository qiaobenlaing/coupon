//
//  UITextFieldEx.m
//  BMSQC
//
//  Created by gh on 15/11/8.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "UITextFieldEx.h"

@implementation UITextFieldEx

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        
        UIToolbar * topView = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 30)];
        [topView setBarStyle:UIBarStyleBlackTranslucent];
        
        UIBarButtonItem * btnSpace = [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:nil];
        
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(2, 5, 50, 25);
        [btn setTitle:@"隐藏" forState:UIControlStateNormal];
        [btn addTarget:self action:@selector(dismissKeyB) forControlEvents:UIControlEventTouchUpInside];
        [btn setImage:[UIImage imageNamed:@"shouqi"] forState:UIControlStateNormal];
        UIBarButtonItem *doneBtn = [[UIBarButtonItem alloc]initWithCustomView:btn];
        NSArray * buttonsArray = [NSArray arrayWithObjects:btnSpace,doneBtn,nil];
        [topView setItems:buttonsArray];
        [self setInputAccessoryView:topView];
    }
    
    return self;
    
}

- (void)dismissKeyB {
    
    [self resignFirstResponder];
    [self.textDelegate keyboardHiddend];
    
}


@end
