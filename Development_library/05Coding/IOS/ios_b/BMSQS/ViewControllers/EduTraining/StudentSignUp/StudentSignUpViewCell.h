//
//  StudentSignUpViewCell.h
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol StudentSignUpViewCellDetegate <NSObject>

- (void)setCellStudentSignUp:(int)handFlag learnFee:(NSString *)learnFee row:(int)row;

@end


@interface StudentSignUpViewCell : UITableViewCell


- (void)setCellWithSignUpDic:(NSDictionary *)dic row:(int)row;

@property (nonatomic, assign)id<StudentSignUpViewCellDetegate>studentSignUpDetegate;


@end
