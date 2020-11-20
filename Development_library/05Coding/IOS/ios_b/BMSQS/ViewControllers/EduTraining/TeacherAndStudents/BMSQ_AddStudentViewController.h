//
//  BMSQ_AddStudentViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/8.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_AddStudentViewController : UIViewControllerEx

@property (nonatomic, strong)NSString * starCode;// 星编码
@property (nonatomic, strong)NSString * signCode;// 报名编码
@property (nonatomic, strong)NSString * starInfo;// 星简介
@property (nonatomic, strong)NSString * starName;// 活动称谓
@property (nonatomic, strong)NSString * starUrl;// 星形象照
@property (nonatomic, strong)NSArray * starWork;// 星作品或荣誉列表

@property (nonatomic, strong)NSString * studentName;


@property (nonatomic, assign)int        number;// 1:教师详情  2:学员之星
@end
