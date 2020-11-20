//
//  ScheduleViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface ScheduleViewCell : UITableViewCell

@property (nonatomic, strong) UILabel * classType; // 课程类型
@property (nonatomic, strong) UIView * goToClassView; //上课时间
@property (nonatomic, strong) UILabel * dateLB1;//上课日期
@property (nonatomic, strong) UILabel * dateLB2;
@property (nonatomic, strong) UILabel * dateLB3;
@property (nonatomic, strong) UILabel * teacherName; //任课老师姓名
@property (nonatomic, strong) UIButton * detailsBut; // 课程详情
@property (nonatomic, strong) UILabel * lineLB1;// 竖线1
@property (nonatomic, strong) UILabel * lineLB2;// 竖线2
@property (nonatomic, strong) UILabel * lineLB3;// 竖线3
@property (nonatomic, strong) UIView  * lineView; //


- (void)setCellWithScheduleDic:(NSDictionary *)scheduleDic;

@end
