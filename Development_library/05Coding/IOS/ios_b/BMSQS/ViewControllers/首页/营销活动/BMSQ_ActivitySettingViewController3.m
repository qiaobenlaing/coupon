//
//  BMSQ_ActivitySettingViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivitySettingViewController3.h"
#import "StringInputTableViewCell.h"
#import "BMSQ_ActivitySettingViewController2ViewController.h"


@interface BMSQ_ActivitySettingViewController3 ()<UITableViewDataSource,UITableViewDelegate>{
    
    BOOL isPrepayRequired;
    BOOL isRegisterRequired;

}


@property (strong ,nonatomic)UITextField *acountText;
@property (strong ,nonatomic)UITextField *payText;

@property (strong ,nonatomic)UIButton *payButton; //需要付费
@property (strong ,nonatomic)UIButton *applyButton; //需要报名

@property (strong ,nonatomic)UIView *secView;

@end

@implementation BMSQ_ActivitySettingViewController3

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:@"营销活动设置"];
    [self setNavBackItem];
  
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(payView:) name:@"setPayView" object:nil];
    
    UIView *fristView = [[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y +20, APP_VIEW_WIDTH, 40)];
    fristView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:fristView];
    
    
    UILabel *label1 = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 40)];
    label1.text = @"  活动人数限制";
    label1.backgroundColor = [UIColor clearColor];
    label1.font = [UIFont systemFontOfSize:12];
    [fristView addSubview:label1];
    
    UILabel *label2 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-40, 0, 40, 40)];
    label2.text = @"人";
    label2.backgroundColor = [UIColor clearColor];
    label2.font = [UIFont systemFontOfSize:12];
    [fristView addSubview:label2];
    
    
    self.acountText = [[UITextField alloc]initWithFrame:CGRectMake(90, 0, APP_VIEW_WIDTH-150, 40)];
    self.acountText.placeholder = @"输入限制人数(可不输入)";
    self.acountText.backgroundColor = [UIColor clearColor];
    self.acountText.textAlignment = NSTextAlignmentCenter;
    self.acountText.font = [UIFont systemFontOfSize:12];
    [fristView addSubview:self.acountText];
    
    self.applyButton = [[UIButton alloc]initWithFrame:CGRectMake(15, fristView.frame.origin.y+fristView.frame.size.height, 50, 50)];
    [self.applyButton setImage:[UIImage imageNamed:@"未选中"] forState:UIControlStateNormal];
    [self.applyButton setImage:[UIImage imageNamed:@"选中"] forState:UIControlStateSelected];
    self.applyButton.tag = 100;
    [self.applyButton addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.applyButton];

    UILabel *applyLabel = [[UILabel alloc]initWithFrame:CGRectMake(60, self.applyButton.frame.origin.y+5, 60, 40)];
    applyLabel.text = @"需要报名";
    applyLabel.backgroundColor = [UIColor clearColor];
    applyLabel.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:applyLabel];
    
    
    self.payButton = [[UIButton alloc]initWithFrame:CGRectMake(140, fristView.frame.origin.y+fristView.frame.size.height, 50, 50)];
    [self.payButton setImage:[UIImage imageNamed:@"未选中"] forState:UIControlStateNormal];
    [self.payButton setImage:[UIImage imageNamed:@"选中"] forState:UIControlStateSelected];
    self.payButton.tag = 200;
    [self.payButton addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.payButton];
    
    UILabel *payLabel = [[UILabel alloc]initWithFrame:CGRectMake(180, self.applyButton.frame.origin.y+5, 60, 40)];
    payLabel.text = @"需要预付费";
    payLabel.backgroundColor = [UIColor clearColor];
    payLabel.font = [UIFont systemFontOfSize:12];
    [self.view addSubview:payLabel];
    
    
    self.secView = [[UIView alloc]initWithFrame:CGRectMake(0, self.payButton.frame.origin.y+self.payButton.frame.size.height, APP_VIEW_WIDTH, 40)];
    self.secView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.secView];
    
    
    UILabel *label21 = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 80, 40)];
    label21.text = @"  预付金额";
    label21.backgroundColor = [UIColor clearColor];
    label21.font = [UIFont systemFontOfSize:12];
    [self.secView addSubview:label21];
    
    UILabel *label22 = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-40, 0, 40, 40)];
    label22.text = @"元";
    label22.backgroundColor = [UIColor clearColor];
    label22.font = [UIFont systemFontOfSize:12];
    [self.secView addSubview:label22];
    
    
    self.payText = [[UITextField alloc]initWithFrame:CGRectMake(90, 0, APP_VIEW_WIDTH-150, 40)];
    self.payText.placeholder = @"输入预付金额";
    self.payText.backgroundColor = [UIColor clearColor];
    self.payText.textAlignment = NSTextAlignmentCenter;
    self.payText.font = [UIFont systemFontOfSize:12];
    [self.secView addSubview:self.payText];
    
    UIButton *beforeBtn = [[UIButton alloc]initWithFrame:CGRectMake(10, self.secView.frame.origin.y+self.secView.frame.size.height+20, (APP_VIEW_WIDTH-40)/2, 40)];
    [beforeBtn setTitle:@"上一步" forState:UIControlStateNormal];
    [beforeBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [beforeBtn addTarget:self action:@selector(clickBefore) forControlEvents:UIControlEventTouchUpInside];
    beforeBtn.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [self.view addSubview:beforeBtn];
    
    UIButton *nextBtn = [[UIButton alloc]initWithFrame:CGRectMake(20+(APP_VIEW_WIDTH-40)/2, beforeBtn.frame.origin.y, (APP_VIEW_WIDTH-40)/2, 40)];
    [nextBtn setTitle:@"下一步" forState:UIControlStateNormal];
    [nextBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    nextBtn.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [nextBtn addTarget:self action:@selector(clickNext) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:nextBtn];
    
    self.secView.hidden = YES;
  
    
    
    
}


-(void)payView:(NSNotification *)noti{
    
    NSString *str = noti.object;
    if ([str intValue] == 0) {
        self.secView.hidden = YES;
    }else{
        self.secView.hidden = NO;
    }
    
    
}

-(void)clickBefore{
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)clickButton:(UIButton *)button{
    
    button.selected = !button.selected;
    
    if (button.tag == 100) {
        self.applyButton.selected = button.selected;
        
    }else if (button.tag == 200){
        self.payButton.selected = button.selected;
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"setPayView" object:[NSString stringWithFormat:@"%d",self.payButton.selected] ];
        
    }
    
}


- (void)clickNext {
    
    if(self.payButton.selected){
        if (self.payText.text.length==0) {
            
            UIAlertView *alerView = [[UIAlertView alloc]initWithTitle:nil message:@"请输入预付金额" delegate:nil cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
            [alerView show];
            return;
        }
    }

    
    NSMutableDictionary *mutableDic = [[NSMutableDictionary alloc]initWithDictionary:self.dataDic];
    [mutableDic setObject:self.acountText.text forKey:@"limitedParticipators"];
    [mutableDic setObject:[NSString stringWithFormat:@"%d",self.applyButton.selected]  forKey:@"isRegisterRequired"];// 1-需要；0-不需要
    [mutableDic setObject:[NSString stringWithFormat:@"%d",self.payButton.selected]  forKey:@"isPrepayRequired"];// 1-需要；0-不需要
    [mutableDic setObject:self.payText.text forKey:@"prePayment"];

    BMSQ_ActivitySettingViewController2ViewController *vc = [[BMSQ_ActivitySettingViewController2ViewController alloc]init];
    vc.dataDic = mutableDic;
    [self.navigationController pushViewController:vc animated:YES];

}

    @end
