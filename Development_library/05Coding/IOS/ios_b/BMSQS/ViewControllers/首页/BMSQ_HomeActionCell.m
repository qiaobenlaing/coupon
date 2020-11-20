//
//  BMSQ_HomeActionCell.m
//  BMSQS
//
//  Created by djx on 15/7/5.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeActionCell.h"
#import "BMSQ_CheckStandController.h"
#import "BMSQ_MarketActivityController.h"
#import "BMSQ_BenefitCardController.h"
#import "BMSQ_MyNoticeController.h"
#import "BMSQ_DZViewController.h"
#import "BMSQ_mineOrderViewController.h"
#import "BMSQ_newMarketingViewController.h"
#import "BMSQ_CollectionMoneyViewController.h"
#import "BMSQ_VerificationCodeViewController.h"
#import "ConfirmOrderViewController.h"
#import "BMSQ_CarFeedViewController.h"

#import "NewActivityViewController.h"

#import "HQAccountBookViewController.h"
#import "BMSQ_CountViewController.h"
#import "BMSQ_ParentCommentViewController.h"
#import "BMSQ_StarTeacherViewController.h"
#import "BMSQ_ScheduleViewController.h"
#import "BMSQ_HeadmasterViewController.h"
#import "BMSQ_TeacherDetailViewController.h"
#import "OpenClassViewController.h"
#import "BMSQ_StudentSignUpViewController.h"
#import "RefundListViewController.h"
#import "BMSQ_StarTeacherListViewController.h"
#import "BMSQ_HonorViewController.h"
#import "EnrolStudentsViewController.h"

@interface BMSQ_HomeActionCell ()

@property (nonatomic, strong)UIView *bottomView;

@end

