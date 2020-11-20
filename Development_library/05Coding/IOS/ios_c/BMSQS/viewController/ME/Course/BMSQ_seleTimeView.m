//
//  BMSQ_seleTimeView.m
//  BMSQC
//
//  Created by liuqin on 16/3/31.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_seleTimeView.h"


@interface BMSQ_seleTimeView ()<UIPickerViewDataSource,UIPickerViewDelegate>


@property (nonatomic, strong)UIPickerView *pickView;

@property (nonatomic,strong)NSMutableArray *HArray;
@property (nonatomic,strong)NSMutableArray *MArray;

@property (nonatomic,strong)NSArray *weekArray;


@property (nonatomic, strong)UILabel *timeLabel;

@property (nonatomic, strong)UIView *dateView;



@property (nonatomic,strong)NSString *weekStr;

@property (nonatomic,strong)NSString *startH;
@property (nonatomic,strong)NSString *endH;
@property (nonatomic,strong)NSString *startM;
@property (nonatomic,strong)NSString *endM;


@end


@implementation BMSQ_seleTimeView


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
//        
//        
        self.dateView = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_HEIGHT/2-250/2, APP_VIEW_WIDTH-20, 250)];
        self.dateView.layer.cornerRadius = 5;
        self.dateView.layer.masksToBounds = YES;
        self.dateView.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.dateView];

        self.weekStr=@"周一";
        
        self.startH=@"00";
        self.endH=@"00";
        self.startM=@"00";
        self.endM=@"00";
        
        
        
        self.timeLabel= [[UILabel alloc]initWithFrame:CGRectMake(20, 0, self.dateView.frame.size.width-40, 30)];
         self.timeLabel.text = @"上课时间: 周一 00:00 00:00";
         self.timeLabel.font = [UIFont systemFontOfSize:13];
         self.timeLabel.textColor = APP_TEXTCOLOR;
         self.timeLabel.backgroundColor = [UIColor whiteColor];
        [self.dateView addSubview: self.timeLabel];
 
 
 
 /*
//        UILabel *endLabel = [[UILabel alloc]initWithFrame:CGRectMake(dateView.frame.size.width/2, 0, dateView.frame.size.width/2, 20)];
//        endLabel.text = @"结束时间: ";
//        endLabel.font = [UIFont systemFontOfSize:13];
//        endLabel.textColor = APP_TEXTCOLOR;
//        endLabel.textAlignment = NSTextAlignmentCenter;
//        [dateView addSubview:endLabel];
//        
//        
//        self.startLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 20, dateView.frame.size.width/2, 20)];
//        self.startLabel.font = [UIFont systemFontOfSize:15];
//        self.startLabel.textColor = APP_NAVCOLOR;
//        self.startLabel.textAlignment = NSTextAlignmentCenter;
//        [dateView addSubview:self.startLabel];
//        
//        self.endLabel = [[UILabel alloc]initWithFrame:CGRectMake(dateView.frame.size.width/2, 20, dateView.frame.size.width/2, 20)];
//        self.endLabel.font = [UIFont systemFontOfSize:15];
//        self.endLabel.textColor = APP_NAVCOLOR;
//        self.endLabel.textAlignment = NSTextAlignmentCenter;
//        [dateView addSubview:self.endLabel];
//        
//        
//        
//        UIDatePicker *StartdatePicker = [ [ UIDatePicker alloc] init];
//        StartdatePicker.frame = CGRectMake(20, 40, dateView.frame.size.width/2-40, dateView.frame.size.width-40);
//        StartdatePicker.datePickerMode = UIDatePickerModeTime;
//        StartdatePicker.tag = 100;
//        [dateView addSubview:StartdatePicker];
//        
//        UIDatePicker *enddatePicker = [ [ UIDatePicker alloc] init];
//        enddatePicker.frame = CGRectMake(20+dateView.frame.size.width/2, 40, dateView.frame.size.width/2-40, dateView.frame.size.width-40);
//        enddatePicker.datePickerMode = UIDatePickerModeTime;
//        enddatePicker.tag = 200;
//        [dateView addSubview:enddatePicker];
//        
//        
//        
//        [StartdatePicker addTarget:self action:@selector(changeTime:) forControlEvents:UIControlEventValueChanged];
//        [enddatePicker addTarget:self action:@selector(changeTime:) forControlEvents:UIControlEventValueChanged];
        
        
        
        
  
        
  */
        
        self.HArray = [[NSMutableArray alloc]init];
        self.MArray = [[NSMutableArray alloc]init];
        
        for (int i=6; i<24; i++) {
            [self.HArray addObject: i>9? [NSString stringWithFormat:@"%d",i]:[NSString stringWithFormat:@"0%d",i]];
        }
        
        for (int i=0; i<60; i++) {
            [self.MArray addObject:i>9? [NSString stringWithFormat:@"%d",i]:[NSString stringWithFormat:@"0%d",i]];
        }
        
        self.weekArray = @[@"周一",@"周二",@"周三",@"周四",@"周五",@"周六",@"周日"];
        
        self.pickView = [[UIPickerView alloc]initWithFrame:CGRectMake(10, 30, self.dateView.frame.size.width-20, self.dateView.frame.size.height-30)];
        self.pickView.delegate = self;
        self.pickView.dataSource = self;
        [self.dateView addSubview:self.pickView];

        [self.pickView reloadAllComponents];
        
        
        
        
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
        
        cancelBtn.center = CGPointMake(APP_VIEW_WIDTH/4,self.dateView.frame.origin.y+self.dateView.frame.size.height+50);
        submitBtn.center = CGPointMake(APP_VIEW_WIDTH/4*3,self.dateView.frame.origin.y+self.dateView.frame.size.height+50);
        
        
        [submitBtn addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        [cancelBtn addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];

        
        
    }
    return self;
}


