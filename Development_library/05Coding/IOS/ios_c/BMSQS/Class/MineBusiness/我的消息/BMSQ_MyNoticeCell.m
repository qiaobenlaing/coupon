//
//  BMSQ_MyNoticeCell.m
//  BMSQC
//
//  Created by djx on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_MyNoticeCell.h"

@implementation BMSQ_MyNoticeCell

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
    
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 5, 60, 60)];
    [self addSubview:iv_logo];
    
    lb_name = [[UILabel alloc]initWithFrame:CGRectMake(80, 5, 150, 30)];
    lb_name.textColor = UICOLOR(36, 36, 36, 1.0);
    [lb_name setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_name];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(80, 35, APP_VIEW_WIDTH - 85, 30)];
    lb_useDes.textColor = UICOLOR(140, 140, 140, 1.0);
    lb_useDes.numberOfLines = 0;
    [lb_useDes setFont:[UIFont systemFontOfSize:12]];
    [self addSubview:lb_useDes];
    
    lb_time = [[UILabel alloc]initWithFrame:CGRectMake(210, 0, APP_VIEW_WIDTH - 215, 43)];
    lb_time.textColor = UICOLOR(140, 140, 140, 1.0);
    [lb_time setFont:[UIFont systemFontOfSize:10]];
    lb_time.textAlignment = NSTextAlignmentRight;
    [self addSubview:lb_time];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 79, APP_VIEW_WIDTH-10, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}

- (void)setCellValue:(NSDictionary*)dicData
{
    if (dicData == nil || dicData.count <= 0)
    {
        return;
    }
    
    [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicData objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    lb_name.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"shopName"]];
    lb_time.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"createTime"]];
    lb_useDes.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"content"]];
    
//    lb_name.text = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"shopName"]];
//    lb_time.text = [NSString stringWithFormat:@"%@", [dicData objectForKey:@"createTime"]];
//    
//    lb_useDes.text = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"content"]];
    
}
- (void)setNewsCellValue:(NSDictionary *)dicData
{
    if (dicData == nil || dicData.count <= 0) {
        return;
    }
    
    NSString *str_logo = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"logoUrl"]];
    
    
    [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicData objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    
    NSString *str = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"shopName"]];
    if ( [str  isEqual: @"<null>"])
    {
        lb_name.text = @" ";
    }
    else
    {
        lb_name.text = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"shopName"]];
    }
    lb_time.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"createTime"]];
    
    lb_useDes.text = [NSString stringWithFormat:@"%@",[dicData objectForKey:@"message"]];
}


@end
