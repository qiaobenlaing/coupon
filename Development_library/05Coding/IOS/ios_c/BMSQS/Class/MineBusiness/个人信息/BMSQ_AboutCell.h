//
//  BMSQ_AboutCell.h
//  BMSQC
//
//  Created by djx on 15/8/2.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_AboutCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_title;
    UIButton* btn_msg;
}

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle;
@end
