//
//  CircularProgressView.h
//  CircularProgressView
//
//  Created by nijino saki on 13-3-2.
//  Copyright (c) 2013年 nijino. All rights reserved.
//

//圆形进度条

#import <UIKit/UIKit.h>


@protocol CircularProgressDelegate;

@interface CircularProgressView : UIView

@property (assign, nonatomic) float progress;
@property (assign, nonatomic) id <CircularProgressDelegate> delegate;

@property (strong, nonatomic) UIColor *backColor;
@property (retain, nonatomic) UIColor *progressColor;
@property (assign, nonatomic) CGFloat lineWidth;
@property (strong, nonatomic) NSTimer *timer;

- (id)initWithFrame:(CGRect)frame
          backColor:(UIColor *)backColor
      progressColor:(UIColor *)progressColor
          lineWidth:(CGFloat)lineWidth;
- (void)updateProgressCircle:(float)progressValue;

@end

@protocol CircularProgressDelegate <NSObject>

- (void)didUpdateProgressView;

@end