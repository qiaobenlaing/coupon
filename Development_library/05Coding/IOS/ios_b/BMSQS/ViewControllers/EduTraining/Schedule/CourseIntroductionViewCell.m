//
//  CourseIntroductionViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CourseIntroductionViewCell.h"
#import "OpenClassUtil.h"

@interface CourseIntroductionViewCell () {
    CGFloat viewHeight;
}


@property (nonatomic, strong)UIView *backView;

@property (nonatomic, strong)UILabel *classNameLabel; //所开班级
@property (nonatomic, strong)UILabel *learnDateLabel; //开始结束时间
@property (nonatomic, strong)UILabel *classWeekInfoLabel;//上课时间
@property (nonatomic, strong)UILabel *learnMemoLabel; //适合年龄段
@property (nonatomic, strong)UILabel *learnFeeLabel;//报名费用
@property (nonatomic, strong)UILabel *learnNumLabel;//所学课时
@property (nonatomic, strong)UILabel *teacherNameLabel;//老师名字
@property (nonatomic, strong)UILabel *signDateLabel;//报名开始结束时间
@property (nonatomic, strong)UILabel *infolabel;//课程简介
@property (nonatomic, strong)UIImageView *classImage; //展示图片

@property (nonatomic, strong)UIView *sublineView;

@property (nonatomic, strong)NSString *classCode;

@end



@implementation CourseIntroductionViewCell


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self) {
        [self setViewUp];
    }
    
    return self;
}


- (void)setViewUp {
    
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH-20, 100)];
    //    self.backView.backgroundColor = [UIColor blueColor];
    self.backView.layer.borderWidth = 1.0;
    self.backView.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    [self.contentView addSubview:self.backView];
    //    self.contentView.backgroundColor = [ UIColor redColor];
    
    NSArray *array = [NSArray arrayWithObjects:@"所开班级", @"学习期间", @"上课时间", @"适合年龄段", @"报名费用", @"所学课时", @"任课老师", @"报名时间", nil];
    
    CGFloat originY = 0;
    CGFloat originX = 10;
    CGFloat sizeWidth = 100;
    
    for (int i=0; i<8; i++) {
        [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30)  text:array[i] view:self.backView];
        
        originY = originY+30;
        //画线
        [gloabFunction ggsetLineView:CGRectMake(0, originY-1, APP_VIEW_WIDTH-20, 1) view:self.backView];
        
    }
    [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30)  text:@"课程简介" view:self.backView];
    
    self.sublineView = [gloabFunction ggsetLineView:CGRectMake(0, originY-1, APP_VIEW_WIDTH-20, 1) view:self.backView];
    
    originX = self.backView.frame.size.width/3;
    originY = 0;
    sizeWidth = self.backView.frame.size.width/3*2;
    
    //所开班级
    self.classNameLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //学习期间
    self.learnDateLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //上课时间
    self.classWeekInfoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //适合年龄段
    self.learnMemoLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    
    originY = originY + 30;
    //报名费用
    self.learnFeeLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //所学课时
    self.learnNumLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    
    //任课老师
    self.teacherNameLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    //报名时间
    self.signDateLabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    
    originY = originY + 30;
    
    //课程简介
    self.infolabel = [OpenClassUtil openClassSetLabel:CGRectMake(originX, originY, sizeWidth, 30) text:@"" view:self.backView];
    self.infolabel.numberOfLines = 0;
    
    originY = originY + 30;
    
//    self.classImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.backView.frame.size.width, 80)];
//    self.classImage.backgroundColor = [UIColor blueColor];
//    [self.backView addSubview:self.classImage];
    
    
}

- (void)setCellWithCourseIntroductionValue:(NSDictionary *)dic {
    
    self.classCode = [NSString stringWithFormat:@"%@",[dic objectForKey:@"classCode"]];
    
    self.classNameLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"className"]];
    self.learnDateLabel.text = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"learnStartDate"], [dic objectForKey:@"learnEndDate"]];
    self.classWeekInfoLabel.text = [NSString stringWithFormat:@"%@",[dic objectForKey:@"className"]];
    self.learnMemoLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnMemo"]];
    self.learnFeeLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnFee"]];
    self.learnNumLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"learnNum"]];
    self.teacherNameLabel.text = [NSString stringWithFormat:@"%@", [dic objectForKey:@"teacherName"]];
    self.signDateLabel.text = [NSString stringWithFormat:@"%@至%@", [dic objectForKey:@"signStartDate"], [dic objectForKey:@"signEndDate"]];
    self.infolabel.text= [NSString stringWithFormat:@"%@", [dic objectForKey:@"classInfo"]];
    
    CGSize size = [OpenClassUtil getInfolabelSize:self.infolabel.text];
    self.infolabel.frame = CGRectMake(self.infolabel.frame.origin.x, 30*8 + (30/2-13.0/2), size.width, size.height);
    
    
    
    self.sublineView.frame = CGRectMake(0, self.infolabel.frame.size.height + self.infolabel.frame.origin.y-1, APP_VIEW_WIDTH-20, 1);

    self.backView.frame = CGRectMake(10, 10, APP_VIEW_WIDTH-20, 30*8 + self.infolabel.frame.size.height + (30/2-13.0/2) + 5);
    
}




@end
