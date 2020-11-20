//
//  BMSQ_seleDateView.m
//  BMSQC
//
//  Created by liuqin on 16/3/31.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_seleDateView.h"



@interface BMSQ_seleDateView ()

@property (nonatomic, strong)UILabel *dateLabel;

@end


@implementation BMSQ_seleDateView

-(id)initWithFrame:(CGRect)frame{
    
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        UIView *bgView = [[UIView alloc]initWithFrame:frame];
        bgView.backgroundColor = [UIColor grayColor];
        bgView.alpha = 0.5;
        [self addSubview:bgView];
        
        bgView.userInteractionEnabled = YES;
        
//        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(hiddenSelf)];
//        [bgView addGestureRecognizer:tapGesture];
        
        
        UIView *dateView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-20, 250)];
        dateView.layer.cornerRadius = 5;
        dateView.layer.masksToBounds = YES;
        dateView.backgroundColor = [UIColor whiteColor];
        [self addSubview:dateView];
        
        
        dateView.center = CGPointMake(APP_VIEW_WIDTH/2, APP_VIEW_HEIGHT/2);
        
        
        
        
        self.remakLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, dateView.frame.size.width-20, 20)];
        self.remakLabel.font = [UIFont systemFontOfSize:15];
        self.remakLabel.textColor = APP_TEXTCOLOR;
        self.remakLabel.textAlignment = NSTextAlignmentCenter;
        [dateView addSubview:self.remakLabel];
        

        
        
        self.dateLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 20, dateView.frame.size.width-20, 20)];
        self.dateLabel.font = [UIFont systemFontOfSize:15];
        self.dateLabel.textColor = APP_NAVCOLOR;
        self.dateLabel.textAlignment = NSTextAlignmentCenter;
        [dateView addSubview:self.dateLabel];

        
        UIDatePicker *datePicker = [ [ UIDatePicker alloc] init];
        datePicker.frame = CGRectMake(0, 40, dateView.frame.size.width, dateView.frame.size.height-40);
        datePicker.datePickerMode = UIDatePickerModeDate;
        datePicker.tag = 100;
        [dateView addSubview:datePicker];
        
      
        
        
        [datePicker addTarget:self action:@selector(changeTime:) forControlEvents:UIControlEventValueChanged];
        
        
        
        
        UIButton *cancelBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 120, 40)];
        cancelBtn.backgroundColor = UICOLOR(179, 179, 179, 1);
        [cancelBtn setTitle:@"取消" forState:UIControlStateNormal];
        [cancelBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        cancelBtn.layer.cornerRadius = 4;
        cancelBtn.layer.masksToBounds = YES;
        cancelBtn.tag = 300;
        [self addSubview:cancelBtn];
        
        
        
        UIButton *submitBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 120, 40)];
        submitBtn.backgroundColor = UICOLOR(242, 171, 13, 1);
        [submitBtn setTitle:@"确定" forState:UIControlStateNormal];
        [submitBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        submitBtn.layer.cornerRadius = 4;
        submitBtn.layer.masksToBounds = YES;
        submitBtn.tag = 400;
        [self addSubview:submitBtn];
        
        cancelBtn.center = CGPointMake(APP_VIEW_WIDTH/4, APP_VIEW_HEIGHT/2+dateView.frame.size.height/2+50);
        submitBtn.center = CGPointMake(APP_VIEW_WIDTH/4*3, APP_VIEW_HEIGHT/2+dateView.frame.size.height/2+50);
        
        
        [submitBtn addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        [cancelBtn addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        
        
    }
    return self;
}

-(void)changeTime:(UIDatePicker *)picker{
    
    
    NSDate *select = [picker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *dateAndTime =  [dateFormatter stringFromDate:select];
    NSLog(@"--->%@",dateAndTime);
    
    self.dateLabel.text = dateAndTime;
    
}

-(void)clickButton:(UIButton *)button{
    
    if (button.tag == 400) {  //确定
//        
        if ([self.delegate respondsToSelector:@selector(seleDate:isSubmit:status:)]) {
            [self.delegate seleDate:self.dateLabel.text isSubmit:YES status:self.status];
        }
        
    }else{
        if ([self.delegate respondsToSelector:@selector(seleDate:isSubmit:status:)]) {
            [self.delegate seleDate:self.dateLabel.text isSubmit:NO status:self.status];
        }
    }
    
    
    
    
}



@end
