//
//  RefundView.h
//  BMSQC
//
//  Created by liuqin on 16/1/21.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@protocol RefundViewDelegate <NSObject>

-(void)seleResult:(int)i;

@end


@interface RefundView : UIView

@property (nonatomic, strong)UILabel *messageLabel;
@property (nonatomic, weak)id<RefundViewDelegate>delegate;

-(void)setData:(NSDictionary *)dic;

@end
