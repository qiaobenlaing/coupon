//
//  BMSQ_AddStarViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_AddStarViewController.h"
#import "BMSQ_AddPersonImageViewController.h"
@interface BMSQ_AddStarViewController ()

@property (nonatomic, strong)UIView * backView;//背景视图
@property (nonatomic, strong)UILabel * teacherNa;//教师名字
@property (nonatomic, strong)UILabel * teacherLevel;//教师职称
@property (nonatomic, strong)UILabel * subject;// 所任课程
@property (nonatomic, strong)UITextField * NameFeld;//
@property (nonatomic, strong)UITextField * LevelFeld;//
@property (nonatomic, strong)UITextField * subjectFeld;//
@property (nonatomic, strong)UILabel     * line1;
@property (nonatomic, strong)UILabel     * line2;


@end

@implementation BMSQ_AddStarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:@"名师堂"];
    [self setNavBackItem];
    [self customRightBtn];
    [self setViewUp];
}


- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 50, 20, 48, 44);
    //    [item setImage:[UIImage imageNamed:@"right_add"] forState:UIControlStateNormal];
    [item setTitle:@"下一步" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemSaveClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

#pragma mark ----- setViewUp
- (void)setViewUp
{
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 10, APP_VIEW_WIDTH, 107)];
    _backView.backgroundColor = [UIColor whiteColor];
//    _backView.layer.borderColor = [UIColor grayColor].CGColor;
//    _backView.layer.borderWidth =1.0;
//    _backView.layer.cornerRadius =5.0;
    [self.view addSubview:self.backView];
    
    self.teacherNa = [[UILabel alloc] initWithFrame:CGRectMake(10, 5, 80, 20)];
    self.teacherNa.text = @"教师姓名";
    self.teacherNa.textColor = UICOLOR(102, 102, 102, 1.0);
    self.teacherNa.font = [UIFont systemFontOfSize:14.0];
    [self.backView addSubview:self.teacherNa];
    
    self.NameFeld = [[UITextField alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 200, 5, 180, 25)];
    self.NameFeld.placeholder = @"请填写教师姓名";
    self.NameFeld.font = [UIFont systemFontOfSize:14.0];
    if (self.teacherName != nil && ![self.teacherName isKindOfClass:[NSNull class]] && [self.teacherName length] != 0) {
        self.NameFeld.text = self.teacherName;
    }
    [self.backView addSubview:self.NameFeld];
    
    self.line1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 35, self.backView.frame.size.width, 0.5)];
    self.line1.backgroundColor = APP_CELL_LINE_COLOR;
    [self.backView addSubview:self.line1]; // 竖线1
    
    self.teacherLevel = [[UILabel alloc] initWithFrame:CGRectMake(10, 41, 80, 20)];
    self.teacherLevel.text = @"教师职称";
    self.teacherLevel.textColor = UICOLOR(102, 102, 102, 1.0);
    self.teacherLevel.font = [UIFont systemFontOfSize:14.0];
    [self.backView addSubview:self.teacherLevel];
    
    self.LevelFeld = [[UITextField alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 200, 41, 180, 25)];
    self.LevelFeld.placeholder = @"请填写教师职称";
    self.LevelFeld.font = [UIFont systemFontOfSize:14.0];
    if (self.teacherTitle != nil && ![self.teacherTitle isKindOfClass:[NSNull class]] && [self.teacherTitle length] != 0) {
        self.LevelFeld.text = self.teacherTitle;
    }
    [self.backView addSubview:self.LevelFeld];
    
    self.line2 = [[UILabel alloc] initWithFrame:CGRectMake(0, 71, self.backView.frame.size.width, 0.5)];
    self.line2.backgroundColor = APP_CELL_LINE_COLOR;
    [self.backView addSubview:self.line2]; // 竖线2
    
    self.subject = [[UILabel alloc] initWithFrame:CGRectMake(10, 76, 80, 20)];
    self.subject.text = @"所任课程";
    self.subject.textColor = UICOLOR(102, 102, 102, 1.0);
    self.subject.font = [UIFont systemFontOfSize:14.0];
    [self.backView addSubview:self.subject];
    
    self.subjectFeld = [[UITextField alloc] initWithFrame:CGRectMake(self.backView.frame.size.width - 200, 76, 180, 25)];
    self.subjectFeld.placeholder = @"请填写所任课程";
    self.subjectFeld.font = [UIFont systemFontOfSize:14.0];
    if (self.teachCourse != nil && ![self.teachCourse isKindOfClass:[NSNull class]] && [self.teachCourse length] != 0) {
        self.subjectFeld.text = self.teachCourse;
    }
    [self.backView addSubview:self.subjectFeld];
    
    
    
}



#pragma mark ----  下一步
- (void)itemSaveClick:(UIButton *)sender
{

    if ([self.NameFeld.text length] == 0 || [self.LevelFeld.text length] == 0 || [self.subjectFeld.text length] == 0) {
        CSAlert(@"请填写完整");
    }else{
        BMSQ_AddPersonImageViewController * VC = [[BMSQ_AddPersonImageViewController alloc] init];
        VC.teacherName = self.NameFeld.text;
        VC.teacherTitle = self.LevelFeld.text;
        VC.teachCourse = self.subjectFeld.text;
        VC.teacherImgUrl = self.teacherImgUrl;
        VC.teacherInfo = self.teacherInfo;
        VC.teacherWork = self.teacherWork;
        VC.teacherCode = self.teacherCode;
        VC.number = 1;
        [self.navigationController pushViewController:VC animated:YES];
    }
    
    
    
}







#pragma mark ------ 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
