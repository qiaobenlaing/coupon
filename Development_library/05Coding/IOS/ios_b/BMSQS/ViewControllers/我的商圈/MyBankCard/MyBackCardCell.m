//
//  MyBackCardCell.m
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "MyBackCardCell.h"

@implementation MyBackCardCell

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
    UIImageView* iv_logo = [[UIImageView alloc]initWithFrame:CGRectMake(15, 15, 32, 32)];
    iv_logo.image = [UIImage imageNamed:@"iv_ICBC"];
    [self addSubview:iv_logo];
    
    lb_name = [[UILabel alloc]initWithFrame:CGRectMake(55, 6, 200, 25)];
    lb_name.font = [UIFont fontWithName:APP_FONT_NAME size:15];
    lb_name.text = @"昵称";
    lb_name.textColor = [UIColor blackColor];
    [self addSubview:lb_name];
    
    lb_cardNum = [[UILabel alloc]initWithFrame:CGRectMake(55, 31, 170, 25)];
    lb_cardNum.font = [UIFont systemFontOfSize:13];
    lb_cardNum.text = @"尾号:1234";
    lb_cardNum.textColor = UICOLOR(134, 134, 134, 1.0);
    [self addSubview:lb_cardNum];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(0, 61, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}

- (void)setCellValue:(NSDictionary*)dicBank
{
    if (dicBank == nil || dicBank.count <= 0)
    {
        return;
    }
    self.dicBank = dicBank;
    lb_name.text = [gloabFunction changeNullToBlank:[dicBank objectForKey:@"bankName"]];
    lb_cardNum.text = [NSString stringWithFormat:@"尾号%@",[gloabFunction changeNullToBlank:[dicBank objectForKey:@"accountNbrLast4"]]];
}



@end
