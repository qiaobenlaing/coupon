//
//  AddBankCardCell.h
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol AddBankCardCellDelegate <NSObject>

- (BOOL)textFieldShouldReturn:(UITextField *)textField;
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField;
- (void)textFieldDidBeginEditing:(UITextField *)textField ;
- (void)textFieldDoneEditing:(UITextField *)textField ;
- (void)textFieldWithText:(UITextField *)textField; //获取text值
@end


@interface AddBankCardCell : UITableViewCell<UITextFieldDelegate>
{
    UILabel* lb_title;
}

@property(nonatomic,assign)NSObject<AddBankCardCellDelegate>* delegate;
@property(nonatomic,strong)UITextField* tx_content;

- (void)setCellValue:(NSString*)strTitle content:(NSString*)strContent;

@end
