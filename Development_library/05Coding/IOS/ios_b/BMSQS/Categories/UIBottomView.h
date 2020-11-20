//
//  UIBottomView.h
//  SDBooking
//
//  Created by djx on 14-2-8.
//  Copyright (c) 2014年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BottomViewDelegate <NSObject>

//按钮点击事件
- (void)bottomClick;

@end

@interface UIBottomView : UIView
{
    UIButton* btn_bottom;
    
}

@property(nonatomic,assign)NSObject<BottomViewDelegate>* delegate;

//设置按钮背景图片
- (void)setButtonBackImage:(NSString*)imageName;
//设置按钮标题
- (void)setButtonTitle:(NSString*)title;
@end
