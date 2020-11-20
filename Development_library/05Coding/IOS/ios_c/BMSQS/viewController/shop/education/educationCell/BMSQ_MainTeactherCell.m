//
//  BMSQ_MainTeactherCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_MainTeactherCell.h"
#import "UIImageView+WebCache.h"
#import "gloabFunction.h"
@implementation BMSQ_MainTeactherCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
       

        float spx =((APP_VIEW_WIDTH-10)/3)/4;
        float w = (APP_VIEW_WIDTH-spx*4)/3;
        self.userInteractionEnabled = YES;
        
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 5, APP_VIEW_WIDTH, w+70+45)];
        bgView.backgroundColor = [UIColor whiteColor];
        [self addSubview:bgView];
        
        
        UILabel *addLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 40)];
        addLabel.backgroundColor = [UIColor whiteColor];
        addLabel.text = @"    名师堂";
        addLabel.textColor = APP_TEXTCOLOR;
        addLabel.font = [UIFont systemFontOfSize:14.f];
        [bgView addSubview:addLabel];
    }
    return self;
}
-(void)setMainTeacher:(NSArray *)teachArr{
  float spx =((APP_VIEW_WIDTH-10)/3)/4;
  float w = (APP_VIEW_WIDTH-spx*4)/3;
    for (UIView *v in self.subviews) {
        if ([v isKindOfClass:[TeacherModelView class]]) {
            [v removeFromSuperview];
        }
    }
    
    for (int i=0 ;i<3;i++) {
        TeacherModelView *teaMode = [[TeacherModelView alloc]initWithFrame:CGRectMake((i+1)*spx+(i*w), 50, w, w+70)];
        teaMode.userInteractionEnabled = YES;

        teaMode.tag = i;
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickTeacher:)];
        [teaMode addGestureRecognizer:tapGesture];
        NSDictionary *teaDic = [teachArr objectAtIndex:i];
        NSString *teachCourse = [teaDic objectForKey:@"teachCourse"];
        NSString *teacherImgUrl = [teaDic objectForKey:@"teacherImgUrl"];
        NSString *teacherName = [teaDic objectForKey:@"teacherName"];
        teaMode.teacherCode = [teaDic objectForKey:@"teacherCode"];
        NSString *isUserClick = [NSString stringWithFormat:@"%@",[teaDic objectForKey:@"isUserClick"]];
        teaMode.zanBtn.selected = [isUserClick intValue]==1?YES:NO;
        [teaMode setTeacher:[NSString stringWithFormat:@"%@%@",IMAGE_URL,teacherImgUrl] name:teacherName class:teachCourse];
        [self addSubview:teaMode];
    }
    
}

-(void)clickTeacher:(UITapGestureRecognizer *)tapView{

    UIView *tap = tapView.view;
    int i =(int)tap.tag;
    [[NSNotificationCenter defaultCenter]postNotificationName:@"gotoTeaDetailVC" object:[NSNumber numberWithInt:i]];
}
@end

@implementation TeacherModelView

-(id)initWithFrame:(CGRect)frame{
    
    self = [super initWithFrame:frame];
    if (self) {
        self.userInteractionEnabled = YES;
        
        float w = frame.size.width;
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, w,w)];
        [self.headImage setImage:[UIImage imageNamed:@"iv_logNodata"]];
        self.headImage.userInteractionEnabled = YES;
    
        
        
        self.teaNameLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, w, w, 25)];
        self.teaNameLabel.font = [UIFont systemFontOfSize:13];
        self.teaNameLabel.textColor = APP_TEXTCOLOR;
        self.classLabe = [[UILabel alloc]initWithFrame:CGRectMake(0, w+25, w, 25)];
        self.classLabe.font = [UIFont systemFontOfSize:13];
        self.classLabe.textColor = APP_TEXTCOLOR;
        
        self.teaNameLabel.text = @"图美女";
        self.classLabe.text = @"英语(一级教师)aaa";
        
        [self addSubview:self.headImage];
        [self addSubview:self.teaNameLabel];
        [self addSubview:self.classLabe];
        
     
        
        
        self.zanBtn = [[UIButton alloc]initWithFrame:CGRectMake(w-25, w-20, 25, 20)];
        [self.zanBtn setImage:[UIImage imageNamed:@"zan_gray"] forState:UIControlStateNormal];
        [self.zanBtn setImage:[UIImage imageNamed:@"zan_red"] forState:UIControlStateSelected];
        [self.zanBtn addTarget:self action:@selector(clickZan:) forControlEvents:UIControlEventTouchUpInside];
        [self.headImage addSubview:self.zanBtn];
        
        
        
        
    }
    return self;
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


-(void)setTeacher:(NSString *)imageS name:(NSString *)name class:(NSString *)classS{
    [self.headImage sd_setImageWithURL:[NSURL URLWithString:imageS] placeholderImage:[UIImage imageNamed:@"iv_logNodata"]];
    self.teaNameLabel.text = name;
    self.classLabe.text = classS;
    
}
@end