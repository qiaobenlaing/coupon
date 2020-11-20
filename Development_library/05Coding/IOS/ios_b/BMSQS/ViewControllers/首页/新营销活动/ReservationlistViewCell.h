//
//  ReservationlistViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/29.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ReservationlistViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * iconImage;
@property (nonatomic, strong) UILabel * userLB;
@property (nonatomic, strong) UILabel * nameLB;
@property (nonatomic, strong) UILabel * numLB;
@property (nonatomic, strong) UIView * listView;
@property (nonatomic, strong) UILabel * ticketTypeLB;
@property (nonatomic, strong) UILabel * numberLB;
@property (nonatomic, strong) UILabel * priceLB;
@property (nonatomic, strong) NSMutableArray * feeScaleAry;
- (void)setCellReservationWithDic:(NSDictionary *)newDic;

- (void)listViewCell;
@end