@implementation BMSQ_HomeActionCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self)
    {
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
//    26 + 51 + 15 + 25;
    float xPosition = 42;
    float yPosition = 26;
    
    float width = 40;
    float height = 40;

    float yOffset = 15;
    float lbOffset = 5;
    float xOffset = (APP_VIEW_WIDTH - 3*width - 2*xPosition)/2;
    
    
    //浏览人数
    UIView *topView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, 79)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.contentView addSubview:topView];
    
    
    
    UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, 55, APP_VIEW_WIDTH/2, 10)];
    label.text = @"总浏览量";
    label.textAlignment = NSTextAlignmentCenter;
    label.font = [UIFont systemFontOfSize:12];
    label.textColor = APP_TEXTCOLOR;
    [topView addSubview:label];
    //0上面的小人头像
    UIImageView *iconimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 15, 20, 18)];
    [iconimageView setImage:[UIImage imageNamed:@"manager.png"]];
    [topView addSubview:iconimageView];
    iconimageView.center = CGPointMake(APP_VIEW_WIDTH/4, 15);
    
    UILabel *countLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, APP_VIEW_WIDTH/2, 40)];
    countLabel.textColor = APP_TEXTCOLOR;
    countLabel.tag = 2000;
    countLabel.font = [UIFont systemFontOfSize:20];
    countLabel.text = @"";
    countLabel.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:countLabel];
    
    //浏览量
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(cellGotoCountVc)];
    [topView addGestureRecognizer:tapGesture];
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 79, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [topView addSubview:lineView];
    
    
    ////////-----80
    
    
    //收款
    UIButton *btnCard = [self homeSetBtn:CGRectMake(APP_VIEW_WIDTH/2+(APP_VIEW_WIDTH/2 - width)/2, 5, width, height) imageName:@"checkout" view:self.contentView tag:1000];
    UILabel *lbCard = [self homeSetLabel:CGRectMake(APP_VIEW_WIDTH/2, btnCard.frame.size.height+btnCard.frame.origin.y, APP_VIEW_WIDTH/2, 20) title:@"收款" View:self.contentView];
    lbCard.textAlignment = NSTextAlignmentCenter;
    
    
    yPosition = yPosition+80;
    
    float lbCenterY = yPosition+height+lbOffset+10;
    
    //验证码
    UIButton *btn1100 = [self homeSetBtn:CGRectMake(xPosition, yPosition, width, height) imageName:@"verificationCode" view:self.contentView tag:1100];
    
    UILabel *label1100 = [self homeSetLabel:CGRectMake(xPosition, yPosition+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"验证码" View:self.contentView];
    label1100.center = CGPointMake(btn1100.frame.origin.x + btn1100.frame.size.width/2, lbCenterY);
    
    //退款
    UIButton *btn1101 = [self homeSetBtn:CGRectMake(xPosition + (width + xOffset), yPosition, width, height) imageName:@"iconRefund" view:self.contentView tag:1101];

    UILabel *label1101 = [self homeSetLabel:CGRectMake(xPosition + (width + xOffset), yPosition+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"退款处理" View:self.contentView];
    label1101.center = CGPointMake(btn1101.frame.origin.x + btn1100.frame.size.width/2, lbCenterY);
    
    //我的消息
    UIButton *btn1102 = [self homeSetBtn:CGRectMake(xPosition + 2*(width + xOffset), yPosition, width, height) imageName:@"myMessageImage" view:self.contentView tag:1102];
    
    UILabel *label1102 = [self homeSetLabel:CGRectMake(xPosition + 2*(width + xOffset)-10,  yPosition+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"我的消息" View:self.contentView];
    label1102.center = CGPointMake(btn1102.frame.origin.x + btn1100.frame.size.width/2, lbCenterY);
    
    
    
    yPosition = yPosition + height + yOffset + 35;
    //营销活动
    lbCenterY = yPosition+height+lbOffset+10;
    
    UIButton* btnActivity = [self homeSetBtn:CGRectMake(xPosition,  yPosition, width, height) imageName:@"activityImage" view:self.contentView tag:1200];
    UILabel *label1200 = [self homeSetLabel:CGRectMake(xPosition-10, btnActivity.frame.origin.y+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"营销活动" View:self.contentView];
    label1200.center = CGPointMake(btnActivity.frame.origin.x + btnActivity.frame.size.width/2, lbCenterY);

    
    //账本
    UIButton* btnCheck = [self homeSetBtn:CGRectMake(xPosition + (width + xOffset), yPosition, width, height) imageName:@"HQbookImage" view:self.contentView tag:1201];
    UILabel *label1201 = [self homeSetLabel:CGRectMake(xPosition + (width + xOffset)-10, btnCheck.frame.origin.y+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"惠圈账本" View:self.contentView];
    label1201.center = CGPointMake(btnCheck.frame.origin.x + btnCheck.frame.size.width/2, lbCenterY);

    
    //我的订单
    UIButton* btnNotice = [self homeSetBtn:CGRectMake(xPosition + 2*(width + xOffset), yPosition, width, height) imageName:@"myOrderImage" view:self.contentView tag:1202];
    UILabel *label1202 = [self homeSetLabel:CGRectMake(xPosition + 2*(width + xOffset)-10, btnNotice.frame.origin.y+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:@"我的订单" View:self.contentView];
    label1202.center = CGPointMake(btnNotice.frame.origin.x + btnNotice.frame.size.width/2, lbCenterY);
    
    
    
    UIView *lineView2 = [[UIView alloc] initWithFrame:CGRectMake(0, 290, APP_VIEW_WIDTH, 1)];
    lineView2.backgroundColor = APP_CELL_LINE_COLOR;
    [topView addSubview:lineView2];
    
//    [self setBottomView];
    
}

- (void)removeBottomView {
    if (self.bottomView != nil) {
        [self.bottomView removeFromSuperview];
        
    }
    
}

- (void)setBottomView {
    
    [self removeBottomView];
    
    float xPosition = 42;
    float yPosition = 26;
    
    float width = 40;
    float height = 40;
    
    float yOffset = 15;
    float lbOffset = 5;
    float xOffset = (APP_VIEW_WIDTH - 3*width - 2*xPosition)/2;
    
    
    self.bottomView = [[UIView alloc] initWithFrame:CGRectMake(0, 291, APP_VIEW_WIDTH, APP_VIEW_WIDTH)];
    self.bottomView.backgroundColor = [UIColor whiteColor];
    [self.contentView addSubview:self.bottomView];
    
//    NSArray *eduImageAry = [NSArray arrayWithObjects:@"", nil];
    NSArray *bottomArray = [NSArray arrayWithObjects:@"开班",@"招生启示",@"课程表",@"荣誉墙",@"名师堂",@"校长之语",@"学员之星",@"家长点评",@"学员报名", nil];
    int tTag = 1300;
    int k = 0;
    
    
    for (int i=0; i<3; i++) {
        for (int j=0; j<3; j++) {
            NSString *imageStr = [NSString stringWithFormat:@"ivEDU0%d", k];
            UIButton *button = [self homeSetBtn:CGRectMake(xPosition, yPosition, width, height) imageName:imageStr view:self.bottomView tag:tTag+j];
            button.backgroundColor = [UIColor clearColor];
            UILabel *label = [self homeSetLabel:CGRectMake(xPosition, yPosition+height+lbOffset, (APP_VIEW_WIDTH-42*2)/3, 20) title:bottomArray[k] View:self.bottomView];
            label.center = CGPointMake(button.frame.origin.x + button.frame.size.width/2, label.frame.origin.y+10);
            label.backgroundColor = [UIColor clearColor];
            xPosition = xPosition + (width + xOffset);
            
            k ++;
        }
        tTag = tTag+100;
        
        xPosition = 42;
        yPosition = yPosition + height + yOffset + 25;
    }

    
}


- (UIViewControllerEx *)viewController
{
    for (UIView* next = [self superview]; next; next = next.superview)
    {
        UIResponder *nextResponder = [next nextResponder];
        if ([nextResponder isKindOfClass:[UIViewController class]])
        {
            return (UIViewControllerEx *)nextResponder;
        }
    }
    return nil;
}

//用户浏览量
-(void)cellGotoCountVc{
    if (self.homeDelegate!= nil) {
        [self.homeDelegate gotoCountVC];
    }
    
}

// 收款按钮方法
- (void)btnCardClick{
    
    BMSQ_CollectionMoneyViewController * collectionVC = [[BMSQ_CollectionMoneyViewController alloc] init];
    collectionVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:collectionVC animated:YES];
    
}
// 验证码按钮方法
- (void)btnCuponClick{

//    [[self viewController].tabBarController setSelectedIndex:2];
    BMSQ_VerificationCodeViewController * verificationVC = [[BMSQ_VerificationCodeViewController alloc] init];
     verificationVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:verificationVC animated:YES];
}

#pragma mark 我的消息
- (void)btnCashClick
{
    BMSQ_MyNoticeController *pushVC = [[BMSQ_MyNoticeController alloc] init];
    pushVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:pushVC animated:YES];
}

#pragma mark 营销活动
- (void)btnActivityClick
{
    BMSQ_newMarketingViewController * activityVC = [[BMSQ_newMarketingViewController alloc] init];
    activityVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:activityVC animated:YES];

}


#pragma mark 惠圈对账
- (void)gotoHQAccountBook{
    
    HQAccountBookViewController *VC = [[HQAccountBookViewController alloc] init];
    VC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:VC animated:YES];
    
    
}

