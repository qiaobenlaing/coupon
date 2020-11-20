//
//  BMSQ_startView.m
//  BMSQC
//
//  Created by liuqin on 16/3/17.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_startView.h"



@implementation BMSQ_startView




-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
        
        StartView *starView = [[StartView alloc]initWithFrame:CGRectMake(0, 10, frame.size.width, 30) numberOfStars:5 seleImage:@"iconfont-starred" norImage:@"iconfont-stargray" titel:@"总体" tag:100];
        [self addSubview:starView];
        
        StartView *starView1 = [[StartView alloc]initWithFrame:CGRectMake(0,starView.frame.origin.y+starView.frame.size.height+10, frame.size.width, 30)numberOfStars:5 seleImage:@"facered" norImage:@"facegray" titel:@"效果" tag:200];
        [self addSubview:starView1];
        
        StartView *starView2 = [[StartView alloc]initWithFrame:CGRectMake(0, starView1.frame.origin.y+starView1.frame.size.height, frame.size.width, 30)numberOfStars:5 seleImage:@"facered" norImage:@"facegray" titel:@"师资" tag:300];
        [self addSubview:starView2];
        
        StartView *starView3 = [[StartView alloc]initWithFrame:CGRectMake(0,  starView2.frame.origin.y+starView2.frame.size.height, frame.size.width, 30)numberOfStars:5 seleImage:@"facered" norImage:@"facegray" titel:@"环境" tag:400];
        [self addSubview:starView3];
        
    }
    return self;
    
    
}


@end



@interface StartView ()
@property (nonatomic, strong)UILabel *tilelabel;
@property (nonatomic, strong)UILabel *explanationLabel;
@property (nonatomic, strong)CWStarRateView *starView;

@end


@implementation StartView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.tilelabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0,40, frame.size.height)];
        self.tilelabel.textColor = APP_TEXTCOLOR;
        self.tilelabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.tilelabel];
        
        self.explanationLabel = [[UILabel alloc]initWithFrame:CGRectMake(frame.size.width-50, 0,40, frame.size.height)];
        self.explanationLabel.textColor = APP_TEXTCOLOR;
        self.explanationLabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.explanationLabel];
        
        self.starView = [[CWStarRateView alloc] initWithFrame:CGRectMake(50, (frame.size.height-15)/2, APP_VIEW_WIDTH/2, 15) numberOfStars:5 seleImage:@"iconfont-stargray" norImage:@"iconfont-starred"] ;
        self.starView.scorePercent = 0.0;
        self.starView.allowIncompleteStar = NO;
        self.starView.hasAnimation = YES;
        self.starView.delegate = self;
        [self addSubview:self.starView];
        
        
    }
    return self;
    
}

-(void)starRateView:(CWStarRateView *)starRateView scroePercentDidChange:(CGFloat)newScorePercent{
    
    float i = (newScorePercent*10)/2;
    NSArray *array = @[[NSString stringWithFormat:@"%d",(int)starRateView.tag],[NSString stringWithFormat:@"%d",(int)i]];

    [[NSNotificationCenter defaultCenter]postNotificationName:@"starRateView" object:array];
    
    
}
- (instancetype)initWithFrame:(CGRect)frame numberOfStars:(NSInteger)numberOfStars  seleImage:(NSString *)seleImagestr norImage:(NSString *)norImageStr titel:(NSString *)titel tag:(int)tag{
    
    self = [super initWithFrame:frame];
    if (self) {
        
        self.tilelabel = [[UILabel alloc]initWithFrame:CGRectMake(10, 0,40, frame.size.height)];
        self.tilelabel.textColor = APP_TEXTCOLOR;
        self.tilelabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.tilelabel];
        
        self.explanationLabel = [[UILabel alloc]initWithFrame:CGRectMake(frame.size.width-50, 0,40, frame.size.height)];
        self.explanationLabel.textColor = APP_TEXTCOLOR;
        self.explanationLabel.font = [UIFont systemFontOfSize:14];
        [self addSubview:self.explanationLabel];
        
        self.starView = [[CWStarRateView alloc] initWithFrame:CGRectMake(50, (frame.size.height-15)/2, APP_VIEW_WIDTH/2, 15) numberOfStars:5 seleImage:seleImagestr norImage:norImageStr] ;
        self.starView.scorePercent = 0.0;
        self.starView.allowIncompleteStar = NO;
        self.starView.hasAnimation = YES;
        self.starView.delegate =self;
        self.starView.tag = tag;
        [self addSubview:self.starView];
        self.tilelabel.text = titel;
//        self.explanationLabel.text = explanStr;
        
    }
    return self;
    
}

//-(void)starRateView:(CWStarRateView *)starRateView scroePercentDidChange:(CGFloat)newScorePercent{
//    
//    NSLog(@" tag =  %d ,%f",(int)starRateView.tag,newScorePercent);
//    
//}

-(void)setStartViewTitle:(NSString *)title norImageStr:(NSString *)norImage seleImageStr:(NSString *)seleImage explan:(NSString *)explanStr{
 
   

    
    
}

@end
