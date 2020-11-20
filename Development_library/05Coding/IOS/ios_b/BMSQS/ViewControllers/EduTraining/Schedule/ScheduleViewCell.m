//
//  ScheduleViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/4.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ScheduleViewCell.h"

@implementation ScheduleViewCell

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
        [self addSubview:self.classType];//
        [self addSubview:self.goToClassView];
        [self addSubview:self.teacherName];
        [self addSubview:self.detailsBut];
        [self addSubview:self.lineLB1];
        [self addSubview:self.lineLB2];
        [self addSubview:self.lineLB3];
        [self addSubview:self.lineView];
    }
    return self;
}

- (UILabel *)classType
{
    if (_classType == nil) {
        self.classType = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, 45)];
        _classType.text = @"常规书法一班";
        _classType.numberOfLines = 0;
        _classType.textAlignment = NSTextAlignmentCenter;
        _classType.font = [UIFont systemFontOfSize:12.0];
//        _classType.backgroundColor = [UIColor purpleColor];
    }
    return _classType;
}

- (UIView *)lineView
{
    if (_lineView == nil) {
        self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 44.5, APP_VIEW_WIDTH, 0.5)];
        self.lineView.backgroundColor = [UIColor grayColor];
    }
    return _lineView;
}

- (UILabel *)lineLB1
{
    if (_lineLB1 == nil) {
        self.lineLB1 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 10, 0, 1, 45)];
        _lineLB1.backgroundColor = [UIColor grayColor];
    }
    return _lineLB1;
}

- (UIView *)goToClassView
{
    if (_goToClassView == nil) {
        self.goToClassView = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 9, 0, (APP_VIEW_WIDTH - 3)/9*4 - 10, 45)];
        [self.goToClassView addSubview:self.dateLB1];
        [self.goToClassView addSubview:self.dateLB2];
//        [self.goToClassView addSubview:self.dateLB3];
    }
    return _goToClassView;
}

- (UILabel *)dateLB1
{
    if (_dateLB1 == nil) {
        self.dateLB1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 5, _goToClassView.frame.size.width, 15)];
        _dateLB1.text = @"2月26日至3月26日";
        _dateLB1.numberOfLines = 0;
        _dateLB1.textAlignment = NSTextAlignmentCenter;
        _dateLB1.font = [UIFont systemFontOfSize:12.0];
        
    }
    return _dateLB1;
}
- (UILabel *)dateLB2
{
    if (_dateLB2 == nil) {
        self.dateLB2 = [[UILabel alloc] initWithFrame:CGRectMake(10, 25, _goToClassView.frame.size.width - 20, 15)];
        _dateLB2.text = @"周一15:30至16:20";
        _dateLB2.numberOfLines = 0;
//        _dateLB2.backgroundColor = [UIColor redColor];
        _dateLB2.textAlignment = NSTextAlignmentCenter;
        _dateLB2.font = [UIFont systemFontOfSize:12.0];
        
    }
    return _dateLB2;
}
//- (UILabel *)dateLB3
//{
//    if (_dateLB3 == nil) {
//        self.dateLB3 = [[UILabel alloc] initWithFrame:CGRectMake(0, 40, _goToClassView.frame.size.width, 15)];
//        _dateLB3.text = @"周五19:30至20:20";
//        _dateLB3.numberOfLines = 0;
//        _dateLB3.textAlignment = NSTextAlignmentCenter;
//        _dateLB3.font = [UIFont systemFontOfSize:12.0];
//        
//    }
//    return _dateLB3;
//}



- (UILabel *)lineLB2
{
    if (_lineLB2 == nil) {
        self.lineLB2 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 19, 0, 1, 45)];
        _lineLB2.backgroundColor = [UIColor grayColor];
    }
    return _lineLB2;
}

- (UILabel *)teacherName
{
    if (_teacherName == nil) {
        self.teacherName = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 18, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, 45)];
        _teacherName.text = @"图小米";
        _teacherName.textAlignment = NSTextAlignmentCenter;
         _teacherName.numberOfLines = 0;
        _teacherName.font = [UIFont systemFontOfSize:12.0];
