//
//  ShopListDataView.h
//  BMSQC
//
//  Created by liuqin on 15/12/2.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol ShopListDataViewDelegate <NSObject>

//-(void)hiddenListDataeView;
-(void)seleCell:(NSDictionary *)dic title:(NSString *)title seleRow:(int)row;

-(void)seleCircleCell:(NSDictionary *)dic title:(NSString *)title  seleRow:(int)row  seleSecRow:(int)secrow;

@end

@interface ShopListDataView : UIView

@property (nonatomic, strong)id<ShopListDataViewDelegate>seleDelegate;

-(void)circleTable:(NSDictionary *)dic;

-(void)onlyTable:(NSDictionary *)dic tag:(int)typeTag;
@end
