//
//  OpenNewClassCell.m
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "OpenNewClassCell.h"
#import "OpenClassUtil.h"

@interface OpenNewClassCell  ()





@end


@implementation OpenNewClassCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    
    return self;
}

- (void)setViewUp {
    
    self.leftLabel = [OpenClassUtil openClassSetLabel:CGRectMake(10, 0, 100, 44) text:@"" font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.contentView];

    

    self.tx_content = [[UITextField alloc]initWithFrame:CGRectMake(110, 0, APP_VIEW_WIDTH - 110, 44)];
    self.tx_content.font = [UIFont systemFontOfSize:14.f];
    self.tx_content.backgroundColor = [UIColor clearColor];
    self.tx_content.delegate = self;
    self.tx_content.returnKeyType = UIReturnKeyDone;
    self.tx_content.textColor = UICOLOR(71, 71, 71, 1.0);
    [self.contentView addSubview:self.tx_content];
    
    
    self.rightLabel = [OpenClassUtil openClassSetLabel:CGRectMake(110, 0, APP_VIEW_WIDTH, 44) text:@"" font:[UIFont systemFontOfSize:14.f] textColor:nil view:self.contentView];
//    self.rightLabel.backgroundColor = [UIColor redColor];
    
    self.rightIv = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-20, 17, 10, 10)];
    self.rightIv.contentMode = UIViewContentModeLeft;
    self.rightIv.image = [UIImage imageNamed:@"garyright"];
    self.rightIv.hidden = YES;
    [self.contentView addSubview:self.rightIv];

    self.lineView = [[UIView alloc] initWithFrame:CGRectMake(0, 43, APP_VIEW_WIDTH, 1)];
    self.lineView.backgroundColor = APP_CELL_LINE_COLOR;
    [self.contentView addSubview:self.lineView];
//    [gloabFunction ggsetLineView:CGRectMake(0, 43, APP_VIEW_WIDTH, 1) view:self.contentView];

}


- (void)setCellValue:(NSString *)textStr {
    
    self.leftLabel.text = textStr;

    
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    
    if (self.delegate != nil) {
        [self.delegate OpenNewCellTag:textField];
    }
    return YES;
}



@end
