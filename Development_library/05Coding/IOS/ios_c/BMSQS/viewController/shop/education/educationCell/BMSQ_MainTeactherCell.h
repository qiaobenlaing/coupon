//
//  BMSQ_MainTeactherCell.h
//  BMSQC
//
//  Created by liuqin on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>







@interface BMSQ_MainTeactherCell : UITableViewCell

-(void)setMainTeacher:(NSArray *)teachArr;


@end




@interface TeacherModelView : UIView

@property (nonatomic, strong)NSString *teacherCode;
@property (nonatomic, strong)UIImageView *headImage;    //图片
@property (nonatomic, strong)UILabel *teaNameLabel;     //姓名
@property (nonatomic, strong)UILabel *classLabe;        //课程
@property (nonatomic, strong)UIButton *zanBtn;
-(void)setTeacher:(NSString *)imageS name:(NSString *)name class:(NSString *)classS;

@end