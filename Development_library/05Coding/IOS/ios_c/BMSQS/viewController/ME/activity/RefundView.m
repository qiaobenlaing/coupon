//
//  RefundView.m
//  BMSQC
//
//  Created by liuqin on 16/1/21.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "RefundView.h"



@interface RefundView ()


@property (nonatomic, strong)UIButton *button;

@end

@implementation RefundView



-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.button = [[UIButton alloc]initWithFrame:CGRectMake(10, 0, frame.size.height, frame.size.height)];
        [self.button setImage:[UIImage imageNamed:@"radio_no"] forState:UIControlStateNormal];
        [self.button setImage:[UIImage imageNamed:@"radio_yes"] forState:UIControlStateSelected];

        
        [self addSubview:self.button];
        
        self.messageLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.button.frame.origin.x+self.button.frame.size.width+5, 0, frame.size.width-40, frame.size.height)];
        self.messageLabel.textColor = APP_TEXTCOLOR;
        self.messageLabel.font = [UIFont systemFontOfSize:13];
        [self addSubview:self.messageLabel];
        
        
    }
    
    return self;
}
-(void)setData:(NSDictionary *)dic{
    
    self.messageLabel.text = [dic objectForKey:@"text"];
    
    NSString *str = [NSString stringWithFormat:@"%@",[dic objectForKey:@"selected"]];
    if ([str isEqualToString:@"1"]) {
        self.button.selected = YES;
    }else{
        self.button.selected = NO;

    }
    self.button.tag =[[NSString stringWithFormat:@"%@",[dic objectForKey:@"id"]] intValue];
    [self.button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];

}
-(void)clickButton:(UIButton *)btn{
    
    if ([self.delegate respondsToSelector:@selector(seleResult:)]) {
        [self.delegate seleResult:(int)btn.tag];
    }
}

@end
