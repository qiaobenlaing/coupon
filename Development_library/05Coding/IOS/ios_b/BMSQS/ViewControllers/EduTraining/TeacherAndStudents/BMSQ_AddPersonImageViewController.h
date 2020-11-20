//
//  BMSQ_AddPersonImageViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/7.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_AddPersonImageViewController : UIViewControllerEx

@property (nonatomic, strong)NSString * teacherName;//教师名字
@property (nonatomic, strong)NSString * teacherTitle;//教师职称
@property (nonatomic, strong)NSString * teachCourse;//所教课程
@property (nonatomic, strong)NSString * teacherImgUrl;// 教师形象照
@property (nonatomic, strong)NSString * teacherInfo;// 教师简介
@property (nonatomic, strong)NSString * teacherCode; // 教师编码
@property (nonatomic, strong)NSArray * teacherWork;// 教师作品或荣誉


@property (nonatomic, strong)NSString * starCode;// 星编码
@property (nonatomic, strong)NSString * signCode;// 报名编码
@property (nonatomic, strong)NSString * starInfo;// 星简介
@property (nonatomic, strong)NSString * starName;// 星姓名
@property (nonatomic, strong)NSString * starUrl;// 星形象照
@property (nonatomic, strong)NSArray * starWork;// 星作品或荣誉列表

@property (nonatomic, assign)int        number;// 1:教师详情  2:学员之星

@end
