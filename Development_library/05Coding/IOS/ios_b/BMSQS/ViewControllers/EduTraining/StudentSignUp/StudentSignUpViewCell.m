//
//  StudentSignUpViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "StudentSignUpViewCell.h"

@interface StudentSignUpViewCell ()

@property (nonatomic, strong)UIView * backView;
@property (nonatomic, strong)UILabel * stuNameLB;
@property (nonatomic, strong)UILabel * stuAgeLB;
@property (nonatomic, strong)UILabel * subjectLB;
@property (nonatomic, strong)UILabel * stuPhoneLB;
@property (nonatomic, strong)UILabel * signUpTimeLB;
@property (nonatomic, strong)UILabel * signUpTimeLB2;
@property (nonatomic, strong)UIView * lineView;
@property (nonatomic, strong)UIButton * disagreeBut;
@property (nonatomic, strong)UIButton * agreeBut;

//@property (nonatomic, strong)UITextView * showTextView;
@property (nonatomic, strong)UILabel  * showTextLB;

@property (nonatomic, strong)NSString * learnFee;// 报名价格

@property (nonatomic, assign)int rowNumber;

@end

@implementation StudentSignUpViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self setCellSignUp];
    }
    return self;
}

- (void)setCellSignUp
{
    
    self.backView = [[UIView alloc] initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 120)];
    self.backView.backgroundColor = [UIColor whiteColor];
    self.backView.layer.borderColor = APP_VIEW_BACKCOLOR.CGColor;
    self.backView.layer.borderWidth =1.0;
    [self addSubview:self.backView];
    
    self.stuNameLB = [[UILabel alloc] initWithFrame:CGRectMake(20, 5, (self.backView.frame.size.width - 40)/3 + 20, 25)];
    self.stuNameLB.text = @"小朋友";
    self.stuNameLB.font = [UIFont systemFontOfSize:13.0];
    self.stuNameLB.textAlignment = NSTextAlignmentLeft;
//    self.stuNameLB.backgroundColor = [UIColor redColor];
    [self.backView addSubview:self.stuNameLB];
    
    self.stuAgeLB = [[UILabel alloc] initWithFrame:CGRectMake((self.backView.frame.size.width - 20)/3 + 30, 5, (self.backView.frame.size.width - 20)/3 - 30, 25)];
    self.stuAgeLB.text = @"8岁";
    self.stuAgeLB.textAlignment = NSTextAlignmentCenter;
    self.stuAgeLB.font = [UIFont systemFontOfSize:13.0];
//    self.stuAgeLB.backgroundColor = [UIColor yellowColor];
    [self.backView addSubview:self.stuAgeLB];
    
    self.subjectLB = [[UILabel alloc] initWithFrame:CGRectMake((self.backView.frame.size.width - 20)/3*2 + 10, 5, (self.backView.frame.size.width - 20)/3, 25)];
    self.subjectLB.text = @"书法一班";
    self.subjectLB.textAlignment = NSTextAlignmentCenter;
    self.subjectLB.font = [UIFont systemFontOfSize:13.0];
//    self.subjectLB.backgroundColor = [UIColor greenColor];
    [self.backView addSubview:self.subjectLB];
    
    self.stuPhoneLB = [[UILabel alloc] initWithFrame:CGRectMake(10, 40, (self.backView.frame.size.width - 20)/3, 25)];
    self.stuPhoneLB.text = @"13999999999";
    self.stuPhoneLB.font = [UIFont systemFontOfSize:13.0];
    [self.backView addSubview:self.stuPhoneLB];
    
    self.signUpTimeLB = [[UILabel alloc] initWithFrame:CGRectMake((self.backView.frame.size.width - 20)/3 + 15, 40, (self.backView.frame.size.width - 20)/3 - 10, 25)];
    self.signUpTimeLB.text = @"2016年3月14日";
    self.signUpTimeLB.font = [UIFont systemFontOfSize:13.0];
    [self.backView addSubview:self.signUpTimeLB];
    
    self.signUpTimeLB2 = [[UILabel alloc] initWithFrame:CGRectMake((self.backView.frame.size.width - 20)/3*2 + 35, 40, (self.backView.frame.size.width - 20)/3 - 20, 25)];
    self.signUpTimeLB2.text = @"18:25:36";
    self.signUpTimeLB2.font = [UIFont systemFontOfSize:13.0];
    [self.backView addSubview:self.signUpTimeLB2];
    
    
    self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 70, APP_VIEW_WIDTH, 0.5)];
    self.lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [self.backView addSubview:self.lineView];
    
    
    [self setCellButton];
    
    [self setCellUITextView];
    
}

