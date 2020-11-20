//
//  OpenNewClassCell.h
//  BMSQS
//
//  Created by gh on 16/3/15.
//  Copyright © 2016年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol OpenNewClassDelegate <NSObject>

- (void)OpenNewCellTag:(UITextField *)textField;

@end


@interface OpenNewClassCell : UITableViewCell<UITextFieldDelegate>

@property (nonatomic, assign)id<OpenNewClassDelegate> delegate;

@property (nonatomic, strong)UILabel *leftLabel;
@property (nonatomic, strong)UITextField *tx_content;
@property (nonatomic, strong)UILabel *rightLabel;
@property (nonatomic, strong)UIImageView *rightIv;
@property (nonatomic, strong)UIView *lineView;

- (void)setCellValue:(NSString *)textStr;


@end