//        _teacherName.backgroundColor = [UIColor purpleColor];
    }
    return _teacherName;
}

- (UILabel *)lineLB3
{
    if (_lineLB3 == nil) {
        self.lineLB3 = [[UILabel alloc] initWithFrame:CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 28, 0, 1, 45)];
        _lineLB3.backgroundColor = [UIColor grayColor];
    }
    return _lineLB3;
}

- (UIButton *)detailsBut
{
    if (_detailsBut == nil) {
        self.detailsBut = [UIButton buttonWithType:UIButtonTypeCustom];
        _detailsBut.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 27, 0, (APP_VIEW_WIDTH - 3)/9 + 27, 45);
        [_detailsBut setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
//        [_detailsBut setTitle:@"详情  icon" forState:UIControlStateNormal];
        [_detailsBut setImage:[UIImage imageNamed:@"查看详情"] forState:UIControlStateNormal];
//        _detailsBut.titleLabel.numberOfLines = 2;
        _detailsBut.titleLabel.font = [UIFont systemFontOfSize:14.0];
    }
    return _detailsBut;
}

- (void)setCellWithScheduleDic:(NSDictionary *)scheduleDic
{
    self.classType.text = [NSString stringWithFormat:@"%@", scheduleDic[@"className"]];
    self.teacherName.text = [NSString stringWithFormat:@"teacherName"];
    
     NSString * startMontStr = [scheduleDic[@"learnStartDate"] substringWithRange:NSMakeRange(5, 2)];
    NSString * startDayStr = [scheduleDic[@"learnStartDate"] substringWithRange:NSMakeRange(8, 2)];

    NSString * endMontStr = [scheduleDic[@"learnEndDate"] substringWithRange:NSMakeRange(5, 2)];
    NSString * endDayStr = [scheduleDic[@"learnEndDate"] substringWithRange:NSMakeRange(8, 2)];
    self.dateLB1.text = [NSString stringWithFormat:@"%@月%@日至%@月%@日",startMontStr,startDayStr,endMontStr,endDayStr];
    NSArray * array = [NSArray arrayWithArray:scheduleDic[@"classWeekInfo"]];
    NSString * Str = @"";
    for (int i = 0; i<array.count; i++) {
        NSArray * newAry = [NSArray arrayWithArray:array[i][@"learnTime"]];
        NSString * weekStr = @"";
        NSString * string = @"";
        NSString * weekName = @"";
        for (int j = 0; j<newAry.count; j++) {
            NSString * learnStr = [NSString stringWithFormat:@"%@至%@        ", newAry[j][@"startTime"], newAry[j][@"endTime"]];
           string = [string stringByAppendingString:learnStr];
        }
        weekName = [NSString stringWithFormat:@"%@", array[i][@"weekName"]];
        weekStr = [weekName stringByAppendingString:string];
        
        Str = [Str stringByAppendingString:weekStr];
    }
    self.dateLB2.text = [NSString stringWithFormat:@"%@",Str];
    CGSize size = [self.dateLB2.text boundingRectWithSize:CGSizeMake(self.dateLB2.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: self.dateLB2.font} context:nil].size;
    
    
    self.classType.frame = CGRectMake(0, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, size.height + 30);
    self.lineView.frame = CGRectMake(0, size.height + 29.5, APP_VIEW_WIDTH, 0.5);
    self.lineLB1.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 10, 0, 1, size.height + 30);
    self.goToClassView.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*2 - 9, 0, (APP_VIEW_WIDTH - 3)/9*4 - 10, size.height + 30);
    self.dateLB2.frame = CGRectMake(10, 25, _goToClassView.frame.size.width - 20, size.height);
    self.lineLB2.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 19, 0, 1, size.height + 30);
    self.teacherName.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*6 - 18, 0, (APP_VIEW_WIDTH - 3)/9*2 - 10, size.height + 30);
    self.lineLB3.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 28, 0, 1, size.height + 30);
    self.detailsBut.frame = CGRectMake((APP_VIEW_WIDTH - 3)/9*8 - 27, 0, (APP_VIEW_WIDTH - 3)/9 + 27, size.height + 30);
    
}




@end
