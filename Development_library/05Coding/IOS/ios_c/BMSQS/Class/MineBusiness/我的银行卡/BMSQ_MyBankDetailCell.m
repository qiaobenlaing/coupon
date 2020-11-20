//
//  BMSQ_MyBankDetailCell.m
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyBankDetailCell.h"

@implementation BMSQ_MyBankDetailCell

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
        
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 24, 24)];
    [self addSubview:iv_logo];
    
    lb_title = [[UILabel alloc]initWithFrame:CGRectMake(44, 0, 170, 43)];
    lb_title.font = [UIFont systemFontOfSize:14];
    lb_title.textColor = UICOLOR(34, 34, 34, 1.0);
    [self addSubview:lb_title];
    
    lb_msg = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 150, 12, 140, 20)];
    lb_msg.font = [UIFont systemFontOfSize:14];
    lb_msg.textAlignment = NSTextAlignmentRight;
    lb_msg.textColor = UICOLOR(71, 71, 71, 1.0);
    [self addSubview:lb_msg];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(44, 44, APP_VIEW_WIDTH-44, 1)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle content:(NSString*)strContent
{
    iv_logo.image = [UIImage imageNamed:imgName];
    lb_title.text = strTitle;
    lb_msg.text = strContent;
}

@end
