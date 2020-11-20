//
//  OpenClassTimePickerView.m
//  BMSQS
//
//  Created by gh on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenClassTimePickerView.h"

@interface OpenClassTimePickerView ()<UIPickerViewDataSource, UIPickerViewDelegate>


@property (nonatomic, strong)NSMutableArray *pickerTimeAry;
@property (nonatomic, strong)NSMutableArray *pickerMinuteAry;

@property (nonatomic, strong)UIPickerView *pickerView;
@property (nonatomic, assign)BOOL isReload;

@end

@implementation OpenClassTimePickerView

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setViewUp];
    }
    
    return self;
}

- (void)reloadView {
    //    self.isReload = YES;
    [self.pickerView reloadAllComponents];
}

- (void)awakeFromNib {
    [super awakeFromNib];

}

- (void)setViewUp {
    
    
    self.backgroundColor = [UIColor whiteColor];
    self.layer.borderColor = APP_CELL_LINE_COLOR.CGColor;
    self.layer.borderWidth = 1.0;
    
    self.pickerTimeAry = [[NSMutableArray alloc] init];
    self.pickerMinuteAry = [[NSMutableArray alloc] init];
    
    for (int i=6; i<=22; i++) {
        for (int j = 0; j<60; j++) {
            
            if (j%5==0) {
                NSString *string ;
                if (i<10) {
                    string = [NSString stringWithFormat:@"0%d:%d", i, j];
                    if (j<10) {
                        string = [NSString stringWithFormat:@"0%d:0%d", i, j];
                    }
                }else {
                    string = [NSString stringWithFormat:@"%d:%d", i, j];
                }
                
                
                [self.pickerTimeAry addObject:string];
                
            }
            
            
        }
        
    }
    
    for (int i=30; i<=60; i++) {
        if (i%5==0) {
            [self.pickerMinuteAry addObject:[NSString stringWithFormat:@"%d", i]];
        }
    }
    
    self.pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, self.frame.size.height-40)];
    self.pickerView.dataSource = self;
    self.pickerView.delegate = self;
    [self addSubview:self.pickerView];
    
    
    UIButton *leftBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    leftBtn.frame = CGRectMake(0, self.frame.size.height-40, self.frame.size.width/2-10, 35);
    [leftBtn setTitle:@"取消" forState:UIControlStateNormal];
    leftBtn.backgroundColor = [UIColor orangeColor];
    [leftBtn addTarget:self action:@selector(removeBtnClick:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:leftBtn];
    
    
    UIButton *rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    rightBtn.frame = CGRectMake(self.frame.size.width/2+5, self.frame.size.height-40, self.frame.size.width/2-10, 35);
    [rightBtn setTitle:@"确认" forState:UIControlStateNormal];
    [rightBtn addTarget:self action:@selector(sureBtnClick:) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.backgroundColor = [UIColor redColor];
    [self addSubview:rightBtn];
    
    
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    
    return 2;
    
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (component == 0 ) {
        return self.pickerTimeAry.count;
    }else {
        return self.pickerMinuteAry.count;
    }
    
    
    
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if (component == 0 ) {
        return [self.pickerTimeAry objectAtIndex:row];
    }else {
        return [self.pickerMinuteAry objectAtIndex:row];
    }
    return nil;
    
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
    //    self.selectDate = [self timeFormat];
    //    [self.delegate getSelectDate:self.selectDate type:self.type];
    NSString *string;
    
    NSInteger timeInteger = [self.pickerView selectedRowInComponent:0];
    NSString *startTime = [self.pickerTimeAry objectAtIndex:timeInteger];
    
    
    NSInteger minuteInteger = [self.pickerView selectedRowInComponent:1];
    [self.pickerMinuteAry objectAtIndex:minuteInteger];

    NSString *endTime = [self getMinute:startTime minute:[self.pickerMinuteAry objectAtIndex:minuteInteger]];
    
    string = [NSString stringWithFormat:@"%@-%@", startTime, endTime];

    NSString *tbID = [NSString stringWithFormat:@"%d",self.tableViewID];
    
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:startTime, @"startTime", endTime, @"endTime",  tbID, @"weekName",  nil];
    
    
    
    [self.delegate getSelectTime:dic reloadArray:self.tableViewID];
    
    [self removeBtnClick:nil];
    
}


- (NSString *)getMinute:(NSString *)hourMinute minute:(NSString *)minute {
    
    int i = [[hourMinute substringWithRange:NSMakeRange(0,2)] intValue];
    int j = [[hourMinute substringWithRange:NSMakeRange(3,2)] intValue];
    
    int k = j + [minute intValue];
    
    
    if (k>=60) {
        i++;
        k = k-60;
    }
    NSString *string;
    string = [NSString stringWithFormat:@"%d:%d", i, k];
    if (k<10) {
        string = [NSString stringWithFormat:@"%d:0%d", i, k];
    }
    if (i<10) {
        string = [NSString stringWithFormat:@"0%d:%d", i, k];
        if (k<10) {
            string = [NSString stringWithFormat:@"0%d:0%d", i, k];
        }
        
    }
    
    return string;
}



- (void)disMiss {
    [self setHidden:YES];
}



@end
