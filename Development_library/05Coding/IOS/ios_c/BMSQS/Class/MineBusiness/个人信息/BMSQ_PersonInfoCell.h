//
//  BMSQ_PersonInfoCell.h
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol PersonInfoCellDelegate <NSObject>

- (BOOL)textFieldShouldReturn:(UITextField *)textField;
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField;
- (void)textFieldDidBeginEditing:(UITextField *)textField ;
- (void)textFieldDoneEditing:(UITextField *)textField ;
- (void)textFieldWithText:(UITextField *)textField; //获取text值
@end

@interface BMSQ_PersonInfoCell : UITableViewCell<UITextFieldDelegate>
{
    UILabel* lb_title;
    UITextField* lb_content;
}
@property(nonatomic,strong)UITextField* lb_content;
@property(nonatomic,assign)NSObject<PersonInfoCellDelegate>* delegate;

- (void)setTextEnable:(BOOL)isEnable;
- (void)setCellValue:(NSString*)strTitle title:(NSString*)strContent;

@end
