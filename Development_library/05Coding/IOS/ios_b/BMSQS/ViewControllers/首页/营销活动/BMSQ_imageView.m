//
//  BMSQ_imageView.m
//  BMSQS
//
//  Created by liuqin on 15/10/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_imageView.h"

@implementation BMSQ_imageView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        float w = frame.size.width;
        self.BgimageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 10,w, w)];
        self.BgimageView.backgroundColor = [UIColor clearColor];
        [self addSubview:self.BgimageView];
//        delete-circular
        self.delebutton = [[UIButton alloc]initWithFrame:CGRectMake(frame.size.width-20, 0, 20, 20)];
        [self.delebutton setImage:[UIImage imageNamed:@"delete-circular"] forState:UIControlStateNormal];
        [self.delebutton addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.delebutton];
        
    }
    return self;
}

-(void)clickButton:(UIButton *)button{
    
    [self.delegate deleActImage:button.tag];
 
}

@end
