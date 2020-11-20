//
//  BMSQ_MineIdentCell.h
//  BMSQC
//
//  Created by gh on 15/9/6.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"

@interface BMSQ_MineIdentCell : UITableViewCell 

@property(nonatomic, strong)NSDictionary *couponDic;
@property (nonatomic,strong)UIButtonEx *btn_payment;

- (void)setCellValue:(NSDictionary*)dicCupon status:(NSString *)status;

@end
