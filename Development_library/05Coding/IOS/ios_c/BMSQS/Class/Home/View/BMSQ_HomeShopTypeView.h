//
//  BMSQ_HomeShopTypeView.h
//  BMSQC
//
//  Created by gh on 15/11/22.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HomeShopTypeViewDelegate <NSObject>

- (void)btnShopTypeClick:(id)object;

@end


@interface BMSQ_HomeShopTypeView : UIView

@property(nonatomic)id<HomeShopTypeViewDelegate>ShopTypeDelegate;
@property (nonatomic, strong)NSArray *m_dataSource;

- (void)setViewValue:(NSDictionary *)dic;

@end
