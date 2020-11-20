//
//  PhotoScrollView.h
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>




@interface PhotoScrollView : UIScrollView


@property(nonatomic,copy)void(^removeSC)();



@property (nonatomic, assign)int count;
-(void)setImageView:(NSArray *)imageArray;
-(void)setImage:(int)i;
-(void)setImageDicView:(NSArray *)imageArray;
-(void)setEnImageArray:(NSArray *)imageArray;
@end
