//
//  BMSQ_RecStuViewController.h
//  BMSQC
//
//  Created by liuqin on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UIViewControllerEx.h"

@interface BMSQ_RecStuViewController : UIViewControllerEx


@property (nonatomic, strong)NSString *shopCode;

@property (nonatomic, strong)NSDictionary *classDic; //课程详情
@property (nonatomic, strong)NSDictionary *classTimeDic;


@property (nonatomic, assign)BOOL isSign;//是否招生简介


@end



@interface ClassInfoCell : UITableViewCell


@property (nonatomic, strong)UIView *bgView;

@property (nonatomic, strong)UIView *line1;
@property (nonatomic, strong)UIView *line2;
@property (nonatomic, strong)UIView *line3;


@property (nonatomic, strong)UILabel *className;
@property (nonatomic, strong)UILabel *classtime;
@property (nonatomic, strong)UILabel *classTeacher;
@property (nonatomic, strong)UILabel *classCount;
@property (nonatomic, strong)UILabel *line;



@property (nonatomic, strong)UILabel *classStr;
@property (nonatomic, strong)UILabel *classDate;
@property (nonatomic, strong)UILabel *classFee;

@property (nonatomic, strong)UIButton *recButton;

@property (nonatomic, strong)NSString *classCode;


-(void)setClassInfoCell:(NSDictionary *)dic heigh:(float)heigh timeStr:(NSString *)timeStr;

@end