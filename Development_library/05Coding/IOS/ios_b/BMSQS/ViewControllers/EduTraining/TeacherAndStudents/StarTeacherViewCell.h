//
//  StarTeacherViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StarTeacherViewCell : UITableViewCell

@property (nonatomic, strong) UIImageView * iconImage;// 教师图片
@property (nonatomic, strong) UILabel     * nameLB;//教师姓名
@property (nonatomic, strong) UILabel     * subjectLB;// 教授科目
@property (nonatomic, strong) UILabel     * levelLB;// 教师级别
@property (nonatomic, strong) UILabel     * introductionLB;//教师介绍

- (void)setCellValueDic:(NSDictionary *)valueDic;

@end
