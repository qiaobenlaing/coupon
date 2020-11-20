//
//  NewMarketingViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NewMarketingViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * topImageView;
@property (nonatomic, strong) UILabel     * signUpLB;// 报名
@property (nonatomic, strong) UILabel     * countLB;
@property (nonatomic, strong) UILabel     * timeLB;
@property (nonatomic, strong) UILabel     * priceLB;
@property (nonatomic, strong) UIView      * background;
- (void)setCellWithDic:(NSDictionary *)newDic;


@end
