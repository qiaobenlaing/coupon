//
//  CustomNotificationView.h
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol CustomNotificationViewDeleagte <NSObject>

-(void)presentViewC:(NSDictionary *)dic;

@end

@interface CustomNotificationView : UIView

@property (nonatomic,strong)NSMutableArray *notiArray;
@property (nonatomic, strong)UITableView *notiTableView;

@property (nonatomic,weak)id<CustomNotificationViewDeleagte>notiDelegate;

-(void)addNoti:(NSDictionary *)dic;

@end
