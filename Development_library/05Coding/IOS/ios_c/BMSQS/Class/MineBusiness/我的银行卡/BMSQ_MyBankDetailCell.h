//
//  BMSQ_MyBankDetailCell.h
//  BMSQC
//
//  Created by djx on 15/8/3.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_MyBankDetailCell : UITableViewCell

{
    UIImageView* iv_logo;
    UILabel* lb_title;
    UILabel* lb_msg;
}

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle content:(NSString*)strContent;

@end