//我的消息
- (void)didClickNoticeButton{
    BMSQ_MyNoticeController *pushVC = [[BMSQ_MyNoticeController alloc] init];
    pushVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:pushVC animated:YES];
}

// 我的订单
-(void)ClickSetter{
    if (self.homeDelegate!= nil) {
        [self.homeDelegate gotoOrderVC];
    }
    
}

#pragma mark ---- 课程表
- (void)gotoSchedule
{
    BMSQ_ScheduleViewController * scheduleVC = [[BMSQ_ScheduleViewController alloc] init];
    scheduleVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:scheduleVC animated:YES];
    
}
#pragma mark ---- 名师堂
- (void)gotoStarTeacher
{
//    BMSQ_StarTeacherViewController * starVC = [[BMSQ_StarTeacherViewController alloc] init];
//    starVC.hidesBottomBarWhenPushed = YES;
//    [[self viewController].navigationController pushViewController:starVC animated:YES];
    
    BMSQ_StarTeacherListViewController * starVC = [[BMSQ_StarTeacherListViewController alloc] init];
    starVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:starVC animated:YES];
    
}
#pragma mark ---- 校长之语
- (void)gotoHeadmaster
{
    BMSQ_HeadmasterViewController * headmasterVC = [[BMSQ_HeadmasterViewController alloc] init];
    headmasterVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:headmasterVC animated:YES];
}
#pragma mark ---- 学员之星
- (void)gotoStarStuender
{
    BMSQ_TeacherDetailViewController * VC = [[BMSQ_TeacherDetailViewController alloc] init];
    VC.number = 2;
    VC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:VC animated:YES];
}
#pragma mark ---- 家长点评
- (void)gotoparents
{
    BMSQ_ParentCommentViewController * parentVC = [[BMSQ_ParentCommentViewController alloc] init];
    parentVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:parentVC animated:YES];
    
}
#pragma mark ------ 学员报名
- (void)gotoStudentSignUp
{
    BMSQ_StudentSignUpViewController * signUp = [[BMSQ_StudentSignUpViewController alloc] init];
    signUp.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:signUp animated:YES];
}

