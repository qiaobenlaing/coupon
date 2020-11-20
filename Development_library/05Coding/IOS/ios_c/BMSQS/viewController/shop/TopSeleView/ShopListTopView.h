//
//  ShopListTopView.h
//  BMSQC
//
//  Created by liuqin on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ShopListTopViewDelegate <NSObject>

-(void)showListDataDelegate:(int)i;

@end

@interface ShopListTopView : UIView

@property (nonatomic, strong)NSMutableArray *titleArray;
@property (nonatomic,weak)id<ShopListTopViewDelegate>listDelegate;

-(void)changeTitle:(NSString *)str tag:(int)tag;
@end
