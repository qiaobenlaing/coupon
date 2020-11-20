//
//  BMSQ_AddIntroduceViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/7.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_AddIntroduceViewController.h"
#import "BMSQ_AddHonorViewController.h"
@interface BMSQ_AddIntroduceViewController ()<UITextViewDelegate>

@property (nonatomic, strong) UITextView * actTextView;
@property (nonatomic, strong) UILabel * textViewPlaceholder;
@end

@implementation BMSQ_AddIntroduceViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    if (self.number == 1) {
        [self setNavTitle:@"名师堂"];
    }else if (self.number == 2){
        [self setNavTitle:@"学员之星"];
    }
    [self setNavBackItem];
    [self customRightBtn];
    [self setViewUp];
    
    
}

- (void)customRightBtn
{
    UIButton * item = [UIButton buttonWithType:UIButtonTypeCustom];
    item.frame = CGRectMake(APP_VIEW_WIDTH - 50, 20, 48, 44);
    [item setTitle:@"下一步" forState:UIControlStateNormal];
    item.titleLabel.font = [UIFont systemFontOfSize:14.0];
    [item setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [item addTarget:self action:@selector(itemsNextClick:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:item];
    
}

- (void)setViewUp
{
    
    UILabel * titleLB = [[UILabel alloc] initWithFrame:CGRectMake(10,  APP_VIEW_ORIGIN_Y + 10, 100, 15)];
    if (self.number == 1) {
        titleLB.text = @"教师简介";
    }else if (self.number == 2){
        titleLB.text = @"学员简介";
    }
    
    titleLB.font = [UIFont systemFontOfSize:14.0];
    [self.view addSubview:titleLB];
    
    self.actTextView = [[UITextView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y + 30, APP_VIEW_WIDTH, 150)];
    self.actTextView.delegate = self;
//    self.actTextView.layer.borderColor = [UIColor grayColor].CGColor;
//    self.actTextView.layer.borderWidth =1.0;
//    self.actTextView.layer.cornerRadius =5.0;
    self.actTextView.backgroundColor = [UIColor whiteColor];
    
    
    [self.view addSubview:self.actTextView];
    
    
    self.textViewPlaceholder = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 20)];
    self.textViewPlaceholder.font = [UIFont systemFontOfSize:13.f];
    self.textViewPlaceholder.textColor = UICOLOR(205, 205, 209, 1.0);
    if (self.number == 1){
        self.textViewPlaceholder.text = @"请填写教师简介";
    }else if (self.number == 2){
        self.textViewPlaceholder.text = @"请填写学员简介";
    }
    
    [self.actTextView addSubview:self.textViewPlaceholder];
    
    if (self.number == 1) {
        
        if (self.teacherInfo) {
            self.actTextView.text = self.teacherInfo;
            self.textViewPlaceholder.hidden = YES;
        }
        
    }else if (self.number == 2){
        
        if (self.starInfo) {
            self.actTextView.text = self.starInfo;
            self.textViewPlaceholder.hidden = YES;
        }
        
    }
    
    
    
    
}

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    self.textViewPlaceholder.hidden = YES;
}




#pragma mark ----- 下一步
- (void)itemsNextClick:(UIButton *)sender
{
    
    if ([self.actTextView.text length] == 0) {
        CSAlert(@"请填写简介");
    }else{
        
        BMSQ_AddHonorViewController * VC = [[BMSQ_AddHonorViewController alloc] init];

        if (self.number == 1) {
            
            VC.teacherName = self.teacherName;
            VC.teacherTitle = self.teacherTitle;
            VC.teachCourse = self.teachCourse;
            VC.teacherImgUrl = self.teacherImgUrl;
            VC.teacherInfo = self.actTextView.text;
            VC.teacherWork = self.teacherWork;
            VC.teacherCode = self.teacherCode;
            VC.number = 1;
        }else if (self.number == 2){
            
            VC.starName = self.starName;
            VC.starCode = self.starCode;
            VC.signCode = self.signCode;
            VC.starInfo = self.actTextView.text;
            VC.starUrl = self.starUrl;
            VC.starWork = self.starWork;
            VC.number = 2;
            
        }
        
        [self.navigationController pushViewController:VC animated:YES];
    }
    
    
}

#pragma mark ----- 内存警告
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
