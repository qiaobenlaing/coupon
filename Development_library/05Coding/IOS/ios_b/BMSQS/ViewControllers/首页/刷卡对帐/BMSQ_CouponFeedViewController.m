//
//  BMSQ_CouponFeedViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/26.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_CouponFeedViewController.h"

#import "SVProgressHUD.h"

@interface BMSQ_CouponFeedViewController ()

@property (nonatomic, strong)NSArray *couponTypeS;

@property (nonatomic, strong)UIView *selePickerView;
@property (nonatomic, strong)UILabel *seleDateLable;

@property (nonatomic, strong)UIScrollView *scrollView;

@property (nonatomic, strong)UILabel *startlabel;
@property (nonatomic, strong)UILabel *endlabel;

@property (nonatomic, assign)float h;

@property (nonatomic, assign)BOOL isStartDate;

@property (nonatomic, strong)NSString *seleDate;

@end

@implementation BMSQ_CouponFeedViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavTitle:@"优惠券对账"];
    [self setNavBackItem];
    
    self.couponTypeS = @[@"",@"N元购",@"",@"抵扣券",@"折扣券",@"实物券",@"体验券"];
  
    
    
    
    
    
    self.scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y)];
    self.scrollView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.scrollView];
    
    
    NSDate* now = [NSDate date];
    NSDateFormatter* fmt = [[NSDateFormatter alloc] init];
    fmt.locale = [[NSLocale alloc] initWithLocaleIdentifier:@"zh_CN"];
    fmt.dateFormat = @"yyyy-MM-dd";
    NSString* dateString = [fmt stringFromDate:now];
    
    UIView *fristView = [[UIView alloc]initWithFrame:CGRectMake(0, 15, APP_VIEW_WIDTH, 81)];
    fristView.backgroundColor = [UIColor clearColor];
    [self.scrollView addSubview:fristView];
    
    self.startlabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
    self.startlabel.backgroundColor = [UIColor whiteColor];
    self.startlabel.text = [NSString stringWithFormat:@"%@",dateString];;
    self.startlabel.font = [UIFont systemFontOfSize:13.f];
    self.startlabel.tag = 100;
    self.startlabel.textAlignment = NSTextAlignmentCenter;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addPIck:)];
    self.startlabel.userInteractionEnabled  = YES;
    [self.startlabel addGestureRecognizer:tap];
    [fristView addSubview:self.startlabel];
    
    self.endlabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 41, APP_VIEW_WIDTH, 40)];
    self.endlabel.backgroundColor = [UIColor whiteColor];
    self.endlabel.text =[NSString stringWithFormat:@"%@",dateString];
    self.endlabel.font = [UIFont systemFontOfSize:13.f];
    self.endlabel.textAlignment = NSTextAlignmentCenter;
    self.endlabel.tag = 200;
    UITapGestureRecognizer *tap1 = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(addPIck:)];

    self.endlabel.userInteractionEnabled  = YES;
    [self.endlabel addGestureRecognizer:tap1];

    [fristView addSubview:self.endlabel];
    
    UIButton *button = [[UIButton alloc]initWithFrame:CGRectMake(10, fristView.frame.origin.y+fristView.frame.size.height +15, APP_VIEW_WIDTH-20, 40)];
    button.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    button.layer.cornerRadius = 5;
    button.layer.masksToBounds = YES;
    [button setTitle:@"统计" forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont systemFontOfSize:14.f];
    [button setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [button addTarget:self action:@selector(getCouponBill) forControlEvents:UIControlEventTouchUpInside];
    [self.scrollView addSubview:button];
    
    self.h =button.frame.origin.y+button.frame.size.height +15;
    
    
    
    
    
    self.selePickerView = [[UIView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH/2-100, APP_VIEW_HEIGHT/2-90, 280, 200)];
    self.selePickerView.backgroundColor = [UIColor whiteColor];
    self.selePickerView.layer.borderWidth = 0.5;
    self.selePickerView.layer.borderColor = [[UIColor grayColor]CGColor];
    self.selePickerView.layer.cornerRadius = 0.3;
    self.selePickerView.layer.masksToBounds = YES;
    self.selePickerView.center = self.view.center;
    [self.view addSubview:self.selePickerView];
    
    [self.view bringSubviewToFront:self.selePickerView];
    self.seleDateLable = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 280, 50)];
    self.seleDateLable.font = [UIFont systemFontOfSize:13.f];
    self.seleDateLable.textColor = UICOLOR(182, 0, 12, 1.0);
    self.seleDateLable.text = @"      选择日期(时间)为:0000-00-00";
    [self.selePickerView addSubview:self.seleDateLable];
    
    UIButton *delebutton = [[UIButton alloc]initWithFrame:CGRectMake(240,0 ,40 ,40)];
    delebutton.backgroundColor = [UIColor clearColor];
    [delebutton setTitle:@"关闭" forState:UIControlStateNormal];
    delebutton.titleLabel.font = [UIFont systemFontOfSize:13.f];
    [delebutton setTitleColor:UICOLOR(182, 0, 12, 1.0) forState:UIControlStateNormal];
    [self.selePickerView addSubview:delebutton];
    [delebutton addTarget:self action:@selector(closePicker) forControlEvents:UIControlEventTouchUpInside];
    
    
    NSDate *tempDate = [NSDate date];
    UIDatePicker *datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 50, 280, 150)];
    datePicker.backgroundColor = [UIColor whiteColor];
    datePicker.layer.borderWidth = 0.5;
    datePicker.layer.borderColor = [[UIColor grayColor]CGColor];
    datePicker.layer.cornerRadius = 0.3;
    datePicker.layer.masksToBounds = YES;    // 设置时区
    [datePicker setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
    // 设置当前显示时间
    [datePicker setDate:tempDate animated:YES];

    // 设置UIDatePicker的显示模式
    [datePicker setDatePickerMode:UIDatePickerModeDate];
    // 当值发生改变的时候调用的方法
    [datePicker addTarget:self action:@selector(datePickerValueChanged:) forControlEvents:UIControlEventValueChanged];
    [self.selePickerView addSubview:datePicker];
    
    
    self.selePickerView.hidden = YES;


    
    [self getCouponBill];

  
    
}



