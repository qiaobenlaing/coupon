//
//  BMSQ_MyCenterCell.m
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyCenterCell.h"

@implementation BMSQ_MyCenterCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = APP_VIEW_BACKCOLOR;

        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    
    UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 45)];
    imgView.backgroundColor = [UIColor whiteColor];
    [self addSubview:imgView];
    
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 24, 24)];
    iv_logo.backgroundColor = [UIColor clearColor];
    [imgView addSubview:iv_logo];
    
    lb_title = [[UILabel alloc]initWithFrame:CGRectMake(44, 0, APP_VIEW_WIDTH-200, 43)];
    lb_title.font = [UIFont systemFontOfSize:14];
    lb_title.backgroundColor = [UIColor clearColor];
    [imgView addSubview:lb_title];
    
//    _img_msg = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 50, 10, 24, 24)];
//    _img_msg.image = [UIImage imageNamed:@"iv_messageCount"];
//    _img_msg.hidden = YES;
//    _img_msg.backgroundColor = [UIColor redColor];
//    [imgView addSubview:_img_msg];
    
    self.lb_msg = [[UILabel alloc] initWithFrame:CGRectMake(lb_title.frame.origin.x+lb_title.frame.size.width+10, 0, APP_VIEW_WIDTH-lb_title.frame.origin.x-lb_title.frame.size.width-35, 43)];
    self.lb_msg.textAlignment = NSTextAlignmentCenter;
    self.lb_msg.font = [UIFont boldSystemFontOfSize:11.0f];
    self.lb_msg.textColor = APP_TEXTCOLOR;
    self.lb_msg.text = @"设置支付密码";
    self.lb_msg.backgroundColor = [UIColor clearColor];
    self.lb_msg.hidden = YES;
    [imgView addSubview:self.lb_msg];

    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 44.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [imgView addSubview:line0];
}

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle
{
    iv_logo.image = [UIImage imageNamed:imgName];
    lb_title.text = strTitle;
}


@end
