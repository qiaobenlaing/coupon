//
//  BMSQ_RedEnvelopeCell.m
//  BMSQC
//
//  Created by djx on 15/8/10.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_RedEnvelopeCell.h"

@implementation BMSQ_RedEnvelopeCell

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
    UIView* back = [[UIView alloc]initWithFrame:CGRectMake(10, 10, APP_VIEW_WIDTH - 20, 80)];
    back.backgroundColor = [UIColor whiteColor];
    [self addSubview:back];
    
    iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(5, 5, 70, 70)];
    //iv_logo.backgroundColor = [UIColor redColor];
    [back addSubview:iv_logo];
    
    lb_name = [[UILabel alloc]initWithFrame:CGRectMake(80, 5, 180, 30)];
    lb_name.font = [UIFont systemFontOfSize:14];
    lb_name.numberOfLines = 0;
    //lb_name.text = @"惠圈红包";
    [back addSubview:lb_name];
    
    lb_price = [[UILabel alloc]initWithFrame:CGRectMake(back.frame.size.width/2, 5,back.frame.size.width/2-10, 30)];
    lb_price.font = [UIFont systemFontOfSize:13];
    lb_price.textAlignment = NSTextAlignmentRight;
    //lb_price.text = @"100元";
    lb_price.textColor = UICOLOR(125, 125, 125, 1.0);
    [back addSubview:lb_price];
    
    lb_status = [[UILabel alloc]initWithFrame:CGRectMake(back.frame.size.width/2, 40, back.frame.size.width/2-10, 30)];
    lb_status.font = [UIFont systemFontOfSize:13];
    lb_status.textAlignment = NSTextAlignmentRight;
    lb_status.textColor = UICOLOR(125, 125, 125, 1.0);
    [back addSubview:lb_status];
    //lb_status.text = @"已领取";
    
}

- (void)setCellValue:(NSDictionary*)dicData
{
    if (dicData == nil || dicData.count <= 0)
    {
        return;
    }
    
    [iv_logo setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicData objectForKey:@"logoUrl"]]]];
    lb_name.text = [gloabFunction changeNullToBlank:[dicData objectForKey:@"shopName"]];
    lb_price.text = [NSString stringWithFormat:@"%0.2f元",[[dicData objectForKey:@"totalValue"] floatValue]];
    lb_status.text = @"已领取";
}

@end
