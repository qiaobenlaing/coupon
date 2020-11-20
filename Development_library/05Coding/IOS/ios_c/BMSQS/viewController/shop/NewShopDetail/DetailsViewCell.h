//
//  DetailsViewCell.h
//  BMSQC
//
//  Created by 新利软件－冯 on 16/2/18.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailsViewCell : UITableViewCell

@property (nonatomic, strong)UIImageView * timeImage;
@property (nonatomic, strong)UIImageView * addressImage;
@property (nonatomic, strong)UILabel * businessTimeLB1;
@property (nonatomic, strong)UILabel * businessTimeLB2;
@property (nonatomic, strong)UILabel * businessTimeLB3;
@property (nonatomic, strong)UILabel * addressLB;
@property (nonatomic, strong)UILabel * popularityLB;// 人气
@property (nonatomic, strong)UIView  * line;

- (void)setDetailsValueDic:(NSDictionary *)valueDic;


@end
