//
//  benTopView.h
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol benTopViewDelegate <NSObject>

-(void)changeType:(int)i;
-(void)changeType:(int)i view:(UIView *) betopView;
@end


@interface benTopView : UIView

@property (nonatomic, strong)NSArray *benTopTitleArray;
@property (nonatomic, assign)BOOL isHiddenLine;

@property(nonatomic, weak)id<benTopViewDelegate>typeDelegate;

-(void)createTopViewtitleArray:(NSArray *)titleArray;

@end
