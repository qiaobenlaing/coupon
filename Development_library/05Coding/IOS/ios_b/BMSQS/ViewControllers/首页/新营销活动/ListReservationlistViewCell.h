//
//  ListReservationlistViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/1/21.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ListReservationlistViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * iconImage;
@property (nonatomic, strong) UILabel * userLB;
@property (nonatomic, strong) UILabel * nameLB;
@property (nonatomic, strong) UILabel * numLB;

- (void)setCellReservationWithDic:(NSDictionary *)newDic;

@end
