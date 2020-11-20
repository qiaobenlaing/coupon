//
//  BMSQ_SettlerViewController.m
//  BMSQS
//
//  Created by liuqin on 15/11/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_SettlerViewController.h"
#import "BMSQ_SetInfoShopViewController.h"


@interface BMSQ_SettlerViewController ()

@end

@implementation BMSQ_SettlerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationItem setTitle:@"我要入驻"];
    
//    [[NSNotificationCenter defaultCenter] ]
    
    
    
    
    UIImageView *backImageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 60,APP_VIEW_WIDTH, APP_VIEW_HEIGHT/4*3-30)];
    [backImageView setImage:[UIImage imageNamed:@"settled_open"]];
    [self.view addSubview:backImageView];
    
    
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(15, backImageView.frame.origin.y+backImageView.frame.size.height+10,APP_VIEW_WIDTH-30, 40)];
    button.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [button setTitle:@"去开店" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    button.layer.cornerRadius = 5;
    button.layer.masksToBounds = YES;
    [self.view addSubview:button];
    
    [button addTarget:self action:@selector(gotoNext) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
    UIButton *phoneBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, button.frame.origin.y+button.frame.size.height+5, APP_VIEW_WIDTH, 25)];
    [phoneBtn setTitleColor: UICOLOR(113, 113, 112, 1.0) forState:UIControlStateNormal];
    phoneBtn.backgroundColor = [UIColor clearColor];
    [phoneBtn setTitle:@"联系惠圈：400-04-95588" forState:UIControlStateNormal];
    phoneBtn.titleLabel.font = [UIFont systemFontOfSize:16];
    [self.view addSubview:phoneBtn];
    [phoneBtn addTarget:self action:@selector(tel) forControlEvents:UIControlEventTouchUpInside];
    
    
}

-(void)tel{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"telprompt://400-04-95588"]];

}

-(void)gotoNext{
    
    BMSQ_SetInfoShopViewController *vc = [[BMSQ_SetInfoShopViewController alloc]init];
    vc.isLogin = self.isLogin;
    
    
    
    
    [self.navigationController pushViewController:vc animated:YES];
    
}
- (void)btnBackClicked{
    
    if (self.isLogin) {
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        [self dismissViewControllerAnimated:YES completion:nil];
    }
}

@end
