//
//  TeactherCollectionCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/13.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "TeactherCollectionCell.h"

#import "UIImageView+WebCache.h"


@interface TeactherCollectionCell()

@property (nonatomic, strong)UIView *bgView;

@property (nonatomic, strong)UIImageView *teaImage;  //教师图片

@property (nonatomic, strong)UILabel *teacherInfo;  //简介
@property (nonatomic, strong)UILabel *teacherName;  //姓名
@property (nonatomic, strong)UILabel *teachCourse;  //课程
@property (nonatomic, strong)UILabel *teacherTitle; //职称
@property (nonatomic, strong)UIButton *zanBtn; //职称


@end


@implementation TeactherCollectionCell

-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.backgroundColor = [UIColor clearColor];
        
        self.bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width-30,frame.size.height-10)];
        self.bgView.backgroundColor = [UIColor whiteColor];
        
        [self addSubview:self.bgView];
        
        
        
        self.teaImage = [[UIImageView alloc]init];
        self.teaImage.userInteractionEnabled = YES;
        [self.bgView addSubview:self.teaImage];
        

        
        self.teacherInfo = [[UILabel alloc]init];
        self.teacherInfo.textColor = APP_TEXTCOLOR;
        self.teacherInfo.font = [UIFont systemFontOfSize:12.f];
        self.teacherInfo.numberOfLines = 0;
        [self.bgView addSubview:self.teacherInfo];
        
        self.teacherName = [[UILabel alloc]init];
        self.teacherName.textColor = APP_TEXTCOLOR;
        self.teacherName.font = [UIFont systemFontOfSize:13.f];
        [self.bgView addSubview:self.teacherName];
        
        self.teachCourse = [[UILabel alloc]init];
        self.teachCourse.textColor = APP_TEXTCOLOR;
        self.teachCourse.font = [UIFont systemFontOfSize:13.f];
        [self.bgView addSubview:self.teachCourse];
        
        self.teacherTitle = [[UILabel alloc]init];
        self.teacherTitle.textColor = APP_TEXTCOLOR;
        self.teacherTitle.font = [UIFont systemFontOfSize:13.f];
        [self.bgView addSubview:self.teacherTitle];
        
        self.zanBtn = [[UIButton alloc]initWithFrame:CGRectMake(0,0, 25, 20)];
        [self.zanBtn setImage:[UIImage imageNamed:@"zan_gray"] forState:UIControlStateNormal];
        [self.zanBtn setImage:[UIImage imageNamed:@"zan_red"] forState:UIControlStateSelected];
        [self.zanBtn addTarget:self action:@selector(clickZan:) forControlEvents:UIControlEventTouchUpInside];
        [self.teaImage addSubview:self.zanBtn];

        

    }
    return self;
    
}
-(void)setTeacher:(NSDictionary *)dic row:(int)row{
    if (row%2==0) {
        self.bgView.frame = CGRectMake(10, 10, self.frame.size.width-15,self.frame.size.height-10);
     
    }else{
         self.bgView.frame = CGRectMake(5, 10, self.frame.size.width-15,self.frame.size.height-10);
       
    }
    
    
    NSString *isUserClick = [NSString stringWithFormat:@"%@",[dic objectForKey:@"isUserClick"]];
    
    self.zanBtn.selected = [isUserClick intValue]==1?YES:NO;
    
    [self.teaImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"teacherImgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
    self.teaImage.frame = CGRectMake(5, 5, self.bgView.frame.size.width-10, self.bgView.frame.size.height/2+20);
    self.zanBtn.frame = CGRectMake(self.teaImage.frame.size.width-27, self.teaImage.frame.size.height-20, 27, 20);
    self.teacherName.text = [dic objectForKey:@"teacherName"];
    self.teacherTitle.text = [dic objectForKey:@"teacherTitle"];
    self.teachCourse.text = [dic objectForKey:@"teachCourse"];
     self.teacherInfo.text =[NSString stringWithFormat:@"简介:%@",[dic objectForKey:@"teacherInfo"]];
    
    self.teacherName.frame = CGRectMake(5, 5+self.teaImage.frame.size.height,self.teaImage.frame.size.width/2 , 22);
    self.teachCourse.frame = CGRectMake(self.teacherName.frame.size.width+8, 5+self.teaImage.frame.size.height,self.teaImage.frame.size.width-self.teaImage.frame.size.width/2 , 22);
    self.teacherTitle.frame = CGRectMake(5, self.teacherName.frame.origin.y+self.teacherName.frame.size.height,self.teaImage.frame.size.width , 22);
    
    CGSize size = [self.teacherInfo.text boundingRectWithSize:CGSizeMake(self.bgView.frame.size.width-10,  self.bgView.frame.size.height-(self.teacherTitle.frame.origin.y+self.teacherTitle.frame.size.height)-10)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:self.teacherInfo.font}
                                                        context:nil].size;
    
    self.teacherInfo.frame = CGRectMake(5, self.teacherTitle.frame.origin.y+self.teacherTitle.frame.size.height, size.width, size.height);
    
    
}
-(void)clickZan:(UIButton *)button{
    if (![gloabFunction isLogin]) {
        
        [[NSNotificationCenter defaultCenter]postNotificationName:@"userlogin" object:self.teacherCode];
    }else{
        
        button.selected = !button.selected;
        CAKeyframeAnimation *k = [CAKeyframeAnimation animationWithKeyPath:@"transform.scale"];
        k.values = @[@(0.1),@(1.0),@(1.5)];
        k.keyTimes = @[@(0.0),@(0.5),@(0.8),@(1.0)];
        k.calculationMode = kCAAnimationLinear;
        [button.layer addAnimation:k forKey:@"SHOW"];
        if (button.selected) {  //点赞
            [[NSNotificationCenter defaultCenter]postNotificationName:@"clickTeacher" object:self.teacherCode];
        }else{                  //取消赞
            [[NSNotificationCenter defaultCenter]postNotificationName:@"cancelClickTeacher" object:self.teacherCode];
        }
    }
}
@end
