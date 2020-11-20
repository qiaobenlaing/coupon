//
//  UserCenterCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "UserCenterCell.h"

@implementation UserCenterCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier  {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        float w = APP_VIEW_WIDTH/3;
        NSArray *titleArray = @[@"付款码",@"手机充值",@"培训报名",@"惠圈资产",@"惠圈订单",@"课程表"];
        NSArray *imageArray = @[[UIImage imageNamed:@"icon_scan"],[UIImage imageNamed:@"icon_recharge"],[UIImage imageNamed:@"icon_train"],[UIImage imageNamed:@"icon_assets"],[UIImage imageNamed:@"icon_order"],[UIImage imageNamed:@"icon_course"]];
        
        for (int i=0; i<6; i++) {
            int x = i%3;
            int y = i/3;
            
            UserCenterView *cellView = [[UserCenterView alloc]initWithFrame:CGRectMake(w*x, y*90, w, 90) image:[imageArray objectAtIndex:i] titile:[titleArray objectAtIndex:i]];
            cellView.tag = 100+i;
            [self addSubview:cellView];
            cellView.userInteractionEnabled = YES;
            
            UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickTap:)];
            [cellView addGestureRecognizer:tapGesture];

        }
    }
    return self;
}

-(void)clickTap:(UITapGestureRecognizer *)tapGesture{
    
    UserCenterView *userView = (UserCenterView *)tapGesture.view;
    int tag =(int)userView.tag;
    
    if ([self.userCenterDelegate respondsToSelector:@selector(clickUserCenterCell:)]) {
        [self.userCenterDelegate clickUserCenterCell:tag];
    }
    
    
    
}

@end



@implementation UserCenterView


-(id)initWithFrame:(CGRect)frame image:(UIImage *)image titile:(NSString *)title{
    self = [super initWithFrame:frame];
    if(self){
        UIImageView *headImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, frame.size.width/2-5, frame.size.width/2-5)];
        headImage.layer.cornerRadius = (frame.size.width/2-5)/2;
        headImage.layer.masksToBounds = YES;
        [headImage setImage:image];
        [self addSubview:headImage];
        
        headImage.center = CGPointMake(frame.size.width/2, frame.size.height/2-10);
        
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(0, frame.size.height-30, frame.size.width, 30)];
        label.textAlignment = NSTextAlignmentCenter;
        label.font = [UIFont systemFontOfSize:12];
        label.text = title;
        label.textColor = APP_TEXTCOLOR;
        [self addSubview:label];
    }
    return self;
}
@end
