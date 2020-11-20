//
//  OpenClassDatePickerView.m
//  BMSQS
//
//  Created by gh on 16/3/11.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenClassDatePickerView.h"


@interface OpenClassDatePickerView ()

@property (nonatomic, strong) NSString *selectDate;
@property (nonatomic, strong)UIDatePicker *pickerView;
@property (nonatomic, assign)int row;

@end

@implementation OpenClassDatePickerView
- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setViewUp];
    }
    
    return self;
    
    
}

//- (id)init {
//    self = [super init];
//    if (self) {
//        [self setViewUp];
//    }
//    
//    return self;
//    
//}


- (void)setViewUp {
    
    self.backgroundColor = [UIColor whiteColor];
    self.layer.borderWidth = 1.0;
    self.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;

    
    self.pickerView = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH-40, 216)];
    self.pickerView.datePickerMode = UIDatePickerModeDate;
    [self addSubview:self.pickerView];
    
    
    UIButton *leftBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    leftBtn.frame = CGRectMake(10, self.pickerView.frame.origin.y+self.pickerView.frame.size.height, (APP_VIEW_WIDTH-40)/2-20, 40);
    [leftBtn setTitle:@"取消" forState:UIControlStateNormal];
    [leftBtn addTarget:self action:@selector(removeBtnClick:) forControlEvents:UIControlEventTouchUpInside];
    leftBtn.backgroundColor = [UIColor orangeColor];
    [self addSubview:leftBtn];
    
    
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    rightBtn.frame = CGRectMake((APP_VIEW_WIDTH-40)/2+10, self.pickerView.frame.origin.y+self.pickerView.frame.size.height, (APP_VIEW_WIDTH-40)/2-20, 40);
    [rightBtn setTitle:@"确认" forState:UIControlStateNormal];
    [rightBtn addTarget:self action:@selector(sureBtnClick:) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.backgroundColor = [UIColor redColor];
    [self addSubview:rightBtn];
    
    
}

/**
 *  设置时间格式，可更改HH、hh改变日期的显示格式，有12小时和24小时制
 *
 *  @return 时间格式
 */
- (NSString *)timeFormat
{
    NSDate *selected = [self.pickerView date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *currentOlderOneDateStr = [dateFormatter stringFromDate:selected];
    return currentOlderOneDateStr;
}


/**
 *  取消按钮点击
 */
- (void)removeBtnClick:(id)sender {
    // 开始动画
    //    [self animationbegin:sender];
    [self setHidden:YES];
}

/**
 *  确定按钮点击,会触发代理事件
 */
- (void)sureBtnClick:(id)sender {
    // 开始动画
    //    [self animationbegin:sender];
    self.selectDate = [self timeFormat];
    
    [self.delegate getSelectDate:self.selectDate row:self.row];
    
    NSLog(@"%@",self.pickerView.date);
    
    
    [self removeBtnClick:nil];
}

- (void)showDateView:(int)row {
    self.row = row;
    self.hidden = NO;
    
}

- (void)disMiss {
    
    self.hidden = YES;
    
}


@end
