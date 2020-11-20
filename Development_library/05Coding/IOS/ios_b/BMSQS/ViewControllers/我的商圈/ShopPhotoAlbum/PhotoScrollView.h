//
//  PhotoScrollView.h
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "UIButtonEx.h"


@protocol PhotoScrollDelegate <NSObject>

- (void)PhotoScrollbtnAction:(UIButtonEx *)button;

@end

@interface PhotoScrollView : UIScrollView

@property(nonatomic,copy)void(^removeSC)();

@property (nonatomic, assign)int count;
-(void)setImageView:(NSArray *)imageArray;
-(void)setImage:(int)i;
-(void)setImageDicView:(NSArray *)imageArray;
-(void)setEnImageArray:(NSArray *)imageArray;

-(void)setHononrImageArray:(NSArray *)imageArray string:(NSString *)string;

-(void)setParentImageArray:(NSArray *)imageArray;

- (void)btnAction:(UIButtonEx *)button;



@property(nonatomic,assign) NSObject<PhotoScrollDelegate>* delegate;

@end
