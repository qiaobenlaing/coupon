//
//  StarTeacherListViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "StarTeacherListViewCell.h"
#import "UIImageView+WebCache.h"
@interface StarTeacherListViewCell ()

@property (nonatomic, strong) UIImageView * iconImage;// 教师图片
@property (nonatomic, strong) UILabel     * nameLB;//教师姓名
@property (nonatomic, strong) UILabel     * subjectLB;// 教授科目
@property (nonatomic, strong) UILabel     * levelLB;// 教师级别
@property (nonatomic, strong) UILabel     * introductionLB;//教师介绍


@end

@implementation StarTeacherListViewCell

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {

        self.backgroundColor = [UIColor whiteColor];
        [self setViewUp];
        
    }
    return self;
}

- (void)setViewUp
{
    self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, (APP_VIEW_WIDTH-20)/2, (APP_VIEW_WIDTH-20)/2)];
    _iconImage.image = [UIImage imageNamed:@"share"];
    [self addSubview:self.iconImage];
    
//    self.nameLB = [[UILabel alloc] initWithFrame:CGRectMake(5, (APP_VIEW_WIDTH-20)/2 + 10, APP_VIEW_WIDTH/5*2 - 60, 20)];
    self.nameLB = [[UILabel alloc] initWithFrame:CGRectMake(5, (APP_VIEW_WIDTH-20)/2 + 10, (APP_VIEW_WIDTH-20)/2 - 5, 20)];
    _nameLB.font = [UIFont systemFontOfSize:13.0];
    [self addSubview:self.nameLB];
    
//    self.subjectLB = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH-20)/2 - 65, (APP_VIEW_WIDTH-20)/2 + 10, 60, 20)];
//    _subjectLB.font = [UIFont systemFontOfSize:13.0];
//    self.subjectLB.textAlignment = NSTextAlignmentRight;
//    _subjectLB.backgroundColor = [UIColor purpleColor];
//    [self addSubview:self.subjectLB];
    
    self.levelLB = [[UILabel alloc] initWithFrame:CGRectMake(5, (APP_VIEW_WIDTH-20)/2 + 40, APP_VIEW_WIDTH/5*2 - 10, 20)];
    _levelLB.font = [UIFont systemFontOfSize:13.0];
//    _levelLB.backgroundColor = [UIColor greenColor];
    [self addSubview:self.levelLB];
    
    self.introductionLB = [[UILabel alloc] initWithFrame:CGRectMake(5,(APP_VIEW_WIDTH-20)/2 + 70, (APP_VIEW_WIDTH-20)/2-20, 30)];//APP_VIEW_WIDTH/5*3 - 70
    _introductionLB.font = [UIFont systemFontOfSize:12.0];
    _introductionLB.numberOfLines = 2;
//    _introductionLB.backgroundColor = [UIColor yellowColor];
    [self addSubview:self.introductionLB];
    
    
    
    
}




- (void)setCellWithListValueDic:(NSDictionary *)valueDic
{
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,valueDic[@"teacherImgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    NSString * nameStr = [NSString stringWithFormat:@"%@", valueDic[@"teacherName"]];
    NSString * subiectStr = [NSString stringWithFormat:@"%@", valueDic[@"teachCourse"]];
    self.nameLB.text = [NSString stringWithFormat:@"%@          %@", nameStr, subiectStr];
//    self.subjectLB.text = [NSString stringWithFormat:@"%@", valueDic[@"teachCourse"]];
    self.levelLB.text = [NSString stringWithFormat:@"%@", valueDic[@"teacherTitle"]];
    self.introductionLB.text = [NSString stringWithFormat:@"简介:%@", valueDic[@"teacherInfo"]];
}



@end
