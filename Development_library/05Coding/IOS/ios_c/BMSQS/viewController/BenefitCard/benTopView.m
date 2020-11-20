//
//  benTopView.m
//  BMSQC
//
//  Created by liuqin on 16/1/12.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "benTopView.h"

@interface benTopView ()

@property (nonatomic, strong)UIView *lineView;


@end



@implementation benTopView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
        
        UIView *bottomLine = [[UIView alloc]initWithFrame:CGRectMake(0, frame.size.height-0.6, APP_VIEW_WIDTH, 0.5)];
        bottomLine.backgroundColor =UICOLOR(240, 239, 245, 1);
        [self addSubview:bottomLine];
        
        
    }
    return self;
    
}



-(void)createTopViewtitleArray:(NSArray *)titleArray{
    
    
    self.benTopTitleArray = titleArray;
    
    float w = APP_VIEW_WIDTH/titleArray.count;
    
    if (self.isHiddenLine){
        
    }else{
        
        for (int i = 0; i<4; i++) {
            UIView *v = [[UIView alloc]initWithFrame:CGRectMake(i*w, 5, 0.5, self.frame.size.height-10)];
            v.backgroundColor = UICOLOR(240, 239, 245, 1);
            [self addSubview:v];
        }
    }
    
    self.lineView = [[UIView alloc]init];
    self.lineView.backgroundColor = APP_NAVCOLOR;
    [self addSubview:self.lineView];
    
    
    for (int i = 0; i<self.benTopTitleArray.count; i++) {
        UIButton *btn = [[UIButton alloc]initWithFrame:CGRectMake(i*w, 0, w, self.frame.size.height)];
        btn.backgroundColor = [UIColor clearColor];
        [btn setTitle:[self.benTopTitleArray objectAtIndex:i] forState:UIControlStateNormal];
        [btn setTitleColor:APP_TEXTCOLOR forState:UIControlStateNormal];
        [btn setTitleColor:APP_NAVCOLOR forState:UIControlStateSelected];
        btn.titleLabel.font = [UIFont systemFontOfSize:14];
        btn.tag = 100+i;
        [btn addTarget:self action:@selector(clickBtn:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        
        if (i == 0) {
            btn.selected = YES;
            self.lineView.frame = CGRectMake(8, btn.frame.size.height-1, btn.frame.size.width-16, 1);
        }else{
            btn.selected = NO;
        }
        
    }
    

    
}


-(void)clickBtn:(UIButton *)btn{
    
    for (int i = 0; i<self.benTopTitleArray.count; i++) {
        UIButton *button = (UIButton *)[self viewWithTag:100+i];
        if (i == btn.tag - 100) {
            button.selected = YES;
        }else{
            button.selected = NO;
        }
        
    }
    

    [UIView animateWithDuration:0.3 animations:^{
         self.lineView.frame = CGRectMake(btn.frame.origin.x+8, btn.frame.size.height-1, btn.frame.size.width-16, 1);
    } completion:^(BOOL finished) {
        
        if ([self.typeDelegate respondsToSelector:@selector(changeType:)]) {
            [self.typeDelegate changeType:(int)btn.tag];
        }
        
        if ([self.typeDelegate respondsToSelector:@selector( changeType:view:)]) {
            [self.typeDelegate changeType:(int)btn.tag view:self];
        }
        
    }];
    
    
}

@end
