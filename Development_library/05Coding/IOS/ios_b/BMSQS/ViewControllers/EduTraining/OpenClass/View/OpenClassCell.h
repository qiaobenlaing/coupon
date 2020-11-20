//
//  OpenClassCell.h
//  BMSQS
//
//  Created by gh on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIButtonEx.h"


@protocol openClassDelegate <NSObject>

- (void)btnDelShopClass:(NSString *)classCode;


@end

@interface OpenClassCell : UITableViewCell

@property (nonatomic, strong)UIButtonEx *buttonEx;

- (void)setCellValue:(NSDictionary *)dic forRow:(int)row;
//
@property (nonatomic, strong)id<openClassDelegate> openCellDelegate;

@end
