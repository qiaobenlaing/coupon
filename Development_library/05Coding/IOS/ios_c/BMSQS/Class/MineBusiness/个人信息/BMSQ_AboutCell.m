//
//  BMSQ_AboutCell.m
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AboutCell.h"

@implementation BMSQ_AboutCell

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
    [self addSubview:lb_title];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle
{
    iv_logo.image = [UIImage imageNamed:imgName];
    lb_title.text = strTitle;
}

@end
