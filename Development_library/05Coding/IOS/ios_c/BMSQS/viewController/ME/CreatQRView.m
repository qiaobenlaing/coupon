//
//  CreatQRView.m
//  BMSQC
//
//  Created by liuqin on 16/1/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CreatQRView.h"

#import "QRCodeGenerator.h"
#import "UIImageView+WebCache.h"

@interface CreatQRView ()


//@property (nonatomic, strong)UIImageView *circleImage;
@property (nonatomic, strong)UIImageView *qrImage;

@end


@implementation CreatQRView

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
//        self.layer.borderColor = [APP_TEXTCOLOR CGColor];
//        self.layer.borderWidth = 0.3;
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 8;
        
        
        
     
        
        UIButton *btn = [[UIButton alloc]init];
        btn.backgroundColor = [UIColor clearColor];
//        btn.layer.borderColor = [APP_NAVCOLOR CGColor];
//        btn.layer.borderWidth = 1;
//        btn.layer.masksToBounds = YES;
//        btn.layer.cornerRadius = 5;
//        [btn setTitle:@"刷新" forState:UIControlStateNormal];
//        [btn setTitleColor:APP_NAVCOLOR forState:UIControlStateNormal];
        [btn setImage:[UIImage imageNamed:@"refresh1"] forState:UIControlStateNormal];
//        btn.titleLabel.font = [UIFont systemFontOfSize:13];
        [btn addTarget:self action:@selector(startAnimation) forControlEvents:UIControlEventTouchUpInside];
        
        UILabel *label = [[UILabel alloc]init];
        label.text = @"每分钟自动更新";
        label.font = [UIFont systemFontOfSize:13];
        
        
        
        [self addSubview:btn];
        [self addSubview:label];
        

        
        CGSize size2 = [@"每分钟自动更新" boundingRectWithSize:CGSizeMake(MAXFLOAT,MAXFLOAT)
                                           options:NSStringDrawingUsesLineFragmentOrigin
                                        attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                           context:nil].size;
        float w = 20 +size2.width +10;
        
        btn.frame = CGRectMake((frame.size.width-w)/2, frame.size.height-30, 20, 20);
        label.frame =  CGRectMake(btn.frame.origin.x+btn.frame.size.width+5, btn.frame.origin.y, size2.width, size2.height+10);
        
        w = frame.size.width-40;
        
        UIButton *clickbtn = [[UIButton alloc]initWithFrame:CGRectMake(btn.frame.origin.x, btn.frame.origin.y, btn.frame.origin.x+btn.frame.size.width+label.frame.size.width , btn.frame.size.height)];
        [clickbtn addTarget:self action:@selector(startAnimation) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:clickbtn];
        
        self.qrImage = [[UIImageView alloc]initWithFrame:CGRectMake(20, 10, w, w)];
        [self addSubview:self.qrImage];
        
        
    }
    return self;
}

-(void) startAnimation
{
    [self.delegate refreshQR ];
}



-(void)creatCode:(NSString *)qrStr{
    
        self.qrImage.image = [QRCodeGenerator qrImageForString:qrStr imageSize:3600 Topimg:self.imageHead];
    
}

@end
