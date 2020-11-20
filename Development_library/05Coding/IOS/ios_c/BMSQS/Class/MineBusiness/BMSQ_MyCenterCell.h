//
//  BMSQ_MyCenterCell.h
//  BMSQC
//
//  Created by djx on 15/8/1.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_MyCenterCell : UITableViewCell
{
    UIImageView* iv_logo;
    UILabel* lb_title;

}

@property(nonatomic,strong)UIImageView* img_msg;
@property(nonatomic,strong)UILabel *lb_msg;

- (void)setCellValue:(NSString*)imgName title:(NSString*)strTitle;


@end
