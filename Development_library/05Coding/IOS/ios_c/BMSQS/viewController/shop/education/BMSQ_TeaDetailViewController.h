//
//  BMSQ_TeaDetailViewController.h
//  BMSQC
//
//  Created by liuqin on 16/3/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_TeaDetailViewController : UIViewControllerEx

@property (nonatomic, strong)NSString *teaTitel;
@property (nonatomic, strong)NSString *teacherCode;

@property (nonatomic, assign)BOOL isTeacher;
@property (nonatomic, strong)NSString *starCode;

@end
