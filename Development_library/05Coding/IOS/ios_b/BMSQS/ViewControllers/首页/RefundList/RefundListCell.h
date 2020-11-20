//
//  RefundListCell.h
//  BMSQS
//
//  Created by gh on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"

@protocol refundDelegate <NSObject>

- (void)clickDisagreeBtn:(UIButtonEx *)leftBtn;
- (void)clickAgreeBnt:(UIButtonEx *)RightBtn;



@end


@interface RefundListCell : UITableViewCell

@property (nonatomic, strong)id<refundDelegate> refDelegate;

@property (nonatomic, strong)UIImageView *userIv;

@property (nonatomic, assign)BOOL isHandle;


@property (nonatomic, strong)UIButtonEx *leftBtn;
@property (nonatomic, strong)UIButtonEx *rightBtn;

- (void)setCellValue:(NSDictionary *)dic;


@end
