//
//  InputViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 15/12/9.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "InputViewCell.h"

@implementation InputViewCell

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
        [self addSubview:self.manualInputLB];
//        [self addSubview:self.inputField];
    }
    return self;
}

- (UILabel *)manualInputLB
{
    if (_manualInputLB == nil) {
        self.manualInputLB = [[UILabel alloc] initWithFrame:CGRectMake(10, self.frame.size.height / 4, 60, self.frame.size.height / 2)];
        _manualInputLB.text = @"手动输入";
        _manualInputLB.font = [UIFont systemFontOfSize:14];
    }
    return _manualInputLB;
}

//- (UITextField *)inputField
//{
//    if (_inputField == nil) {
//        self.inputField = [[UITextField alloc] initWithFrame:CGRectMake(70, self.frame.size.height / 6, self.frame.size.width - 100, self.frame.size.height / 6 * 4)];
//        _inputField.borderStyle = UITextBorderStyleRoundedRect;
//        _inputField.backgroundColor = APP_VIEW_BACKCOLOR;
//        _inputField.keyboardType = UIKeyboardTypeEmailAddress;
//    }
//    return _inputField;
//}



@end
