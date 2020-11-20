//
//  topSelectView.h
//  51youHui
//
//  Created by cq on 13-2-20.
//  Copyright (c) 2013年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "selectBtnObject.h"

@protocol topSelectViewDelegate <NSObject>

//按钮选择时间
- (void)btnSelect:(id)sender btnObj:(selectBtnObject*)obj;

@end

//页面顶部可滑动的选择分类的view
@interface topSelectView : UIView
{
    UIImageView* m_imageViewSelected; //顶部移动的图片
    NSMutableArray* m_arraySelectTabButtons;//顶部选项卡按钮数组
    NSMutableArray* m_arraySelectTab; //顶部选项卡数组
    CGRect rectTopTab;
    int indexClick;
}

@property(nonatomic,assign)id<topSelectViewDelegate> delegate;

- (id)initWithFrame:(CGRect)frame buttonArray:(NSMutableArray*)btnAry;
- (id)initWithFrame:(CGRect)frame buttonArray:(NSMutableArray*)btnAry btbColor:(UIColor*)color;
- (void)topBarButtonClicked:(id)sender;
- (void)setImageViewSelectedAnimation:(NSInteger)index;
- (void)setImageViewSelectedNoAnimation:(NSInteger)index;

@end
