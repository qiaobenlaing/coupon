//
//  BMSQ_MyNoticeCell.h
//  BMSQC
//
//  Created by djx on 15/8/9.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_MyNoticeCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_name;
    UILabel* lb_useDes;
    UILabel* lb_time;
}

- (void)setCellValue:(NSDictionary*)dicData;

@end