-(void)datePickerValueChanged:(UIDatePicker *)picker{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [outputFormatter setTimeZone:timeZone];
    [outputFormatter setDateFormat:@"yyyy-MM-dd"];
    self.seleDate = [outputFormatter stringFromDate:picker.date];
    self.seleDateLable.text = [NSString stringWithFormat:@"      选择日期为:%@",self.seleDate];
    
    
}

-(void)closePicker{
    self.selePickerView.hidden = YES;
    
    if (self.isStartDate) {
        self.startlabel.text = [NSString stringWithFormat:@"%@",self.seleDate];
    }else{
        self.endlabel.text = [NSString stringWithFormat:@"%@",self.seleDate];
        
    }

}
-(void)addPIck:(UIGestureRecognizer *)gesture{
    UIView *gesView = gesture.view;
    NSLog(@"gesView = %d",gesView.tag);
    self.selePickerView.hidden = NO;
    
    
    if (gesView.tag == 100) { //开始时间
        self.isStartDate = YES;
    }else{                     //结束时间
        self.isStartDate = NO;
    }

}
-(void)refreshView:(NSArray *)arry{

    for (int i = 0; i< 5; i++) {
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, i*122+i*15 + self.h, APP_VIEW_WIDTH, 122)];
        bgView.backgroundColor = [UIColor clearColor];
        [self.scrollView addSubview:bgView];
        NSDictionary *dic = [arry objectAtIndex:i];
        
        int type = [[dic objectForKey:@"couponType"]intValue];
        
        UILabel *label11 = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
        label11.backgroundColor = [UIColor whiteColor];
        label11.text =[NSString stringWithFormat:@"   %@",[self.couponTypeS objectAtIndex:type]];
        label11.font = [UIFont systemFontOfSize:13.f];
        [bgView addSubview:label11];
        
        UILabel *label12 = [[UILabel alloc]initWithFrame:CGRectMake(0, 41, APP_VIEW_WIDTH, 40)];
        label12.backgroundColor = [UIColor whiteColor];
        label12.text = [NSString stringWithFormat:@"   共计  %@元",[dic objectForKey:@"usedAmount"]];
        label12.font = [UIFont systemFontOfSize:13.f];
        [bgView addSubview:label12];
        
        UILabel *label13 = [[UILabel alloc]initWithFrame:CGRectMake(0, 82, APP_VIEW_WIDTH, 40)];
        label13.backgroundColor = [UIColor whiteColor];
        label13.text =[NSString stringWithFormat:@"   其中惠圈出  %@元",[dic objectForKey:@"hqAmount"]];
        label13.font = [UIFont systemFontOfSize:13.f];
        [bgView addSubview:label13];
        
        
        self.scrollView.contentSize = CGSizeMake(APP_VIEW_WIDTH, i*122+i*15+self.h*2 );
        
    }
    
    
}
-(void)getCouponBill{
    
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:self.startlabel.text forKey:@"startDate"];
    [params setObject:self.endlabel.text forKey:@"endDate"];
    
    NSString* vcode = [gloabFunction getSign:@"getCouponBill" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:@""];
    __weak typeof(self) weakSelf = self;
    
    [self.jsonPrcClient invokeMethod:@"getCouponBill" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        [weakSelf refreshView:responseObject];

    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

@end