//返回几列
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    
    return 5;
}
-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    if (component ==0) {
        return self.weekArray.count;
    }
    else if(component ==1 || component==3){
        return self.HArray.count;
    }else{
        return self.MArray.count;
    }
    
}
-(CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component{
    return self.pickView.frame.size.width/5-10;
}

-(UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view{
    
    if (!view) {
        view = [[UIView alloc]init];
        
    }
    
    
    if (component ==0) {
        UILabel *text = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, self.pickView.frame.size.width/5-10, 30)];
        text.backgroundColor = [UIColor whiteColor];
        text.text = [self.weekArray objectAtIndex:row];
        text.textAlignment = NSTextAlignmentCenter;
        [view addSubview:text];
    }
    else if(component ==1 || component==3){
        UILabel *text = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, self.pickView.frame.size.width/5-10, 30)];
        text.backgroundColor = [UIColor whiteColor];
        text.text = [self.HArray objectAtIndex:row];
        text.textAlignment = NSTextAlignmentCenter;
        [view addSubview:text];
        
    }
    else{
        
        UILabel *text = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, self.pickView.frame.size.width/5-10, 30)];
        text.backgroundColor = [UIColor whiteColor];
        text.text = [self.MArray objectAtIndex:row];
        text.textAlignment = NSTextAlignmentCenter;
        [view addSubview:text];
    }
    return view;
    
}








-(void)clickButton:(UIButton *)button{
    
    if (button.tag == 400) {  //确定
        
        if ([self.delegate respondsToSelector:@selector(seleWeek:startTime:endTime:isSubmit:)]) {

            
            [self.delegate seleWeek:self.weekStr startTime:[NSString stringWithFormat:@"%@:%@",self.startH,self.startM] endTime:[NSString stringWithFormat:@"%@:%@",self.endH,self.endM] isSubmit:YES];
        }
        
    }else{
        if ([self.delegate respondsToSelector:@selector(seleWeek:startTime:endTime:isSubmit:)]) {
            
            
            [self.delegate seleWeek:self.weekStr startTime:[NSString stringWithFormat:@"%@:%@",self.startH,self.startM] endTime:[NSString stringWithFormat:@"%@:%@",self.endH,self.endM] isSubmit:NO];
        }
        
    }
    
    
    
    
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    
    if (component==0) {
        self.weekStr = [self.weekArray objectAtIndex:row];

    }else if (component ==1){
        self.startH = [self.HArray objectAtIndex:row];

    }else if(component ==2){
        self.startM = [self.MArray objectAtIndex:row];

    }else if (component ==3){
        
        self.endH = [self.HArray objectAtIndex:row];
    }else if (component ==4){
        
        self.endM = [self.MArray objectAtIndex:row];

    }
    
    
    self.timeLabel.text = [NSString stringWithFormat:@"上课时间: %@ %@:%@ %@:%@",self.weekStr,self.startH,self.startM,self.endH,self.endM];
    
    
    
}

-(void)hiddenSelf{
    
    //    [self removeFromSuperview];
}


@end
