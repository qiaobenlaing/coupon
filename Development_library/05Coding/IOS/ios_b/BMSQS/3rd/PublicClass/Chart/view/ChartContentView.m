//
//  ChartContentView.m
//  气泡
//
//  Created by zzy on 14-5-13.
//  Copyright (c) 2014年 zzy. All rights reserved.
//
#define kContentStartMargin 25
#import "ChartContentView.h"
#import "ChartMessage.h"
@implementation ChartContentView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backImageView=[[UIImageView alloc]init];
        self.backImageView.userInteractionEnabled=YES;
        [self addSubview:self.backImageView];
        
        self.iconImageView=[[UIImageView alloc]init];
        self.iconImageView.userInteractionEnabled=NO;
        [self addSubview:self.iconImageView];
        
        self.contentLabel=[[UILabel alloc]init];
        self.contentLabel.numberOfLines=0;
        self.contentLabel.textAlignment=NSTextAlignmentLeft;
        self.contentLabel.font=[UIFont systemFontOfSize:13.f];
        [self addSubview:self.contentLabel];

        [self addGestureRecognizer: [[UILongPressGestureRecognizer alloc]initWithTarget:self action:@selector(longTap:)]];

        [self addGestureRecognizer:[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapPress:)]];
    }
    return self;
}
-(void)setFrame:(CGRect)frame
{
    [super setFrame:frame];
    
    self.backImageView.frame=self.bounds;
    CGFloat contentLabelX=0;
    if(self.chartMessage.messageType==kMessageFrom){
     
        contentLabelX=kContentStartMargin*0.8;
    }else if(self.chartMessage.messageType==kMessageTo){
        contentLabelX=kContentStartMargin*0.5;
    }
    if(_chartMessage.dict&&[_chartMessage.dict objectForKey:@"bestanswer"]&&[[_chartMessage.dict objectForKey:@"bestanswer"] intValue]==1){
        self.contentLabel.frame=CGRectMake(contentLabelX+40, -3, self.frame.size.width-kContentStartMargin-5-40, self.frame.size.height);
        
        self.iconImageView.frame = CGRectMake(27, 15, 30, 38);
        self.iconImageView.image = [UIImage imageNamed:@"LEKnowledge_GetAn"];
    }else{
        self.contentLabel.frame=CGRectMake(contentLabelX, -3, self.frame.size.width-kContentStartMargin-5, self.frame.size.height);
        /*清除获得最佳的logo*/
        self.iconImageView.image = nil;

    }
}
-(void)longTap:(UILongPressGestureRecognizer *)longTap
{
    if([self.delegate respondsToSelector:@selector(chartContentViewLongPress:content:)]){
        
        [self.delegate chartContentViewLongPress:self content:self.contentLabel.text];
    }
}
-(void)tapPress:(UILongPressGestureRecognizer *)tapPress
{
    if([self.delegate respondsToSelector:@selector(chartContentViewTapPress:content:)]){
    
        [self.delegate chartContentViewTapPress:self content:self.contentLabel.text];
    }
}
@end
