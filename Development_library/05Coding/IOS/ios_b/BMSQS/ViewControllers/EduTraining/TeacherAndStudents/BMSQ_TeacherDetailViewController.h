//
//  BMSQ_TeacherDetailViewController.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_TeacherDetailViewController : UIViewControllerEx

@property (nonatomic, strong)NSString * teacherCode;

@property (nonatomic, strong)NSString * nameStr;
@property (nonatomic, assign)int        number;// 1:教师详情  2:学员之星
@end
