//
//  BMSQ_InputPwdView.m
//  BMSQC
//
//  Created by liuqin on 15/12/11.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_InputPwdView.h"

@implementation BMSQ_InputPwdView


-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.label = [[UILabel alloc]init];
        self.label.text = @"请输入登录密码";
        self.label.textAlignment = NSTextAlignmentCenter;
        self.label.textColor = APP_TEXTCOLOR;
        self.label.font = [UIFont systemFontOfSize:13];
        [self addSubview:self.label];
        
        self.pwdLable = [[UITextField alloc]init];
        self.pwdLable.placeholder = @"输入登录密码";
        self.pwdLable.layer.borderWidth = 0.5;
        self.pwdLable.layer.borderColor = [APP_TEXTCOLOR CGColor];
        self.pwdLable.font = [UIFont systemFontOfSize:14];
        self.pwdLable.secureTextEntry = YES;
        [self addSubview:self.pwdLable];
        
        self.subButon = [[UIButton alloc]init];
        self.subButon.backgroundColor = APP_NAVCOLOR;
        [self.subButon setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [self.subButon setTitle:@"确定" forState:UIControlStateNormal];
        self.subButon.titleLabel.font = [UIFont systemFontOfSize:13];
        [self.subButon addTarget:self action:@selector(submit:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.subButon];
        
        self.cancelButon = [[UIButton alloc]init];
        self.cancelButon.backgroundColor = [UIColor colorWithRed:106/255.0 green:106/255.0 blue:106/255.0 alpha:1];
        [self.cancelButon setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [self.cancelButon setTitle:@"取消" forState:UIControlStateNormal];
        self.cancelButon.titleLabel.font = [UIFont systemFontOfSize:13];
        [self.cancelButon addTarget:self action:@selector(cancel:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:self.cancelButon];
        

    }
    return self;
}

-(void)submit:(UIButton *)button{
    if (self.clickSubmit) {
        self.clickSubmit(self.pwdLable.text,(int)button.tag);
    }
}
-(void)cancel:(UIButton *)button{
    if (self.clickCancel) {
        self.clickCancel((int)button.tag);
    }
}
@end
