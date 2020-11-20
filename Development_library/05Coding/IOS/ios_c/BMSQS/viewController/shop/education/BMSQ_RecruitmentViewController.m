//
//  BMSQ_RecruitmentViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_RecruitmentViewController.h"
#import "BMSQ_RecStuViewController.h"
#import "MobClick.h"
@interface BMSQ_RecruitmentViewController ()

@end

@implementation BMSQ_RecruitmentViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"Recruitment"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"Recruitment"];
}

- (void)viewDidLoad {
    [super viewDidLoad];
 

    [super setNavBackItem];
    [self setNavTitle:@"招生"];


    UIImageView *bgImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    NSString *recruitUrl = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[self.shopRecruitInfo objectForKey:@"recruitUrl"]];
    [bgImage sd_setImageWithURL:[NSURL URLWithString:recruitUrl] placeholderImage:[UIImage imageNamed:@"noDataImage"]];
    [self.view addSubview:bgImage];

    UIButton *bottomBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, APP_VIEW_HEIGHT-50, APP_VIEW_WIDTH, 50)];
    [bottomBtn setTitle:@"报名" forState:UIControlStateNormal];
    bottomBtn.backgroundColor = APP_NAVCOLOR;
    [bottomBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    bottomBtn.layer.cornerRadius=6;
    bottomBtn.layer.masksToBounds = YES;
    [self.view addSubview:bottomBtn];
    
    [bottomBtn addTarget:self action:@selector(clickRectuitButton) forControlEvents:UIControlEventTouchUpInside];
}

-(void)clickRectuitButton{
    
    BMSQ_RecStuViewController *vc = [[BMSQ_RecStuViewController alloc]init];
    vc.shopCode = self.shopCode;
    vc.isSign = YES;
    [self.navigationController pushViewController: vc animated:YES];
    

    
}

@end
