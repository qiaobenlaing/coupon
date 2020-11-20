//
//  BMSQ_AddBankCardCell.m
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_AddBankCardCell.h"

@implementation BMSQ_AddBankCardCell

@synthesize delegate;
@synthesize tx_content;

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
    lb_title = [[UILabel alloc]initWithFrame:CGRectMake(10, 0, 100, 43)];
    lb_title.font = [UIFont systemFontOfSize:14];
    lb_title.textColor = UICOLOR(34, 34, 34, 1.0);
    [self addSubview:lb_title];
    
    tx_content = [[UITextField alloc]initWithFrame:CGRectMake(110, 12, APP_VIEW_WIDTH - 110, 20)];
    tx_content.font = [UIFont systemFontOfSize:13];
    tx_content.delegate = self;
    tx_content.returnKeyType = UIReturnKeyDone;
    tx_content.textColor = UICOLOR(71, 71, 71, 1.0);
    [tx_content addTarget:self action:@selector(searchBarTextChanged:) forControlEvents:UIControlEventEditingChanged];
    [self addSubview:tx_content];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(10, 43, APP_VIEW_WIDTH-10, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}
-(void)searchBarTextChanged:(UITextField *)textfiled{
    
        [delegate textFieldWithText:textfiled];
    
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)setCellValue:(NSString*)strTitle content:(NSString*)strContent
{
    lb_title.text = strTitle;
    tx_content.placeholder = strContent;
}





@end