- (void)setCellWithSignUpDic:(NSDictionary *)dic row:(int)row
{
    self.rowNumber = row;
    
    if (self.disagreeBut != nil || self.agreeBut != nil) {
        [self.disagreeBut removeFromSuperview];
        [self.agreeBut removeFromSuperview];
        [self setCellButton];
    }
    
    
    if (self.showTextLB != nil) {
        [self.showTextLB removeFromSuperview];
        [self setCellUITextView];
    }
    
    self.learnFee = [NSString stringWithFormat:@"%@", dic[@"learnFee"]];
    NSString * handFlag = [NSString stringWithFormat:@"%@", dic[@"handFlag"]];
    if (handFlag.intValue == 0) {
        self.disagreeBut.hidden = NO;
        self.agreeBut.hidden = NO;
        self.backView.frame = CGRectMake(10, 10, APP_VIEW_WIDTH - 20, 120);
    }else if (handFlag.intValue == 1){
        self.disagreeBut.hidden = YES;
        self.agreeBut.hidden = YES;
        self.showTextLB.hidden = NO;
        self.showTextLB.frame = CGRectMake(20, 79, APP_VIEW_WIDTH - 40, 34);
        self.showTextLB.text = [NSString stringWithFormat:@"学费:%@", dic[@"signFee"]];
        self.backView.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, 120);
    }else if (handFlag.intValue == 2){
        self.disagreeBut.hidden = YES;
        self.agreeBut.hidden = YES;
        self.showTextLB.hidden = NO;
        self.showTextLB.frame = CGRectMake(10, 79, APP_VIEW_WIDTH - 20, 34);
        self.showTextLB.text = [NSString stringWithFormat:@"意见说明: %@", dic[@"handMemo"]];
        CGSize size = [self.showTextLB.text boundingRectWithSize:CGSizeMake(self.showTextLB.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: self.showTextLB.font} context:nil].size;
        if (size.height > 34) {
            self.backView.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, 86 + size.height);
            self.showTextLB.frame = CGRectMake(10, 79, self.backView.frame.size.width - 20, size.height);
        }else{
            self.backView.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, 120);
            self.showTextLB.frame = CGRectMake(10, 79, self.backView.frame.size.width - 20, 34);
        }
        
    }
    self.stuNameLB.text = [NSString stringWithFormat:@"%@", dic[@"studentName"]];
    NSString * ageStr = [NSString stringWithFormat:@"%@", dic[@"studentAge"]];
    if (ageStr.intValue == 0) {
        self.stuAgeLB.text = @"年龄未知";
    }else{
        self.stuAgeLB.text = [NSString stringWithFormat:@"%@岁", ageStr];
    }
    
    self.subjectLB.text = [NSString stringWithFormat:@"%@", dic[@"className"]];
    self.stuPhoneLB.text = [NSString stringWithFormat:@"%@", dic[@"studentTel"]];
    NSString * dateStr = [dic[@"signTime"] substringWithRange:NSMakeRange(0, 10)];
    NSString * timeStr = [dic[@"signTime"] substringWithRange:NSMakeRange(11, 8)];
    self.signUpTimeLB.text = [NSString stringWithFormat:@"%@", dateStr];
    self.signUpTimeLB2.text = [NSString stringWithFormat:@"%@", timeStr];
    
}


- (void)setCellButton
{
    self.disagreeBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.disagreeBut.frame = CGRectMake(30, 80, (self.backView.frame.size.width - 90)/2, 30);
    self.disagreeBut.backgroundColor = APP_NAVCOLOR;
    self.disagreeBut.hidden = YES;
    [self.disagreeBut setTitle:@"不同意" forState:UIControlStateNormal];
    [self.disagreeBut addTarget:self action:@selector(disagreeClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:self.disagreeBut];
    
    self.agreeBut = [UIButton buttonWithType:UIButtonTypeCustom];
    self.agreeBut.frame = CGRectMake((self.backView.frame.size.width - 90)/2 + 60, 80, (self.backView.frame.size.width - 90)/2, 30);
    self.agreeBut.backgroundColor = APP_NAVCOLOR;
    self.agreeBut.hidden = YES;
    [self.agreeBut setTitle:@"同意" forState:UIControlStateNormal];
    [self.agreeBut addTarget:self action:@selector(agreeClick:) forControlEvents:UIControlEventTouchUpInside];
    [self.backView addSubview:self.agreeBut];
}

- (void)setCellUITextView
{
    self.showTextLB = [[UILabel alloc] init];
    self.showTextLB.hidden = YES;
    self.showTextLB.font = [UIFont systemFontOfSize:12.0];
    self.showTextLB.numberOfLines = 0;
//    self.showTextLB.layer.borderColor = [UIColor grayColor].CGColor;
//    self.showTextLB.layer.borderWidth =1.0;
//    self.showTextLB.layer.cornerRadius =5.0;
    self.showTextLB.backgroundColor = [UIColor whiteColor];
    [self.backView addSubview:self.showTextLB];
}

#pragma mark ------ disagreeClick
- (void)disagreeClick:(UIButton *)sender
{
    if ([self.studentSignUpDetegate respondsToSelector:@selector(setCellStudentSignUp:learnFee:row:)]) {
        [self.studentSignUpDetegate setCellStudentSignUp:2 learnFee:nil row:self.rowNumber];
    }
}
- (void)agreeClick:(UIButton *)sender
{
    if ([self.studentSignUpDetegate respondsToSelector:@selector(setCellStudentSignUp:learnFee:row:)]) {
        [self.studentSignUpDetegate setCellStudentSignUp:1 learnFee:self.learnFee row:self.rowNumber];
    }
}

@end
