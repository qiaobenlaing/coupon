//
//  BMSQ_ICBCCuponCell.h
//  BMSQC
//
//  Created by djx on 15/7/26.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BMSQ_ICBCCuponCell : UITableViewCell
{
    UIImageView* iv_shopLogo; //商家Logo
    UILabel* lb_distance; //距离
    UILabel* lb_shopName; //商家名称
    UIImageView* iv_contentLogo; //内容图片
    UILabel* lb_content; //内容
}

- (void)setCellValue:(NSDictionary*)dicData;
@end