#pragma mark ------ 荣誉墙
- (void)gotoHonorWall
{
    BMSQ_HonorViewController * HonorVC = [[BMSQ_HonorViewController alloc] init];
    HonorVC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:HonorVC animated:YES];
}

//开班
- (void)gotoOpenClassVC {
    OpenClassViewController *VC = [[OpenClassViewController alloc] init];
    VC.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:VC animated:YES];
    
}
//退款
- (void)btnRefundClick {
    RefundListViewController *vc = [[RefundListViewController alloc] init];
    vc.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:vc animated:YES];

}
//招生启示
- (void)gotoEnrolStudentsVC {
    EnrolStudentsViewController *vc = [[EnrolStudentsViewController alloc] init];
    vc.hidesBottomBarWhenPushed = YES;
    [[self viewController].navigationController pushViewController:vc animated:YES];
}

- (void)btnClick:(UIButton *)button {
    int btnTag = (int)button.tag;

    switch (btnTag) {
        case 1000: //收款
            [self btnCardClick];
            break;
        case 1100: //验证码
            [self btnCuponClick];
            break;
        case 1101: //退款
            [self btnRefundClick];
            break;
        case 1102: //我的消息
            [self btnCashClick];
            break;
        case 1200: //营销活动
            [self btnActivityClick];
            break;
        case 1201: //惠圈账本
            [self gotoHQAccountBook];
            break;
        case 1202: //我的订单
            [self ClickSetter];
            break;
        case 1300://开班
            [self gotoOpenClassVC];
            break;
        case 1301://招生启示
            [self gotoEnrolStudentsVC];
            break;
        case 1302://课程表
            [self gotoSchedule];
            break;
        case 1400://荣誉墙
            [self gotoHonorWall];
            break;
        case 1401://名师堂
            [self gotoStarTeacher];
            break;
        case 1402://校长之语
            [self gotoHeadmaster];
            break;
        case 1500://学员之星
            [self gotoStarStuender];
            break;
        case 1501://家长点评
            [self gotoparents];
            break;
        case 1502://学员报名
            [self gotoStudentSignUp];
            break;
            
        default:
            break;
    }
    
    
}


- (UIButton *)homeSetBtn:(CGRect)frame imageName:(NSString *)imageName  view:view tag:(int)tag {
    UIButton* button = [[UIButton alloc]initWithFrame:frame];
    button.tag = tag;
    [button setBackgroundImage:[UIImage imageNamed:imageName] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];
    [view addSubview:button];
    return button;
}


- (UILabel *)homeSetLabel:(CGRect)frame title:title View:view{
    UILabel* label = [[UILabel alloc]initWithFrame:frame];
    [label setBackgroundColor:[UIColor clearColor]];
    [label setText:title];
    [label setTextAlignment:NSTextAlignmentCenter];
    [label setFont:[UIFont systemFontOfSize:13.f]];
    [label setTextColor:UICOLOR(84, 84, 84, 1.0)];
    [view addSubview:label];
    return label;
}


@end
