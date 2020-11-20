//
//  BMSQ_PersonInfoCell.m
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PersonInfoCell.h"

@implementation BMSQ_PersonInfoCell
@synthesize delegate;
@synthesize lb_content;

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
    lb_title.textColor = UICOLOR(36, 36, 36, 1.0);
    [lb_title setFont:[UIFont systemFontOfSize:14]];
    [self addSubview:lb_title];
    
    lb_content = [[UITextField alloc]initWithFrame:CGRectMake(115, 0, APP_VIEW_WIDTH - 145, 43)];
    lb_content.textColor = UICOLOR(140, 140, 140, 1.0);
    [lb_content setFont:[UIFont systemFontOfSize:14]];
    lb_content.delegate = self;
    lb_content.textAlignment = NSTextAlignmentRight;
    lb_content.returnKeyType = UIReturnKeyDone;
    lb_content.enabled = NO;
    [lb_content addTarget:self action:@selector(textFieldDoneEditing:) forControlEvents:UIControlEventEditingDidEndOnExit];
    [lb_content addTarget:self action:@selector(textFieldWithText:) forControlEvents:UIControlEventEditingChanged];
    [self addSubview:lb_content];
    
    UIView* line0 = [[UIView alloc]initWithFrame:CGRectMake(10, 44, APP_VIEW_WIDTH-10, APP_CELL_LINE_HEIGHT)];
    line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:line0];
}

- (void)setCellValue:(NSString*)strTitle title:(NSString*)strContent
{
    lb_content.text = strContent;
    lb_title.text = strTitle;
}

- (void)setTextEnable:(BOOL)isEnable
{
    lb_content.enabled = isEnable;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    if (delegate != nil && [delegate respondsToSelector:@selector(textFieldShouldReturn:)])
    {
        return [delegate textFieldShouldReturn:textField];
    }
    
    [textField resignFirstResponder];
    return YES;
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if (delegate != nil && [delegate respondsToSelector:@selector(textFieldShouldBeginEditing:)])
    {
        return [delegate textFieldShouldBeginEditing:textField];
    }
    
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField           // became first responder
{
    if (delegate != nil)
    {
        [delegate textFieldDidBeginEditing:textField];
    }
}

- (void)textFieldDoneEditing:(UITextField *)textField
{
    if (delegate != nil && [delegate respondsToSelector:@selector(textFieldDoneEditing:)])
    {
        return [delegate textFieldDoneEditing:textField];
    }
}

- (void)textFieldWithText:(UITextField *)textField
{
    if (delegate != nil && [delegate respondsToSelector:@selector(textFieldWithText:)])
    {
        return [delegate textFieldWithText:textField];
    }
}


@end
