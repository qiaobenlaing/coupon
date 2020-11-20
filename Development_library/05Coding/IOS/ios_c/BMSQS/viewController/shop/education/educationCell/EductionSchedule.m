//
//  EductionSchedule.m
//  BMSQC
//
//  Created by liuqin on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "EductionSchedule.h"

#import "ScheduleStr.h"
@interface EductionSchedule ()

@property (nonatomic, strong)UILabel *classNameLabel; //所开班级
@property (nonatomic, strong)UILabel *classWeekInfo;  //上课时间
@property (nonatomic, strong)UILabel *teacherName;    //任课教师
@property (nonatomic, strong)UIView *findView;
@property (nonatomic, strong)UIButton *findBtn;

@property (nonatomic, strong)UIView *bgView;    //

@end


@implementation EductionSchedule
-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        self.bgView = [[UIView alloc]initWithFrame:CGRectMake(9, 0, APP_VIEW_WIDTH-18, 80)];
        self.bgView.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.bgView];
     
        
        self.classNameLabel = [[UILabel alloc]init];
        self.classNameLabel.backgroundColor = [UIColor clearColor];
        self.classNameLabel.textColor = APP_TEXTCOLOR;
        self.classNameLabel.font = [UIFont systemFontOfSize:10.f];
        self.classNameLabel.textAlignment = NSTextAlignmentCenter;
        self.classNameLabel.numberOfLines = 0;
        [self.bgView addSubview:self.classNameLabel];
        
        self.classWeekInfo = [[UILabel alloc]init];
        self.classWeekInfo.backgroundColor = [UIColor clearColor];
        self.classWeekInfo.textColor = APP_TEXTCOLOR;
        self.classWeekInfo.font = [UIFont systemFontOfSize:12.f];
        self.classWeekInfo.textAlignment = NSTextAlignmentCenter;
        self.classWeekInfo.numberOfLines = 0;

        [self.bgView addSubview:self.classWeekInfo];
        
        self.teacherName = [[UILabel alloc]init];
        self.teacherName.backgroundColor = [UIColor clearColor];
        self.teacherName.textColor = APP_TEXTCOLOR;
        self.teacherName.font = [UIFont systemFontOfSize:13.f];
        self.teacherName.textAlignment = NSTextAlignmentCenter;
        self.teacherName.numberOfLines = 0;
        [self.bgView addSubview:self.teacherName];
        
        self.findView = [[UIView alloc]init];
        self.findView.backgroundColor = [UIColor clearColor];
        [self.bgView addSubview:self.findView];
        
        self.findBtn = [[UIButton alloc]init];
        [self.findBtn setImage:[UIImage imageNamed:@"findSchdule"] forState:UIControlStateNormal];
        [self.findBtn addTarget:self action:@selector(clickfindBtn:) forControlEvents:UIControlEventTouchUpInside];
        [self.findView addSubview:self.findBtn];
        
    }
    return self;
}

-(void)setSchedule:(NSDictionary *)schDic heigh:(float)heigh{
    self.bgView.frame = CGRectMake(9, 0, APP_VIEW_WIDTH-18, heigh-1);
    self.schduleDic = schDic;
    
    for (UIView *va in self.bgView.subviews) {
        if (va.tag >= 101 && va.tag<=103) {
            [va removeFromSuperview];
        }
    }
    
    
    float w = (self.bgView.frame.size.width/5);
    float w1 =(self.bgView.frame.size.width)/5*2;
    
    float  x =w;
    for (int i=1; i<4; i++) {
        UIView *lineV = [[UIView alloc]initWithFrame:CGRectMake(x, 0, 1, self.bgView.frame.size.height)];
        lineV.backgroundColor =APP_VIEW_BACKCOLOR;
        [self.bgView addSubview:lineV];
        if (i==1) {
            x=x+w1;
        }else{
            x=x+w;
        }
        lineV.tag = i+100;
    };
    
    NSDictionary *dic = [ScheduleStr scheduleStr:schDic w:(self.bgView.frame.size.width)/5*2];
    
    self.classNameLabel.frame = CGRectMake(5, 5, w-10, self.bgView.frame.size.height-10);
    self.classWeekInfo.frame = CGRectMake(w, 5, w1, self.bgView.frame.size.height-10);
    self.teacherName.frame = CGRectMake(5+w+w1, 5, w-10, self.bgView.frame.size.height-10);
    self.findView.frame = CGRectMake(5+w+w1+w, 5, w-10, self.bgView.frame.size.height-10);
    
    self.findBtn.frame = CGRectMake(0, 0, 100, 120);
    self.findBtn.center = CGPointMake(self.findView.frame.size.width/2, self.findView.frame.size.height/2);
    
    self.classNameLabel.text = [schDic objectForKey:@"className"];
    self.teacherName.text = [schDic objectForKey:@"teacherName"];
    self.classWeekInfo.text = [dic objectForKey:@"str"];

    
}


-(void)clickfindBtn:(UIButton *)button{
    if ([self.schDelegate respondsToSelector:@selector(findSchduleDetail:)]) {
        [self.schDelegate findSchduleDetail:self.schduleDic];
    }
    
    
}
@end
