//
//  BMSQ_PhoneViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_PhoneViewController.h"

@interface BMSQ_PhoneViewController ()

@property (nonatomic, strong)UIScrollView *phoneScView;


@property (nonatomic, strong)UIButton *billButton;
@property (nonatomic, strong)UIButton *flowButton;
@property (nonatomic, strong)UILabel *chageLabel;


@end

@implementation BMSQ_PhoneViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setNavBackItem];
    [self setNavigationBar];
    [self setNavTitle:@"手机充值"];

    UITextField *phoneText = [[UITextField alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 50)];
    phoneText.backgroundColor = [UIColor whiteColor];
    phoneText.placeholder = @"   请输入需要充值的手机号码";
    [self.view addSubview:phoneText];
    
    UIView *phoneView = [[UIView alloc]initWithFrame:CGRectMake(0, phoneText.frame.origin.y+phoneText.frame.size.height+1, APP_VIEW_WIDTH, 40)];
    phoneView.backgroundColor = [UIColor whiteColor];
    UILabel *phoneLable = [[UILabel alloc]initWithFrame:CGRectMake(30, 0, APP_VIEW_WIDTH-30, 40)];
    phoneLable.text = @"本手机号签约工行永不停机";
    phoneLable.font = [UIFont systemFontOfSize:11];
    phoneLable.textColor = APP_TEXTCOLOR;
    [phoneView addSubview:phoneLable];
    
    
    UIButton *phoneBtn = [[UIButton alloc]initWithFrame:CGRectMake(15, 15, 10, 10)];
    phoneBtn.layer.borderWidth = 0.5;
    phoneBtn.layer.borderColor = [APP_TEXTCOLOR CGColor];
    phoneBtn.layer.cornerRadius = 2;
    phoneBtn.layer.masksToBounds = YES;
    [phoneView addSubview:phoneBtn];
    
    
    
    
    [self.view addSubview:phoneView];
    
    UIView *seleView = [[UIView alloc]initWithFrame:CGRectMake(0, phoneView.frame.origin.y+phoneView.frame.size.height+1, APP_VIEW_WIDTH, 45)];
    seleView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:seleView];
    seleView.userInteractionEnabled = YES;
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 5, 1, 35)];
    lineView.backgroundColor = self.view.backgroundColor;
    [seleView addSubview:lineView];
    
    
    self.billButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2, seleView.frame.size.height)];
    [self.billButton setTitle:@"充话费" forState:UIControlStateNormal];
    [self.billButton setTitleColor:APP_NAVCOLOR forState:UIControlStateSelected];
    [self.billButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    self.billButton.titleLabel.font = [UIFont systemFontOfSize:14];
    self.billButton.tag  = 100;
    self.billButton.selected = YES;
    [seleView addSubview:self.billButton];
    
    self.flowButton = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, seleView.frame.size.height)];
    [self.flowButton setTitle:@"充流量" forState:UIControlStateNormal];
    [self.flowButton setTitleColor:APP_NAVCOLOR forState:UIControlStateSelected];
    [self.flowButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    self.flowButton.titleLabel.font = [UIFont systemFontOfSize:14];
    self.flowButton.tag = 200;
    [seleView addSubview:self.flowButton];
    
    self.chageLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/2-100, 1)];
    self.chageLabel.backgroundColor = APP_NAVCOLOR;
    self.chageLabel.center = CGPointMake(self.billButton.frame.size.width/2, self.billButton.frame.size.height-1);
    [seleView addSubview:self.chageLabel];
    [seleView bringSubviewToFront:self.chageLabel];
    
    [self.billButton addTarget:self action:@selector(clickSeleButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.flowButton addTarget:self action:@selector(clickSeleButton:) forControlEvents:UIControlEventTouchUpInside];

    
    
    self.phoneScView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, seleView.frame.origin.y+seleView.frame.size.height+1, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - (seleView.frame.origin.y+seleView.frame.size.height))];
    self.phoneScView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.phoneScView];
    
    
    for (int i=0; i<6; i++) {
        int x = i%3;
        int y = i/3;
        
        
        PhoneButton *phoneBtn = [[PhoneButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/3*x, y*80, APP_VIEW_WIDTH/3,100) title:@"1000.00元\n售价998.00元"];
        phoneBtn.tag = 100+i;
        [self.phoneScView addSubview:phoneBtn];
        
    }
    
    
    
    

}

-(void)clickSeleButton:(UIButton *)button{
    
    
    if (button.tag == 100) {  //充话费
        self.billButton.selected = YES;
        self.flowButton.selected = NO;
        
        [UIView animateWithDuration:0.3 animations:^{
            self.chageLabel.center = CGPointMake(button.frame.size.width/2, self.billButton.frame.size.height-1);

        }];
        

        
    }else if (button.tag == 200){//充流量
        self.billButton.selected = NO;
        self.flowButton.selected = YES;
        [UIView animateWithDuration:0.3 animations:^{
            self.chageLabel.center = CGPointMake(button.frame.size.width/2+APP_VIEW_WIDTH/2, self.billButton.frame.size.height-1);
            
        }];
        
    }
}

@end

@implementation PhoneButton

-(id)initWithFrame:(CGRect)frame title:(NSString *)title{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
       
        
        UIButton *phontBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, frame.size.width-30 ,50 )];
        
        phontBtn.layer.cornerRadius = 4;
        phontBtn.layer.masksToBounds = YES;
        phontBtn.titleLabel.font = [UIFont systemFontOfSize:11];
        phontBtn.titleLabel.numberOfLines = 2;
        [phontBtn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        
        phontBtn.layer.borderColor = [APP_TEXTCOLOR CGColor];
        phontBtn.layer.borderWidth = 1;
        phontBtn.layer.cornerRadius = 4;
        phontBtn.layer.masksToBounds = YES;
        phontBtn.titleLabel.textAlignment = NSTextAlignmentCenter;
        
        [phontBtn setTitle:title forState:UIControlStateNormal];
        phontBtn.center = CGPointMake(frame.size.width/2, frame.size.height/2);
        
        [self addSubview:phontBtn];
        
        
        
    }
    return self;
}

@end
