//
//  BMSQ_CuponHistoryCell.m
//  BMSQC
//
//  Created by djx on 15/7/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_CuponHistoryCell.h"

@implementation BMSQ_CuponHistoryCell

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
    iv_topBack = [[UIImageView alloc]initWithFrame:CGRectMake(10, 8, APP_VIEW_WIDTH-20, 80)];
    iv_topBack.image = [UIImage imageNamed:@"iv_cuponInvalid"];
    iv_topBack.userInteractionEnabled = YES;
    [self addSubview:iv_topBack];
    
    iv_shopLogo = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 55, 55)];
    [iv_topBack addSubview:iv_shopLogo];
    
    lb_shopName = [[UILabel alloc]initWithFrame:CGRectMake(70, 5, iv_topBack.frame.size.width - 165, 25)];
    lb_shopName.backgroundColor = [UIColor clearColor];
    lb_shopName.text = @"TEST2";
    [lb_shopName setFont:[UIFont fontWithName:APP_FONT_NAME size:14]];
    [iv_topBack addSubview:lb_shopName];
    
    lb_useDes = [[UILabel alloc]initWithFrame:CGRectMake(70, 30, APP_VIEW_WIDTH - 165, 20)];
    lb_useDes.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_useDes.text = @"满100元可以使用";
    lb_useDes.font = [UIFont systemFontOfSize:12];
    [iv_topBack addSubview:lb_useDes];
    
    lb_endTime = [[UILabel alloc]initWithFrame:CGRectMake(70, 50, APP_VIEW_WIDTH - 165, 20)];
    lb_endTime.textColor = UICOLOR(135, 135, 135, 1.0);
    lb_endTime.text = @"2015.07.25-2015.08.01";
    lb_endTime.font = [UIFont systemFontOfSize:12];
    [iv_topBack addSubview:lb_endTime];
    
    iv_cuponStatus = [[UIImageView alloc]initWithFrame:CGRectMake(iv_topBack.frame.size.width - 85, 7, 83, 62)];
    //iv_cuponStatus.backgroundColor = [UIColor greenColor];
    [iv_topBack addSubview:iv_cuponStatus];
}

- (void)setCellValue:(NSDictionary*)dicCupon indexPath:(NSIndexPath*)path cuponType:(NSString*)type
{
    if(dicCupon == nil || dicCupon.count <= 0)
    {
        return;
    }
    
    self.dic = dicCupon;
    lb_shopName.text = [gloabFunction changeNullToBlank:[dicCupon objectForKey:@"shopName"]];
    lb_useDes.text = [NSString stringWithFormat:@"满%@元可使用",[dicCupon objectForKey:@"availablePrice"]];
    
    NSString *startTimeString = [dicCupon objectForKey:@"startUsingTime"];
    NSString *expireTimeString = [dicCupon objectForKey:@"expireTime"];
    
    lb_endTime.text = [NSString stringWithFormat:@"%@-%@",startTimeString,expireTimeString];

    
    
    [iv_shopLogo sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dicCupon objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    if ([type isEqualToString:@"0"])
    {
        iv_cuponStatus.image = [UIImage imageNamed:@"iv_notValid"];
    }
    else
    {
        
        int status = [[NSString stringWithFormat:@"%@",[dicCupon objectForKey:@"status"]]intValue];
        
        if (status == 11) {  //已退款
            iv_cuponStatus.image = [UIImage imageNamed:@"refunded"];

        }else{
            iv_cuponStatus.image = [UIImage imageNamed:@"iv_hadUsed"];

        }
    }
}

@end
