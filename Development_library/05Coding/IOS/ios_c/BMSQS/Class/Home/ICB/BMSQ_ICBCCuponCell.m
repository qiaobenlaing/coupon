//
//  BMSQ_ICBCCuponCell.m
//  BMSQC
//
//  Created by djx on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ICBCCuponCell.h"

@implementation BMSQ_ICBCCuponCell

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
    if (self)
    {
        [self setCellUp];
    }
    
    return self;
}

- (void)setCellUp
{
    iv_shopLogo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 30, 30)];
    iv_shopLogo.backgroundColor = [UIColor clearColor];
    [self addSubview:iv_shopLogo];
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(50, 10, APP_VIEW_WIDTH - 100, 30)];
    [lb_shopName setBackgroundColor:[UIColor clearColor]];
    [lb_shopName setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_shopName];
    
    lb_distance = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 100, 10, 100, 30)];
    [lb_distance setBackgroundColor:[UIColor clearColor]];
    [lb_distance setTextColor:UICOLOR(142, 142, 142, 1.0)];
    [lb_distance setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_distance];
    
    UIView* back = [[UIView alloc]initWithFrame:CGRectMake(0, 50, APP_VIEW_WIDTH, 70)];
    back.backgroundColor = UICOLOR(246, 246, 246, 1.0);
    [self addSubview:back];
    
    iv_contentLogo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 0, 70, 70)];
    iv_contentLogo.backgroundColor = [UIColor clearColor];
    [back addSubview:iv_contentLogo];
    
    lb_content = [[UILabel alloc]initWithFrame:CGRectMake(90, 10, APP_VIEW_WIDTH - 100, 50)];
    [lb_content setBackgroundColor:[UIColor clearColor]];
    [lb_content setFont:[UIFont systemFontOfSize:14]];
    lb_content.numberOfLines = 2;
    [lb_content setTextColor:UICOLOR(83, 83, 83, 1.0)];
    [back addSubview:lb_content];
    
    UIImageView* iv_jt = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH - 10, 30, 5, 10)];
    iv_jt.image = [UIImage imageNamed:@"iv_jtRight"];
    [back addSubview:iv_jt];
    
    UIView* line = [[UIView alloc]initWithFrame:CGRectMake(0, 127, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line.backgroundColor =  APP_CELL_LINE_COLOR;
    [self addSubview:line];
}

- (void)setCellValue:(NSDictionary*)dicData
{
    if(dicData == nil || dicData.count <= 0)
    {
        return;
    }
    
    lb_shopName.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"shopName"]];
    lb_content.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"txtContent"]];
    
    float distance = [[dicData objectForKey:@"distance"] floatValue];
    if (distance < 1000)
    {
        lb_distance.text = [NSString stringWithFormat:@"%@m",[dicData objectForKey:@"distance"]];
    }
    else
    {
        lb_distance.text = [NSString stringWithFormat:@"%.1fkm",distance/1000];
    }
    
    [iv_contentLogo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[gloabFunction changeNullToBlank:[dicData objectForKey:@"activityLogo"]]]]];
    [iv_shopLogo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[gloabFunction changeNullToBlank:[dicData objectForKey:@"shopLogo"]]]]];
}

@end
