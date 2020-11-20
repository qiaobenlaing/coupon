//
//  HonorWallViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HonorWallViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * HonorImage;// 荣誉照片

- (void)setCellHonorDic:(NSDictionary *)HonorDic number:(int)number;

@end
