//
//  NewActivityCell.m
//  BMSQS
//
//  Created by gh on 15/12/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "NewSecondActivityCell.h"


#define secondCellHeight 45.0


@interface NewSecondActivityCell ()




@end


@implementation NewSecondActivityCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
    
        CGFloat y = 0;
        
        [self drawLabel:y title:@"多种收费规格"];
        
        self.label01 = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, y, APP_VIEW_WIDTH/2-30, secondCellHeight)];
        self.label01.text = @"";
        self.label01.textAlignment = NSTextAlignmentRight;
        self.label01.font = [UIFont systemFontOfSize:15.f];
        [self.contentView addSubview:self.label01];
        
        UIButton *button1 = [[UIButton alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-100, y, 90, secondCellHeight)];
        button1.backgroundColor = [UIColor clearColor];
        [button1 setImage:[UIImage imageNamed:@"garyright"] forState:UIControlStateNormal];
        button1.imageEdgeInsets = UIEdgeInsetsMake(0, 90-10, 0, 0);
        [self.contentView addSubview:button1];
        button1.tag = 2001;
        [button1 addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        
        
        y = y + secondCellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"价格"];
        
        self.label02 = [[UILabel alloc] initWithFrame:CGRectMake(0, y, APP_VIEW_WIDTH-10, secondCellHeight)];
        self.label02.font = [UIFont systemFontOfSize:15.f];
        self.label02.textAlignment = NSTextAlignmentRight;
        self.label02.text = @"0元";
        [self.contentView addSubview:self.label02];
        
        
        
        
        y = y + secondCellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"退款:"];
        

        self.btn3 = [UIButton buttonWithType:UIButtonTypeCustom];
        self.btn3.frame = CGRectMake(APP_VIEW_WIDTH/3, y+10, APP_VIEW_WIDTH/3*2-10, secondCellHeight-20);
        [self.btn3 setTitle:@"退款模式" forState:UIControlStateNormal];
        self.btn3.titleLabel.font = [UIFont systemFontOfSize: 14.0];
        self.btn3.backgroundColor = [UIColor clearColor];
        self.btn3.tag = 2003;
        
        [self.btn3.layer setMasksToBounds:YES];
        [self.btn3.layer setBorderWidth:0.5]; //边框宽度
        [self.btn3.layer setBorderColor:[UICOLOR(153, 153, 153, 1) CGColor]];//边框颜色
        [self.btn3 setTitleColor:UICOLOR(51, 51, 51, 1) forState:UIControlStateNormal];//title color
        [self.btn3 addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
        [self.contentView addSubview:self.btn3];
        
        
        
        y = y + secondCellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"提前"];
        [self drawRightLabel:y title:@"天预约"];
        
        self.textField4 = [[UITextField alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, y, APP_VIEW_WIDTH/2-60, secondCellHeight)];
        self.textField4.tag = 2004;
        [self.textField4 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField4.keyboardType = UIKeyboardTypeDecimalPad;
        self.textField4.font = [UIFont systemFontOfSize:15.f];
        self.textField4.textAlignment = NSTextAlignmentRight;
        self.textField4.backgroundColor = [UIColor clearColor];
        [self.contentView addSubview:self.textField4];
        
        
        y = y + secondCellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"单人报名人数"];
        [self drawRightLabel:y title:@"人"];
        
        self.textField5 = [[UITextField alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, y, APP_VIEW_WIDTH/2-30, secondCellHeight)];
        self.textField5.tag = 2005;
        [self.textField5 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField5.keyboardType = UIKeyboardTypeDecimalPad;
        self.textField5.font = [UIFont systemFontOfSize:15.f];
        self.textField5.textAlignment = NSTextAlignmentRight;
        self.textField5.backgroundColor = [UIColor clearColor];
//        self.textField5.placeholder = @"单人报名人数";
        [self.contentView addSubview:self.textField5];
        
        
        
        y = y + secondCellHeight;
        [self DrawLineView:y];
        [self drawLabel:y title:@"活动最大参与人数"];
        [self drawRightLabel:y title:@"人"];
        
        self.textField6 = [[UITextField alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, y, APP_VIEW_WIDTH/2-30, secondCellHeight)];
        self.textField6.tag = 2006;
        [self.textField6 addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
        self.textField6.keyboardType = UIKeyboardTypeDecimalPad;
        self.textField6.font = [UIFont systemFontOfSize:15.f];
        self.textField6.textAlignment = NSTextAlignmentRight;
        self.textField6.backgroundColor = [UIColor clearColor];
//        self.textField6.placeholder = @"活动最大参与人数";
        [self.contentView addSubview:self.textField6];
        
        
    }
    return self;
}


- (void)btnAct:(UIButton *)sender {
    if (sender.tag == 2001) {//收费
        if (self.activityDelegate != nil) {
            [self.activityDelegate gotoChargeVC];
        }
    }else if (sender.tag == 2003) { //退款模式
        if (self.activityDelegate != nil) {
            [self.activityDelegate showRefundList];
        }
        
    }
}

//输入文字
- (void)textFieldChange:(UITextField *)textField {
    if (self.activityDelegate != nil) {
        [self.activityDelegate secondTextField:textField];
    }
    
}




- (void)drawRightLabel:(CGFloat)labelY title:(NSString *)title {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, labelY, APP_VIEW_WIDTH-20, secondCellHeight)];
    label.text = title;
    label.font = [UIFont systemFontOfSize:15.f];
    label.textAlignment = NSTextAlignmentRight;
    [self.contentView addSubview:label];
}


- (void)drawLabel:(CGFloat)labelY title:(NSString *)title {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, labelY, APP_VIEW_WIDTH, secondCellHeight)];
    label.text = title;
    label.font = [UIFont systemFontOfSize:15.f];
    [self.contentView addSubview:label];
    
    
}


- (void)DrawLineView:(CGFloat)lineY {
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, lineY-0.5, APP_VIEW_WIDTH, 0.5)];
    line.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.contentView addSubview:line];
    
}



@end
