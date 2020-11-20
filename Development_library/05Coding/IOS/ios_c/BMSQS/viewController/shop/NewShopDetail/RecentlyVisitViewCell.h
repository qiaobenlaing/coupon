//
//  RecentlyVisitViewCell.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RecentlyVisitViewCell : UITableViewCell

@property (nonatomic, strong)UILabel * recentlyVisitLB;
@property (nonatomic, strong)UIImageView * userIconImage1;
@property (nonatomic, strong)UIImageView * userIconImage2;
@property (nonatomic, strong)UIImageView * userIconImage3;

- (void)setRecentlyValueAry:(NSArray *)valueAry;

@end
