//
//  OrderListViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OrderListViewCell : UITableViewCell

@property (nonatomic,strong)id object;
@property (nonatomic) BOOL isSelect;
@property (nonatomic, strong) UIImageView * hookImage;
@property (nonatomic, strong) UILabel * foodNameLB;
@property (nonatomic, strong) UILabel * numberLB;
@property (nonatomic, strong) UILabel * priceLB;

- (int)didClickCell:(BOOL)select;

- (BOOL)isdidClick:(BOOL)select;

- (void)setCellValue:(id)object;

@end
